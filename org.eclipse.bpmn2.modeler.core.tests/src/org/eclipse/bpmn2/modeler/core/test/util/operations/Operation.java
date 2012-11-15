package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.IContext;

public abstract class Operation<C extends IContext, F extends IFeature> {
	
	protected final IDiagramTypeProvider diagramTypeProvider;
	protected C context;
	protected F feature;

	public Operation(IDiagramTypeProvider diagramTypeProvider) {
		this.diagramTypeProvider = diagramTypeProvider;
	}
	
	protected abstract void createContext();
	protected abstract void createFeature();
	
	protected void executeFeature() {
		diagramTypeProvider.getDiagramEditor().executeFeature(feature, context);
	}
	
	public void execute() {
		executeFeature();
	}
	
}
