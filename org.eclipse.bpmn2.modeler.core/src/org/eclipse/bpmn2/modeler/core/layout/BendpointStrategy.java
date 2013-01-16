package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.services.Graphiti;

public class BendpointStrategy extends LayoutStrategy<Tuple<Docking, Docking>, Void> {
	
	private FreeFormConnection connection;
	
	private boolean none;

	private boolean horizontal;
	private boolean vertical;
	
	private boolean single;
	private boolean singleSrcRelAbove;
	private boolean singleSrcRelBelow;
	private boolean singleSrcRelRight;
	private boolean singleSrcRelLeft;
	
	private Docking startDocking;
	private Docking endDocking;
	
	public BendpointStrategy(FreeFormConnection connection) {
		this.connection = connection;
	}
	
	@Override
	protected void setContext(Tuple<Docking, Docking> startEndDocking) {
		this.startDocking = startEndDocking.getFirst();
		this.endDocking = startEndDocking.getSecond();
	}
	
	public BendpointStrategy horizontal() {
		horizontal = true;
		vertical = false;
		return this;
	}
	
	private BendpointStrategy vertical() {
		vertical = true;
		horizontal = false;
		return this;
	}
	
	private BendpointStrategy none() {
		none = true;
		return this;
	}
	
	private BendpointStrategy single() {
		single = true;
		return this;
	}
	
	private BendpointStrategy singleSrcRelAbove() {
		singleSrcRelAbove = true;
		return this;
	}
	
	private BendpointStrategy singleSrcRelBelow() {
		singleSrcRelBelow = true;
		return this;
	}
	
	private BendpointStrategy singleSrcRelRight() {
		singleSrcRelRight = true;
		return this;
	}
	
	private BendpointStrategy singleSrcRelLeft() {
		singleSrcRelLeft = true;
		return this;
	}
	
	@Override
	protected Void doExecute() {
		connection.getBendpoints().clear();
		
		final int relMargin = 15;
		
		double treshold = LayoutUtil.getLayoutTreshold(connection);
		if (none) {
			return null;
		}
		else
		if (singleSrcRelLeft) {
			if (treshold != 0.0 && treshold != 1.0) {
				LayoutUtil.addTurningBendpointsHorizontal(connection, startDocking.getPosition(), endDocking.getPosition(), ConversionUtil.point(startDocking.getPosition().getX(), startDocking.getPosition().getY()) );
			}
		}
		else
		if (singleSrcRelRight) {
			if (treshold != 0.0 && treshold != 1.0) {
				LayoutUtil.addTurningBendpointsHorizontal(connection, startDocking.getPosition(), endDocking.getPosition(), ConversionUtil.point(startDocking.getPosition().getX(), startDocking.getPosition().getY()) );
			}
		}
		else
		if (singleSrcRelAbove) {
			LayoutUtil.addTurningBendpointsVertical(connection, startDocking, endDocking, true);
		}
		else
		if (singleSrcRelBelow) {
			LayoutUtil.addTurningBendpointsVertical(connection, startDocking, endDocking, false);
		}
		else
		if (single) {
			if (treshold != 0.0 && treshold != 1.0) {
				LayoutUtil.addRectangularBendpoint(connection, startDocking.getPosition(), endDocking.getPosition());
			}
		}
		else
		if (horizontal) {
			if (treshold != 0.0) {
				LayoutUtil.addHorizontalCenteredBendpoints(connection, startDocking.getPosition(), endDocking.getPosition());
			}
		} 
		else 
		if (vertical) {
			if (treshold != 1.0 && treshold != 0.0) {
				LayoutUtil.addVerticalCenteredBendpoints(connection, startDocking.getPosition(), endDocking.getPosition());
			}
		}
		
		return null;
	}
	
	protected void sectorSwitch(Sector sector) {
		switch(sector) {
		case TOP_LEFT:
		case BOTTOM_LEFT:
		case TOP_RIGHT:
		case BOTTOM_RIGHT:
			this.vertical();
			break;
		
		// FIXME: Had to change this to none() otherwise 
		// direct horizontal and vertical layouting do not work anymore
		case LEFT:
		case RIGHT:
			this.vertical();
			break;
		
		case TOP:
		case BOTTOM:
			this.horizontal();
			break;
		
		default: 
			throw new IllegalArgumentException("Cant define BendpointStrategy for undefined sector");
		}
	}
	
	protected void typeSwitch(Sector sector, BaseElement sourceElement, BaseElement targetElement) {

		if (sourceElement instanceof Gateway) {
			gatewaySwitch(sector);
		}
		
		BaseElement flowElement = (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(connection);	
		if (flowElement instanceof MessageFlow) {
			messageFlowSwitch(sector);
		}
		
		if (sourceElement instanceof BoundaryEvent) {
			boundaryEventSwitch(sector);
		}
	}

	private void gatewaySwitch(Sector sector) {
		
		if (sector == Sector.TOP || sector == Sector.BOTTOM) {
			return;
		}
		
		double treshold = LayoutUtil.getAbsLayoutTreshold(connection);
		
		if (treshold <= LayoutUtil.MAGIC_VALUE) {
			this.single();
		}
	}

	private void boundaryEventSwitch(Sector sector) {
		Sector relativeSectorToRef = LayoutUtil.getBoundaryEventRelativeSector(startDocking.getShape());
		
		switch (relativeSectorToRef) {
		case RIGHT:
			switch(sector) {
			case BOTTOM:
			case TOP:
				this.horizontal();
				break;
			case RIGHT:
				this.vertical();
				break;
			default:
				this.singleSrcRelRight();
				break;
			}
			break;
		case LEFT:
			switch(sector) {
			case BOTTOM:
			case TOP:
				this.horizontal();
				break;
			case LEFT:
				this.vertical();
				break;
			default:
				this.singleSrcRelLeft();
				break;
			}
			break;
		case TOP:
		case TOP_RIGHT:
		case TOP_LEFT:
			switch (sector) {
			case LEFT:
				this.singleSrcRelAbove();
				break;
			case RIGHT:
				this.singleSrcRelAbove();
				break;
			case BOTTOM:
			case BOTTOM_RIGHT:
			case BOTTOM_LEFT:
				this.singleSrcRelAbove();
				break;
			case TOP:
				this.horizontal();
				break;
			case TOP_RIGHT:
			case TOP_LEFT:
				this.single();
				break;
			default:
				break;
			}
			break;
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
		case BOTTOM:
			switch (sector) {
			case LEFT:
				this.singleSrcRelBelow();
				break;
			case RIGHT:
				this.singleSrcRelBelow();
				break;
			case TOP:
			case TOP_RIGHT:
			case TOP_LEFT:
				this.singleSrcRelBelow();
				break;
			case BOTTOM:
				this.horizontal();
				break;
			case BOTTOM_RIGHT:
			case BOTTOM_LEFT:
				this.single();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private void messageFlowSwitch(Sector sector) {
		switch (sector) {
		case TOP_RIGHT:
		case TOP_LEFT:
		case BOTTOM_RIGHT:
		case BOTTOM_LEFT:
			this.horizontal();
			break;
		}
	}

}
