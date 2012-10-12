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
import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.MessageFlow;
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
import org.eclipse.bpmn2.modeler.core.importer.handlers.ArtifactShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.AssociationShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.DataInputAssociationShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.DataInputShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.DataObjectShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.DataOutputAssociationShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.DataOutputShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.DatastoreReferenceShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.FlowNodeShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.LaneShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.MessageFlowShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.ParticipantShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.SequenceFlowShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.SubProcessShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.handlers.TaskShapeHandler;
import org.eclipse.bpmn2.modeler.core.importer.util.ErrorLogger;
import org.eclipse.bpmn2.modeler.core.importer.util.ModelHelper;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
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
 */
public class ModelImport {

	protected IFeatureProvider featureProvider;
	protected Bpmn2Resource resource;
	protected IDiagramTypeProvider diagramTypeProvider;
	protected Bpmn2Preferences preferences;
	
	// map collecting all DiagramElements (BPMN DI) indexed by the IDs of the ProcessElements they reference. 
	protected Map<String, DiagramElement> diagramElementMap = new HashMap<String, DiagramElement>();
	
	// list collecting DI elements that do not reference bpmn model elements. (for instance, labels only)
	protected List<DiagramElement> nonModelElements = new ArrayList<DiagramElement>();
	
	// list collecting the created PictogramElements (Graphiti) indexed by bpmn model elements
	protected HashMap<BaseElement, PictogramElement> pictogramElements = new HashMap<BaseElement, PictogramElement>();
	
	// list of exceptions classified as warnings which occurred during the import
	protected List<ImportException> warnings = new ArrayList<ImportException>();
	
	// list of deferred actions
	protected List<DeferredAction<?>> deferredActions = new ArrayList<DeferredAction<?>>();
	
	public ModelImport(IDiagramTypeProvider diagramTypeProvider, Bpmn2Resource resource) {
		
		this.diagramTypeProvider = diagramTypeProvider;
		this.resource = resource;
		
		featureProvider = diagramTypeProvider.getFeatureProvider();
		preferences = Bpmn2Preferences.getInstance(resource);
	}
	
	public void execute() {
		EList<EObject> contents = resource.getContents();
		
		if (contents.isEmpty()) {
			throw new InvalidContentException("No document root in resource bundle");
		} else {
			DocumentRoot documentRoot = (DocumentRoot) contents.get(0);
			handleDocumentRoot(documentRoot);
			
			if (contents.size() > 1) {
				// TODO: is there a possibility for a resource to have multiple DocumentRoots?
				InvalidContentException exception = new InvalidContentException("Multiple document roots in resource");
				log(exception);
			}
		}
	}
		
	protected void handleDocumentRoot(DocumentRoot documentRoot) {
		Definitions definitions = documentRoot.getDefinitions();
		if (definitions == null) {
			throw new InvalidContentException("Document Root has no definitions", documentRoot);
		} else {
			handleDefinitions(definitions);
		}
	}

	protected void handleDefinitions(Definitions definitions) {
		
		// first we process the DI diagrams and associate them with the process elements
		List<BPMNDiagram> diagrams = definitions.getDiagrams();
		for (BPMNDiagram bpmnDiagram : diagrams) {
			handleDIBpmnDiagram(bpmnDiagram);
		}
		
		// copied from old DIImport 
		// iterates over all elements in the diagram -> may be bad but is there another solution?
		
		// add all ids to the mapping table so that they won't be used when 
		// new ids are generated later
		TreeIterator<EObject> iter = definitions.eAllContents();
		while (iter.hasNext()) {
			ModelUtil.addID(iter.next());
		}
		
		// end copied from old DIImport 
		
		// next, process the BPMN model elements and start building the Graphiti diagram
		// first check if we display a single process or collaboration
		List<RootElement> rootElements = definitions.getRootElements();
		List<Process> processes = new ArrayList<Process>();
		Collaboration collaboration = null;
		
		for (RootElement rootElement: rootElements) {
			if (rootElement instanceof Process) {
				processes.add((Process) rootElement);
			} else if (rootElement instanceof Collaboration) {
				if (collaboration != null) {
					UnsupportedFeatureException exception = new UnsupportedFeatureException("Multiple collaborations not supported. Displaying first one only", definitions);
					log(exception);
				} else {
					collaboration = (Collaboration) rootElement;
				}
			} else {
				System.out.println("Unhandled RootElement: " + rootElement);
			}
		}

		Diagram rootDiagram = createEditorRootDiagram(diagrams, collaboration, processes);
		
		if (collaboration != null) {
			// we display a collaboration
			handleCollaboration(collaboration, rootDiagram);
			
		} else if (!processes.isEmpty()) {
			// we display one or more processes
			for (Process process : processes) {
				handleProcess(process, rootDiagram);
			}
			
		} else {
			// We have no root process or collaboration
			createDefaultDiagramContents(definitions);
		}
		
		// handle deferred rendering of, e.g. associations and data associations
		handleDeferredActions();
		
		// finally layout all elements
		performLayout();
	}

