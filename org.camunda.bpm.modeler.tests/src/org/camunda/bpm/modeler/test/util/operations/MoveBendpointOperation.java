package org.camunda.bpm.modeler.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IMoveBendpointFeature;
import org.eclipse.graphiti.features.context.impl.MoveBendpointContext;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

public class MoveBendpointOperation extends Operation<MoveBendpointContext, IMoveBendpointFeature> {

	public MoveBendpointOperation(Point point, FreeFormConnection connection, IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
		this.context = createContext(point);
		
		onConnection(connection);
	}

	public MoveBendpointOperation onConnection(FreeFormConnection connection) {
		
		context.setConnection(connection);
		context.setBendpointIndex(connection.getBendpoints().indexOf(context.getBendpoint()));
		
		return this;
	}
	
	public MoveBendpointOperation toLocation(int x, int y) {
		context.setLocation(x, y);
		
		return this;
	}
	
	public MoveBendpointOperation toLocation(Point point) {
		return toLocation(point.getX(), point.getY());
	}
	
	protected MoveBendpointContext createContext(Point bendpoint) {
		return new MoveBendpointContext(bendpoint);
	}
	
	@Override
	protected MoveBendpointContext createContext() {
		throw new UnsupportedOperationException("Use #createContext(Point)");
	}

	@Override
	protected IMoveBendpointFeature createFeature(MoveBendpointContext context) {
		return diagramTypeProvider.getFeatureProvider().getMoveBendpointFeature(context);
	}
	
	////// static utilities ///////////////////////////////////////
	
	public static MoveBendpointOperation moveBendpoint(Point bendpoint, FreeFormConnection connection, IDiagramTypeProvider diagramTypeProvider) {
		return new MoveBendpointOperation(bendpoint, connection, diagramTypeProvider);
	}
}
