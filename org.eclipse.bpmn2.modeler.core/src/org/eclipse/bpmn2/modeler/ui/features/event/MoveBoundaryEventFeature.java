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
package org.eclipse.bpmn2.modeler.ui.features.event;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.location;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.modeler.core.features.DefaultMoveBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.MoveActivityFeature;
import org.eclipse.bpmn2.modeler.core.features.rules.ModelOperations;
import org.eclipse.bpmn2.modeler.core.features.rules.ModelOperations.ModelOperation;
import org.eclipse.bpmn2.modeler.core.layout.util.BoundaryEventUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class MoveBoundaryEventFeature extends DefaultMoveBPMNShapeFeature {

	public static final String MOVE_WITH_ACTIVITY = "MoveBoundaryEventFeature.MOVE_WITH_ACTIVITY";
	
	public MoveBoundaryEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		
		// two types of movements exist for boundary events
		// (1) single shape movements
		// (2) movements with their parent
		
		// another type of movement is done implicitly via the 
		// boundary events layouting mechanism
		// (3) re-attach to shape after the shape got 
		//     moved or resized
		
		if (isMoveWithActivity(context)) {
			ContextUtil.set(context, MOVE_WITH_ACTIVITY);
			return true;
		}
		
		if (isMovedByActivity(context)) {
			return true;
		}
		
		return canAttach(context);
	}

	/**
	 * Returns true if the move is done together with the attached activity
	 * 
	 * @param context
	 * @return
	 */
	protected boolean isMoveWithActivity(IMoveShapeContext context) {
		if (ContextUtil.is(context, MOVE_WITH_ACTIVITY)) {
			return true;
		}
		
		Shape boundaryShape = context.getShape();
		BoundaryEvent boundaryEvent = getBusinessObject(boundaryShape, BoundaryEvent.class);
		
		PictogramElement attachedToActivityShape = BusinessObjectUtil.getLinkingPictogramElement(boundaryEvent.getAttachedToRef(), getDiagram());
		
		return isEditorSelection(attachedToActivityShape);
	}

	protected boolean isMovedByActivity(IMoveShapeContext context) {
		return ContextUtil.is(context, MoveActivityFeature.ACTIVITY_MOVE_PROPERTY);
	}
	
	/**
	 * Returns true if the boundary event can move on an attached 
	 * activity shape.
	 * 
	 * @param context
	 * @return
	 */
	protected boolean canAttach(IMoveShapeContext context) {

		// not allowed to drop on connections
		if (context.getTargetConnection() != null) {
			return false;
		}
		
		Shape boundaryShape = context.getShape();
		ContainerShape attachedToShape = context.getTargetContainer();

		BoundaryEvent boundaryEvent = getBusinessObject(boundaryShape, BoundaryEvent.class);
		
		// only allow move on already attached activity
		Activity attachToActivity = getBusinessObject(attachedToShape, Activity.class);
		if (!boundaryEvent.getAttachedToRef().equals(attachToActivity)) {
			return false;
		}
		
		IRectangle attachToBounds = LayoutUtil.getRelativeBounds(attachedToShape);
		ILocation boundaryMidpoint = getBoundaryMoveMidpoint(context);
		
		return BoundaryEventUtil.canMoveEvent(boundaryMidpoint, attachToBounds);
	}
	
	/**
	 * Returns the boundary move midpoint from a given move context
	 * @param context
	 * @return
	 */
	private ILocation getBoundaryMoveMidpoint(IMoveShapeContext context) {
		Shape boundaryShape = context.getShape();
		IRectangle boundaryBounds = LayoutUtil.getRelativeBounds(boundaryShape);
		
		// during a move operation, the x and y coordinates
		// reflect the shape x/y position instead of the mouse pointer
		return location(
			context.getX() + boundaryBounds.getWidth() / 2, 
			context.getY() + boundaryBounds.getHeight() / 2);
	}

	@Override
	protected void preMoveShape(IMoveShapeContext context) {
		super.preMoveShape(context);
		
		// no alignment has to be done when moving with activity
		// or being moved by activity
		if (!isAttach(context)) {
			return;
		}
		
		adjustContextForAttachedMove(context);
	}
	
	/**
	 * Adjust the given move context for moving of the 
	 * designated boundary event.
	 * 
	 * @param context
	 */
	protected void adjustContextForAttachedMove(IMoveShapeContext context) {

		ContainerShape targetContainer = context.getTargetContainer();
		Activity activity = BusinessObjectUtil.getFirstElementOfType(targetContainer, Activity.class);
		
		// we only move boundary events already attached to activity shapes
		// if not something went wrong previously
		Assert.isNotNull(activity);
		
		ContainerShape logicalContainer = context.getTargetContainer();
		
		// parent container from Graphiti's point of view is the attachedToShape
		// in order to make the boundary event visible we have to adjust that 
		// to the attachedToShapes container
		ContainerShape actualContainer = (ContainerShape) logicalContainer.getContainer();

		IRectangle boundaryBounds = LayoutUtil.getRelativeBounds(context.getShape());
		IRectangle boundaryContainerBounds = LayoutUtil.getRelativeBounds(logicalContainer);
		
		MoveShapeContext c = (MoveShapeContext) context;
		
		ILocation snappedBoundaryMidPoint = BoundaryEventUtil.snapToBounds(context.getX() + boundaryBounds.getWidth() / 2, context.getY() + boundaryBounds.getHeight() / 2, boundaryContainerBounds);
		
		c.setLocation(snappedBoundaryMidPoint.getX() - boundaryBounds.getWidth() / 2, snappedBoundaryMidPoint.getY() - boundaryBounds.getHeight() / 2);
		c.setTargetContainer(actualContainer);
	}
	
	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		
		Shape boundaryShape = context.getShape();
		
		// perform move of the business object
		// in between containers
		ModelOperation<IMoveShapeContext> moveOperation = ModelOperations.getFlowNodeMoveAlgorithms(context);
		if (!moveOperation.isEmpty()) {
			moveOperation.execute(context);
		}
		
		// move only if this move is an attach move, ie.
		// a user controlled drop on a parent shape
		if (isAttach(context)) {
			BoundaryEventUtil.updateBoundaryAttachment(boundaryShape, getDiagram());
		}
		
		super.postMoveShape(context);
	}

	private boolean isAttach(IMoveShapeContext context) {
		return !isMovedByActivity(context) && !isMoveWithActivity(context);
	}
}