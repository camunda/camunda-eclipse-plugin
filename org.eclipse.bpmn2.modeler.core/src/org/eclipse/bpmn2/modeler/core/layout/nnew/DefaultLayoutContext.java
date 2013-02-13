package org.eclipse.bpmn2.modeler.core.layout.nnew;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.location;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.bpmn2.modeler.core.layout.AnchorPointStrategy;
import org.eclipse.bpmn2.modeler.core.layout.BendpointStrategy;
import org.eclipse.bpmn2.modeler.core.layout.Docking;
import org.eclipse.bpmn2.modeler.core.layout.LayoutStrategy;
import org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DefaultLayoutContext implements LayoutContext {

	protected FreeFormConnection connection;
	
	private Anchor startAnchor;
	private Anchor endAnchor;
	
	private Shape startShape;
	private Shape endShape;
	
	protected IRectangle startShapeBounds;
	protected IRectangle endShapeBounds;
	
	protected List<Point> connectionPoints;

	private List<ConnectionPart> connectionParts;
	private Set<ConnectionPart> diagonalConnectionParts;

	private boolean relayoutOnRepairFail = false;
	
	/**
	 * Stores the result of the last repair operation
	 */
	private boolean repaired = false;
	
	public DefaultLayoutContext(FreeFormConnection connection, boolean relayoutOnRepairFail) {
		this.connection = connection;
		this.relayoutOnRepairFail = relayoutOnRepairFail;
		
		initSourceAndTarget(connection.getStart(), connection.getEnd());
	}

	private void initSourceAndTarget(Anchor start, Anchor end) {
		startAnchor = start;
		endAnchor = end;

		startShape = (Shape) start.getParent();
		endShape = (Shape) end.getParent();

		startShapeBounds = LayoutUtil.getAbsoluteBounds(startShape);
		endShapeBounds = LayoutUtil.getAbsoluteBounds(endShape);
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
		
		this.diagonalConnectionParts = brokenParts;
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
	
	/**
	 * Returns true if the connection enclosed in this context is
	 * already laid out and can be repaired and false to indicate that 
	 * initial layouting is needed.
	 * 
	 * @return true if the connection is repairable, false otherwise
	 */
	public boolean isRepairable() {
		if (diagonalConnectionParts.isEmpty()) {
			return true;
		}
		
		if (isDirect()) {
			
			// nothing to repair
			return false;
		} else {
			// repair only if we have only one diagonal connection
			return !isManyDiagonal();
		}
	}

	@Override
	public boolean repair() {
		
		boolean repeatNeeded;
		// FIXME this is for testing purposes since we have 
		// there are sometimes loops after move of selections
		
		int cancelCounter = Integer.MAX_VALUE - 2;
		
		do {
			cancelCounter++;
			
			if (cancelCounter == Integer.MAX_VALUE) {
				break;
			}
			
			// recompute so that our layouting check later works
			recomputePointsAndParts();
			
			// apply bendpoint removal
			prune();
			
			if (!isRepairable()) {
				return false;
			}
			
			// repair bendpoints
			repeatNeeded = 
				repairBendpointsAndAnchors() ||
				isRepeatNeeded();
			
		} while (repeatNeeded);
		
		return true;
	}

	protected boolean isRepeatNeeded() {
		
		Tuple<Point, Point> bendpoints = getFirstAndLastBendpoints();

		// are first or last bendpoints moved during repair to close or into one of the shapes
		// if yes, force repeat
		
		if (isContained(startShapeBounds, bendpoints.getFirst()) ||
			isContained(endShapeBounds, bendpoints.getSecond())) {
			
			return true;
		}
		
		return false;
	}

	protected boolean repairBendpointsAndAnchors() {
		
		List<ConnectionPart> parts = connectionParts;
		
		boolean repaired = true;
		
		if (parts.get(0).needsLayout()) {
			repaired &= layoutConnectionParts(parts, true);
		}
		
		repaired &= fixAnchor(startShape, startAnchor, true);
		
		if (parts.get(parts.size() - 1).needsLayout()) {
			repaired &= layoutConnectionParts(parts, false);
		}
		
		repaired &= fixAnchor(endShape, endAnchor, false);
		
		// save repair success result
		setRepaired(repaired);
		
		return !repaired;
	}

	/**
	 * Perform clean up before actual repair starts
	 */
	public void prune() {
		// check if shape lies on one of the connection points
		removeOverlappingBendpoints();
	}

	private void removeOverlappingBendpoints() {
		
		boolean fromStart = true;
		Point removeCandidate = null;
		
		for (final Point p: connection.getBendpoints()) {
			if (isContained(startShapeBounds, p)) {
				fromStart = true;
				removeCandidate = p;
				
				// continue to search (search for the last overlapping bendpoint)
			}
			
			if (isContained(endShapeBounds, p)) {
				
				fromStart = false;
				removeCandidate = p;
				
				// no more search
				break;
			}
		}
		
		if (removeCandidate != null) {
			removeBendpointsUpTo(removeCandidate, fromStart);
			recomputePointsAndParts();
		}
	}
	
	private boolean isContained(IRectangle bounds, Point p) {
		return LayoutUtil.isContained(bounds, location(p), 13);
	}

	private void removeBendpointsUpTo(Point point, boolean fromStart) {
		List<Point> bendpoints = connection.getBendpoints();
		
		System.out.println(
			String.format(
				"[reconnect] prune: remove bendpoints %s up to (%s,%s)", 
				fromStart ? "from start" : "from end", 
				point.getX(), 
				point.getY()));
		
		ListIterator<Point> iterator = bendpoints.listIterator(fromStart ? 0 : bendpoints.size());
		while (fromStart ? iterator.hasNext() : iterator.hasPrevious()) {
			
			Point current = fromStart ? iterator.next() : iterator.previous();
			
			// remove element
			iterator.remove();
			
			if (current.equals(point)) {
				// reached last element to delete
				break;
			}
		}
	}
	
	private boolean fixAnchor(Shape targetShape, Anchor targetShapeAnchor, boolean start) {
		
		// TODO: Do not blindly unset custom anchors
		// instead try to retain them
		
		Anchor centerAnchor = LayoutUtil.getCenterAnchor(targetShape);
		
		// no fix required for center anchor
		if (targetShapeAnchor.equals(centerAnchor)) {
			return true;
		}
		
		// set center anchor
			
		if (start) {
			setNewStartAnchor(centerAnchor);
		} else {
			setNewEndAnchor(centerAnchor);
		}
		
		return false;
	}
	
	private void recomputePointsAndParts() {
		computeConnectionPoints();
		computeConnectionParts();
	}
	
	private void setNewStartAnchor(Anchor anchor) {
		this.startAnchor = anchor;
		this.connection.setStart(anchor);
		
		recomputePointsAndParts();
	}

	private void setNewEndAnchor(Anchor anchor) {
		this.endAnchor = anchor;
		this.connection.setEnd(anchor);
		
		recomputePointsAndParts();
	}
	
	/**
	 * Layouts connection parts from the given direction.
	 * 
	 * @param parts
	 * @param start
	 * 
	 * @return true if the repair operation was successful, false otherwise
	 */
	protected boolean layoutConnectionParts(List<ConnectionPart> parts, boolean start) {
		ConnectionPart next;
		ConnectionPart part;
		
		if (parts.size() < 2) {
			return false;
		}
		
		if (start) {
			part = parts.get(0);
			next = parts.get(1);
		} else {
			part = parts.get(parts.size() - 1);
			next = parts.get(parts.size() - 2);
		}
		
		if (next.needsLayout()) {
			return false;
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
		
		return true;
	}

	/**
	 * returns true if the
	 * 
	 * @return true if the connection needs relayout, false otherwise
	 */
	public boolean needsLayout() {
		
		// recompute what is going on
		recomputePointsAndParts();
		
		// we do not want to layout many diagonal (aka custom layouted) connections
		if (isManyDiagonal()) {
			return false;
		}
		
		// we do want to layout other non-repairable connections, though
		if (!isRepaired()) {
			return isRelayoutOnRepairFail();
		}
		
		if (hasOverlappingBendpoints()) {
			return isRelayoutOnRepairFail();
		}
		
		Tuple<Point, Point> bendpoints = getFirstAndLastBendpoints();
		
		Tuple<Docking, Docking> dockings = getDockings();
		if (dockings != null) {
			Sector afterRepairStartSector = LayoutUtil.getSector(ConversionUtil.location(bendpoints.getFirst()), startShapeBounds);
			Sector afterRepairEndSector = LayoutUtil.getSector(ConversionUtil.location(bendpoints.getSecond()), endShapeBounds);
			
			if (needsLayoutByDockings(dockings, afterRepairStartSector, afterRepairEndSector)) {
				return isRelayoutOnRepairFail();
			}
		}
		
		return false;
	}

	/**
	 * Answer true if the connection has overlapping bendpoints
	 * 
	 * @return
	 */
	private boolean hasOverlappingBendpoints() {
		Point last = null;
		
		for (Point point: connectionPoints) {
			if (last != null) {
				if (GraphicsUtil.pointsEqual(point, last)) {
					return true;
				}
			}
			
			last = point;
		}
		
		return false;
	}

	protected void setRepaired(boolean repaired) {
		this.repaired = repaired;
	}
	
	protected boolean isRepaired() {
		return repaired && diagonalConnectionParts.isEmpty();
	}

	/**
	 * Return whether we have many diagonal connection parts
	 * @return
	 */
	protected boolean isManyDiagonal() {
		return diagonalConnectionParts.size() > 1;
	}

	protected boolean isDirect() {
		return connection.getBendpoints().isEmpty();
	}
	
	protected boolean isRelayoutOnRepairFail() {
		return relayoutOnRepairFail;
	}

	/**
	 * Returns true if the context needs new layout based on the resulting dockings.
	 * 
	 * @param dockings
	 * 
	 * @param afterRepairEndSector 
	 * @param afterRepairStartSector 
	 * 
	 * @return
	 */
	protected boolean needsLayoutByDockings(Tuple<Docking, Docking> dockings, Sector afterRepairStartSector, Sector afterRepairEndSector) {
		return false;
	}
	
	protected Tuple<Point, Point> getFirstAndLastBendpoints() {
		Point firstBendpoint = connectionPoints.get(1);
		Point lastBendpoint = connectionPoints.get(connectionPoints.size() - 2);
		
		return new Tuple<Point, Point>(firstBendpoint, lastBendpoint);
	}
	
	@Override
	public void layout() {
		layoutBendpoints(layoutAnchors());
	}
	
	public Tuple<Docking, Docking> layoutAnchors() {
		return createAnchorPointStrategy().execute();
	}
	
	public void layoutBendpoints(Tuple<Docking, Docking> connectionDocking) {
		// change bendpoints only when anchor points were applied
		if (connectionDocking != null) {
			LayoutStrategy.build(BendpointStrategy.class, connection, connectionDocking).execute();
		}
	}
	
	public Tuple<Docking, Docking> getDockings() {
		return createAnchorPointStrategy().getDockings();
	}

	private AnchorPointStrategy createAnchorPointStrategy() {
		return AnchorPointStrategy.build(AnchorPointStrategy.class, connection, null);
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