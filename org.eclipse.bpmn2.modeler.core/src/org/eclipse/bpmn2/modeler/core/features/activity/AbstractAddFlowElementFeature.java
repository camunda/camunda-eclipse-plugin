package org.eclipse.bpmn2.modeler.core.features.activity;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.location;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBpmnShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.PropertyNames;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.IRemoveBendpointFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.features.context.impl.RemoveBendpointContext;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

/**
 * Abstract feature for all flow elements.
 * 
 * @author nico.rehwaldt
 *
 * @param <T>
 */
public abstract class AbstractAddFlowElementFeature<T extends FlowElement> extends AbstractAddBpmnShapeFeature<T> {

	public AbstractAddFlowElementFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoSubProcess = FeatureSupport.isTargetSubProcess(context);
		
		return intoDiagram || intoLane || intoParticipant || intoSubProcess;
	}
	
	@Override
	protected void postAddHook(IAddContext context, ContainerShape newShape) {
		super.postAddHook(context, newShape);

		// split connection in case we have a drop on connection
		if (context.getTargetConnection() != null) {
			splitConnection(context, newShape);
		}
	}
	
	protected void adjustLocationForDropOnConnection(IAddContext context) {
		Connection connection = context.getTargetConnection();
		
		if (context instanceof AddContext) {

			AddContext addContext = (AddContext) context;
			
			ContainerShape targetContainer = context.getTargetContainer();
			IRectangle targetContainerBounds = LayoutUtil.getAbsoluteBounds(targetContainer);
			
			int targetX = targetContainerBounds.getX();
			int targetY = targetContainerBounds.getY();
			
			// x / y is relative to target container
			
			Point point = point(context.getX() + targetX, context.getY() + targetY);
			Point connectionReferencePoint = LayoutUtil.getConnectionReferencePoint(connection, point);
			
			// translate to target container relative bounds
			int x = connectionReferencePoint.getX() - targetX;
			int y = connectionReferencePoint.getY() - targetY;
			
			addContext.setLocation(x, y);
		}
		
		// no adjustment done right now
	}

	protected void splitConnection(IAddContext context, ContainerShape newShape) {
		
		if (isImport(context)) {
			return;
		}
		
		Object newObject = getBusinessObject(context);
		FreeFormConnection connection = (FreeFormConnection) context.getTargetConnection();

		ReconnectionContext reconnectCtx;
		
		Anchor newShapeAnchor = LayoutUtil.getCenterAnchor(newShape);
		Point splitPoint = point(LayoutUtil.getAbsoluteShapeCenter(newShape));
		
		List<Point> bendpointsBeforeSplit = LayoutUtil.getConnectionBendpointsTo(connection, splitPoint);
		List<Point> bendpoints = connection.getBendpoints();

		List<Point> secondPartBendpoints = new ArrayList<Point>(bendpoints);
		secondPartBendpoints.removeAll(bendpointsBeforeSplit);
		
		ILocation newShapeAnchorLocation = LayoutUtil.getAnchorLocation(newShapeAnchor);
		
		if (newObject instanceof StartEvent) {
			bendpoints.removeAll(bendpointsBeforeSplit);
		} else {
			bendpoints.retainAll(bendpointsBeforeSplit);
		}
		
		if (newObject instanceof StartEvent) {
			
			// reconnection newShape to connection end only
			reconnectCtx = new ReconnectionContext(connection, connection.getStart(), newShapeAnchor, newShapeAnchorLocation);
			reconnectCtx.setReconnectType(ReconnectionContext.RECONNECT_SOURCE);
			reconnectCtx.setTargetPictogramElement(newShape);
			
			reconnectAfterSplit(reconnectCtx);
		} else
		if (newObject instanceof EndEvent) {
			// reconnection connection start to newShape only
			reconnectCtx = new ReconnectionContext(connection, connection.getEnd(), newShapeAnchor, newShapeAnchorLocation);
			reconnectCtx.setReconnectType(ReconnectionContext.RECONNECT_TARGET);
			reconnectCtx.setTargetPictogramElement(newShape);
			
			reconnectAfterSplit(reconnectCtx);
		} else {
			// create new connection from newShape to end
			CreateConnectionContext createCtx = new CreateConnectionContext();
			
			Anchor targetAnchor = connection.getEnd();
			ILocation targetAnchorLocation = LayoutUtil.getAnchorLocation(targetAnchor);
			
			createCtx.setSourceAnchor(newShapeAnchor);
			createCtx.setSourcePictogramElement(newShape);
			createCtx.setSourceLocation(newShapeAnchorLocation);
			
			createCtx.setTargetAnchor(targetAnchor);
			createCtx.setTargetPictogramElement(targetAnchor.getParent());
			createCtx.setTargetLocation(targetAnchorLocation);
			
			ContextUtil.set(createCtx, PropertyNames.CONNECTION_BENDPOINTS, secondPartBendpoints);
			
			createConnectionAfterSplit(createCtx, Bpmn2Package.eINSTANCE.getSequenceFlow());
			
			// reconnect start to newShape
			reconnectCtx = new ReconnectionContext(connection, connection.getEnd(), newShapeAnchor, newShapeAnchorLocation);
			reconnectCtx.setReconnectType(ReconnectionContext.RECONNECT_TARGET);
			reconnectCtx.setTargetPictogramElement(newShape);
			
			reconnectAfterSplit(reconnectCtx);
		}
	}

	private void removeBendpoint(FreeFormConnection connection, Point point) {
		RemoveBendpointContext removeCtx = new RemoveBendpointContext(connection, point);
		IRemoveBendpointFeature removeBendpointFeature = getFeatureProvider().getRemoveBendpointFeature(removeCtx);
		
		if (removeBendpointFeature.canExecute(removeCtx)) {
			removeBendpointFeature.execute(removeCtx);
		}
	}
	
	/**
	 * Execute reconnect after split
	 * 
	 * @param context
	 */
	private void reconnectAfterSplit(ReconnectionContext context) {
		IReconnectionFeature reconnectionFeature = getFeatureProvider().getReconnectionFeature(context);
		
		ContextUtil.set(context, PropertyNames.REPAIR_IF_POSSIBLE);
		
		if (reconnectionFeature.canExecute(context)) {
			reconnectionFeature.reconnect(context);
		}
	}
	
	/**
	 * Execute create after split
	 * 
	 * @param context
	 * @param connectionType
	 */
	private void createConnectionAfterSplit(CreateConnectionContext context, EClass connectionType) {
		for (ICreateConnectionFeature createConnectionFeature : getFeatureProvider().getCreateConnectionFeatures()) {
			if (createConnectionFeature instanceof AbstractCreateFlowFeature) {
		        AbstractCreateFlowFeature<?, ?, ?> createFlowFeature = (AbstractCreateFlowFeature<?, ?, ?>) createConnectionFeature;
		        if (createFlowFeature.getBusinessObjectClass().equals(connectionType)) {
					if (createConnectionFeature.canExecute(context)) {
						createConnectionFeature.execute(context);
					}
		        }
			}
		}
	}
}
