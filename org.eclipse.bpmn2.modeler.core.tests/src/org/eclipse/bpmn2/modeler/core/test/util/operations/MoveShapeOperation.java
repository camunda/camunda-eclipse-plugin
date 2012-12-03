package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * 
 * @author Daniel Meyer
 *
 */
public abstract class MoveShapeOperation<T extends DefaultMoveShapeFeature, V> extends ShapeOperation<MoveShapeContext, T> {

	private V myself;
	
	public MoveShapeOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider, Class<V> cls) {
		super(shape, diagramTypeProvider);
		
		myself = (V) this;
	}

	@Override
	protected void createContext() {
		context = new MoveShapeContext(shape);
		context.setSourceContainer(shape.getContainer());
	}
	
	public V by(int x, int y) {
		context.setDeltaX(x);
		context.setDeltaY(y);
		
		// the delta information is not used in the Graphiti default implementations
		// context x / y should contain the new coordinates, Graphiti will calculate the delta itself
		context.setX(context.getShape().getGraphicsAlgorithm().getX()+x);
		context.setY(context.getShape().getGraphicsAlgorithm().getY()+y);
		
		return myself;
	}
	
	public V toContainer(ContainerShape containerShape) {
		context.setTargetContainer(containerShape);
		return myself;
	}
}