	protected void handleDeferredActions() {
		for (DeferredAction<?> action: deferredActions) {
			action.handle();
		}
	}

	protected BPMNDiagram getOrCreateDiagram(List<BPMNDiagram> diagrams, Collaboration collaboration, List<Process> processes) {

		if (diagrams.isEmpty()) {
			BPMNDiagram newDiagram = ModelHelper.create(resource, BPMNDiagram.class);
			diagrams.add(newDiagram);
		}

		BPMNDiagram bpmnDiagram = diagrams.get(0);
		BPMNPlane bpmnPlane = bpmnDiagram.getPlane();
		
		if (bpmnPlane == null || bpmnPlane.eIsProxy()) {
			bpmnPlane = ModelHelper.create(resource, BPMNPlane.class);
			bpmnDiagram.setPlane(bpmnPlane);
		}
		
		return bpmnDiagram;
	}

	protected void createDefaultDiagramContents(Definitions definitions) {

		// create process
		Process process = ModelHelper.create(resource, Process.class);
		definitions.getRootElements().add(process);
		
		// associate process with bpmn plane
		definitions.getDiagrams().get(0).getPlane().setBpmnElement(process);
	}

	protected Diagram createEditorRootDiagram(List<BPMNDiagram> diagrams, Collaboration collaboration, List<Process> processes) {
		BPMNDiagram bpmnDiagram = getOrCreateDiagram(diagrams, collaboration, processes);
		
		IDiagramEditor diagramEditor = diagramTypeProvider.getDiagramEditor();
		Diagram diagram = DIUtils.getOrCreateDiagram(diagramEditor,bpmnDiagram);
		diagramTypeProvider.init(diagram, diagramEditor);

		featureProvider.link(diagram, bpmnDiagram);
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
		
		if (participants.isEmpty()) {
			InvalidContentException exception = new InvalidContentException("No participants in collaboration", collaboration);
			logAndThrow(exception);
		}
		
		for (Participant participant : participants) {
			handleParticipant(participant, container);
		}
		
		for (MessageFlow messageFlow: collaboration.getMessageFlows()) {
			handleMessageFlow(messageFlow, container);
		}
	}

	/**
	 * This draws a participant (Pool) in a Collaboration 
	 * 
	 * @param participant
	 * @param container
	 */
	protected void handleParticipant(Participant participant, ContainerShape container) {
		
		Process process = participant.getProcessRef();
		if (process != null) {
			if (process.eIsProxy()) {
				throw new ImportException("Invalid process referenced by participant");
			}
		}
		
		// TODO: process.isIsClosed == !collapsed ?
		// TODO: or rather bpmnShape.isIsExpanded()
		
		// BPMNShape bpmnShape = (BPMNShape) getDiagramElement(participant);
		// if (process == null || !bpmnShape.isIsExpanded()) {
		if (process == null) {
			// collapsed pool
			handleCollapsedParticipant(participant, container);
		} else {
			handleExpandedParticipant(participant, process, container);
		}
	}

