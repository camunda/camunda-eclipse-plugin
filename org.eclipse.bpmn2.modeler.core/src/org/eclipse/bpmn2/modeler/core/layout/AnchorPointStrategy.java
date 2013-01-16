package org.eclipse.bpmn2.modeler.core.layout;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.location;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class AnchorPointStrategy extends LayoutStrategy<Void, Tuple<Docking, Docking>> {

	protected DockingSwitch start;
	protected DockingSwitch end;

	public AnchorPointStrategy(FreeFormConnection connection) {
		this.connection = connection;
		
		this.start = new DockingSwitch(getStartShape());
		this.end = new DockingSwitch(getEndShape());
	}
	
	public DockingSwitch start() {
		return start;
	}

	public DockingSwitch end() {
		return end;
	}
	
	@Override
	public Tuple<Docking, Docking> doExecute() {
		Tuple<Docking, Docking> dockings = getDockings();
		
		// apply anchor point selections
		connection.setStart(dockings.getFirst().getAnchor());
		connection.setEnd(dockings.getSecond().getAnchor());
		
		// return results
		return dockings;
	}
	
	public Tuple<Docking, Docking> getDockings() {
		Docking startDocking = start.execute();
		Docking endDocking = end.execute();
		
		return new Tuple<Docking, Docking>(startDocking, endDocking);
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
		Sector relativeSectorToRef = 
			LayoutUtil.getBoundaryEventRelativeSector(LayoutUtil.getStartShape(connection));
		
		switch (relativeSectorToRef) {
		case RIGHT:
			switch (targetElementSector) {
			case TOP:
				start.right();
				end.bottom();
				break;
			case BOTTOM:
				start.right();
				end.top();
				break;
			case RIGHT:
				start.right();
				end.left();
				break;
			case BOTTOM_RIGHT:
				start.right();
				end.left();
				break;
			case TOP_RIGHT:
				start.right();
				end.left();
				break;
			default:
				start.right();
				end.right();
			}
			break;
		case LEFT:
			switch (targetElementSector) {
			case TOP:
				start.left();
				end.bottom();
				break;
			case BOTTOM:
				start.left();
				end.top();
				break;
			case LEFT:
				start.left();
				end.right();
				break;
			case BOTTOM_LEFT:
				start.left();
				end.right();
				break;
			case TOP_LEFT:
				start.left();
				end.right();
				break;
			default:
				start.left();
				end.left();
			}
			break;
		case TOP:
		case TOP_RIGHT:
		case TOP_LEFT:
			switch (targetElementSector) {
			case RIGHT:
				start.top();
				end.top();
				break;
			case LEFT: 
				start.top();
				end.top();
				break;
			case BOTTOM:
			case BOTTOM_RIGHT:
			case BOTTOM_LEFT:
				start.top();
				end.top();
				break;
			case TOP_RIGHT:
				start.top();
				end.left();
				break;
			case TOP_LEFT:
				start.top();
				end.right();
				break;
			case TOP:
				start.top();
				end.bottom();
				break;
			default:
				break;
			}
			break;
		case BOTTOM:
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
			switch (targetElementSector) {
			case RIGHT:
				start.bottom();
				end.bottom();
				break;
			case LEFT: 
				start.bottom();
				end.bottom();
				break;
			case BOTTOM_RIGHT:
				start.bottom();
				end.left();
				break;
			case BOTTOM_LEFT:
				start.bottom();
				end.right();
				break;
			case BOTTOM:
				start.bottom();
				end.top();
				break;
			case TOP:
			case TOP_RIGHT:
			case TOP_LEFT:
				start.bottom();
				end.bottom();
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
			// Check if gateway lies directly below or above the connecting shape
			// in that case choose the connecting sector of that shape as Sector.TOP / Sector.BOTTOM
			// and later apply the two bendpoint strategy
			IRectangle startBounds = LayoutUtil.getAbsoluteBounds((Shape) connection.getStart().getParent());
			IRectangle endBounds = LayoutUtil.getAbsoluteBounds((Shape) connection.getEnd().getParent());
			
			ILocation midPoint = location(startBounds.getX() + startBounds.getWidth() / 2, endBounds.getY());
			
			if (LayoutUtil.isContained(endBounds, midPoint, 13)) {
				if (targetElementSector == Sector.BOTTOM) {
					end.top();
				} else {
					end.bottom();
				}
				
				// no more left / right thresholding
				break;
			}
			
			
			if (treshold > 0.0) {
				end.left();
			} else {
				end.right();
			}
			break;
		}
	}
	
	@Override
	public LayoutStrategy<Void, Tuple<Docking, Docking>> getSubStrategy(FreeFormConnection connection, BaseElement sourceElement) {
		
		if (isConnectionBpmnType(connection, Association.class) || isConnectionBpmnType(connection, DataAssociation.class)) {
			return new AssocationAnchorPointStrategy(connection);
		}
		
		return super.getSubStrategy(connection, sourceElement);
	}
	
}
