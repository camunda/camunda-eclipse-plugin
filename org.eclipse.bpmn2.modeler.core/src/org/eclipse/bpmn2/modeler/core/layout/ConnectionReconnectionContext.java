package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
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
		if (!(connection instanceof FreeFormConnection)) {
			throw new IllegalArgumentException("Unable to reconnection non FreeFormConnection");
		}
		
		FreeFormConnection freeFormConnection = (FreeFormConnection) connection;
		
		double treshold = LayoutUtil.getLayoutTreshold(startShape, endShape);
		if (treshold == 0.0 || treshold == 1.0) {
			freeFormConnection.getBendpoints().clear();
			return;
		}
		
		if ( treshold < 1 && !(treshold == 0.0 || treshold == 1.0) && treshold > LayoutUtil.MAGIC_VALUE  ) {
			Anchor rightAnchor = startShape.getAnchors().get(2);
			connection.setStart(rightAnchor);

			ILocation startAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
			ILocation endAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(endAnchor);
			
			int midX = ((endAnchorLocation.getX() - startAnchorLocation.getX()) / 2) + startAnchorLocation.getX();
			
			Point firstPoint = Graphiti.getCreateService().createPoint(midX, startAnchorLocation.getY());
			Point secondPoint = Graphiti.getCreateService().createPoint(midX, endAnchorLocation.getY());
			
			freeFormConnection.getBendpoints().clear();
			
			freeFormConnection.getBendpoints().add(firstPoint);
			freeFormConnection.getBendpoints().add(secondPoint);
		}
	}

	private void reconnectGateway() {
		// TODO Auto-generated method stub
		
	}
	
}
