package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class ConnectionReconnectionContext {

	private Connection connection;
	
	private Shape startShape;
	private Shape endShape;

	public ConnectionReconnectionContext(Connection connection, Shape startShape, Shape endShape) {
		
		this.connection = connection;
		this.startShape = startShape;
		this.endShape = endShape;
	}

	public void reconnect() {
		
		EObject bpmnElement = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(startShape);
		
		if (!(connection instanceof FreeFormConnection)) {
			throw new IllegalArgumentException("Unable to reconnect non FreeFormConnection");
		}
		
		// TODO if you reconnect the connection manually with a shape, the anchors are not fixpoint anchors anymore,
		// we need to fix this
		
		FreeFormConnection freeFormConnection = (FreeFormConnection) connection;
		freeFormConnection.getBendpoints().clear();
		
		double treshold = LayoutUtil.getLayoutTreshold(startShape, endShape);
		
		if (bpmnElement instanceof Activity) {
			reconnectActivity(treshold, freeFormConnection);
		}else if (bpmnElement instanceof Gateway) {
			reconnectGateway(treshold, freeFormConnection);
		}else if (bpmnElement instanceof Event) {
			reconnectEvent(treshold, freeFormConnection);
		}else {
			throw new LayoutingException("Layouting unsupported BPMN element type");
		}
	}
	
	private abstract class BaseReconnectStrategy {
		protected double treshold = 0.0;
		FreeFormConnection freeFormConnection;
		
		BaseReconnectStrategy(double treshold, FreeFormConnection freeFormConnection) {
			this.freeFormConnection = freeFormConnection;
			this.treshold = treshold;
		}
		
		protected abstract void addBendPoints();
		
	}
	
	private class LeftRightReconnectStrategy extends BaseReconnectStrategy{
		
		public LeftRightReconnectStrategy(double treshold, FreeFormConnection freeFormConnection) {
			super(treshold, freeFormConnection);
		}
		
		public boolean execute() {
			if (LayoutUtil.isLeftToStartShape(startShape, endShape)) {
				if (!LayoutUtil.anchorEqual(freeFormConnection.getStart(), LayoutUtil.getLeftAnchor(startShape))) {
					freeFormConnection.setStart(LayoutUtil.getLeftAnchor(startShape));
				}
				if (!LayoutUtil.anchorEqual(freeFormConnection.getEnd(), LayoutUtil.getRightAnchor(endShape))) {
					freeFormConnection.setEnd(LayoutUtil.getRightAnchor(endShape));
				}
				addBendPoints();
				
				return true;

			} 
			else if (LayoutUtil.isRightToStartShape(startShape, endShape)) {
				if (!LayoutUtil.anchorEqual(freeFormConnection.getStart(), LayoutUtil.getRightAnchor(startShape))) {
					freeFormConnection.setStart(LayoutUtil.getRightAnchor(startShape));
				}
				if (!LayoutUtil.anchorEqual(freeFormConnection.getEnd(), LayoutUtil.getLeftAnchor(endShape))) {
					freeFormConnection.setEnd(LayoutUtil.getLeftAnchor(endShape));
				}
				addBendPoints();
				
				return true;
			}
			return false;
		}
		
		@Override
		protected void addBendPoints() {
			if (treshold != 1.0) {
				LayoutUtil.addVerticalCenteredBendpoints(freeFormConnection);
			}
		}
		
	}
	
	private class AboveBeneathReconnectStrategy extends BaseReconnectStrategy {
		
		AboveBeneathReconnectStrategy(double treshold,
				FreeFormConnection freeFormConnection) {
			super(treshold, freeFormConnection);
		}

		public boolean execute() {
			if (treshold > LayoutUtil.MAGIC_VALUE) {
				return false;
			}
			
			if (LayoutUtil.isAboveStartShape(startShape, endShape)) {
				if (!LayoutUtil.anchorEqual(freeFormConnection.getStart(), LayoutUtil.getTopAnchor(startShape))) {
					freeFormConnection.setStart(LayoutUtil.getTopAnchor(startShape));
				}
				
				if (!LayoutUtil.anchorEqual(freeFormConnection.getEnd(), LayoutUtil.getBottomAnchor(endShape))) {
					updateAboveEndAnchor();
				}
				addBendPoints();
				
				return true;
			}
			else if (LayoutUtil.isBeneathStartShape(startShape, endShape)) {
				if (!LayoutUtil.anchorEqual(freeFormConnection.getStart(), LayoutUtil.getBottomAnchor(startShape))) {
					freeFormConnection.setStart(LayoutUtil.getBottomAnchor(startShape));
				}
				if (!LayoutUtil.anchorEqual(freeFormConnection.getEnd(), LayoutUtil.getTopAnchor(endShape))) {
					updateBeneathEndAnchor();
				}
				addBendPoints();
				
				return true;
			}
			return false;
		}
		
		@Override
		protected void addBendPoints() {
			if (treshold != 0.0) {
				internalAddBendPoints();
			}
		}
		
		protected void internalAddBendPoints() {
			LayoutUtil.addHorizontalCenteredBendpoints(freeFormConnection);
		}
		
		protected void updateAboveEndAnchor() {
			freeFormConnection.setEnd(LayoutUtil.getBottomAnchor(endShape));
		}
		
		protected void updateBeneathEndAnchor() {
			freeFormConnection.setEnd(LayoutUtil.getTopAnchor(endShape));
		}
	}
	
	private class SingleBendPointAboveBeneathStrategy extends AboveBeneathReconnectStrategy {

		SingleBendPointAboveBeneathStrategy(double treshold,
				FreeFormConnection freeFormConnection) {
			super(treshold, freeFormConnection);
		}
		
		@Override
		protected void internalAddBendPoints() {
			LayoutUtil.addRectangularBendpoint(freeFormConnection);
		}
		
		@Override
		protected void updateAboveEndAnchor() {
			freeFormConnection.setEnd(LayoutUtil.getLeftAnchor(endShape));
		}
		
		@Override
		protected void updateBeneathEndAnchor() {
			freeFormConnection.setEnd(LayoutUtil.getLeftAnchor(endShape));
		}
		
	}
	
	private void reconnectAboveFirst(double treshold, FreeFormConnection freeFormConnection) {
		if(!new AboveBeneathReconnectStrategy(treshold, freeFormConnection).execute()) {
			new LeftRightReconnectStrategy(treshold, freeFormConnection).execute();
		}
	}
	
	private void reconnectLeftFirst(double treshold, FreeFormConnection freeFormConnection) {
		if(!new LeftRightReconnectStrategy(treshold, freeFormConnection).execute()) {
			new AboveBeneathReconnectStrategy(treshold, freeFormConnection).execute();
		}
	}
	
	private void reconnectActivity(double treshold, FreeFormConnection freeFormConnection) {
		reconnectLeftFirst(treshold, freeFormConnection);
	}

	private void reconnectEvent(double treshold, FreeFormConnection freeFormConnection) {
		reconnectLeftFirst(treshold, freeFormConnection);
	}
	
	private void reconnectGateway(double treshold, FreeFormConnection freeFormConnection) {
		if(!new SingleBendPointAboveBeneathStrategy(treshold, freeFormConnection).execute()) {
			new LeftRightReconnectStrategy(treshold, freeFormConnection).execute();
		}
	}
	
}
