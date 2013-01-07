package org.eclipse.bpmn2.modeler.core.layout;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Docking information for a layouted connection.
 * 
 * @author nico.rehwaldt
 */
public class Docking {

	private Sector sector;
	private Point position;
	private Anchor anchor;

	public Docking(Anchor anchor, Sector sector, Point position) {
		this.sector = sector;
		this.position = position;
		this.anchor = anchor;
	}
	
	public Sector getSector() {
		return sector;
	}
	
	public Anchor getAnchor() {
		return anchor;
	}
	
	public Point getPosition() {
		return position;
	}
	
	/**
	 * Returns the shape the docking applies to
	 * 
	 * @return
	 */
	public Shape getShape() {
		AnchorContainer anchorContainer = anchor.getParent();
		if (anchorContainer instanceof Shape) {
			return (Shape) anchorContainer;
		} else {
			return null;
		}
	}
}
