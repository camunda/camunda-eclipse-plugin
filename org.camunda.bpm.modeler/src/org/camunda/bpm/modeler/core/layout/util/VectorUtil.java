package org.camunda.bpm.modeler.core.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.asVectors;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.vector;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.graphiti.mm.algorithms.styles.Point;

/**
 * Vector utilities based on {@link Point}s. 
 * 
 * @author nico.rehwaldt
 */
public class VectorUtil {

	public static double length(Vector p) {
		return p.getLength();
	}
	
	public static Vector normalized(Vector p) {
		double length = length(p);
		
		return divide(p, length);
	}

	public static Vector divide(Vector p, double divisor) {
		Assert.isLegal(divisor != 0.0);
		
		return multiply(p, 1 / divisor);
	}
	
	public static Vector multiply(Vector p, double factor) {
		return p.getMultiplied(factor);
	}
	
	public static Vector substract(Vector a, Vector b) {
		return a.getSubtracted(b);
	}
	
	public static double distance(Vector a, Vector b) {
		return length(substract(a, b));
	}
	
	public static Vector add(Vector a, Vector b) {
		return a.getAdded(b);
	}

	public static Vector midPoint(Vector a, Vector b) {
		return divide(add(a, b), 2);
	}
	
	/////// equality helpers ///////////////////////////////////////////////////////////
	
	public static boolean equal(Vector a, Vector b) {
		return equal(a, b, 0.0);
	}
	
	public static boolean equal(Vector a, Vector b, double tolerance) {
		return Math.abs(a.x - b.x) <= tolerance && Math.abs(a.y- b.y) <= tolerance;
	}

	public static boolean equal(Point a, Point b) {
		return equal(a, b, 0.0);
	}
	
	public static boolean equal(Point a, Point b, double tolerance) {
		return equal(vector(a), vector(b), tolerance);
	}
	
	/////// stringify helpers ///////////////////////////////////////////////////////////
	
	/**
	 * Returns a string representation for the given point
	 * 
	 * @param point
	 * @return
	 */
	public static String asString(Vector point) {
		return String.format("(%s, %s)", point.x, point.y);
	}
	
	/**
	 * Returns a string representation for the given list of points
	 * 
	 * @param points
	 * @return
	 */
	public static String asString(List<Vector> points) {
		StringBuilder builder = new StringBuilder();
		for (Vector point : points) {
			
			if (builder.length() != 0) {
				builder.append(", ");
			}
			
			builder.append(asString(point));
		}
		
		return "[" + builder.toString() + "]";
	}
}
