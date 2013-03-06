package org.eclipse.bpmn2.modeler.core.utils;

import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class ScrollUtil {
	public static class ScrollShapeHolder extends EObjectImpl{
		Shape scrollShape;

		public ScrollShapeHolder(Shape scrollShape) {
			super();
			this.scrollShape = scrollShape;
		}
		
		public Shape getScrollShape() {
			return scrollShape;
		}
	}

	public static final int SCROLL_PADDING = 200;

	public static Shape getScrollShape(Shape shape) {
		ScrollShapeHolder holder = BusinessObjectUtil.getFirstElementOfType(BusinessObjectUtil.getDiagram(shape), ScrollShapeHolder.class);
		if (holder != null) {
			return holder.getScrollShape();
		}
		return null;
	}

	public static void updateScrollShape(PictogramElement element,
			IRectangle rect) {
		Shape scrollShape = getScrollShape((Shape) element);
		
		// during model import there might be updates also, scrollshape is null then
		if (scrollShape == null) {
			return;
		}
		
		scrollShape.getGraphicsAlgorithm().setX(Math.max(rect.getX() + rect.getWidth() + SCROLL_PADDING, scrollShape.getGraphicsAlgorithm().getX()));
		scrollShape.getGraphicsAlgorithm().setY(Math.max(rect.getY() + rect.getHeight() + SCROLL_PADDING, scrollShape.getGraphicsAlgorithm().getY()));
	}
	
}
