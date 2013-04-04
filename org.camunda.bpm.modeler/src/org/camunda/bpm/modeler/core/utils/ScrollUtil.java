package org.camunda.bpm.modeler.core.utils;

import org.camunda.bpm.modeler.core.layout.util.ConversionUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class ScrollUtil {
	
	/**
	 * We are using this Holder class, to be able to have typed link in the diagram shape,
	 * so we can use the link methods to extract the scrollshape from the diagram  
	 *
	 * @see #getScrollShape()
	 * 
	 * @author drobisch
	 *
	 */
	public static class ScrollShapeHolder extends EObjectImpl {
		Shape scrollShape;

		public ScrollShapeHolder() {
		}
		
		public ScrollShapeHolder(Shape scrollShape) {
			super();
			this.scrollShape = scrollShape;
		}
		
		public Shape getScrollShape() {
			return scrollShape;
		}
		
		public void setScrollShape(Shape scrollShape) {
			this.scrollShape = scrollShape;
		}
		
		/**
		 * make this a proxy -> Don't serialize this object in the graphiti model 
		 */
		@Override
		public boolean eIsProxy() {
			return true;
		}
		
	}

	public static final int SCROLL_PADDING = 150;
	public static final String SCROLL_SHAPE_MARKER = "ScrollUtil.SCROLL_SHAPE_MARKER";

	public static Shape getScrollShape(Shape shape) {
		ScrollShapeHolder holder = BusinessObjectUtil.getFirstElementOfType(LayoutUtil.getDiagram(shape), ScrollShapeHolder.class);
		if (holder != null) {
			return holder.getScrollShape();
		}
		return null;
	}
	
	/**
	 * update the scroll shape in the current diagram
	 * 
	 * @param diagram
	 * @return
	 */
	public static Shape updateScrollShape(Diagram diagram) {
		Shape scrollShape = getScrollShape(diagram);
		
		int xpos = 0;
		int ypos = 0;
		
		if (scrollShape != null && scrollShape.eContainer() != null) {
			xpos = scrollShape.getGraphicsAlgorithm().getX();
			ypos = scrollShape.getGraphicsAlgorithm().getY();
		} else {
			scrollShape = createScrollShape(diagram, getHolder(diagram));
		}
		
		IRectangle bounds = LayoutUtil.getChildrenBBox(diagram);
		
		updateScrollRect(bounds, xpos, ypos, scrollShape, bounds.getX(), bounds.getY());
		
		return scrollShape;
	}
	
	public static Shape addScrollShape(Diagram rootDiagram) {
		return addScrollShape(rootDiagram, null, true, 0, 0);
	}	
	
	/**
	 * Create a scroll shape on the diagram
	 * 
	 * @param rootDiagram the diagram to create the scrollshape in 
	 * @param bounds the bounds to consider for the scrollshape padding, may be null, the bounds are calculated in this case
	 * @param boundsOffset flag to decide if we should offset the scrollshape by the bounds position
	 * @param minX the minimal x position of the scrollshape
	 * @param minY the minimal y position of the scrollshape
	 * @return the scroll shape
	 */
	public static Shape addScrollShape(Diagram rootDiagram, IRectangle bounds, boolean boundsOffset, int minX, int minY) {
		ScrollShapeHolder holder = getHolder(rootDiagram);
		
		if (bounds == null) {
			bounds = LayoutUtil.getChildrenBBox(rootDiagram);
			if (bounds == null) {
				bounds = ConversionUtil.rectangle(0, 0, 0, 0);
			}
		}
		
		Shape scrollShape = createScrollShape(rootDiagram, holder);
		
		int xoffset = 0;
		int yoffset = 0;
		
		if (boundsOffset) {
			xoffset = bounds.getX();
			yoffset = bounds.getY();
		}
		
		updateScrollRect(bounds, minX, minY, scrollShape, xoffset,
				yoffset);
		
		return scrollShape;
	}

	private static ScrollShapeHolder getHolder(Diagram rootDiagram) {
		ScrollShapeHolder holder = BusinessObjectUtil.getFirstElementOfType(LayoutUtil.getDiagram(rootDiagram), ScrollShapeHolder.class);
		
		if (holder == null) {
			holder = new ScrollUtil.ScrollShapeHolder();
			rootDiagram.getLink().getBusinessObjects().add(holder);
			rootDiagram.eResource().getContents().add(holder);
		}
		
		return holder;
	}

	private static Shape createScrollShape(Diagram rootDiagram, ScrollShapeHolder holder) {
		if (holder.getScrollShape() == null) {
			Shape scrollShape = Graphiti.getPeService().createContainerShape(rootDiagram, true);
			holder.setScrollShape(scrollShape);
		}
		return holder.getScrollShape();
	}

	private static void updateScrollRect(IRectangle bounds, int xpos, int ypos, Shape scrollShape, int xoffset, int yoffset) {
		Rectangle scrollRect = (Rectangle) scrollShape.getGraphicsAlgorithm();
		
		if (scrollRect == null) {
			scrollRect =  Graphiti.getGaService().createRectangle(scrollShape);
		}
		
		xpos = Math.max(xoffset + bounds.getWidth() + ScrollUtil.SCROLL_PADDING , xpos);
		ypos = Math.max(yoffset + bounds.getHeight() + ScrollUtil.SCROLL_PADDING , ypos);
		
		scrollRect.setX(xpos);
		scrollRect.setY(ypos);
		
		scrollRect.setWidth(1);
		scrollRect.setHeight(1);
		
		scrollRect.setFilled(false);
		scrollRect.setTransparency(1.0);
		
		Graphiti.getPeService().setPropertyValue(scrollShape, SCROLL_SHAPE_MARKER, "true");
	}
	
	/**
	 * Returns true if the given element is the scroll shape
	 * 
	 * @param e
	 * @return
	 */
	public static boolean isScrollShape(PictogramElement e) {
		return Graphiti.getPeService().getPropertyValue(e, SCROLL_SHAPE_MARKER) != null;
	}
}
