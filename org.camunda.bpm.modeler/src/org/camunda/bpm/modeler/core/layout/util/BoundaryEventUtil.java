/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.core.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.location;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.ui.features.event.BoundaryAttachment;
import org.camunda.bpm.modeler.ui.features.event.MoveBoundaryEventFeature;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.PropertyContainer;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

/**
 * Boundary event utility which holds shared functionality.
 * 
 * @author nico.rehwaldt
 */
public class BoundaryEventUtil {

	/**
	 * Const under which th boundary attachment property is stored
	 * 
	 * @see BoundaryAttachment
	 */
	public static final String EVENT_ATTACHMENT_PROP = "BoundaryEvent.BOUNDARY_EVENT_ATTACHMENT_PROP";
	
	/**
	 * Inner drop padding in which a boundary event may be attached to a shape
	 */
	public static final int EVENT_INNER_PADDING = 10;
	
	/**
	 * Outer drop padding in which a boundary event may be attached to a shape
	 */
	public static final int EVENT_OUTER_PADDING = 3;

	// EVENT ATTACHMENT CHECKS ////////////////////////////////////////////////
	
	public static boolean canCreateEventAt(int x, int y, IDimension containerDimensions) {
		return canCreateEventAt(location(x, y), rectangle(0, 0, containerDimensions));
	}

	private static boolean canCreateEventAt(ILocation boundaryLocation, IRectangle parentRect) {
		return canAttach(boundaryLocation, parentRect);
	}

	public static boolean canMoveEvent(int x, int y, IDimension containerDimensions) {
		return canMoveEvent(location(x, y), rectangle(0, 0, containerDimensions));
	}
	
	public static boolean canMoveEvent(ILocation boundaryLocation, IDimension containerDimensions) {
		return canMoveEvent(boundaryLocation, rectangle(0, 0, containerDimensions));
	}

	private static boolean canMoveEvent(ILocation boundaryLocation, IRectangle parentRect) {
		return canAttach(boundaryLocation, parentRect);
	}

	private static boolean canAttach(ILocation boundaryLocation, IRectangle parentRect) {
		return LayoutUtil.isContained(parentRect, boundaryLocation, EVENT_OUTER_PADDING) && 
			   !LayoutUtil.isContained(parentRect, boundaryLocation, -1 * EVENT_INNER_PADDING);
	}

	// SNAPPING //////////////////////////////////////////////////

	/**
	 * Snaps the given coordinates to the bounds of the target element.
	 * Suitable especially for boundary events.
	 * 
	 * Tastes best when heated. 
	 * 
	 * @param x target relative x
	 * @param y target relative y
	 * @param targetBounds
	 * 
	 * @return snap bounds relative to target bounds coordinate system
	 */
	public static ILocation snapToBounds(int x, int y, IRectangle targetBounds) {
		ILocation relativeLocation = location(x + targetBounds.getX(), y + targetBounds.getY());
		return LayoutUtil.snapToBounds(relativeLocation, targetBounds, EVENT_INNER_PADDING);
	}
	
	// BOUNDARY ATTACHMENT ////////////////////////////////////////////////
	
	public static void storeBoundaryAttachment(PropertyContainer propertyContainer, BoundaryAttachment attachment) {
		IPeService peService = Graphiti.getPeService();
		peService.setPropertyValue(propertyContainer, EVENT_ATTACHMENT_PROP, attachment.toString());
	}

	public static BoundaryAttachment getStoredBoundaryAttachment(PropertyContainer propertyContainer) {
		IPeService peService = Graphiti.getPeService();
		String value = peService.getPropertyValue(propertyContainer, EVENT_ATTACHMENT_PROP);
		
		return BoundaryAttachment.fromString(value);
	}
	
	public static void updateBoundaryAttachment(Shape boundaryShape, Diagram diagram) {
		BoundaryAttachment attachment = getAttachment(boundaryShape, diagram);
		
		storeBoundaryAttachment(boundaryShape, attachment);
	}

	public static Shape getAttachedToShape(Shape boundaryShape, Diagram diagram) {
		BoundaryEvent event = BusinessObjectUtil.getFirstElementOfType(boundaryShape, BoundaryEvent.class);
		
		return (Shape) BusinessObjectUtil.getLinkingPictogramElement(event.getAttachedToRef(), diagram);
	}
	
