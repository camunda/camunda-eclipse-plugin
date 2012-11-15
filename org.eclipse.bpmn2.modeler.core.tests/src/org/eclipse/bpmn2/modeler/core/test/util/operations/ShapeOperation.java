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
public abstract class ShapeOperation<C extends IContext, F extends IFeature> extends Operation<C, F> {
	
	protected final Shape shape;
	
	public ShapeOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		this.shape = shape;
		createFeature();
		createContext();
	}

}
