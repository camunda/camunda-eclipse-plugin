package org.eclipse.bpmn2.modeler.core.layout.util;

import org.eclipse.dd.dc.impl.PointImpl;
import org.eclipse.dd.di.DiFactory;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.internal.datatypes.impl.LocationImpl;
import org.eclipse.graphiti.internal.datatypes.impl.RectangleImpl;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.services.Graphiti;

/**
 * Manages conversions between different Graphiti, Ecplise.draw2d, ...
 * datatypes.
 * 
 * @author nico.rehwaldt
 *
 */
public class ConversionUtil {
	
	public static Vector vector(Point point) {
		return new Vector(point.getX(), point.getY());
	}

	public static Vector vector(ILocation l) {
		return new Vector(l.getX(), l.getY());
	}
	
	public static ILocation location(Vector v) {
		return location((int) Math.round(v.x), (int) Math.round(v.y));
	}

	public static Point point(int x, int y) {
		return Graphiti.getCreateService().createPoint(x, y);
	}
	
	public static Point point(org.eclipse.dd.dc.Point p) {
		return point((int) Math.round(p.getX()), (int) Math.round(p.getY()));
	}

	public static org.eclipse.dd.dc.Point diPoint(ILocation l) {
		org.eclipse.dd.dc.Point p = org.eclipse.dd.dc.DcFactory.eINSTANCE.createPoint();
		
		p.setX(l.getX());
		p.setY(l.getY());
		
		return p;
	}
	
	public static Point point(ILocation l) {
		return point(l.getX(), l.getY());
	}
	
	public static ILocation location(Point p) {
		return location(p.getX(), p.getY());
	}
	
	public static ILocation location(org.eclipse.dd.dc.Point p) {
		return location((int) Math.round(p.getX()), (int) Math.round(p.getY()));
	}
	
	public static ILocation location(int x, int y) {
		return new LocationImpl(x, y);
	}
	
	@SuppressWarnings("restriction")
	public static IRectangle rectangle(int x, int y, int width, int height) {
		
		return new RectangleImpl(x, y, width, height);
	}
}
