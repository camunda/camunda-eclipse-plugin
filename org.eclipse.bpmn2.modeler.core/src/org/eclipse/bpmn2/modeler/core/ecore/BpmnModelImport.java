/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.ecore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
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
public class BpmnModelImport {

	protected IFeatureProvider featureProvider;
	protected Diagram diagram;
	protected Bpmn2Resource resource;
	protected IDiagramTypeProvider diagramTypeProvider;
	
	// in this map we collect all DiagramElements (BPMN DI) indexed by the IDs of the ProcessElements they reference. 
	protected Map<String, DiagramElement> diagramElementMap = new HashMap<String, DiagramElement>();
	
	// this list collects DI elements that do not reference bpmn model elements. (for instance, lables only)
	protected List<DiagramElement> nonModelElements = new ArrayList<DiagramElement>();
	

	public BpmnModelImport(IDiagramTypeProvider diagramTypeProvider, Bpmn2Resource resource) {
		
		this.diagramTypeProvider = diagramTypeProvider;
		this.resource = resource;
		
		featureProvider = diagramTypeProvider.getFeatureProvider();
		diagram = diagramTypeProvider.getDiagram();

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
			handleBpmnDiagram(bpmnDiagram);
		}
		// next, process the BPMN model elements and start building the Graphiti diagram
		List<RootElement> processes = definitions.getRootElements();
		for (RootElement rootElement : processes) {
			if (rootElement instanceof org.eclipse.bpmn2.Process) {
				org.eclipse.bpmn2.Process process = (org.eclipse.bpmn2.Process) rootElement;
				handleProcess(process);				
			} else {
				System.out.println("[Warn] not handling " + rootElement);
			}
		}
		
	}

	protected void handleProcess(Process process) {
	}

	protected void handleBpmnDiagram(BPMNDiagram bpmnDiagram) {
		
		BPMNPlane plane = bpmnDiagram.getPlane();
		if(plane == null) {
			throw new Bpmn2ImportException("BPMNDiagram has no BPMNPlane");
		} else {
			handleBpmnPlane(plane);
		}
	}

	protected void handleBpmnPlane(BPMNPlane plane) {
		
		BaseElement bpmnElement = plane.getBpmnElement();
		if(bpmnElement.eIsProxy()) {
			throw new Bpmn2ImportException("BPMNPlane references unexisting bpmnElement '"+bpmnElement+"'.");
		} else {
			diagramElementMap.put(bpmnElement.getId(), plane);
		}
		
		List<DiagramElement> planeElement = plane.getPlaneElement();
		for (DiagramElement diagramElement : planeElement) {
			handleDiagramElement(diagramElement);
		}
				
	}

	protected void handleDiagramElement(DiagramElement diagramElement) {
		if (diagramElement instanceof BPMNShape) {
			handleShape((BPMNShape) diagramElement);			
		} else if(diagramElement instanceof BPMNEdge) {
			handleEdge((BPMNEdge) diagramElement);
		} else {
			nonModelElements.add(diagramElement);
		}
	}


	protected void handleEdge(BPMNEdge diagramElement) {
		BaseElement bpmnElement = diagramElement.getBpmnElement();
		if(bpmnElement.eIsProxy()) {
			throw new Bpmn2ImportException("BPMNEdge references unexisting bpmnElement '"+bpmnElement+"'.");
		} else {
			diagramElementMap.put(bpmnElement.getId(), diagramElement);
		}
	}

	protected void handleShape(BPMNShape diagramElement) {
		BaseElement bpmnElement = diagramElement.getBpmnElement();
		if(bpmnElement.eIsProxy()) {
			throw new Bpmn2ImportException("BPMNShape references unexisting bpmnElement '"+bpmnElement+"'.");
		} else {
			diagramElementMap.put(bpmnElement.getId(), diagramElement);
		}
	}

}
