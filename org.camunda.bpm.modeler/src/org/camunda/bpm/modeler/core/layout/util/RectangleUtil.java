package org.camunda.bpm.modeler.core.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;

/**
 * Common operations on rectangles.
 * 
 * @author nico.rehwaldt
 */
public class RectangleUtil {

	public static IRectangle resize(IRectangle rect, Point delta, Sector direction) {
		
		int dx = delta.getX();
		int dy = delta.getY();
		
		int rx = rect.getX();
		int ry = rect.getY();
		int rwidth = rect.getWidth();
		int rheight = rect.getHeight();
		
		if (direction.isTop()) {
			ry += dy;
			rheight -= dy;
		}
		
		if (direction.isBottom()) {
			rheight += dy;
		}
		
		if (direction.isLeft()) {
			rx += dx;
			rwidth -= dx;
		}
		
		if (direction.isRight()) {
			rwidth += dx;
		}
		
		return rectangle(rx, ry, rwidth, rheight);
	}

	/**
	 * Translate a rectangle using the given translation coordinates
	 * 
	 * @param rect
	 * @param translation
	 * 
	 * @return
	 */
	public static IRectangle translate(IRectangle rect, IRectangle translation) {
		return rectangle(
			rect.getX() + translation.getX(),
			rect.getY() + translation.getY(),
			rect.getWidth() + translation.getWidth(), 
			rect.getHeight() + translation.getHeight());
	}
	
	/**
	 * Translate a rectangle by the given x/y translation
	 * 
	 * @param rect
	 * @param xyTranslation
	 * 
	 * @return
	 */
	public static IRectangle translate(IRectangle rect, Point xyTranslation) {
		return translate(rect, rectangle(xyTranslation.getX(), xyTranslation.getY(), 0, 0));
	}
}
