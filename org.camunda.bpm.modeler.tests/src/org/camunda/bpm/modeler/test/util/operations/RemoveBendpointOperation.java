package org.camunda.bpm.modeler.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IRemoveBendpointFeature;
import org.eclipse.graphiti.features.context.impl.RemoveBendpointContext;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

public class RemoveBendpointOperation extends Operation<RemoveBendpointContext, IRemoveBendpointFeature> {

	public RemoveBendpointOperation(Point point, FreeFormConnection connection, IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
		this.context = createContext(point, connection);
	}
	
	protected RemoveBendpointContext createContext(Point bendpoint, FreeFormConnection connection) {
		return new RemoveBendpointContext(connection, bendpoint);
	}
	
	@Override
	protected RemoveBendpointContext createContext() {
		throw new UnsupportedOperationException("Use #createContext(Point, FreeFormConnection)");
	}

	@Override
	protected IRemoveBendpointFeature createFeature(RemoveBendpointContext context) {
		return diagramTypeProvider.getFeatureProvider().getRemoveBendpointFeature(context);
	}
	
	////// static utilities ///////////////////////////////////////
	
	public static RemoveBendpointOperation removeBendpoint(Point bendpoint, FreeFormConnection connection, IDiagramTypeProvider diagramTypeProvider) {
		return new RemoveBendpointOperation(bendpoint, connection, diagramTypeProvider);
	}
}