	protected void handleExpandedParticipant(Participant participant, Process process, ContainerShape container) {
		
		// draw the participant (pool)
		ParticipantShapeHandler shapeHander = new ParticipantShapeHandler(this);
		ContainerShape participantContainer = (ContainerShape) handleDiagramElement(participant, container, shapeHander);
		
		List<LaneSet> laneSets = process.getLaneSets();
		if (laneSets.isEmpty()) {
			// if there are no lanes, simply draw the process into the pool (including sequence flows)
			handleProcess(process, participantContainer);
		} else {
			
			// handle io specification (data input and output)
			handleInputOutputSpecification(process, participantContainer);
			
			//  draw the lanes (possibly nested). The lanes reference the task elements they contain, but not the sequence flows.
			for (LaneSet laneSet: laneSets) {
				handleLaneSet(laneSet, process, participantContainer);
			}
			
			// draw flow elements not referenced from lanes
			List<FlowElement> flowElements = process.getFlowElements();
			
			handleUnreferencedFlowElements(container, flowElements);
			
			// draw the sequence flows:
			handleSequenceFlows(participantContainer, flowElements);
			
			// draw artifacts (e.g. groups)
			List<Artifact> artifacts = process.getArtifacts();		
			handleArtifacts(container, artifacts);
		}
	}

	protected void handleUnreferencedFlowElements(ContainerShape containerShape, List<FlowElement> flowElements) {
		List<FlowElement> unreferencedFlowElements = new ArrayList<FlowElement>();
		
		for (FlowElement e: flowElements) {
			// sequence flows handled after handling of unreferenced elements
			if (e instanceof SequenceFlow) {
				continue;
			} else {
				if (getPictogramElementOrNull(e) == null) {
					log(new UnmappedElementException("element not assigned to lane", e));
					unreferencedFlowElements.add(e);
				}
			}
		}
		
		if (unreferencedFlowElements != null) {
			// render 
			handleFlowElements(containerShape, unreferencedFlowElements);
		}
	}

	protected void handleCollapsedParticipant(Participant participant, ContainerShape container) {
		// draw the participant (pool)
		ParticipantShapeHandler shapeHander = new ParticipantShapeHandler(this);
		handleDiagramElement(participant, container, shapeHander);
	}

	protected void handleSequenceFlows(ContainerShape participantContainer, List<FlowElement> flowElements) {
		for (FlowElement flowElement : flowElements) {
			if (flowElement instanceof SequenceFlow) {
				handleSequenceFlow((SequenceFlow) flowElement, participantContainer);				
			}
		}
	}

	protected void handleDataOutputAssociations(List<DataOutputAssociation> dataOutputAssociations, ContainerShape container) {
		for (DataOutputAssociation outputAssociation: dataOutputAssociations) {
			handleDataOutputAssociation(outputAssociation, container);
		}
	}

	private void handleDataOutputAssociation(DataOutputAssociation flowElement, ContainerShape container) {
		handleLater(new DeferredAction<DataOutputAssociation>(flowElement, container, new DataOutputAssociationShapeHandler(this)));
	}

	protected void handleDataInputAssociations(List<DataInputAssociation> dataInputAssociations, ContainerShape container) {
		for (DataInputAssociation inputAssociation: dataInputAssociations) {
			handleDataInputAssociation(inputAssociation, container);
		}
	}
	
	private void handleDataInputAssociation(DataInputAssociation flowElement, ContainerShape container) {
		handleLater(new DeferredAction<DataInputAssociation>(flowElement, container, new DataInputAssociationShapeHandler(this)));
	}

	protected void handleLaneSet(LaneSet laneSet, FlowElementsContainer scope, ContainerShape container) {
		
		List<Lane> lanes = laneSet.getLanes();
		if (lanes.isEmpty()) {
			log(new InvalidContentException("LaneSet has no lanes specified", laneSet));
		}
		
		for (Lane lane: lanes) {
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
			handleFlowElements(thisContainer, (List)referencedNodes);
		}
	}

	protected void handleProcess(Process process, ContainerShape container) {
		
		// data store, data input, data output
		handleInputOutputSpecification(process, container);
		
		// handle direct children of the process element (not displaying lanes)
		List<FlowElement> flowElements = process.getFlowElements();
		handleFlowElements(container, flowElements);
		
		handleSequenceFlows(container, flowElements);
		
		// e.g. groups, text annotation, ...
		List<Artifact> artifacts = process.getArtifacts();		
		handleArtifacts(container, artifacts);
	}
	
	protected void handleInputOutputSpecification(Process process, ContainerShape container) {
		InputOutputSpecification inputOutputSpecification = process.getIoSpecification();
		if (inputOutputSpecification != null) {
			handleInputOutputSpecification(inputOutputSpecification, container);
		}
	}

	protected void handleInputOutputSpecification(Activity activity, ContainerShape container) {
		InputOutputSpecification inputOutputSpecification = activity.getIoSpecification();
		if (inputOutputSpecification != null) {
			handleInputOutputSpecification(inputOutputSpecification, container);
		}
	}
	
