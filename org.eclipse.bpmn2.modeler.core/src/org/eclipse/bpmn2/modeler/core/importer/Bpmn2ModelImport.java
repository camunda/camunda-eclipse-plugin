/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.importer.handlers.AbstractDiagramElementHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.AbstractShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.FlowNodeShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.LaneShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.ParticipantShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.SequenceFlowShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.SubProcessShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.TaskShapeHandler;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.IDiagramEditor;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 * 
 */
public class Bpmn2ModelImport {

	protected IFeatureProvider featureProvider;
	protected Bpmn2Resource resource;
	protected IDiagramTypeProvider diagramTypeProvider;
	protected Bpmn2Preferences preferences;
	
	// in this map we collect all DiagramElements (BPMN DI) indexed by the IDs of the ProcessElements they reference. 
	protected Map<String, DiagramElement> diagramElementMap = new HashMap<String, DiagramElement>();
	
	// this list collects DI elements that do not reference bpmn model elements. (for instance, lables only)
	protected List<DiagramElement> nonModelElements = new ArrayList<DiagramElement>();
	
	// this list collects the created PictogramElements (Graphiti) indexed by bpmn model elements
	protected HashMap<BaseElement, PictogramElement> pictogramElements = new HashMap<BaseElement, PictogramElement>();
	
	public Bpmn2ModelImport(IDiagramTypeProvider diagramTypeProvider, Bpmn2Resource resource) {
		
		this.diagramTypeProvider = diagramTypeProvider;
		this.resource = resource;
		
		featureProvider = diagramTypeProvider.getFeatureProvider();
		preferences = Bpmn2Preferences.getInstance(resource);

	}
	
	public void execute() {
		EList<EObject> contents = resource.getContents();
		
		if(contents.isEmpty()) {
			throw new Bpmn2ImportException("No document root");
		} else {
			handleDocumentRoot((DocumentRoot) contents.get(0));
		}
		// TODO: is there a possibility for a resource to have multiple DocumentRoots?
	}
		
	protected void handleDocumentRoot(DocumentRoot documentRoot) {
		Definitions definitions = documentRoot.getDefinitions();
		if(definitions == null) {
			throw new Bpmn2ImportException("Document Root has no definitions");
		} else {
			handleDefinitions(definitions);
		}
	}

	protected void handleDefinitions(Definitions definitions) {
		// first we process the DI diagrams. 
		List<BPMNDiagram> diagrams = definitions.getDiagrams();
		for (BPMNDiagram bpmnDiagram : diagrams) {
			handleDIBpmnDiagram(bpmnDiagram);
		}
		
		// TODO: remove link to BPMNDiagram?!
		Diagram rootDiagram = createRootDiagram(diagrams.get(0));
		
		// next, process the BPMN model elements and start building the Graphiti diagram
		List<RootElement> rootElements = definitions.getRootElements();
		List<Process> processes = new ArrayList<Process>();
		Collaboration collaboration = null;
		
		for (RootElement rootElement : rootElements) {
			
			if (rootElement instanceof Process) {
				processes.add((Process) rootElement);
				
			} else if (rootElement instanceof Collaboration) {
				if (collaboration != null) {
					throw new Bpmn2ImportException("Multiple collaborations not supported");
				}
				
				collaboration = (Collaboration) rootElement;
				handleCollaboration(collaboration, rootDiagram);
				
			} else {
				System.out.println("Unhandled RootElement: "+rootElement);
			}
		}

		if (collaboration == null) {
			for (Process process : processes) {
				handleProcess(process, rootDiagram);
			}
		}
		
		// finally layout all elements
		performLayout();
	}

	protected Diagram createRootDiagram(BPMNDiagram bpmnDiagram) {
		IDiagramEditor diagramEditor = diagramTypeProvider.getDiagramEditor();
		Diagram diagram = DIUtils.getOrCreateDiagram(diagramEditor,bpmnDiagram);
		diagramTypeProvider.init(diagram, diagramEditor);
		return diagram;
	}

	protected void performLayout() {
		// finally layout all elements
		for (Entry<BaseElement, PictogramElement> entry : pictogramElements.entrySet()) {
			BaseElement baseElement = entry.getKey();
			PictogramElement pictogramElement = entry.getValue();
			
			if (baseElement instanceof SubProcess) {
				// we need the layout to hide children if collapsed
				LayoutContext context = new LayoutContext(pictogramElement);
				ILayoutFeature feature = featureProvider.getLayoutFeature(context);
				if (feature != null && feature.canLayout(context)) {
					feature.layout(context);
				}

			} else if (baseElement instanceof FlowNode) {
				LayoutContext context = new LayoutContext(pictogramElement);
				ILayoutFeature feature = featureProvider.getLayoutFeature(context);
				if (feature != null && feature.canLayout(context)) {
					feature.layout(context);
				}
			}
			
		}
	}
	
