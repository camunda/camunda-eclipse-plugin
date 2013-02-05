package org.eclipse.bpmn2.modeler.core.layout.util;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.mm.algorithms.styles.Point;

/**
 * Vector utilities based on {@link Point}s. 
 * 
 * @author nico.rehwaldt
 */
public class Vector {

	public static double length(Point p) {
		return Math.sqrt(Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2));
	}
	
	public static Point normalized(Point p) {
		double length = length(p);
		
		return divide(p, length);
	}

	public static Point divide(Point p, double divisor) {
		Assert.isLegal(divisor != 0.0);
		
		return multiply(p, 1 / divisor);
	}
	
	public static Point multiply(Point p, double factor) {
		return point(p.getX() * factor, p.getY() * factor);
	}
	
	public static Point substract(Point a, Point b) {
		return point(a.getX() - b.getX(), a.getY() - b.getY());
	}
	
	public static double distance(Point a, Point b) {
		return length(substract(a, b));
	}
	
	public static Point add(Point a, Point b) {
		return point(a.getX() + b.getX(), a.getY() + b.getY());
	}
	
	public static boolean equal(Point a, Point b) {
		return equal(a, b, 0.0);
	}
	
	public static boolean equal(Point a, Point b, double tolerance) {
		return Math.abs(a.getX() - b.getX()) <= tolerance && Math.abs(a.getY()- b.getY()) <= tolerance;
	}
	
	public static Point midPoint(Point a, Point b) {
		return divide(add(a, b), 2);
	}
	
	/////// stringify helpers ///////////////////////////////////////////////////////////
	
	/**
	 * Returns a string representation for the given point
	 * 
	 * @param point
	 * @return
	 */
	public static String asString(Point point) {
		return String.format("(%s, %s)", point.getX(), point.getY());
	}
	
	/**
	 * Returns a string representation for the given list of points
	 * 
	 * @param points
	 * @return
	 */
	public static String asString(List<Point> points) {
		StringBuilder builder = new StringBuilder();
		for (Point point : points) {
			
			if (builder.length() != 0) {
				builder.append(", ");
			}
			
			builder.append(asString(point));
		}
		
		return "[" + builder.toString() + "]";
	}
}