	private void handleInputOutputSpecification(InputOutputSpecification inputOutputSpecification, ContainerShape container) {
		
		// handle data inputs
		handleDataInputs(container, inputOutputSpecification.getDataInputs());
		
		// and data outputs
		handleDataOutputs(container, inputOutputSpecification.getDataOutputs());
	}

	protected void handleDataOutputs(ContainerShape container, List<DataOutput> dataOutputs) {
		if (dataOutputs == null) {
			return;
		}
		
		for (DataOutput output: dataOutputs) {
			handleDataOutput(output, container);
		}
	}

	protected void handleDataOutput(DataOutput output, ContainerShape container) {
		handleDiagramElement(output, container, new DataOutputShapeHandler(this));
	}

	protected void handleDataInputs(ContainerShape container, List<DataInput> dataInputs) {
		if (dataInputs == null) {
			return;
		}
		
		for (DataInput input: dataInputs) {
			handleDataInput(input, container);
		}
	}

	private void handleDataInput(DataInput input, ContainerShape container) {
		handleDiagramElement(input, container, new DataInputShapeHandler(this));
	}

	protected void handleArtifacts(ContainerShape container, List<Artifact> artifacts) {

		for (Artifact artifact: artifacts) {
			if (artifact instanceof Association) {
				// association rendering is done deferred
				handleAssociation((Association) artifact, container);
			} else {
				handleArtifact(artifact, container);
			}
		}
	}
	
	protected void handleAssociation(Association association, ContainerShape container) {
		
		handleLater(new DeferredAction<Association>(association, container, new AssociationShapeHandler(this)));
	}

	/**
	 * processes all {@link FlowElement FlowElements} in a given container.
	 * 
	 * @param container
	 * @param flowElementsToBeDrawn
	 */
	protected void handleFlowElements(ContainerShape container, List<FlowElement> flowElementsToBeDrawn) {
		
		for (FlowElement flowElement: flowElementsToBeDrawn) {
			
		    if (flowElement instanceof Gateway) {
				handleGateway((Gateway) flowElement, container);
				
			} else if (flowElement instanceof SubProcess) {
				handleSubProcess((SubProcess) flowElement, container);
				
			} else if (flowElement instanceof CallActivity) {
				handleCallActivity((CallActivity) flowElement, container);
			
			} else if (flowElement instanceof Task) {
				handleTask((Task) flowElement, container);	
				
			} else if (flowElement instanceof Event) {
				handleEvent((Event) flowElement, container);
				
			} else if (flowElement instanceof DataObject) {
				handleDataObject((DataObject) flowElement, container);
				
			} else if (flowElement instanceof DataStoreReference) {
				handleDataStoreReference((DataStoreReference) flowElement, container);
				
			} else {
				System.out.println("Unhandled: " + flowElement);
			}
		    
		    if (flowElement instanceof Activity) {
		    	Activity activity = (Activity) flowElement;
		    	
				handleDataInputAssociations(activity.getDataInputAssociations(), container);
				handleDataOutputAssociations(activity.getDataOutputAssociations(), container);
		    }
		}
	}

	private void handleDataObject(DataObject flowElement, ContainerShape container) {
		
		handleDiagramElement(flowElement, container, new DataObjectShapeHandler(this));
	}

	protected void handleSubProcess(SubProcess subProcess, ContainerShape container) {
		
		// draw subprocess shape
		ContainerShape subProcessContainer = (ContainerShape) handleDiagramElement(subProcess, container, new SubProcessShapeHandler(this));

		// input and output associations for sub process
		handleInputOutputSpecification(subProcess, subProcessContainer);
		
		// descend into scope
		List<FlowElement> flowElements = subProcess.getFlowElements();
		handleFlowElements(subProcessContainer, flowElements);
		
		handleSequenceFlows(subProcessContainer, flowElements);
		
		// TODO: handle artifacts?
	}
	
	protected void handleArtifact(Artifact artifact, ContainerShape container) {
		handleDiagramElement(artifact, container, new ArtifactShapeHandler(this));
	}

