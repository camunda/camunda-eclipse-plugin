package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public abstract class AbstractAddOperation<T extends IFeature, V extends AbstractAddOperation<T, V>> extends Operation<CreateContext, T> {

	protected final V myself;
	
	public AbstractAddOperation(IDiagramTypeProvider diagramTypeProvider, Class<V> cls) {
		super(diagramTypeProvider);
		
		this.context = createContext();
		this.myself = cls.cast(this);
	}

	public V atLocation(int x, int y) {
		context.setLocation(x, y);
		return myself;
	}
	
	public V atLocation(Point point) {
		return atLocation(point.getX(), point.getY());
	}
	
	public V sized(int width, int height) {
		context.setSize(width, height);
		return myself;
	}
	
	public V toConnection(Connection connection) {
		context.setTargetConnection(connection);
		
		return myself;
	}
	
	public V toContainer(ContainerShape containerShape) {
		context.setTargetContainer(containerShape);
		
		return myself;
	}
	
	@Override
	protected CreateContext createContext() {
		CreateContext context = new CreateContext();
		context.setX(0);
		context.setY(0);
		
		return context;
	}

	@Override
	protected abstract T createFeature(CreateContext context);
}
