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

import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.layout.ConnectionService;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DefaultMoveBPMNShapeFeature extends DefaultMoveShapeFeature {

	public static final String MOVE_BENDPOINTS = "DefaultMoveBPMNShapeFeature.MOVE_BENDPOINTS";
	
	int preShapeX;
	int preShapeY;

	public DefaultMoveBPMNShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void moveAllBendpoints(IMoveShapeContext context) {
		if (isMoveBendpoints(context)) {
			super.moveAllBendpoints(context);
		}
	}
	
	@Override
	protected void preMoveShape(IMoveShapeContext context) {
		super.preMoveShape(context);
		preShapeX = 0;
		preShapeX = 0;

		if (context.getShape().getGraphicsAlgorithm() != null) {
			preShapeX = context.getShape().getGraphicsAlgorithm().getX();
			preShapeY = context.getShape().getGraphicsAlgorithm().getY();
		}
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		PictogramElement shape = context.getPictogramElement();
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(shape, BPMNShape.class);

		ContainerShape sourceContainer = context.getSourceContainer();
		ContainerShape targetContainer = context.getTargetContainer();

		// move label after the shape has been moved
		moveLabel(shape, sourceContainer, targetContainer, bpmnShape);
		
		if (isReconnectShapeAfterMove(context)) {
			ConnectionService.reconnectShapeAfterMove(shape);
		}
		
		// update di
		DIUtils.updateDIShape(shape, bpmnShape);
	}

	private boolean isReconnectShapeAfterMove(IMoveShapeContext context) {
		// contexts may disable reconnect by
		// setting ConnectionService.RECONNECT_AFTER_MOVE to false
		
		Object prop = context.getProperty(ConnectionService.RECONNECT_AFTER_MOVE);
		if (prop != null) {
			return Boolean.parseBoolean(prop.toString());
		}
		
		// assume reconnect per default
		return true;
	}
	
	private boolean isMoveBendpoints(IMoveShapeContext context) {
		
		// contexts may disable automatic moving of bendpoints by 
		// setting DefaultMoveBPMNShapeFeature.MOVE_BENDPOINTS to false
		
		Object prop = context.getProperty(DefaultMoveBPMNShapeFeature.MOVE_BENDPOINTS);
		if (prop != null) {
			return Boolean.parseBoolean(prop.toString());
		}
		
		// assume move bendpoints per default
		return true;
	}

	private void moveLabel(PictogramElement shape, ContainerShape sourceContainer, ContainerShape targetContainer,
			BPMNShape bpmnShape) {

		if (bpmnShape == null) {
			throw new IllegalArgumentException("Argument bpmnShape must not be null");
		}

		Shape label = GraphicsUtil.getLabel((Shape) shape, getDiagram());

		// no label, no work to do
		if (label == null) {
			return;
		}

		ContainerShape containerShape = (ContainerShape) shape;

		// align shape and label if the label
		// lies outside the shape
		if (shape != label) {
			containerShape = (ContainerShape) label;

			// only align when not selected, the move feature of the label will
			// do the job when selected
			GraphicsUtil.alignWithShape((AbstractText) containerShape.getChildren().get(0).getGraphicsAlgorithm(),
					containerShape, shape.getGraphicsAlgorithm().getWidth(), shape.getGraphicsAlgorithm().getHeight(),
					shape.getGraphicsAlgorithm().getX(), shape.getGraphicsAlgorithm().getY(), preShapeX, preShapeY);

			// adjust label container if the connected shapes container changed
			if (sourceContainer != targetContainer) {
				sourceContainer.getChildren().remove(label);
				targetContainer.getChildren().add((Shape) label);
			}
		}

		DIUtils.updateDILabel(containerShape, bpmnShape);
	}
}