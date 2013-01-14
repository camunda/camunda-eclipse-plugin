package org.eclipse.bpmn2.modeler.core.test.util.operations;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.location;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class ReconnectConnectionEndOperation extends ConnectionOperation<ReconnectionContext, IReconnectionFeature> {
	
	private boolean reconnectStart;

	public ReconnectConnectionEndOperation(Connection connection, boolean reconnectStart, IDiagramTypeProvider diagramTypeProvider) {
		super(connection, diagramTypeProvider);
		
		this.reconnectStart = reconnectStart;
		
		context.setOldAnchor(reconnectStart ? connection.getStart() : connection.getEnd());
	}
	
	@Override
	protected ReconnectionContext createContext() {
		return new ReconnectionContext(connection, null, null, null);
	}

	@Override
	protected IReconnectionFeature createFeature(ReconnectionContext context) {
		
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return featureProvider.getReconnectionFeature(context);
	}
	
	public ReconnectConnectionEndOperation withTargetLocation(int x, int y) {
		context.setTargetLocation(location(x, y));
		return this;
	}
	
	public ReconnectConnectionEndOperation toElement(AnchorContainer element) {
		if (element == null) {
			throw new IllegalArgumentException("Element may not be null");
		}
		
		context.setNewAnchor(Graphiti.getCreateService().createChopboxAnchor(element));
		context.setTargetPictogramElement(element);
		
		if (reconnectStart) {
			context.setReconnectType(ReconnectionContext.RECONNECT_SOURCE);
		} else {
			context.setReconnectType(ReconnectionContext.RECONNECT_TARGET);
		}
		
		if (context.getTargetLocation() == null) {
			ILocation shapeLocationMidpoint = LayoutUtil.getShapeLocationMidpoint((Shape) element);
			context.setTargetLocation(shapeLocationMidpoint);
		}
		
		return this;
	}
	
	public static ReconnectConnectionEndOperation reconnectEnd(Connection connection, IDiagramTypeProvider diagramTypeProvider) {
		return new ReconnectConnectionEndOperation(connection, false, diagramTypeProvider);
	}
	
	public static ReconnectConnectionEndOperation reconnectStart(Connection connection, IDiagramTypeProvider diagramTypeProvider) {
		return new ReconnectConnectionEndOperation(connection, true, diagramTypeProvider);
	}
}