	protected void handleGateway(Gateway flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new FlowNodeShapeHandler(this));
	}

	protected void handleMessageFlow(MessageFlow flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new MessageFlowShapeHandler(this));
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
	
	protected void handleDataStoreReference(DataStoreReference flowElement, ContainerShape container) {
		handleDiagramElement(flowElement, container, new DatastoreReferenceShapeHandler(this));
	}
	
	public <T extends BaseElement> PictogramElement handleDiagramElement(T flowElement, ContainerShape container,
			AbstractDiagramElementHandler<T> flowNodeShapeHandler) {
		
		DiagramElement diagramElement = getDiagramElement(flowElement);
		PictogramElement pictogramElement = flowNodeShapeHandler.handleDiagramElement(flowElement, diagramElement, container);
		
		pictogramElements.put(flowElement, pictogramElement);
		
		return pictogramElement;
	}

	protected void handleLater(DeferredAction<?> deferredAction) {
		deferredActions.add(deferredAction);
	}
	
	// handling of DI Elements ///////////////////////////////////////////////////////////////

	protected void handleDIBpmnDiagram(BPMNDiagram bpmnDiagram) {
		
		BPMNPlane plane = bpmnDiagram.getPlane();
		if (plane == null) {
			throw new InvalidContentException("BPMNDiagram has no BPMNPlane", bpmnDiagram);
		} else {
			handleDIBpmnPlane(plane);
		}
	}

	protected void handleDIBpmnPlane(BPMNPlane plane) {
		
		BaseElement bpmnElement = plane.getBpmnElement();
		if (bpmnElement.eIsProxy()) {
			throw new UnmappedElementException("BPMNPlane references unexisting bpmnElement", plane);
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
		if (bpmnElement == null || bpmnElement.eIsProxy()) {
			ImportException exception = new UnmappedElementException("BPMNEdge references unexisting bpmnElement", diagramElement);
			log(exception);
		} else {
			linkInDiagramElementMap(diagramElement, bpmnElement);
		}
	}

	protected void handleDIShape(BPMNShape diagramElement) {
		BaseElement bpmnElement = diagramElement.getBpmnElement();
		if (bpmnElement == null || bpmnElement.eIsProxy()) {
			ImportException exception = new UnmappedElementException("BPMNEdge references unexisting bpmnElement", diagramElement);
			log(exception);
		} else {
			linkInDiagramElementMap(diagramElement, bpmnElement);
		}
	}
	
	protected void linkInDiagramElementMap(DiagramElement diagramElement, BaseElement bpmnElement) {
		// FIXME: otherwise works only if BPMN element has an id
		if (bpmnElement.getId() == null) {
			ModelUtil.setID(bpmnElement, resource);
		}
		
		diagramElementMap.put(bpmnElement.getId(), diagramElement);
	}
	
	// Error logging ////////////////////////////////////////////
	
	public void log(ImportException e) {
		warnings.add(e);
		ErrorLogger.log(e);
	}
	
	public void logAndThrow(ImportException e) throws ImportException {
		ErrorLogger.logAndThrow(e);
	}
	
	// Getters //////////////////////////////////////////////////
	
	public IFeatureProvider getFeatureProvider() {
		return featureProvider;
	}
	
	public DiagramElement getDiagramElement(BaseElement bpmnElement) {
		DiagramElement element = diagramElementMap.get(bpmnElement.getId());
		if (element == null) {
			UnmappedElementException exception = new UnmappedElementException("Diagram element not found", bpmnElement);
			logAndThrow(exception);
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

	public List<ImportException> getImportWarnings() {
		return warnings;
	}
	
	public PictogramElement getPictogramElementOrNull(BaseElement node) {
		return pictogramElements.get(node);
	}
	
	public PictogramElement getPictogramElement(BaseElement node) {
		PictogramElement element = getPictogramElementOrNull(node);
		if (element == null) {
			throw new UnmappedElementException("Pictogram element not yet processed", node);
		}
		
		return element;
	}

	// Deferred diagram element handling /////////////////////////////////////////////////////
	
	public class DeferredAction<T extends BaseElement>  {

		private T flowElement;
		private ContainerShape container;
		private AbstractDiagramElementHandler<T> handler;

		public DeferredAction(T flowElement, ContainerShape container, AbstractDiagramElementHandler<T> handler) {
			
			this.flowElement = flowElement;
			this.container = container;
			this.handler = handler;
		}
		
		/**
		 * Handle the deferred diagram action
		 */
		public void handle() {
			handleDiagramElement(flowElement, container, handler);
		}
	}
}
