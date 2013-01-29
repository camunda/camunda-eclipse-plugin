package org.eclipse.bpmn2.modeler.core.layout;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.features.ContextConstants;
import org.eclipse.bpmn2.modeler.core.layout.nnew.DefaultLayoutStrategy;
import org.eclipse.bpmn2.modeler.core.layout.nnew.LayoutContext;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.services.Graphiti;

public class ConnectionReconnectionContext {

	private Connection connection;
	
	private boolean forceLayout;

	private boolean relayoutOnRepairFail;

	private IFeatureProvider featureProvider;
	
	public ConnectionReconnectionContext(IFeatureProvider featureProvider, Connection connection, boolean forceLayout, boolean relayoutOnRepairFail) {
		
		this.connection = connection;
		this.forceLayout = forceLayout;
		this.featureProvider = featureProvider;
		
		this.relayoutOnRepairFail = relayoutOnRepairFail;
	}

	public void reconnect() {
		assertFreeFormConnection(connection);
		
		// (0) check if connection is layouted
		// (1) (true?) repair if needed
		// (2) (false?) 
		// (2.1) set correct anchor points
		// (2.2) set correct bend points
		
		FreeFormConnection freeFormConnection = (FreeFormConnection) connection;
		
		BaseElement model = BusinessObjectUtil.getFirstBaseElement(freeFormConnection);
		
		System.out.println();
		System.out.println("[reconnect] " + model.getId());
		
		LayoutContext layoutingContext = new DefaultLayoutStrategy().createLayoutingContext(freeFormConnection, relayoutOnRepairFail);
		
		if (forceLayout) {
			System.out.println("[reconnect] forced layout, no repair");
			layoutingContext.layout();
		} else {
			System.out.println("[reconnect] repair");
			boolean repaired = layoutingContext.repair();

			System.out.println("[reconnect] " + (repaired ? "repair success" : "repair failed"));
			
			if (layoutingContext.needsLayout()) {
				System.out.println("[reconnect] repair failed, relayout");
				layoutingContext.layout();
			}
		}
		
		updateLabel(connection);
		
	}

	private void updateLabel(Connection connection) {
		ContainerShape labelShape = GraphicsUtil.getLabelShape(connection, featureProvider.getDiagramTypeProvider().getDiagram());

		double labelRefLength = getLabelRefLength(labelShape);
		Point labelRefOffset = getLabelRefOffset(labelShape);
		if (labelRefOffset == null) {
			labelRefOffset = point(10, 10);
		}
		
		Point labelRefPoint = LayoutUtil.getPointAtLength(connection, labelRefLength);
		
		Point newLabelPosition = point(
				labelRefOffset.getX() + labelRefPoint.getX(), 
				labelRefOffset.getY() + labelRefPoint.getY());
		
		MoveShapeContext context = new MoveShapeContext(labelShape);
		context.setLocation(
			newLabelPosition.getX(), newLabelPosition.getY());
		context.setTargetContainer(labelShape.getContainer());
		context.setSourceContainer(labelShape.getContainer());
		ContextUtil.set(context, ContextConstants.LABEL_ABS_MOV);
		
		IMoveShapeFeature feature = featureProvider.getMoveShapeFeature(context);
		if (feature.canExecute(context)) {
			feature.execute(context);
		}
	}

	private Point getLabelRefOffset(ContainerShape labelShape) {
		try {
			return point(
				Integer.parseInt(Graphiti.getPeService().getPropertyValue(labelShape, ContextConstants.LABEL_REF_OFFSET_X)), 
				Integer.parseInt(Graphiti.getPeService().getPropertyValue(labelShape, ContextConstants.LABEL_REF_OFFSET_Y)));
		} catch (Exception e) {
			
			// illegal argument (number format)
			// null pointer (no property set)
			// can occur
			return null;
		}
	}

	private double getLabelRefLength(ContainerShape labelShape) {
		return Double.parseDouble(Graphiti.getPeService().getPropertyValue(labelShape, ContextConstants.LABEL_REF_LENGTH));
	}

	private void assertFreeFormConnection(Connection connection) {
		if (connection instanceof FreeFormConnection) {
			// ok
		} else {
			throw new IllegalArgumentException("Unable to reconnect non FreeFormConnections");
		}
	}
}
