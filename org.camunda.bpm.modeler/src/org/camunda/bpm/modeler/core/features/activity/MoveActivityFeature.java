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
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core.features.activity;

import static org.camunda.bpm.modeler.core.utils.ContextUtil.copyProperties;
import static org.camunda.bpm.modeler.core.utils.ContextUtil.isNot;

import org.camunda.bpm.modeler.core.features.MoveFlowNodeFeature;
import org.camunda.bpm.modeler.core.features.event.AbstractBoundaryEventOperation;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class MoveActivityFeature extends MoveFlowNodeFeature {

	public static final String ACTIVITY_MOVE_PROPERTY = "activity.move";
	public static final String SELECTION_MOVE_PROPERTY = "selection.move";
	
	public static final String SKIP_MOVE_BOUNDARY_EVENTS = "MoveActivityFeature.SKIP_MOVE_BOUNDARY_EVENTS";

	public MoveActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		return context.getTargetConnection() == null && super.canMoveShape(context);
	}
	
	@Override
	protected void postMoveShape(final IMoveShapeContext context) {

		if (isMoveBoundaryEvents(context)) {
			repositionBoundaryEvents(context);
		}
		
		// perform post move and layout
		super.postMoveShape(context);
	}
	
	protected boolean isMoveBoundaryEvents(IMoveShapeContext context) {
		return isNot(context, SKIP_MOVE_BOUNDARY_EVENTS);
	}

	private void repositionBoundaryEvent(ContainerShape boundaryShape, IMoveShapeContext context) {

		IRectangle boundaryBounds = LayoutUtil.getRelativeBounds(boundaryShape);
		
		MoveShapeContext newContext = new MoveShapeContext(boundaryShape);
		
		int dx = context.getDeltaX();
		int dy = context.getDeltaY();
		
		newContext.setLocation(boundaryBounds.getX() + dx, boundaryBounds.getY() + dy);
		
		newContext.setSourceContainer(context.getSourceContainer());
		newContext.setTargetContainer(context.getTargetContainer());
		
		newContext.putProperty(ACTIVITY_MOVE_PROPERTY, true);
		
		copyProperties(context, newContext, MOVE_PROPERTIES);
		
		IMoveShapeFeature moveFeature = getFeatureProvider().getMoveShapeFeature(newContext);
		if (moveFeature.canMoveShape(newContext)) {
			moveFeature.moveShape(newContext);
		}
	}
	
	private void repositionBoundaryEvents(final IMoveShapeContext context) {
		Shape activityShape = (Shape) context.getPictogramElement();
		
		new AbstractBoundaryEventOperation() {
			@Override
			protected void applyTo(ContainerShape boundaryEventShape) {
				
				// move only if it is not moved by itself
				if (!isEditorSelection(boundaryEventShape)) {
					repositionBoundaryEvent(boundaryEventShape, context);
				}
			}
		}.execute(activityShape, context.getSourceContainer());
	}
}