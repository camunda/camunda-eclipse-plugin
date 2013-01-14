package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.mm.pictograms.Connection;

/**
 * 
 * @author Daniel Meyer
 *
 * @param <C> The {@link IContext}
 * @param <F> The {@link IFeature}
 */
public abstract class ConnectionOperation<C extends IContext, F extends IFeature> extends Operation<C, F> {
	
	protected final Connection connection;
	
	public ConnectionOperation(Connection connection, IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		this.connection = connection;
		
		this.context = createContext();
	}

}
