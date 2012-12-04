package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.ui.features.flow.SequenceFlowFeatureContainer.ReconnectSequenceFlowFeature;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.internal.datatypes.impl.LocationImpl;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class ReconnectConnectionEndOperation extends ConnectionOperation<ReconnectionContext, ReconnectSequenceFlowFeature> {
	
	public ReconnectConnectionEndOperation(Connection connection, IDiagramTypeProvider diagramTypeProvider) {
		super(connection, diagramTypeProvider);
		
		context.setOldAnchor(connection.getEnd());
	}
	
	@Override
	protected void createContext() {
		context = new ReconnectionContext(connection, null, null, null);
	}

	@Override
	protected void createFeature() {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		feature = new ReconnectSequenceFlowFeature(featureProvider);
	}
	
	@SuppressWarnings("restriction")
	public ReconnectConnectionEndOperation withTargetLocation(int x, int y) {
		context.setTargetLocation(new LocationImpl(x, y));
		return this;
	}
	
	public ReconnectConnectionEndOperation toElement(AnchorContainer element) {
		context.setNewAnchor(Graphiti.getCreateService().createChopboxAnchor(element));
		context.setTargetPictogramElement(element);
		context.setReconnectType(ReconnectionContext.RECONNECT_TARGET);
		
		if (context.getTargetLocation() == null) {
			ILocation shapeLocationMidpoint = LayoutUtil.getShapeLocationMidpoint((Shape) element);
			context.setTargetLocation(shapeLocationMidpoint);
		}
		
		return this;
	}
	
	public static ReconnectConnectionEndOperation reconnect(Connection connection, IDiagramTypeProvider diagramTypeProvider) {
		return new ReconnectConnectionEndOperation(connection, diagramTypeProvider);
	}
}
