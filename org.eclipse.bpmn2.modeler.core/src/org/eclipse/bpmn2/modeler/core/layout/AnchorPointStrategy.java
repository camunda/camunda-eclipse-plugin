package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil.AnchorLocation;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class AnchorPointStrategy extends LayoutStrategy {
	private AnchorUtil.AnchorLocation location;

	AnchorPointStrategy start;
	AnchorPointStrategy end;
	
	boolean unchanged = false;

	public AnchorPointStrategy() {
	}

	public AnchorPointStrategy(FreeFormConnection connection) {
		this.connection = connection;

		this.start = new AnchorPointStrategy();
		this.start.setConnection(connection);

		this.end = new AnchorPointStrategy();
		this.end.setConnection(connection);
	}
	
	public AnchorPointStrategy start() {
		return start;
	}

	public AnchorPointStrategy end() {
		return end;
	}

	public AnchorPointStrategy bottom() {
		setLocation(AnchorLocation.BOTTOM);
		return this;
	}

	public AnchorPointStrategy top() {
		setLocation(AnchorLocation.TOP);
		return this;
	}

	public AnchorPointStrategy left() {
		setLocation(AnchorLocation.LEFT);
		return this;
	}

	public AnchorPointStrategy right() {
		setLocation(AnchorLocation.RIGHT);
		return this;
	}

	@Override
	public void doExecute() {
		start.internalExecute(true);
		end.internalExecute(false);
	}

	protected void internalExecute(boolean isStart) {
		if (isStart) {
			getConnection().setStart(getAnchor(getStartShape()));
		} else {
			getConnection().setEnd(getAnchor(getEndShape()));
		}
	}

	protected Anchor getAnchor(Shape shape) {
		switch (getLocation()) {
		case LEFT:
			return LayoutUtil.getLeftAnchor(shape);
		case RIGHT:
			return LayoutUtil.getRightAnchor(shape);
		case TOP:
			return LayoutUtil.getTopAnchor(shape);
		case BOTTOM:
			return LayoutUtil.getBottomAnchor(shape);
		}
		return null;
	}

	@Override
	protected void sectorSwitch(Sector sector) {
		switch (sector) {

		case LEFT:
		case TOP_LEFT:
		case BOTTOM_LEFT:
			start.left();
			end.right();
			break;

		case RIGHT:
		case TOP_RIGHT:
		case BOTTOM_RIGHT:
			start.right();
			end.left();
			break;

		case TOP:
			start.top();
			end.bottom();
			break;

		case BOTTOM:
			start.bottom();
			end.top();
			break;

		default:
			throw new IllegalArgumentException(
					"Cant define AnchorPointStrategy for undefined sector for connection " + connection);
		}
	}
	
	@Override
	protected void typeSwitch(Sector targetElementSector, BaseElement sourceElement, BaseElement targetElement) {
		
		if (sourceElement instanceof Gateway) {
			gatewaySwitch(targetElementSector, (Gateway) sourceElement);
		}
		
		BaseElement flowElement = (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(getConnection());	
		if (flowElement instanceof MessageFlow) {
			messageFlowSwitch(targetElementSector, sourceElement);
		}
		
		if (sourceElement instanceof BoundaryEvent) {
			boundaryEventSwitch(targetElementSector, (BoundaryEvent) sourceElement);
		}
		
	}

	private void boundaryEventSwitch(Sector targetElementSector, BoundaryEvent sourceElement) {
		Sector relativeSectorToRef = LayoutUtil
				.getBoundaryEventRelativeSector(LayoutUtil.getStartShape(connection));

		switch (relativeSectorToRef) {
		case BOTTOM:
			start.bottom();
			break;
		case TOP:
			start.top();
			break;
		case LEFT:
			start.left();
			break;
		case RIGHT:
			start.right();
			break;
		case TOP_LEFT:
			switch (targetElementSector) {
			case BOTTOM:
			case BOTTOM_RIGHT:
				start.left();
				break;
			case RIGHT:
			case TOP_RIGHT:
			case TOP_LEFT:
				start.top();
				break;
			default:
				break;
			}
			break;
		case BOTTOM_LEFT:
			switch (targetElementSector) {
			case TOP:
			case TOP_RIGHT:
				start.left();
				break;
			case RIGHT:
			case BOTTOM_RIGHT:
			case BOTTOM_LEFT:
				start.bottom();
				break;
			default:
				break;
			}
			break;
		case TOP_RIGHT:
			switch (targetElementSector) {
			case BOTTOM:
			case BOTTOM_LEFT:
				start.right();
				break;
			case LEFT:
			case TOP_LEFT:
			case TOP_RIGHT:
				start.top();
				break;
			default:
				break;
			}
			break;
		case BOTTOM_RIGHT:
			switch (targetElementSector) {
			case TOP:
			case TOP_LEFT:
				start.right();
				break;
			case LEFT:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
				start.bottom();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

	}

	private void messageFlowSwitch(Sector targetElementSector, BaseElement sourceElement) {
		switch (targetElementSector) {
		case TOP_RIGHT:
		case TOP_LEFT:
			start.top();
			end.bottom();
			break;
		case BOTTOM_RIGHT:
		case BOTTOM_LEFT:
			start.bottom();
			end.top();
			break;
		}
	}

	private void gatewaySwitch(Sector targetElementSector, Gateway sourceElement) {
		double treshold = LayoutUtil.getAbsLayoutTreshold(connection);
		
		switch (targetElementSector) {
		case TOP_RIGHT:
		case TOP_LEFT:
			if (treshold <= LayoutUtil.MAGIC_VALUE) {
				start.top();
			}
			break;
		case BOTTOM_RIGHT:
		case BOTTOM_LEFT:
			if (treshold <= LayoutUtil.MAGIC_VALUE) {
				start.bottom();
			}
			break;
		case TOP:
		case BOTTOM:
			if (treshold > 0.0) {
				end.left();
			} else {
				end.right();
			}
			break;
		}

	}

	public AnchorUtil.AnchorLocation getLocation() {
		return location;
	}

	public void setLocation(AnchorUtil.AnchorLocation location) {
		this.location = location;
	}
	
	@Override
	public LayoutStrategy getSubStrategy(FreeFormConnection connection,
			BaseElement sourceElement) {
		
		if (isConnectionBpmnType(connection, Association.class) || isConnectionBpmnType(connection, DataAssociation.class)) {
			return new AssocationAnchorPointStrategy(connection);
		}
		
		return super.getSubStrategy(connection, sourceElement);
	}
	
}
