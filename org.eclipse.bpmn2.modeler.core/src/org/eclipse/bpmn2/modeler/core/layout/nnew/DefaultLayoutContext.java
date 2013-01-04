package org.eclipse.bpmn2.modeler.core.layout.nnew;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.anchorLocation;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.location;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.bpmn2.modeler.core.layout.AnchorPointStrategy;
import org.eclipse.bpmn2.modeler.core.layout.BendpointStrategy;
import org.eclipse.bpmn2.modeler.core.layout.LayoutStrategy;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DefaultLayoutContext implements LayoutContext {

	private FreeFormConnection connection;

	private List<ConnectionPart> connectionParts;
	
	private Set<ConnectionPart> brokenConnectionParts;
	
	private Anchor startAnchor;
	private Anchor endAnchor;
	
	private Shape startShape;
	private Shape endShape;
	
	private IRectangle startShapeBounds;
	private IRectangle endShapeBounds;
	
	private List<Point> connectionPoints;

	public DefaultLayoutContext(FreeFormConnection connection) {
		this.connection = connection;
		
		initSourceAndTarget(connection.getStart(), connection.getEnd());
		
		computeConnectionPoints();
		
		// compute connection parts
		computeConnectionParts();
	}
	
	private void initSourceAndTarget(Anchor start, Anchor end) {
		startAnchor = start;
		endAnchor = end;

		startShape = (Shape) start.getParent();
		endShape = (Shape) end.getParent();

		startShapeBounds = LayoutUtil.getAbsoluteRectangle(startShape);
		endShapeBounds = LayoutUtil.getAbsoluteRectangle(endShape);
	}

	protected void computeConnectionParts() {
		
		// compute connection points
		List<Point> points = connectionPoints;
		
		List<ConnectionPart> parts = new ArrayList<ConnectionPart>();
		Set<ConnectionPart> brokenParts = new HashSet<ConnectionPart>();
		
		Point current = points.get(0);
		
		for (int i = 1; i < points.size(); i++) {
			Point p = points.get(i);
			
			ConnectionPart part = new ConnectionPart(current, p);
			
			parts.add(part);
			
			if (part.needsLayout()) {
				brokenParts.add(part);
			}
			
			current = p;
		}
		
		this.brokenConnectionParts = brokenParts;
		this.connectionParts = parts;
	}

	protected void computeConnectionPoints() {
		ArrayList<Point> points = new ArrayList<Point>();

		ILocation startLocation = LayoutUtil.getAnchorLocation(startAnchor);
		ILocation endLocation = LayoutUtil.getAnchorLocation(endAnchor);
		
		points.add(point(startLocation));
		
		// assumption: we have only two anchors (start and end)
		for (Point bendpoint: connection.getBendpoints()) {
			points.add(bendpoint);
		}
		
		points.add(point(endLocation));
		
		this.connectionPoints = points;
	}
	
	@Override
	public boolean isLayouted() {
		
		if (brokenConnectionParts.isEmpty()) {
			return true;
		} 
		
		if (connection.getBendpoints().isEmpty()) {
			
			// nothing to repair
			return false;
		} else {
			if (brokenConnectionParts.size() > 1) {
				
				// too many broken parts
				return false;
			} else {
				// only one connection part is broken --> layout change
				return true;
			}
		}
	}
	
	@Override
	public boolean repair() {
		List<ConnectionPart> parts = connectionParts;

		boolean repaired = true;
		ConnectionPartLayoutResult firstPartLayout = null;
		
		if (parts.get(0).needsLayout()) {
			firstPartLayout = layoutConnectionParts(parts, true);
			
			repaired = repaired && 
					firstPartLayout.partsLayouted;
		}
		
		repaired = repaired && 
				fixOptimalAnchor(firstPartLayout, startShape, startAnchor, connectionPoints.get(1));
		
		ConnectionPartLayoutResult lastPartLayout = null;
		
		if (parts.get(parts.size() - 1).needsLayout()) {
			lastPartLayout = layoutConnectionParts(parts, false);
			
			repaired = repaired && lastPartLayout.partsLayouted;
		}
		
		repaired = repaired &&
				fixOptimalAnchor(lastPartLayout, endShape, endAnchor, connectionPoints.get(connectionPoints.size() - 2));
		
		return repaired && isConnectionRepaired();
	}

	private boolean fixOptimalAnchor(ConnectionPartLayoutResult partLayout, Shape targetShape, Anchor targetShapeAnchor, Point firstBendpoint) {
		SectorAdaption sectorAdaption = adaptVerticalRepairSector(LayoutUtil.getSector(location(firstBendpoint), LayoutUtil.getShapeCenter(targetShape)), partLayout);
		Sector requiredAnchorSector = sectorAdaption.getResultSector();
	
		if (requiredAnchorSector == Sector.UNDEFINED) {
			return false;
		}
		
		// no fix required for center anchor
		if (targetShapeAnchor.equals(LayoutUtil.getCenterAnchor(targetShape))) {
			return true;
		}
		
		// check if fix for boundary anchors is required 
		Anchor anchor = AnchorUtil.getAnchor(targetShape, anchorLocation(requiredAnchorSector));
		
		if (anchor == null) {
			throw new IllegalStateException("Anchor is null");
		}
		
		if (connection.getEnd().equals(targetShapeAnchor)) {
			connection.setEnd(anchor);
		} else {
			connection.setStart(anchor);
		}
		
		if (sectorAdaption.adapted) { // we adapted the anchor, we need to update the repair candidate
			FixPointAnchor fixPointAnchor = (FixPointAnchor) anchor;
			partLayout.getRepairCandidate().setX(LayoutUtil.getAnchorLocation(fixPointAnchor).getX());
		}
		
		return true;
	}
	
	private class SectorAdaption {
		final Boolean adapted;
		final Sector resultSector;
		
		public SectorAdaption(Boolean adapted, Sector resultSector) {
			super();
			this.adapted = adapted;
			this.resultSector = resultSector;
		}
		
		public Boolean getAdapted() {
			return adapted;
		}
		public Sector getResultSector() {
			return resultSector;
		}
	}

	private SectorAdaption adaptVerticalRepairSector(Sector sector, ConnectionPartLayoutResult partLayout) {
		if (partLayout == null) {
			return new SectorAdaption(false, sector);
		}
		
		if (partLayout.partsLayouted && partLayout.getDirection() == Direction.VERTICAL) {
			switch (sector) {
				case TOP_LEFT:
				case TOP_RIGHT:
					return new SectorAdaption(true, sector.TOP);
				case BOTTOM_LEFT:
				case BOTTOM_RIGHT:
					return new SectorAdaption(true, sector.BOTTOM);
			}
		}

		return new SectorAdaption(false, sector);
	}
	
	private class ConnectionPartLayoutResult {
		final private Direction direction;
		final private Boolean partsLayouted;
		final private List<ConnectionPart> parts;
		final private Point repairCandidate;
		final private Point reference;
		final private Boolean start;
		
		public ConnectionPartLayoutResult(List<ConnectionPart> parts, Boolean start, Boolean partsLayouted) {
			this.partsLayouted = partsLayouted;
			this.direction = null;
			this.parts = parts;
			this.repairCandidate = null;
			this.reference = null;
			this.start = start;
		}
		
		public ConnectionPartLayoutResult(List<ConnectionPart> parts, Boolean start, Boolean partsLayouted, Direction direction, Point repairCandidate, Point reference) {
			this.direction = direction;
			this.partsLayouted = partsLayouted;
			this.parts = parts;
			this.repairCandidate = repairCandidate;
			this.reference = reference;
			this.start = start;
		}
		
		public Direction getDirection() {
			return direction;
		}
		
		public Boolean getPartsLayouted() {
			return partsLayouted;
		}
		
		public List<ConnectionPart> getParts() {
			return parts;
		}
		
		public Point getRepairCandidate() {
			return repairCandidate;
		}
		
		public Point getReference() {
			return reference;
		}
		
		public Boolean getStart() {
			return start;
		}
		
	}

	protected ConnectionPartLayoutResult layoutConnectionParts(List<ConnectionPart> parts, boolean start) {
		ConnectionPart next;
		ConnectionPart part;
		
		if (start) {
			part = parts.get(0);
			next = parts.get(1);
		} else {
			part = parts.get(parts.size() - 1);
			next = parts.get(parts.size() - 2);
		}

		if (next.needsLayout()) {
			return new ConnectionPartLayoutResult(parts, start, true);
		}
		
		Direction direction = part.computeDirection(next);
		
		Point repairCandidate = part.getRepairCandidate(start);
		Point reference = part.getReference(start);
		
		switch (direction) {
		case HORIZONTAL: 
			repairCandidate.setY(reference.getY());
			break;
		case VERTICAL:
			repairCandidate.setX(reference.getX());
			break;
		}
		
		return new ConnectionPartLayoutResult(parts, start, true, direction, repairCandidate, reference);
	}

	protected boolean isConnectionRepaired() {
		
		boolean repaired = true;
		
		List<Point> points = connectionPoints;
		
		Point firstBendpoint = points.get(1);
		Point lastBendpoint = points.get(points.size() - 2);

		repaired &= !LayoutUtil.isContained(startShapeBounds, location(firstBendpoint), 13);
		
		repaired &= !LayoutUtil.isContained(endShapeBounds, location(lastBendpoint), 13);
		
		// see if anchor point is on start shape bounds
		repaired &= (startShapeBounds.getX() != firstBendpoint.getX() || startShapeBounds.getX() + startShapeBounds.getWidth() != firstBendpoint.getX());
		repaired &= (startShapeBounds.getY() != firstBendpoint.getY() || startShapeBounds.getY() + startShapeBounds.getHeight() != firstBendpoint.getY());
		
		return repaired;
	}

	@Override
	public void layout() {
		LayoutStrategy.build(AnchorPointStrategy.class, connection).execute();
		LayoutStrategy.build(BendpointStrategy.class, connection).execute();
	}

	/**
	 * Enum indicating the layed out direction of a connection part in a layouted connection
	 * 
	 * @author nico.rehwaldt
	 */
	protected static enum Direction {
		
		VERTICAL, 
		HORIZONTAL, 
		UNSPECIFIED, 
		UNKNOWN;
		
		public Direction inverse() {
			switch (this) {
			case VERTICAL: 
				return HORIZONTAL;
			case HORIZONTAL:
				return VERTICAL;
			default: 
				return this;
			}
		}
	}
	
	/**
	 * Connection part
	 * 
	 * @author nico.rehwaldt
	 */
	protected class ConnectionPart {
		
		private Point first;
		private Point second;
		
		public ConnectionPart(Point first, Point second) {
			this.first = first;
			this.second = second;
		}
		
		/**
		 * Compute own direction based on the next connection part
		 * 
		 * @param next
		 * @return
		 */
		public Direction computeDirection(ConnectionPart next) {
			if (next != null) {
				return next.getComputedDirection().inverse();
			} else {
				return Direction.UNKNOWN;
			}
		}
		
		public Direction getComputedDirection() {
			if (coordinatesEqual(first.getX(), second.getX())) {
				return Direction.VERTICAL;
			} else 
			if (coordinatesEqual(first.getY(), second.getY())) {
				return Direction.HORIZONTAL;
			} else {
				return Direction.UNKNOWN;
			}
		}
		
		protected boolean coordinatesEqual(int a, int b) {
			return Math.abs(a - b) < 2;
		}
		
		public Point getReference(boolean start) {
			return start ? first : second;
		}
		
		public Point getRepairCandidate(boolean start) {
			return start ? second : first;
		}
		
		public boolean needsLayout() {
			return getComputedDirection() == Direction.UNKNOWN;
		}
	}
}