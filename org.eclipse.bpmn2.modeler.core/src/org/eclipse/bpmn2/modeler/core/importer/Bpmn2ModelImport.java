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

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.importer.handlers.FlowNodeShapeHandler;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 * 
 */
public class Bpmn2ModelImport {

	protected IFeatureProvider featureProvider;
	protected Diagram diagram;
	protected Bpmn2Resource resource;
	protected IDiagramTypeProvider diagramTypeProvider;
	protected Bpmn2Preferences preferences;
	
	// in this map we collect all DiagramElements (BPMN DI) indexed by the IDs of the ProcessElements they reference. 
	protected Map<String, DiagramElement> diagramElementMap = new HashMap<String, DiagramElement>();
	
	// this list collects DI elements that do not reference bpmn model elements. (for instance, lables only)
	protected List<DiagramElement> nonModelElements = new ArrayList<DiagramElement>();
	

	public Bpmn2ModelImport(IDiagramTypeProvider diagramTypeProvider, Bpmn2Resource resource) {
		
		this.diagramTypeProvider = diagramTypeProvider;
		this.resource = resource;
		
		featureProvider = diagramTypeProvider.getFeatureProvider();
		diagram = diagramTypeProvider.getDiagram();
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
		// next, process the BPMN model elements and start building the Graphiti diagram
		List<RootElement> processes = definitions.getRootElements();
		for (RootElement rootElement : processes) {
			if (rootElement instanceof org.eclipse.bpmn2.Process) {
				org.eclipse.bpmn2.Process process = (org.eclipse.bpmn2.Process) rootElement;
				handleProcess(process);
			} else {
				System.out.println("Unhandled RootElement: "+rootElement);
			}
		}
		
	}
	
	// handling of BPMN Model Elements ///////////////////////////////////////////////////////////////

	protected void handleProcess(Process process) {
		
		// handle the children of the process element
		handleFlowElementsContainer(process);
	}

	/**
	 * processes all {@link FlowElement FlowElements} in a given scope.
	 * 
	 * @param scope
	 */
	protected void handleFlowElementsContainer(FlowElementsContainer scope) {
		List<FlowElement> flowElements = scope.getFlowElements();

		// sequence flows are collected in this map and processed after the activities are processed
		List<SequenceFlow> sequenceFlows = new ArrayList<SequenceFlow>();
		for (FlowElement flowElement : flowElements) {
			if (flowElement instanceof SequenceFlow) {
				sequenceFlows.add((SequenceFlow) flowElement);				
			} else if(flowElement instanceof Activity) {
				handleActivity((Activity) flowElement, scope);
			}
		}
	}


	protected void handleActivity(Activity flowElement, FlowElementsContainer scope) {
		
		BPMNShape shape = (BPMNShape) diagramElementMap.get(flowElement.getId());
		
		FlowNodeShapeHandler flowNodeShapeHandler = new FlowNodeShapeHandler(this);
		flowNodeShapeHandler.handleShape(flowElement, shape, diagram);
		
	}

	protected void handleFlowElement(FlowElement flowElement) {
		
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
			throw new Bpmn2ImportException("BPMNPlane references unexisting bpmnElement '"+bpmnElement+"'.");
		} else {
			diagramElementMap.put(bpmnElement.getId(), plane);
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
	
	public IDiagramTypeProvider getDiagramTypeProvider() {
		return diagramTypeProvider;
	}

	public Diagram getDiagram() {
		return diagram;
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

}
