package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class ConnectionReconnectionContext {

	private Connection connection;
	
	private Anchor startAnchor;
	private Anchor endAnchor;
	
	private Shape startShape;
	private Shape endShape;

	public ConnectionReconnectionContext(Connection connection, Anchor startAnchor, Anchor endAnchor,
			Shape startShape, Shape endShape) {
		
		this.connection = connection;
		this.startAnchor = startAnchor;
		this.endAnchor = endAnchor;
		this.startShape = startShape;
		this.endShape = endShape;
	}

	public void reconnect() {
		
		EObject bpmnElement = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(startShape);
		if (bpmnElement instanceof Activity) {
			reconnectActivity();
		}else if (bpmnElement instanceof Gateway) {
			reconnectGateway();
		}else if (bpmnElement instanceof Event) {
			reconnectEvent();
		}else {
			throw new LayoutingException("Layouting unsupported BPMN element type");
		}
	}

	private void reconnectEvent() {
		// TODO Auto-generated method stub
	}

	private void reconnectActivity() {

	}

	private void reconnectGateway() {
		// TODO Auto-generated method stub
		
	}
	
}
