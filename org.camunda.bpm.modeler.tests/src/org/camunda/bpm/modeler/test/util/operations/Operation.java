package org.camunda.bpm.modeler.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;

public abstract class Operation<C extends IContext, F extends IFeature> {
	
	protected final IDiagramTypeProvider diagramTypeProvider;
	
	protected C context;

	public Operation(IDiagramTypeProvider diagramTypeProvider) {
		this.diagramTypeProvider = diagramTypeProvider;
	}
	
	protected abstract C createContext();
	
	protected abstract F createFeature(C context);
	
	protected Diagram getDiagram() {
		return diagramTypeProvider.getDiagram();
	}
	
	public Object execute() {
		F feature = createFeature(context);
		return diagramTypeProvider.getDiagramEditor().executeFeature(feature, context);
	}
}
