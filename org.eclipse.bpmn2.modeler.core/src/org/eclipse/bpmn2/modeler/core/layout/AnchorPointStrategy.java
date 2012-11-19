package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.BaseElement;
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

public class AnchorPointStrategy {
	private FreeFormConnection connection;
	
	private AnchorUtil.AnchorLocation location;

	AnchorPointStrategy start;
	AnchorPointStrategy end;

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

	public void execute() {
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

	protected Shape getStartShape() {
		return (Shape) getConnection().getStart().getParent();
	}

	protected Shape getEndShape() {
		return (Shape) getConnection().getEnd().getParent();
	}

	public static AnchorPointStrategy strategyFor(FreeFormConnection connection) {
		Sector sector = LayoutUtil.getEndShapeSector(connection);
		BaseElement sourceElement = LayoutUtil.getSourceBaseElement(connection);

		AnchorPointStrategy strategy = new AnchorPointStrategy(connection);

		sectorSwitch(strategy, sector);
		typeSwitch(strategy, sector, sourceElement);

		return strategy;
	}

	private static AnchorPointStrategy sectorSwitch(
			AnchorPointStrategy strategy, Sector sector) {
		switch (sector) {

		case LEFT:
		case TOP_LEFT:
		case BOTTOM_LEFT:
			strategy.start.left();
			strategy.end.right();
			return strategy;

		case RIGHT:
		case TOP_RIGHT:
		case BOTTOM_RIGHT:
			strategy.start.right();
			strategy.end.left();
			return strategy;

		case TOP:
			strategy.start.top();
			strategy.end.bottom();
			return strategy;

		case BOTTOM:
			strategy.start.bottom();
			strategy.end.top();
			return strategy;

		default:
			throw new IllegalArgumentException(
					"Cant define AnchorPointStrategy for undefined sector");
		}
	}

	private static AnchorPointStrategy typeSwitch(AnchorPointStrategy strategy, Sector sector, BaseElement sourceElement) {
		double treshold = LayoutUtil.getLayoutTreshold(strategy.getConnection());
		
		if (sourceElement instanceof Gateway) {
			switch (sector) {
			case TOP_RIGHT:
			case TOP_LEFT:
				if (treshold <= LayoutUtil.MAGIC_VALUE) {
					strategy.start.top();
					return strategy;
				}
				
			case BOTTOM_RIGHT:
			case BOTTOM_LEFT:
				if (treshold <= LayoutUtil.MAGIC_VALUE) {
					strategy.start.bottom();
					return strategy;
				}
			case TOP:
			case BOTTOM:
				if (treshold > 0.0) {
					strategy.end.left();
				} else {
					strategy.end.right();
				}
				return strategy;
			}
			
		}
		
		
		BaseElement flowElement = (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(strategy.getConnection());	
		if (flowElement instanceof MessageFlow) {
			switch (sector) {
			case TOP_RIGHT:
			case TOP_LEFT:
				strategy.start.top();
				strategy.end.bottom();
				return strategy;
			case BOTTOM_RIGHT:
			case BOTTOM_LEFT:
				strategy.start.bottom();
				strategy.end.top();
				return strategy;
			}
		}
		
		return strategy;
	}

	public FreeFormConnection getConnection() {
		return connection;
	}

	public void setConnection(FreeFormConnection connection) {
		this.connection = connection;
	}

	public AnchorUtil.AnchorLocation getLocation() {
		return location;
	}

	public void setLocation(AnchorUtil.AnchorLocation location) {
		this.location = location;
	}

}
