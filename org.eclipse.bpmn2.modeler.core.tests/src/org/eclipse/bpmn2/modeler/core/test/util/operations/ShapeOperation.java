package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * 
 * @author Daniel Meyer
 *
 * @param <C> The {@link IContext}
 * @param <F> The {@link IFeature}
 */
public abstract class ShapeOperation<C extends IContext, F extends IFeature> {
	
	protected final Shape shape;
	protected final IDiagramTypeProvider diagramTypeProvider;
	protected C context;
	protected F feature;
	
	public ShapeOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		this.shape = shape;
		this.diagramTypeProvider = diagramTypeProvider;
		createFeature();
		createContext();
	}
	
	protected abstract void createContext();
	protected abstract void createFeature();
	
	protected void executeFeature() {
		diagramTypeProvider.getDiagramEditor().executeFeature(feature, context);
	}
	
	public void execute() {
		executeFeature();
	}
	
	public static MoveFlowNodeOperation move(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		return new MoveFlowNodeOperation(shape, diagramTypeProvider);
	}

}
