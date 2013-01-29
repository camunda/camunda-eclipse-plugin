package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Single point of entry for connecting and layouting shapes. 
 * 
 * @author nico.rehwaldt
 */
public class ConnectionService {

	public static void reconnectShapeAfterMove(PictogramElement shape, IFeatureProvider featureProvider) {
		new BpmnElementReconnectionContext(featureProvider).reconnect(shape);
	}
	
	public static void reconnectShapeAfterResize(Shape shape, IFeatureProvider featureProvider) {
		new BpmnElementReconnectionContext(featureProvider).reconnect(shape);
	}

	public static void reconnectConnectionAfterCreate(Connection connection, IFeatureProvider featureProvider) {
		reconnectConnection(featureProvider, connection, true, true);
	}

	public static void reconnectConnectionAfterMove(Connection connection, IFeatureProvider featureProvider) {
		reconnectConnection(featureProvider, connection, false, true);
	}
	
	public static void reconnectConnectionAfterConnectionEndChange(Connection connection, IFeatureProvider featureProvider) {
		reconnectConnection(featureProvider, connection, true, false);
	}
	
	protected static void reconnectConnection(IFeatureProvider featureProvider, Connection connection, boolean forceLayout, boolean relayoutOnRepairFail) {
	
		// check if new anchor point is neccessary
		AnchorContainer startAnchorContainer = connection.getStart().getParent();
		AnchorContainer endAnchorContainer = connection.getEnd().getParent();
		
		if (startAnchorContainer instanceof Shape && endAnchorContainer instanceof Shape) {
			new ConnectionReconnectionContext(featureProvider, connection, forceLayout, relayoutOnRepairFail).reconnect();
		} else {
			throw new LayoutingException("Cannot handle connection: " + connection);
		}
		
		DIUtils.updateDIEdge(connection);
	}

	public static void reconnectContainerAfterMove(ContainerShape container, IFeatureProvider featureProvider) {
		reconnectShapeAfterMove(container, featureProvider);
	}
}
