package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.mm.pictograms.Shape;

public abstract class AssociationOperation<C extends IContext, F extends IFeature> extends Operation<C, F> {

	protected final Shape source;
	protected final Shape target;
	
	public AssociationOperation(Shape source, Shape target, IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		this.source = source;
		this.target = target;
		createContext();
		createFeature();
	}

}
