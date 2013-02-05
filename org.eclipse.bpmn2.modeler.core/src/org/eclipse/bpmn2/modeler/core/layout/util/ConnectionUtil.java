package org.eclipse.bpmn2.modeler.core.layout.util;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.layout.util.Vector.add;
import static org.eclipse.bpmn2.modeler.core.layout.util.Vector.asString;
import static org.eclipse.bpmn2.modeler.core.layout.util.Vector.distance;
import static org.eclipse.bpmn2.modeler.core.layout.util.Vector.length;
import static org.eclipse.bpmn2.modeler.core.layout.util.Vector.multiply;
import static org.eclipse.bpmn2.modeler.core.layout.util.Vector.normalized;
import static org.eclipse.bpmn2.modeler.core.layout.util.Vector.substract;
import static org.eclipse.bpmn2.modeler.core.layout.util.Vector.midPoint;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.mm.algorithms.styles.Point;

/**
 * Utility methods for connections.
 * 
 * @author nico.rehwaldt
 */
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

	/**
	 * Returns the length of a connection defined by its way points. 
	 * 
	 * @param waypoints
	 * @return
	 */
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

	/**
	 * Returns the mid point on a connection
	 * 
	 * @param waypoints
	 * @return
	 */
	public static Point getMidPoint(List<Point> waypoints) {
		assertMinWaypoints(waypoints);
		
		double length = getLength(waypoints);
		return getPointAtLength(waypoints, length / 2);
	}
	
	/**
	 * Returns the mid point on a connection
	 * 
	 * @param waypoints
	 * @return
	 */
	public static Point getPointRelativeToLength(List<Point> waypoints, double factor) {
		assertMinWaypoints(waypoints);
		
		double length = getLength(waypoints);
		return getPointAtLength(waypoints, length * factor);
	}
	
	/**
	 * Returns the connection length at a given point
	 * 
	 * @param waypoints
	 * @param point
	 * @return
	 */
	public static double getLengthAtPoint(List<Point> waypoints, Point point) {
		assertMinWaypoints(waypoints);
		
		Iterator<Point> waypointsIterator = waypoints.iterator();
		Point last = waypointsIterator.next();

		double currentLength = 0.0;
		
		while (waypointsIterator.hasNext()) {
			
			Point next = waypointsIterator.next();
			Point lastToNext = substract(next, last);
			
			if (isContainedInSegment(last, next, point)) { 
				return currentLength + distance(last, point);
			}
			
			currentLength += length(lastToNext);
		}
		
		throw new IllegalArgumentException(String.format("Point <(%s)> not contained on Connection(points=<%s>)", asString(point), asString(waypoints)));
	}
	
	/**
	 * Returns the closes point to a point on a connection
	 * 
	 * @param waypoints
	 * @param point
	 * 
	 * @return
	 */
	public static Point getClosestPointOnConnection(List<Point> waypoints, Point point) {
		assertMinWaypoints(waypoints);
		
		Iterator<Point> waypointsIterator = waypoints.iterator();
		
		Point last = waypointsIterator.next();
		Point closestPoint = last;
		
		while (waypointsIterator.hasNext()) {

			Point next = waypointsIterator.next();
			
			if (distance(point, closestPoint) > distance(point, next)) {
				closestPoint = point;
			}
			
			Point pointOnLineSegment = getClosesPointOnLineSegment(last, next, point);
			
			if (distance(point, closestPoint) > distance(pointOnLineSegment, point)) {
				closestPoint = pointOnLineSegment;
			}
		}
		
		return closestPoint;
	}

	private static Point getClosesPointOnLineSegment(Point l1, Point l2, Point point) {
		
		double radius = Math.min(distance(l1, point), distance(l2, point));
		List<Point> intersectionPoints = getCircleLineIntersectionPoint(l1, l2, point, radius);
		
		if (intersectionPoints.size() == 0) {
			return intersectionPoints.get(0);
		}
		
		if (intersectionPoints.size() == 2) {
			Point midPoint = midPoint(intersectionPoints.get(0), intersectionPoints.get(1));
			
			if (isContainedInSegment(l2, l2, midPoint)) {
				return midPoint;
			}
		}
		
		return null;
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
		
		if (equal(segStart, reference) || equal(segEnd, reference)) {
			return true;
		}
		
		double startToRefDistance = distance(segStart, reference);
		Point startToEndVector = substract(segEnd, segStart);
		
		Point startToRefVector = multiply(normalized(startToEndVector), startToRefDistance);
		
		Point shouldBeRef = add(segStart, startToRefVector);
		
		return equal(reference, shouldBeRef);
	}
	
	/**
	 * Returns the circle line intersection points.
	 * 
	 * Adapted from {@link http://stackoverflow.com/questions/13053061/circle-line-intersection-points}.
	 * 
	 * @param l1
	 * @param l2
	 * 
	 * @param circleCenter
	 * @param circleRadius
	 * @return
	 */
	protected static List<Point> getCircleLineIntersectionPoint(Point l1, Point l2, Point circleCenter, double circleRadius) {

		double baX = l2.getX() - l1.getX();
		double baY = l2.getY() - l1.getY();
		double caX = circleCenter.getX() - l1.getX();
		double caY = circleCenter.getY() - l1.getY();

		double a = baX * baX + baY * baY;
		double bBy2 = baX * caX + baY * caY;
		double c = caX * caX + caY * caY - circleRadius * circleRadius;

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

		Point p1 = point(l1.getX() - baX * abScalingFactor1, l1.getY() - baY * abScalingFactor1);
		if (disc == 0) { // abScalingFactor1 == abScalingFactor2
			return Collections.singletonList(p1);
		}
		
		Point p2 = point(l1.getX() - baX * abScalingFactor2, l1.getY() - baY * abScalingFactor2);
		
		return Arrays.asList(p1, p2);
	}

	////// private utilities /////////////////////////////////////////////////////
	
	private static boolean equal(Point a, Point b) {
		return Vector.equal(a, b, 0.01);
	}
	
	private static void assertMinWaypoints(List<Point> waypoints) {
		Assert.isLegal(waypoints.size() > 1, "Must have minimum two waypoints");
	}
}
