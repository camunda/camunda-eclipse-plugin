package org.eclipse.bpmn2.modeler.core.layout.util;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.asVectors;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.vector;
import static org.eclipse.bpmn2.modeler.core.layout.util.VectorUtil.add;
import static org.eclipse.bpmn2.modeler.core.layout.util.VectorUtil.asString;
import static org.eclipse.bpmn2.modeler.core.layout.util.VectorUtil.distance;
import static org.eclipse.bpmn2.modeler.core.layout.util.VectorUtil.length;
import static org.eclipse.bpmn2.modeler.core.layout.util.VectorUtil.midPoint;
import static org.eclipse.bpmn2.modeler.core.layout.util.VectorUtil.multiply;
import static org.eclipse.bpmn2.modeler.core.layout.util.VectorUtil.normalized;
import static org.eclipse.bpmn2.modeler.core.layout.util.VectorUtil.substract;
import static org.eclipse.bpmn2.modeler.core.utils.PictogramElementPropertyUtil.set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.features.PropertyNames;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Shape;

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
		
		List<Vector> connectionVectors = asVectors(waypoints);
		
		Iterator<Vector> connectionVectorsIterator = connectionVectors.iterator();
		Vector last = connectionVectorsIterator.next();
		
		double currentLength = 0.0;
		
		while (connectionVectorsIterator.hasNext()) {
			
			Vector next = connectionVectorsIterator.next();
			Vector lastToNext = substract(next, last);
			
			double segmentLength = length(lastToNext);
			
			if (currentLength + segmentLength > length) {
				// we need to find point on current dist
				
				Vector pointOnLineVector = multiply(normalized(lastToNext), length - currentLength);
				return point(add(last, pointOnLineVector));
			}
			
			currentLength += segmentLength;
			last = next;
		}
		
		if (currentLength == length) {
			// return last waypoint if length == total length
			return waypoints.get(waypoints.size() - 1);
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

		List<Vector> connectionVectors = asVectors(waypoints);
		
		Iterator<Vector> connectionVectorsIterator = connectionVectors.iterator();
		Vector last = connectionVectorsIterator.next();

		double currentLength = 0.0;
		
		while (connectionVectorsIterator.hasNext()) {
			
			Vector next = connectionVectorsIterator.next();
			Vector lastToNext = substract(next, last);
			
			currentLength += length(lastToNext);
			last = next;
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
		return getPointAtLength(waypoints, length / 100 * factor);
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
		
		Vector pointAsVector = vector(point);
		List<Vector> connectionVectors = asVectors(waypoints);
		
		Iterator<Vector> connectionVectorsIterator = connectionVectors.iterator();
		Vector last = connectionVectorsIterator.next();

		double currentLength = 0.0;
		
		while (connectionVectorsIterator.hasNext()) {
			
			Vector next = connectionVectorsIterator.next();
			Vector lastToNext = substract(next, last);
			
			if (isContainedInSegment(last, next, pointAsVector)) { 
				return currentLength + distance(last, pointAsVector);
			}
			
			currentLength += length(lastToNext);
			last = next;
		}
		
		throw new IllegalArgumentException(String.format("Point <%s> not contained part of Connection(points=<%s>)", asString(pointAsVector), asString(connectionVectors)));
	}
	
	public static List<Point> getPointsTo(List<Point> waypoints, Point point) {

		List<Point> pointsTo = new ArrayList<Point>();

		point = getClosestPointOnConnection(waypoints, point);
		
		List<Vector> connectionVectors = asVectors(waypoints);
		Vector pointAsVector = vector(point);

		Iterator<Point> waypointsIterator = waypoints.iterator();
		Iterator<Vector> connectionVectorsIterator = connectionVectors.iterator();
		
		Vector last = connectionVectorsIterator.next();
		Point lastWaypoint = waypointsIterator.next();
		
		pointsTo.add(lastWaypoint);
		
		while (connectionVectorsIterator.hasNext()) {
			
			Vector next = connectionVectorsIterator.next();
			
			// keep waypoints iterator in sync
			// for access of original waypoint
			Point nextWaypoint = waypointsIterator.next();
			
			if (isContainedInSegment(last, next, pointAsVector)) { 
				break;
			}

			pointsTo.add(nextWaypoint);
			last = next;
		}
		
		return pointsTo;
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

		List<Vector> connectionVectors = asVectors(waypoints);
		Vector pointAsVector = vector(point);
		
		Iterator<Vector> connectionVectorsIterator = connectionVectors.iterator();
		Vector last = connectionVectorsIterator.next();
		
		Vector closestPoint = last;
		
		while (connectionVectorsIterator.hasNext()) {

			Vector next = connectionVectorsIterator.next();

			if (equal(next, last)) {
				continue;
			}
			
			if (distance(pointAsVector, closestPoint) > distance(pointAsVector, next)) {
				closestPoint = next;
			}
			
			Vector pointOnLineSegment = getClosesPointOnLineSegment(last, next, pointAsVector);
			if (pointOnLineSegment != null) {
				if (distance(pointAsVector, closestPoint) > distance(pointAsVector, pointOnLineSegment)) {
					closestPoint = pointOnLineSegment;
				}
			}
			
			last = next;
		}
		
		return point(closestPoint);
	}

	protected static Vector getClosesPointOnLineSegment(Vector l1, Vector l2, Vector point) {
		
		double radius = Math.min(distance(l1, point), distance(l2, point));
		List<Vector> intersectionPoints = getCircleLineIntersections(l1, l2, point, radius);
		
		if (intersectionPoints.size() == 0) {
			return intersectionPoints.get(0);
		}
		
		if (intersectionPoints.size() == 2) {
			Vector midPoint = midPoint(intersectionPoints.get(0), intersectionPoints.get(1));
			
			if (isContainedInSegment(l1, l2, midPoint)) {
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
	public static boolean isContainedInSegment(Vector segStart, Vector segEnd, Vector reference) {
		
		if (equal(segStart, reference) || equal(segEnd, reference)) {
			return true;
		}
		
		// try to interpret the reference vector as a movement 
		// from start to end of segment
		
		double startToRefDistance = distance(segStart, reference);
		if (startToRefDistance > distance(segStart, segEnd)) {
			return false;
		}
		
		Vector startToEndVector = substract(segEnd, segStart);
		
		Vector startToRefVector = multiply(normalized(startToEndVector), startToRefDistance);
		
		Vector shouldBeRefOnSegment = add(segStart, startToRefVector);
	
		// if that moved vector and the reference equal, the point is on the line
		return equal(reference, shouldBeRefOnSegment);
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
	protected static List<Vector> getCircleLineIntersections(Vector l1, Vector l2, Vector circleCenter, double circleRadius) {

		double baX = l2.x - l1.x;
		double baY = l2.y - l1.y;
		double caX = circleCenter.x - l1.x;
		double caY = circleCenter.y - l1.y;

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

		Vector p1 = vector(l1.x - baX * abScalingFactor1, l1.y - baY * abScalingFactor1);
		if (disc == 0) { // abScalingFactor1 == abScalingFactor2
			return Collections.singletonList(p1);
		}
		
		Vector p2 = vector(l1.x - baX * abScalingFactor2, l1.y - baY * abScalingFactor2);
		
		return Arrays.asList(p1, p2);
	}

	////// private utilities /////////////////////////////////////////////////////
	
	private static boolean equal(Vector a, Vector b) {
		return VectorUtil.equal(a, b, 0.99);
	}
	
	private static void assertMinWaypoints(List<Point> waypoints) {
		Assert.isLegal(waypoints.size() > 1, "Must have minimum two waypoints");
	}
}
