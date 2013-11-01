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
package org.camunda.bpm.modeler.core.features;

import org.camunda.bpm.modeler.core.features.rules.ModelOperations;
import org.camunda.bpm.modeler.core.features.rules.ModelOperations.ModelMoveOperation;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class MoveFlowNodeFeature extends DefaultBpmn2MoveShapeFeature {

	private ModelMoveOperation moveOperation;

	public MoveFlowNodeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (!(getBusinessObjectForPictogramElement(context.getShape()) instanceof FlowNode)) {
			return false;
		}

		ModelMoveOperation moveOperation = ModelOperations.getFlowNodeMoveOperation(context);

		if (moveOperation.isEmpty()) {
			return onMoveAlgorithmNotFound(context);
		}

		return moveOperation.canExecute(context);
	}

	protected boolean onMoveAlgorithmNotFound(IMoveShapeContext context) {
		return super.canMoveShape(context);
	}

	@Override
	protected void preMoveShape(IMoveShapeContext context) {
		super.preMoveShape(context);
		
		if (context.getTargetConnection() != null) {
			
			// target x/y does not show the mouse cursor but the point on the connection
			// we need to correct that before proceeding and simulate a drop of
			// the shape on the given point
			
			if (context instanceof MoveShapeContext) {
				MoveShapeContext moveContext = (MoveShapeContext) context;
				Shape shape = moveContext.getShape();
				
				IRectangle bounds = LayoutUtil.getAbsoluteBounds(shape);
				
				// perform correction
				moveContext.setX(moveContext.getX() - bounds.getWidth() / 2);
				moveContext.setY(moveContext.getY() - bounds.getHeight() / 2);
			}
		}
		
		// init algorithm container for move operation
		this.moveOperation = ModelOperations.getFlowNodeMoveOperation(context);
	}
	
	@Override
	protected void postMoveShape(IMoveShapeContext context) {

		Shape shape = context.getShape();
		FlowNode flowNode = BusinessObjectUtil.getFirstElementOfType(shape, FlowNode.class);
		
		Assert.isNotNull(flowNode);
		
		if (!moveOperation.isEmpty()) {
			moveOperation.execute(context);
		}
		
		super.postMoveShape(context);
	}
}