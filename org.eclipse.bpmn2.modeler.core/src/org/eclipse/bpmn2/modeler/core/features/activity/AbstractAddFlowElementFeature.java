package org.eclipse.bpmn2.modeler.core.features.activity;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBpmnShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

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
		
		// no adjustment done right now
	}

	protected void splitConnection(IAddContext context, ContainerShape newShape) {
		
		if (isImport(context)) {
			return;
		}
		
		Object newObject = getBusinessObject(context);
		Connection connection = context.getTargetConnection();

		ReconnectionContext reconnectCtx;
		
		Anchor newShapeAnchor = LayoutUtil.getCenterAnchor(newShape);
		ILocation newShapeAnchorLocation = LayoutUtil.getAnchorLocation(newShapeAnchor);
		
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
			
			createConnectionAfterSplit(createCtx, Bpmn2Package.eINSTANCE.getSequenceFlow());
			
			// reconnect start to newShape
			reconnectCtx = new ReconnectionContext(connection, connection.getEnd(), newShapeAnchor, newShapeAnchorLocation);
			reconnectCtx.setReconnectType(ReconnectionContext.RECONNECT_TARGET);
			reconnectCtx.setTargetPictogramElement(newShape);
			
			reconnectAfterSplit(reconnectCtx);
		}
	}

	/**
	 * Execute reconnect after split
	 * 
	 * @param context
	 */
	private void reconnectAfterSplit(ReconnectionContext context) {
		IReconnectionFeature reconnectionFeature = getFeatureProvider().getReconnectionFeature(context);
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
