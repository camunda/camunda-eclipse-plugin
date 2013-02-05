package org.eclipse.bpmn2.modeler.core.layout.util;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.eclipse.bpmn2.modeler.core.layout.util.Vector.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;

public class ConnectionUtil {

	/**
	 * Returns the point at the given length for a number of waypoints comprising a connection.
	 * 
	 * @param waypoints
	 * @param length
	 * 
	 * @return
	 */
	public static Point getPointAtLength(List<Point> waypoints, double length) {
		
		assertMinWaypoints(waypoints);
		
		Iterator<Point> waypointsIterator = waypoints.iterator();
		Point last = waypointsIterator.next();

		double currentLength = 0.0;
		
		while (waypointsIterator.hasNext()) {
			
			Point next = waypointsIterator.next();
			Point lastToNext = substract(next, last);
			
			double segmentLength = length(lastToNext);
			
			if (currentLength + segmentLength > length) {
				// we need to find point on current dist
				
				Point pointOnLineVector = multiply(normalized(lastToNext), currentLength + segmentLength - length);
				return add(last, pointOnLineVector);
			}
			
			currentLength += segmentLength;
		}
		
		return null;
	}

	public static double getLength(List<Point> waypoints) {

		assertMinWaypoints(waypoints);
		
		Iterator<Point> waypointsIterator = waypoints.iterator();
		Point last = waypointsIterator.next();

		double currentLength = 0.0;
		
		while (waypointsIterator.hasNext()) {
			
			Point next = waypointsIterator.next();
			Point lastToNext = substract(next, last);
			
			double segmentLength = length(lastToNext);
			currentLength += segmentLength;
		}
		
		return currentLength;
	}
	
	public static Point getMidPoint(List<Point> waypoints) {
		assertMinWaypoints(waypoints);
		
		double length = getLength(waypoints);
		return getPointAtLength(waypoints, length / 2);
	}
	
	public static double getLengthAtPoint(List<Point> waypoints, Point point) {
		assertMinWaypoints(waypoints);
		
	}
	
	public static Point getClosestPointOnConnection(List<Point> waypoints, Point point) {
		
	}
	
	public static Point getPointAtLength(Connection connection, double length) {
		SegmentInfo segmentInfo = getSegmentInfo(connection);

		return ConversionUtil.point(getRelativeConnectionPoint(segmentInfo, length / segmentInfo.getFullLength()));
	}

	public static SegmentInfo getSegmentInfo(Connection connection) {
		BPMNEdge bpmnEdge = BusinessObjectUtil.getFirstElementOfType(connection, BPMNEdge.class);

		Assert.isNotNull(bpmnEdge);

		return getSegmentInfo(bpmnEdge);
	}

	public static SegmentInfo getSegmentInfo(BPMNEdge bpmnEdge) {
		double fullLength = 0;
		List<Segment> segments = new ArrayList<Segment>();

		if (bpmnEdge != null) {

			for (int pointIndex = 0; pointIndex < bpmnEdge.getWaypoint().size() - 1; pointIndex++) {
				// add twice, put weight on the bendpoints
				org.eclipse.dd.dc.Point segmentStart = bpmnEdge.getWaypoint().get(pointIndex);
				org.eclipse.dd.dc.Point segmentEnd = bpmnEdge.getWaypoint().get(pointIndex + 1);
				Vector segmentVector = new Vector(segmentEnd.getX() - segmentStart.getX(), segmentEnd.getY()
						- segmentStart.getY());
				double startLength = fullLength;
				fullLength += segmentVector.getLength();
				segments.add(new Segment(ConversionUtil.point(segmentStart.getX(), segmentStart.getY()), ConversionUtil
						.point(segmentEnd.getX(), segmentEnd.getY()), segmentVector, startLength, fullLength));
			}
		}

		return new SegmentInfo(segments, fullLength);
	}

