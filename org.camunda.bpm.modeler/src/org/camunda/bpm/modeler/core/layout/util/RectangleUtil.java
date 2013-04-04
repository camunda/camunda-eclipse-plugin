package org.camunda.bpm.modeler.core.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;

/**
 * Common operations on rectangles
 * 
 * @author nico.rehwaldt
 */
public class RectangleUtil {

	/**
	 * Represents diff between two rects which resulted during a resize operation
	 * 
	 * @author nico.rehwaldt
	 */
	public static class ResizeDiff {
		
		private Point delta;
		private Sector direction;
		
		public ResizeDiff(Point delta, Sector direction) {
			this.delta = delta;
			this.direction = direction;
		}
		
		/**
		 * Returns the resize delta of this diff
		 * 
		 * @return
		 */
		public Point getResizeDelta() {
			return delta;
		}
		
		/**
		 * Returns the resize sector of this diff
		 * 
		 * @return
		 */
		public Sector getResizeDirection() {
			return direction;
		}
	}

	public static IRectangle resize(IRectangle rect, ResizeDiff diff) {
		return resize(rect, diff.getResizeDelta(), diff.getResizeDirection());
	}

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
	 * Substract the given rectangle from other rect
	 * 
	 * @param rect
	 * @param substractRect
	 * 
	 * @return
	 */
	public static IRectangle substract(IRectangle rect, IRectangle substractRect) {
		IRectangle substraction = rectangle(
			-1 * substractRect.getX(),
			-1 * substractRect.getY(),
			-1 * substractRect.getWidth(),
			-1 * substractRect.getHeight());
		
		return translate(rect, substraction);
	}
	
	/**
	 * Returns the {@link ResizeDiff}, ie. the delta between two rects as 
	 * a result of a resize operation. 
	 * 
	 * @param preResizeRect
	 * @param postResizeRect
	 * 
	 * @return
	 * 
	 * @throws IllegalArgumentException if the resize diff cannot be deduced from the shapes
	 */
	public static ResizeDiff resizeDiff(IRectangle preResizeRect, IRectangle postResizeRect) {
		
		IRectangle diff = substract(postResizeRect, preResizeRect);
		
		int dx = 0, dy = 0;
		
		boolean left = false, right = false, top = false, bottom = false;
		
		if (diff.getX() != 0) {
			dx = diff.getX();
			left = true;
		}
		
		if (diff.getY() != 0) {
			dy = diff.getY();
			top = true;
		}
		
		if (!left && diff.getWidth() != 0) {
			dx = diff.getWidth();
			right = true;
		}
		
		if (!top && diff.getHeight() != 0) {
			dy = diff.getHeight();
			bottom = true;
		}
		
		Sector direction = Sector.fromBooleans(left, right, top, bottom);
		
		return new ResizeDiff(point(dx, dy), direction);
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
	
	/**
	 * Returns true if both rectangles equal each other
	 * 
	 * @param rect1
	 * @param rect2
	 * 
	 * @return
	 */
	public static boolean rectanglesEqual(IRectangle rect1, IRectangle rect2) {
		return 
			rect1.getX() == rect2.getX() && 
			rect1.getY() == rect2.getY() && 
			rect1.getWidth() == rect2.getWidth() && 
			rect1.getHeight() == rect2.getHeight();
	}
}
