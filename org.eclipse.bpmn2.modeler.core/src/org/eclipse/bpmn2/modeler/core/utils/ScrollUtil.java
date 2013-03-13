package org.eclipse.bpmn2.modeler.core.utils;

import org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

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

		public ScrollShapeHolder(Shape scrollShape) {
			super();
			this.scrollShape = scrollShape;
		}
		
		public Shape getScrollShape() {
			return scrollShape;
		}
		
		/**
		 * make this a proxy -> Don't serialize this object in the graphiti model 
		 */
		@Override
		public boolean eIsProxy() {
			return true;
		}
		
		@Override
		public URI eProxyURI() {
			return URI.createURI("scrollshape");
		}
		
	}

	public static final int SCROLL_PADDING = 150;

	public static Shape getScrollShape(Shape shape) {
		ScrollShapeHolder holder = BusinessObjectUtil.getFirstElementOfType(LayoutUtil.getDiagram(shape), ScrollShapeHolder.class);
		if (holder != null) {
			return holder.getScrollShape();
		}
		return null;
	}

	public static ILocation updateScrollShape(PictogramElement element) {
		Shape scrollShape = getScrollShape((Shape) element);

		// during model import there might be updates also, scrollshape is null
		// then
		if (scrollShape == null) {
			return null;
		}

		int scrollShapeX = scrollShape.getGraphicsAlgorithm().getX();
		int scrollShapeY = scrollShape.getGraphicsAlgorithm().getY();
		
		// reset scroll shape before getting bounds, otherwise it will always scroll beacuse of the padding
		scrollShape.getGraphicsAlgorithm().setX(0);
		scrollShape.getGraphicsAlgorithm().setY(0);
		
		IRectangle rect = LayoutUtil.getBounds(LayoutUtil.getDiagram((Shape) element), 0, 0, 0, 0);
		
		int newX = Math.max(rect.getX() + rect.getWidth() + SCROLL_PADDING, scrollShapeX);
		int newY = Math.max(rect.getY() + rect.getHeight() + SCROLL_PADDING, scrollShapeY);
		
		scrollShape.getGraphicsAlgorithm().setX(newX);
		scrollShape.getGraphicsAlgorithm().setY(newY);
		
		return ConversionUtil.location(newX, newY);
	}
	
	public static Shape addScrollShape(Diagram rootDiagram, IRectangle importBounds) {
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		
		Shape scrollShape = peService.createContainerShape(rootDiagram, true);
		
		ScrollShapeHolder holder = new ScrollUtil.ScrollShapeHolder(scrollShape);
		rootDiagram.getLink().getBusinessObjects().add(holder);
		
		Rectangle scrollRect = gaService.createRectangle(scrollShape);
		
		scrollRect.setX(importBounds.getWidth() + ScrollUtil.SCROLL_PADDING);
		scrollRect.setY(importBounds.getHeight() +  ScrollUtil.SCROLL_PADDING);
		
		scrollRect.setWidth(1);
		scrollRect.setHeight(1);
		
		scrollRect.setFilled(false);
		scrollRect.setTransparency(1.0);
		
		return scrollShape;
	}
	
}
