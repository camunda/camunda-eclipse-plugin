package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.layout.nnew.DefaultLayoutStrategy;
import org.eclipse.bpmn2.modeler.core.layout.nnew.LayoutContext;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

public class ConnectionReconnectionContext {

	private Connection connection;
	
	private boolean forceLayout;

	private boolean relayoutOnRepairFail;
	
	public ConnectionReconnectionContext(Connection connection) {
		this(connection, false, true);
	}
	
	public ConnectionReconnectionContext(Connection connection, boolean forceLayout, boolean relayoutOnRepairFail) {
		
		this.connection = connection;
		this.forceLayout = forceLayout;
		
		this.relayoutOnRepairFail = relayoutOnRepairFail;
	}

	public void reconnect() {
		assertFreeFormConnection(connection);
		
		// (0) check if connection is layouted
		// (1) (true?) repair if needed
		// (2) (false?) 
		// (2.1) set correct anchor points
		// (2.2) set correct bend points
		
		boolean layouted = false;
		
		FreeFormConnection freeFormConnection = (FreeFormConnection) connection;
		
		BaseElement model = BusinessObjectUtil.getFirstBaseElement(freeFormConnection);
		
		System.out.println();
		System.out.println("[reconnect] " + model.getId());
		
		LayoutContext layoutingContext = new DefaultLayoutStrategy().createLayoutingContext(freeFormConnection, relayoutOnRepairFail);
		
		if (forceLayout) {
			System.out.println("[reconnect] forced layout, no repair");
			layoutingContext.layout();
			layouted = true;
		} else {
			System.out.println("[reconnect] repair");
			boolean repaired = layoutingContext.repair();

			System.out.println("[reconnect] " + (repaired ? "repair success" : "repair failed"));
			
			if (layoutingContext.needsLayout()) {
				System.out.println("[reconnect] repair failed, relayout");
				layoutingContext.layout();
				layouted = true;
			}
		}
		
		// perform move label after re-layout
		moveLabel(connection, layouted);
	}

	private void moveLabel(Connection connection, boolean layouted) {
		ContainerShape labelShape = LabelUtil.getLabelShape(connection, connection.getParent());
		if (layouted) {
			
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
