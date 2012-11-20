package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class BendpointStrategy {
	
	private FreeFormConnection connection;
	
	private boolean horizontal;
	private boolean vertical;
	private boolean single;
	
	public BendpointStrategy(FreeFormConnection connection) {
		this.connection = connection;
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
	
	public void execute() {
		connection.getBendpoints().clear();
		
		double treshold = LayoutUtil.getLayoutTreshold(connection);
		
		if (single) {
			if (treshold != 0.0 && treshold != 1.0) {
				LayoutUtil.addRectangularBendpoint(connection);
			}
		}
		else if (horizontal) {
			if (treshold != 0.0) {
				LayoutUtil.addHorizontalCenteredBendpoints(connection);
			}
		} else if (vertical) {
			if (treshold != 1.0) {
				LayoutUtil.addVerticalCenteredBendpoints(connection);
			}
		}
	}
	
	
	public static BendpointStrategy strategyFor(FreeFormConnection connection) {
		Sector sector = LayoutUtil.getEndShapeSector(connection);
		BendpointStrategy strategy = new BendpointStrategy(connection);
		
		BaseElement sourceElement = LayoutUtil.getSourceBaseElement(connection);
		
		sectorSwitch(strategy, sector);
		typeSwitch(strategy, sector, sourceElement);
		
		return strategy;
	}
	
	private static BendpointStrategy sectorSwitch(BendpointStrategy strategy, Sector sector) {
		switch(sector) {
		case LEFT:
		case TOP_LEFT:
		case BOTTOM_LEFT:
		case RIGHT:
		case TOP_RIGHT:
		case BOTTOM_RIGHT:
			strategy.vertical();
			return strategy;
			
		case TOP:
		case BOTTOM:
			strategy.horizontal();
			return strategy;
		
		default: 
			throw new IllegalArgumentException("Cant define BendpointStrategy for undefined sector");
		}
	}
	
	private static BendpointStrategy typeSwitch(BendpointStrategy strategy, Sector sector, BaseElement sourceElement) {
		
		double treshold = LayoutUtil.getLayoutTreshold(strategy.connection);
		
		if (sourceElement instanceof Gateway) {
			if (treshold <= LayoutUtil.MAGIC_VALUE) {
				strategy.single();
			}
		}
		BaseElement flowElement = (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(strategy.connection);	
		if (flowElement instanceof MessageFlow) {
			switch (sector) {
			case TOP_RIGHT:
			case TOP_LEFT:
			case BOTTOM_RIGHT:
			case BOTTOM_LEFT:
				strategy.horizontal();
				return strategy;
			}
		}
		
		if (sourceElement instanceof BoundaryEvent) {
			Sector relativeSectorToRef = LayoutUtil.getBoundaryEventRelativeSector((Shape) strategy.connection.getStart().getParent());
			
			switch (relativeSectorToRef) {
			case BOTTOM:
			case TOP:
				strategy.single();
				return strategy;
			case TOP_LEFT:
				switch (sector) {
				case BOTTOM:
				case BOTTOM_RIGHT:
				case RIGHT:
				case TOP_RIGHT:
				case TOP_LEFT:
					strategy.single();
					break;
				default:
					break;
				}
				return strategy;
			case BOTTOM_LEFT:
				switch (sector) {
				case TOP:
				case TOP_RIGHT:
				case RIGHT:
				case BOTTOM_RIGHT:
				case BOTTOM_LEFT:
					strategy.single();
					break;
				default:
					break;
				}
				return strategy;
			case TOP_RIGHT:
				switch (sector) {
				case BOTTOM:
				case BOTTOM_LEFT:
				case LEFT:
				case TOP_LEFT:
				case TOP_RIGHT:
					strategy.single();
					break;
				default:
					break;
				}
				return strategy;
			case BOTTOM_RIGHT:
				switch (sector) {
				case TOP:
				case TOP_LEFT:
				case LEFT:
				case BOTTOM_LEFT:
				case BOTTOM_RIGHT:
					strategy.single();
					break;
				default:
					break;
				}
				return strategy;
			default:
				break;
			}
		}
		
		return strategy;
	}

}
