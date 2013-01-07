package org.eclipse.bpmn2.modeler.core.layout;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Docking strategy
 * 
 * @author nico.rehwaldt
 */
public class DockingSwitch extends Strategy<Docking> {
	
	private Shape dockingShape;
	
	private Sector sector;
	
	public DockingSwitch(Shape dockingShape) {
		this.dockingShape = dockingShape;
	}
	
	public DockingSwitch bottom() {
		setSector(Sector.BOTTOM);
		return this;
	}

	public DockingSwitch top() {
		setSector(Sector.TOP);
		return this;
	}

	public DockingSwitch left() {
		setSector(Sector.LEFT);
		return this;
	}

	public DockingSwitch right() {
		setSector(Sector.RIGHT);
		return this;
	}

	public Docking execute() {
		Anchor anchor = getDockingAnchor(dockingShape);
		Point position = getDockingPosition(dockingShape);
		
		return new Docking(anchor, sector, position);
	}

	protected Point getDockingPosition(Shape dockingShape) {
		
		IRectangle dockingBounds = LayoutUtil.getAbsoluteRectangle(dockingShape);
		
		int x = dockingBounds.getX();
		int y = dockingBounds.getY();
		
		int cx = x + dockingBounds.getWidth() / 2;
		int cy = y + dockingBounds.getHeight() / 2;
		
		int height = dockingBounds.getHeight();
		int width = dockingBounds.getWidth();
		
		switch (sector) {
		case TOP:
			return point(cx, y);
		case LEFT:
			return point(x, cy);
		case BOTTOM:
			return point(cx, y + height);
		case RIGHT:
			return point(x + width, cy);
		
		default:
			throw new IllegalArgumentException("Cannot handle Sector." + sector);
		}
	}

	protected Anchor getDockingAnchor(Shape shape) {
		
		// always use center anchors for auto layouting from now on
		return LayoutUtil.getCenterAnchor(shape);
	}

	protected void setSector(Sector sector) {
		this.sector = sector;
	}
}
