package org.eclipse.bpmn2.modeler.core.layout;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.di.DIUtils;
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
		List<Connection> tmp = new ArrayList<Connection>(connections);
		for (Connection c: tmp) {
			reconnectConnection(c);
		}
	}

	private void reconnectConnection(Connection connection) {
		
		// check if new anchor point is neccessary
		AnchorContainer startAnchorContainer = connection.getStart().getParent();
		AnchorContainer endAnchorContainer = connection.getEnd().getParent();
		
		if (startAnchorContainer instanceof Shape && endAnchorContainer instanceof Shape) {
			new ConnectionReconnectionContext(connection).reconnect();
		} else {
			throw new LayoutingException("Cannot handle connection: " + connection);
		}
		
		DIUtils.updateDIEdge(connection);
	}
}
