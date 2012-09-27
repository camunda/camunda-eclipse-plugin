package org.eclipse.bpmn2.modeler.core.importer.handlers;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ImportException;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public abstract class AbstractDiagramElementHandler<T extends BaseElement> {
	
	protected Bpmn2ModelImport bpmn2ModelImport;
	protected IFeatureProvider featureProvider;

	public AbstractDiagramElementHandler(Bpmn2ModelImport bpmn2ModelImport) {
		this.bpmn2ModelImport = bpmn2ModelImport;
		
		featureProvider = bpmn2ModelImport.getFeatureProvider();
	}
	
	public abstract PictogramElement handleDiagramElement(T bpmnElement, DiagramElement diagramElement, ContainerShape container);

	/**
	 * Return a pictogram element for the given flow node.
	 * 
	 * @param node
	 * @throws Bpmn2ImportException if corresponding pictogram element is not yet present
	 * @return
	 */
	protected PictogramElement getPictogramElement(FlowNode node) {
		return bpmn2ModelImport.getPictogramElement(node);
	}

	/**
	 * 
	 * @param bpmnElement
	 * 
	 * @throws Bpmn2ImportException if corresponding diagram element is not yet present
	 * @return
	 */
	protected DiagramElement getDiagramElement(FlowElement bpmnElement) {
		return bpmn2ModelImport.getDiagramElement(bpmnElement);
	}
	
	protected void createLink(T bpmnElement, DiagramElement shape, PictogramElement newContainer) {
		featureProvider.link(newContainer, new Object[] { bpmnElement, shape });
	}
}