	// handling of BPMN Model Elements ///////////////////////////////////////////////////////////////
	
	protected void handleCollaboration(Collaboration collaboration, ContainerShape container) {
		
		List<Participant> participants = collaboration.getParticipants();
		for (Participant participant : participants) {
			handleParticipant(participant, container);
		}
	}

	protected void handleParticipant(Participant participant, ContainerShape container) {
		Process process = participant.getProcessRef();
		
		if (process == null || process.eIsProxy()) {
			return;
		}

		// draw the participant (pool)
		ParticipantShapeHandler shapeHander = new ParticipantShapeHandler(this);
		ContainerShape participantContainer = (ContainerShape) handleDiagramElement(participant, container, shapeHander);
		
		List<LaneSet> laneSets = process.getLaneSets();
		if(laneSets.isEmpty()) {
			handleProcess(process, participantContainer);
			
		} else {
			for (LaneSet laneSet: laneSets) {
				handleLaneSet(laneSet, process, participantContainer);
			}
		}
	}

	protected void handleProcessInCollaboration(Process process, Participant participant, ContainerShape container) {
		
		
	}
	
	protected void handleLaneSet(LaneSet laneSet, FlowElementsContainer scope, ContainerShape container) {
		for (Lane lane: laneSet.getLanes()) {
			handleLane(lane, scope, container);
		}
	}

	protected void handleLane(Lane lane, FlowElementsContainer scope, ContainerShape container) {
		AbstractShapeHandler<Lane> shapeHandler = new LaneShapeHandler(this);
		
		
		// TODO: Draw lane the right way
		DiagramElement diagramElement = getDiagramElement(lane);
		ContainerShape thisContainer = (ContainerShape) shapeHandler.handleDiagramElement(lane, diagramElement, container);
		pictogramElements.put(lane, thisContainer);
		
		LaneSet childLaneSet = lane.getChildLaneSet();
		if (childLaneSet != null) {
			handleLaneSet(childLaneSet, scope, thisContainer);
		} else {
			List<FlowNode> referencedNodes = lane.getFlowNodeRefs();
			
			handleFlowElementsContainer(thisContainer, (List)referencedNodes);
		}
	}

	protected void handleProcess(Process process, ContainerShape container) {
		// handle direct children of the process element (not displaying lanes)
		handleFlowElementsContainer(process, container);
	}
	
	/**
	 * processes all {@link FlowElement FlowElements} in a given scope.
	 * 
	 * @param scope
	 * @param container 
	 */
	protected void handleFlowElementsContainer(FlowElementsContainer scope, ContainerShape container) {
		handleFlowElementsContainer(container, scope.getFlowElements());
	}
	
	/**
	 * processes all {@link FlowElement FlowElements} in a given scope.
	 * 
	 * @param scope
	 * @param container 
	 */
	protected void handleFlowElementsContainer(ContainerShape container, List<FlowElement> flowElementsToBeDrawn) {
		
		// sequence flows are collected in this map and processed after the activities are processed
		List<SequenceFlow> sequenceFlows = new ArrayList<SequenceFlow>();
		
		for (FlowElement flowElement : flowElementsToBeDrawn) {
			if (flowElement instanceof SequenceFlow) {
				// process sequence flows later
				sequenceFlows.add((SequenceFlow) flowElement);
				
			} else if (flowElement instanceof Gateway) {
				handleGateway((Gateway) flowElement, container);	
			
			} else if (flowElement instanceof SubProcess) {
				handleSubProcess((SubProcess) flowElement, container);
				
			} else if (flowElement instanceof CallActivity) {
				handleCallActivity((CallActivity) flowElement, container);
			
			} else if (flowElement instanceof Task) {
				handleTask((Task) flowElement, container);	
				
			} else if (flowElement instanceof Event) {
				handleEvent((Event) flowElement, container);
				
			} else if (flowElement instanceof Activity) {
				handleActivity((Activity) flowElement, container);	
				
			} else {
				System.out.println("Not handled: " + flowElement);
			}
		}
		
		// add sequence flows
		for (SequenceFlow sequenceFlow: sequenceFlows) {
			handleSequenceFlow(sequenceFlow, container);
		}
	}

	protected void handleActivity(Activity flowElement,
			ContainerShape container) {
		
		handleDiagramElement(flowElement, container, new FlowNodeShapeHandler(this));
	}

