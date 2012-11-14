package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class BpmnElementReconnectionContext {


	public BpmnElementReconnectionContext(Diagram diagram) {
		
	}

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
	
	private void reconnectConnections(EList<Connection> connections) {
		for (Connection c: connections) {
			reconnectConnection(c);
		}
	}

	private void reconnectConnection(Connection connection) {
		
		// check if new anchor point is neccessary
		Anchor start = connection.getStart();
		Anchor end = connection.getEnd();

		AnchorContainer startAnchorContainer = start.getParent();
		AnchorContainer endAnchorContainer = end.getParent();
		
		if (startAnchorContainer instanceof Shape && endAnchorContainer instanceof Shape) {
			new ConnectionReconnectionContext(connection, start, end, (Shape) startAnchorContainer, (Shape) endAnchorContainer).reconnect();
		} else {
			throw new LayoutingException("Cannot handle connection: " + connection);
		}
		
		// add new bendpoints starting from 
	}
}