	public static BoundaryAttachment getAttachment(Shape boundaryShape, Diagram diagram) {

		Shape attachedToShape = getAttachedToShape(boundaryShape, diagram);
		
		IRectangle boundaryBounds = LayoutUtil.getAbsoluteBounds(boundaryShape);
		IRectangle attachedToBounds = LayoutUtil.getAbsoluteBounds(attachedToShape);
		
		return getAttachment(boundaryBounds, attachedToBounds);
	}
	
	private static BoundaryAttachment getAttachment(IRectangle boundaryBounds, IRectangle attachedToBounds) {
		ILocation boundaryCenter = LayoutUtil.getRectangleCenter(boundaryBounds);
		return getAttachment(boundaryCenter, attachedToBounds);
	}

	public static BoundaryAttachment getAttachment(ILocation boundaryCenter, IRectangle attachedToBounds) {
		int tolerance = 5;
		return BoundaryAttachment.fromAttachCoordinates(attachedToBounds, boundaryCenter, tolerance);
	}

	/**
	 * Return the attachment point (center coordinate) of a boundary event given the specified 
	 * attachment and the attachedToBounds of the parent shape.
	 * 
	 * @param attachment
	 * @param attachedToBounds
	 * 
	 * @return
	 */
	public static Point getPosition(BoundaryAttachment attachment, IRectangle attachedToBounds) {
		
		double percentage = attachment.getPercentage();
		
		int swidth = attachedToBounds.getWidth();
		int sheight = attachedToBounds.getHeight();
		
		int sx1 = attachedToBounds.getX();
		int sx2 = sx1 + swidth;
		
		int sy1 = attachedToBounds.getY();
		int sy2 = sy1 + sheight;
		
		switch (attachment.getSector()) {
		case TOP_LEFT:
			return point(sx1, sy1);
		case TOP:
			return point(sx1 + (int) Math.round(swidth * percentage), sy1);
		case TOP_RIGHT: 
			return point(sx2, sy1);
		case RIGHT:
			return point(sx2, sy1 + (int) Math.round(sheight * percentage));
		case BOTTOM_RIGHT:
			return point(sx2, sy1 + sheight);
		case BOTTOM:
			return point(sx1 + (int) Math.round(swidth * percentage), sy2);
		case BOTTOM_LEFT:
			return point(sx1, sy2);
		case LEFT:
			return point(sx1, sy1 + (int) Math.round(sheight * percentage));
		
		default:
			// undefined case
			return null;
		}
	}

	// BOUNDARY REPOSITION (after parent relayout) /////////////////////////////
	
	public static void repositionBoundaryEvent(ContainerShape boundaryShape, Shape attachedToShape, IFeatureProvider featureProvider) {
		
		BoundaryAttachment attachment = getStoredBoundaryAttachment(boundaryShape);
		
		Assert.isNotNull(attachment);
		
		IRectangle boundaryBounds = LayoutUtil.getRelativeBounds(boundaryShape);
		
		Point newBoundaryMidPoint = getPosition(attachment, LayoutUtil.getRelativeBounds(attachedToShape));
		
		Assert.isNotNull(newBoundaryMidPoint);
		
		MoveShapeContext moveContext = new MoveShapeContext(boundaryShape);
		moveContext.setSourceContainer(boundaryShape.getContainer());
		moveContext.setTargetContainer(boundaryShape.getContainer());
		
		moveContext.setLocation(
				newBoundaryMidPoint.getX() - boundaryBounds.getWidth() / 2, 
				newBoundaryMidPoint.getY() - boundaryBounds.getHeight() / 2);
		
		ContextUtil.set(moveContext, MoveBoundaryEventFeature.MOVE_WITH_ACTIVITY);
		
		IMoveShapeFeature moveShapeFeature = featureProvider.getMoveShapeFeature(moveContext);
		
		Assert.isNotNull(moveShapeFeature);
		
		if (moveShapeFeature.canMoveShape(moveContext)) {
			moveShapeFeature.moveShape(moveContext);
		}
	}
}