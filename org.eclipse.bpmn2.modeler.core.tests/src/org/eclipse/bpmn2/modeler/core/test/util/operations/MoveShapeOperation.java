package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;

/**
 * 
 * @author Daniel Meyer
 * @author Nico Rehwaldt
 *
 */
public class MoveShapeOperation extends ShapeOperation<MoveShapeContext, IMoveShapeFeature> {
	
	public MoveShapeOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		super(shape, diagramTypeProvider);
	}

	@Override
	protected MoveShapeContext createContext() {
		MoveShapeContext context = new MoveShapeContext(shape);
		
		context.setSourceContainer(shape.getContainer());
		context.setTargetContainer(shape.getContainer());
		
		return context;
	}
	
	public MoveShapeOperation by(int x, int y) {
		context.setDeltaX(x);
		context.setDeltaY(y);
		
		// the delta information is not used in the Graphiti default implementations
		// context x / y should contain the new coordinates, Graphiti will calculate the delta itself
		context.setX(context.getShape().getGraphicsAlgorithm().getX()+x);
		context.setY(context.getShape().getGraphicsAlgorithm().getY()+y);
		
		return this;
	}
	
	public MoveShapeOperation toContainer(ContainerShape containerShape) {
		context.setTargetContainer(containerShape);
		return this;
	}
	
	@Override
	protected IMoveShapeFeature createFeature(MoveShapeContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		
		IMoveShapeFeature feature = featureProvider.getMoveShapeFeature(context);
		
		Assertions.assertThat(feature).as("Move feature").isNotNull();
		
		return feature;
	}
	
	public static MoveShapeOperation move(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		return new MoveShapeOperation(shape, diagramTypeProvider);
	}
}
