package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.modeler.core.layout.nnew.LayoutContext;
import org.eclipse.bpmn2.modeler.core.layout.nnew.DefaultLayoutStrategy;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

public class ConnectionReconnectionContext {

	private Connection connection;
	
	private boolean forceLayout;
	
	public ConnectionReconnectionContext(Connection connection) {
		this(connection, false);
	}
	
	public ConnectionReconnectionContext(Connection connection, boolean forceLayout) {
		
		this.connection = connection;
		this.forceLayout = forceLayout;
	}

	public void reconnect() {
		assertFreeFormConnection(connection);
		
		// (0) check if connection is layouted
		// (1) (true?) repair if needed
		// (2) (false?) 
		// (2.1) set correct anchor points
		// (2.2) set correct bend points
		
		FreeFormConnection freeFormConnection = (FreeFormConnection) connection;
		
		System.out.println("Reconnect " + freeFormConnection);
		
		LayoutContext layoutingContext = new DefaultLayoutStrategy().createLayoutingContext(freeFormConnection);
		
		boolean layouted = false;
		
		if (!forceLayout && layoutingContext.isLayouted()) {
			layouted = layoutingContext.repair();
			System.out.println("[layout] repaired ? " + layouted);
		}
		
		if (!layouted) {
			layoutingContext.layout();
			System.out.println("[layout] layouted new");
		}
	}

	private void assertFreeFormConnection(Connection connection) {
		if (connection instanceof FreeFormConnection) {
			// ok
		} else {
			throw new IllegalArgumentException("Unable to reconnect non FreeFormConnections");
		}
	}
	
}
