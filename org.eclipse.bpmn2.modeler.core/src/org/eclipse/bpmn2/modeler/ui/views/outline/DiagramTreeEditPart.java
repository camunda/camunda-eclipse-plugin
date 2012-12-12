package org.eclipse.bpmn2.modeler.ui.views.outline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.graphiti.mm.StyleContainer;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.algorithms.styles.Style;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.swt.graphics.Image;

public class DiagramTreeEditPart extends AbstractGraphicsTreeEditPart {

	List<Diagram> diagrams;
	int id;
	
	public DiagramTreeEditPart(int id, Diagram diagram) {
		super(null, diagram);
		setDiagramEditPart(this);
		this.id = id;
	}

	public Diagram getDiagram() {
		return (Diagram) getBpmnModel();
	}

	public List<Diagram> getAllDiagrams() {
		if (diagrams==null)
			diagrams = new ArrayList<Diagram>();
		return diagrams;
	}
	
	// ======================= overwriteable behaviour ========================

	@Override
	protected void createEditPolicies() {
	}

	/**
	 * Returns the children of this EditPart.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List<Object> getModelChildren() {
		List<Object> retList = new ArrayList<Object>();
		Diagram diagram = getDiagram();
		BPMNDiagram bpmnDiagram = (BPMNDiagram) BusinessObjectUtil.getBusinessObjectForPictogramElement(diagram);
		if (bpmnDiagram!=null) {
			Definitions definitions = (Definitions)bpmnDiagram.eContainer();
			if (id == BPMN2EditorOutlinePage.ID_BUSINESS_MODEL_OUTLINE)
				retList.addAll(definitions.getRootElements());
			else if (id == BPMN2EditorOutlinePage.ID_INTERCHANGE_MODEL_OUTLINE)
				retList.addAll(definitions.getDiagrams());
			
			// build a list of all Graphiti Diagrams - these will be needed by other
			// TreeEditParts to map the business objects to PictogramElements
			ResourceSet resourceSet = diagram.eResource().getResourceSet();
			for (BPMNDiagram bd : definitions.getDiagrams()) {
				getAllDiagrams().add( DIUtils.findDiagram(resourceSet, bd) );
			}
		}		
		return retList;
	}

	@Override
	protected String getText() {
		BPMNDiagram bpmnDiagram = (BPMNDiagram) BusinessObjectUtil.getBusinessObjectForPictogramElement(getDiagram());
		if (bpmnDiagram!=null && bpmnDiagram.getName()!=null)
			return bpmnDiagram.getName();
		return "";
		
	}
}