	/**
	 * get a point on a connection relative to the connection length
	 * 
	 * @param segmentInfo
	 *            of the connection
	 * @param lengthFactor
	 *            on the connection should between 0.0 and 1.0 , 0.0 is the
	 *            start of the connection 1.0 is the end, 0.5 is the midpoint
	 *            with respect to the full connection length
	 * 
	 * @return the point at on the connection at the length defined by the
	 *         lengthFactor
	 */
	public static ILocation getRelativeConnectionPoint(SegmentInfo segmentInfo, double lengthFactor) {
		double searchLength = segmentInfo.getFullLength() * lengthFactor;
		Segment midPointSegment = null;

		for (int segmentIndex = 0; segmentIndex < segmentInfo.getSegments().size(); segmentIndex++) {
			Segment segment = segmentInfo.getSegments().get(segmentIndex);

			if (segment.getAccummulatedLength() > searchLength) {
				midPointSegment = segmentInfo.getSegments().get(segmentIndex);
				break;
			}
		}

		if (midPointSegment == null) { // must be in last segment
			midPointSegment = segmentInfo.getSegments().get(segmentInfo.getSegments().size() - 1);
		}

		double searchFactor = searchLength - midPointSegment.getStartLength();
		Vector midPointVector = midPointSegment.getVector().getDivided(midPointSegment.getVector().getLength())
				.getMultiplied(searchFactor);

		return ConversionUtil.location(midPointSegment.getSegmentStart().getX() + midPointVector.x, midPointSegment
				.getSegmentStart().getY() + midPointVector.y);
	}

	protected static ILocation getConnectionMidPoint(SegmentInfo segmentInfo) {
		return getRelativeConnectionPoint(segmentInfo, 0.5);
	}

	/**
	 * Returns the circle line intersection points.
	 * 
	 * Adapted from {@link http
	 * ://stackoverflow.com/questions/13053061/circle-line-intersection-points}
	 * 
	 * @param linePointA
	 * @param linePointB
	 * @param center
	 * @param radius
	 * @return
	 */
	protected static List<Point> getCircleLineIntersectionPoint(Point linePointA, Point linePointB, Point center,
			double radius) {

		double baX = linePointB.getX() - linePointA.getX();
		double baY = linePointB.getY() - linePointA.getY();
		double caX = center.getX() - linePointA.getX();
		double caY = center.getY() - linePointA.getY();

		double a = baX * baX + baY * baY;
		double bBy2 = baX * caX + baY * caY;
		double c = caX * caX + caY * caY - radius * radius;

		double pBy2 = bBy2 / a;
		double q = c / a;

		double disc = pBy2 * pBy2 - q;
		if (disc < 0) {
			return Collections.emptyList();
		}
		// if disc == 0 ... dealt with later
		double tmpSqrt = Math.sqrt(disc);
		double abScalingFactor1 = -pBy2 + tmpSqrt;
		double abScalingFactor2 = -pBy2 - tmpSqrt;

		Point p1 = ConversionUtil.point(linePointA.getX() - baX * abScalingFactor1, linePointA.getY() - baY
				* abScalingFactor1);
		if (disc == 0) { // abScalingFactor1 == abScalingFactor2
			return Collections.singletonList(p1);
		}
		Point p2 = ConversionUtil.point(linePointA.getX() - baX * abScalingFactor2, linePointA.getY() - baY
				* abScalingFactor2);
		return Arrays.asList(p1, p2);
	}

	protected static double getRelativeLengthOnSegments(Point point, SegmentInfo segmentInfo, double searchRadius) {
		for (Segment segment : segmentInfo.getSegments()) {
			List<Point> intersections = getCircleLineIntersectionPoint(segment.getSegmentStart(), segment.getSegmentEnd(), point, new Double(searchRadius));

			if (intersections.isEmpty()) {
				continue;
			}

			if (intersections.size() > 1) {
				Point interSectionsMidpoint = point(
						Math.abs((intersections.get(0).getX() + intersections.get(1).getX()) / 2),
						Math.abs((intersections.get(0).getY() + intersections.get(1).getY()) / 2));

				return segment.getStartLength()
						+ new Vector(interSectionsMidpoint.getX() - segment.getSegmentStart().getX(),
								interSectionsMidpoint.getY() - segment.getSegmentStart().getY()).getLength();

			} else {
				return segment.getStartLength() + searchRadius;
			}

		}
		return -1.0;
	}

	/**
	 * Returns true if the reference point is contained on the segment defined by start and end.
	 * 
	 * @param segStart
	 * @param segEnd
	 * @param reference
	 * 
	 * @return
	 */
	public static boolean isContainedInSegment(Point segStart, Point segEnd, Point reference) {
		
		double startToRefDistance = distance(segStart, reference);
		Point startToEndVector = substract(segEnd, segStart);
		
		
	}
	
	public static void getConnectionLengthAtPoint(Connection connection, Point point) {

		double labelReferenceLength = LayoutUtil.getRelativeLengthOnSegments(
				point(LayoutUtil.getRectangleCenter(rectangle(midPoint.getX(), midPoint.getY(), width, height))),
				segmentInfo, width + 15.0);
	}

	private static void assertMinWaypoints(List<Point> waypoints) {
		Assert.isLegal(waypoints.size() > 1, "Must have minimum two waypoints");
	}
}