	protected void handleSubProcess(SubProcess subProcess, ContainerShape container) {
		
		// draw subprocess shape
		PictogramElement diagramElement = handleDiagramElement(subProcess, container, new SubProcessShapeHandler(this));
		
		// descend into scope
		handleFlowElementsContainer(subProcess, (ContainerShape) diagramElement);
		
	}

	protected void handleGateway(Gateway flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new FlowNodeShapeHandler(this));
	}

	protected void handleSequenceFlow(SequenceFlow flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new SequenceFlowShapeHandler(this));
	}

	protected void handleEvent(Event flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new FlowNodeShapeHandler(this));
	}
	
	protected void handleCallActivity(CallActivity flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new FlowNodeShapeHandler(this));		
	}

	protected void handleTask(Task flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new TaskShapeHandler(this));
	}
	
	public <T extends BaseElement> PictogramElement handleDiagramElement(T flowElement, ContainerShape container,
			AbstractDiagramElementHandler<T> flowNodeShapeHandler) {
		
		DiagramElement diagramElement = getDiagramElement(flowElement);
		PictogramElement pictogramElement = flowNodeShapeHandler.handleDiagramElement(flowElement, diagramElement, container);
		
		pictogramElements.put(flowElement, pictogramElement);
		
		return pictogramElement;
	}
	
	// handling of DI Elements ///////////////////////////////////////////////////////////////

	protected void handleDIBpmnDiagram(BPMNDiagram bpmnDiagram) {
		
		BPMNPlane plane = bpmnDiagram.getPlane();
		if(plane == null) {
			throw new Bpmn2ImportException("BPMNDiagram has no BPMNPlane");
		} else {
			handleDIBpmnPlane(plane);
		}
	}

	protected void handleDIBpmnPlane(BPMNPlane plane) {
		
		BaseElement bpmnElement = plane.getBpmnElement();
		if(bpmnElement.eIsProxy()) {
			throw new Bpmn2ImportException("BPMNPlane references unexisting bpmnElement '" + bpmnElement + "'.");
		} else {
//			diagramElementMap.put(bpmnElement.getId(), plane);
		}
		
		List<DiagramElement> planeElement = plane.getPlaneElement();
		for (DiagramElement diagramElement : planeElement) {
			handleDIDiagramElement(diagramElement);
		}
				
	}

	protected void handleDIDiagramElement(DiagramElement diagramElement) {
		if (diagramElement instanceof BPMNShape) {
			handleDIShape((BPMNShape) diagramElement);			
		} else if(diagramElement instanceof BPMNEdge) {
			handleDIEdge((BPMNEdge) diagramElement);
		} else {
			nonModelElements.add(diagramElement);
		}
	}


	protected void handleDIEdge(BPMNEdge diagramElement) {
		BaseElement bpmnElement = diagramElement.getBpmnElement();
		if(bpmnElement.eIsProxy()) {
			throw new Bpmn2ImportException("BPMNEdge references unexisting bpmnElement '"+bpmnElement+"'.");
		} else {
			diagramElementMap.put(bpmnElement.getId(), diagramElement);
		}
	}

	protected void handleDIShape(BPMNShape diagramElement) {
		BaseElement bpmnElement = diagramElement.getBpmnElement();
		if(bpmnElement.eIsProxy()) {
			throw new Bpmn2ImportException("BPMNShape references unexisting bpmnElement '"+bpmnElement+"'.");
		} else {
			diagramElementMap.put(bpmnElement.getId(), diagramElement);
		}
	}
	
	// Getters //////////////////////////////////////////////////
	
	public IFeatureProvider getFeatureProvider() {
		return featureProvider;
	}
	
	public DiagramElement getDiagramElement(BaseElement bpmnElement) {
		DiagramElement element = diagramElementMap.get(bpmnElement.getId());
		if (element == null) {
			throw new Bpmn2ImportException("Not yet processed: " + bpmnElement);
		}
		return element;
	}
	
	public IDiagramTypeProvider getDiagramTypeProvider() {
		return diagramTypeProvider;
	}

	public Bpmn2Resource getResource() {
		return resource;
	}
	
	public Map<String, DiagramElement> getDiagramElementMap() {
		return diagramElementMap;
	}

	public List<DiagramElement> getNonModelElements() {
		return nonModelElements;
	}
	
	public Bpmn2Preferences getPreferences() {
		return preferences;
	}

	public PictogramElement getPictogramElement(FlowNode node) {
		PictogramElement element = pictogramElements.get(node);
		if (element == null) {
			throw new Bpmn2ImportException("Not yet processed: " + node);
		}
		
		return element;
	}

}
