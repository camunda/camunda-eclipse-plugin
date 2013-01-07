package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.services.Graphiti;

public class BendpointStrategy extends LayoutStrategy<Tuple<Docking, Docking>, Void> {
	
	private FreeFormConnection connection;
	
	private boolean horizontal;
	private boolean vertical;
	private boolean single;

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
	
	private BendpointStrategy single() {
		single = true;
		return this;
	}
	
	@Override
	protected Void doExecute() {
		connection.getBendpoints().clear();
		
		double treshold = LayoutUtil.getLayoutTreshold(connection);
		
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
		} else 
		
		if (vertical) {
			if (treshold != 1.0) {
				LayoutUtil.addVerticalCenteredBendpoints(connection, startDocking.getPosition(), endDocking.getPosition());
			}
		}
		
		return null;
	}
	
	protected void sectorSwitch(Sector sector) {
		switch(sector) {
		case LEFT:
		case TOP_LEFT:
		case BOTTOM_LEFT:
		case RIGHT:
		case TOP_RIGHT:
		case BOTTOM_RIGHT:
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
		
		double treshold = LayoutUtil.getAbsLayoutTreshold(connection);
		
		if (sourceElement instanceof Gateway) {
			gatewaySwitch(treshold);
		}
		
		BaseElement flowElement = (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(connection);	
		if (flowElement instanceof MessageFlow) {
			messageFlowSwitch(sector);
		}
		
		if (sourceElement instanceof BoundaryEvent) {
			boundaryEventSwitch(sector);
		}
	}

	private void gatewaySwitch(double treshold) {
		if (treshold <= LayoutUtil.MAGIC_VALUE) {
			this.single();
		}
	}

	private void boundaryEventSwitch(Sector sector) {
		Sector relativeSectorToRef = LayoutUtil.getBoundaryEventRelativeSector(startDocking.getShape());
		
		switch (relativeSectorToRef) {
		case BOTTOM:
		case TOP:
			this.single();
			break;
		case TOP_LEFT:
			switch (sector) {
			case BOTTOM:
			case BOTTOM_RIGHT:
			case RIGHT:
			case TOP_RIGHT:
			case TOP_LEFT:
				this.single();
				break;
			default:
				break;
			}
			break;
		case BOTTOM_LEFT:
			switch (sector) {
			case TOP:
			case TOP_RIGHT:
			case RIGHT:
			case BOTTOM_RIGHT:
			case BOTTOM_LEFT:
				this.single();
				break;
			default:
				break;
			}
			break;
		case TOP_RIGHT:
			switch (sector) {
			case BOTTOM:
			case BOTTOM_LEFT:
			case LEFT:
			case TOP_LEFT:
			case TOP_RIGHT:
				this.single();
				break;
			default:
				break;
			}
			break;
		case BOTTOM_RIGHT:
			switch (sector) {
			case TOP:
			case TOP_LEFT:
			case LEFT:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
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
