package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.modeler.core.features.MoveFlowNodeFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * 
 * @author Daniel Meyer
 *
 */
public class MoveFlowNodeOperation extends ShapeOperation<MoveShapeContext, MoveFlowNodeFeature> {

	public MoveFlowNodeOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		super(shape, diagramTypeProvider);
	}

	@Override
	protected void createContext() {
		context = new MoveShapeContext(shape);
		context.setSourceContainer(shape.getContainer());
	}

	@Override
	protected void createFeature() {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		feature = new MoveFlowNodeFeature(featureProvider);		
	}
	
	public MoveFlowNodeOperation by(int x, int y) {
		context.setDeltaX(x);
		context.setDeltaY(y);
		
		// the delta information is not used in the Graphiti default implementations
		// context x / y should contain the new coordinates, Graphiti will calculate the delta itself
		context.setX(context.getShape().getGraphicsAlgorithm().getX()+x);
		context.setY(context.getShape().getGraphicsAlgorithm().getY()+y);
		
		return this;
	}
	
	public MoveFlowNodeOperation toContainer(ContainerShape containerShape) {
		context.setTargetContainer(containerShape);
		return this;
	}
	
	
}
