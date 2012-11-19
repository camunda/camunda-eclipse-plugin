package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

public class ConnectionReconnectionContext {

	private Connection connection;
	
	public ConnectionReconnectionContext(Connection connection) {
		
		this.connection = connection;
	}

	public void reconnect() {
		
		if (!(connection instanceof FreeFormConnection)) {
			throw new IllegalArgumentException("Unable to reconnect non FreeFormConnection");
		}
		
		FreeFormConnection freeFormConnection = (FreeFormConnection) connection;
		
		AnchorPointStrategy.strategyFor(freeFormConnection).execute();
		BendpointStrategy.strategyFor(freeFormConnection).execute();
	}
	
}
