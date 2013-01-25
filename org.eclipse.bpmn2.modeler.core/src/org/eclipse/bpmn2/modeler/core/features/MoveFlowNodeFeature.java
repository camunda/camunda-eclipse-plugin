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
package org.eclipse.bpmn2.modeler.core.features;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.rules.Algorithms;
import org.eclipse.bpmn2.modeler.core.features.rules.Algorithms.AlgorithmContainer;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class MoveFlowNodeFeature extends DefaultMoveBPMNShapeFeature {

	private AlgorithmContainer algorithmContainer;

	public MoveFlowNodeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (!(getBusinessObjectForPictogramElement(context.getShape()) instanceof FlowNode)) {
			return false;
		}

		ModelHandler handler = ModelHandler.getInstance(getDiagram());

		AlgorithmContainer algorithmContainer = Algorithms.getFlowNodeMoveAlgorithms(context);

		if (algorithmContainer.isEmpty()) {
			return onMoveAlgorithmNotFound(context);
		}

		return algorithmContainer.isMoveAllowed(getSourceBo(context, handler), getTargetBo(context, handler));
	}

	protected boolean onMoveAlgorithmNotFound(IMoveShapeContext context) {
		return super.canMoveShape(context);
	}

	@Override
	protected void preMoveShape(IMoveShapeContext context) {
		super.preMoveShape(context);
		
		// init algorithm container for move operation
		this.algorithmContainer = Algorithms.getFlowNodeMoveAlgorithms(context);
	}
	
	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		ModelHandler modelHandler;
		
		try {
			modelHandler = ModelHandler.getInstance(getDiagram());
		} catch (Exception e) {
			throw new IllegalStateException("Failed to execute post move", e);
		}

		Shape shape = context.getShape();
		FlowNode flowNode = BusinessObjectUtil.getFirstElementOfType(shape, FlowNode.class);
		
		Assert.isNotNull(flowNode);
		
		if (!algorithmContainer.isEmpty()) {
			algorithmContainer.move(
				flowNode, 
				getSourceBo(context, modelHandler),
				getTargetBo(context, modelHandler));
		}
		
		super.postMoveShape(context);
	}
	
	private Object getSourceBo(IMoveShapeContext context, ModelHandler handler) {
		if (context.getSourceContainer().equals(getDiagram()))
			return handler.getFlowElementContainer(context.getSourceContainer());
		return getBusinessObjectForPictogramElement(context.getSourceContainer());
	}

	private Object getTargetBo(IMoveShapeContext context, ModelHandler handler) {
		if (context.getTargetContainer().equals(getDiagram()))
			return handler.getFlowElementContainer(context.getTargetContainer());
		return getBusinessObjectForPictogramElement(context.getTargetContainer());
	}
}