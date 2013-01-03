package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.modeler.core.features.MoveFlowNodeFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.MoveActivityFeature;
import org.eclipse.bpmn2.modeler.ui.features.event.MoveBoundaryEventFeature;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * 
 * @author Daniel Meyer
 * @author Nico Rehwaldt
 */
public class MoveFlowNodeOperation extends MoveShapeOperation<MoveFlowNodeFeature, MoveFlowNodeOperation> {

	public MoveFlowNodeOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		super(shape, diagramTypeProvider, MoveFlowNodeOperation.class);
	}
	
	@Override
	protected void createFeature() {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		EObject element = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(shape);
		
		assertInstanceOf(FlowNode.class, element);
		
		if (element instanceof Activity) {
			feature = new MoveActivityFeature(featureProvider);
		} else if (element instanceof BoundaryEvent) {
			feature = new MoveBoundaryEventFeature(featureProvider);
		} else {
			feature = new MoveFlowNodeFeature(featureProvider);		
		}
	}
	
	public static MoveFlowNodeOperation move(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		return new MoveFlowNodeOperation(shape, diagramTypeProvider).toContainer(shape.getContainer());
	}
}
