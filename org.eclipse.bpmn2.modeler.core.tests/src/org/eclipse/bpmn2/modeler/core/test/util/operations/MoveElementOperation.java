package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.modeler.core.features.DefaultMoveBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.MoveFlowNodeFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.MoveActivityFeature;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.ui.features.event.MoveBoundaryEventFeature;
import org.eclipse.bpmn2.modeler.ui.features.label.LabelFeatureContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * 
 * @author Daniel Meyer
 * @author Nico Rehwaldt
 */
public class MoveElementOperation extends MoveShapeOperation<DefaultMoveShapeFeature, MoveElementOperation> {

	public MoveElementOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		super(shape, diagramTypeProvider, MoveElementOperation.class);
	}
	
	@Override
	protected void createFeature() {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		EObject element = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(shape);
		
		assertInstanceOf(FlowElement.class, element);

		if (GraphicsUtil.isLabel(shape)) {
			feature = new LabelFeatureContainer.MoveLabelFeature(featureProvider);
		} else {
			if (element instanceof Activity) {
				feature = new MoveActivityFeature(featureProvider);
			} else if (element instanceof BoundaryEvent) {
				feature = new MoveBoundaryEventFeature(featureProvider);
			} else if (element instanceof FlowNode) {
				feature = new MoveFlowNodeFeature(featureProvider);		
			} else {
				feature = new DefaultMoveBPMNShapeFeature(featureProvider);
			}
		}
	}
	
	public static MoveElementOperation move(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		return new MoveElementOperation(shape, diagramTypeProvider).toContainer(shape.getContainer());
	}
}
