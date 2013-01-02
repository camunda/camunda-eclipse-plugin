package org.eclipse.bpmn2.modeler.core.layout;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.context.ILocationContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Reconnection context for bpmn elements (shapes)
 * 
 * @author nico.rehwaldt
 */
public class BpmnElementReconnectionContext {

	public void reconnect(PictogramElement element) {
		
		if (element instanceof Shape) {
			reconnectShape((Shape) element);
		} else {
			throw new LayoutingException("not a " + Shape.class.getName() + ": " + element);
		}
	}
	
	protected void reconnectShape(Shape shape) {
		EList<Anchor> anchors = shape.getAnchors();
		
		for (Anchor anchor: anchors) {
			EList<Connection> incomingConnections = anchor.getIncomingConnections();
			reconnectConnections(incomingConnections);
			
			EList<Connection> outgoingConnections = anchor.getOutgoingConnections();
			reconnectConnections(outgoingConnections);
		}
	}
	
	private void reconnectConnections(List<Connection> connections) {
		List<Connection> tmp = new ArrayList<Connection>(connections);
		for (Connection connection: tmp) {
			ConnectionService.reconnectConnectionAfterMove(connection);
		}
	}

}
