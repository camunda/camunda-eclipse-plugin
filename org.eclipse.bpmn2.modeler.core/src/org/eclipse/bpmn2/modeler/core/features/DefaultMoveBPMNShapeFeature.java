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

import static org.eclipse.bpmn2.modeler.core.utils.ContextUtil.isNot;

import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.layout.util.Layouter;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DefaultMoveBPMNShapeFeature extends DefaultMoveShapeFeature {

	public static final String SKIP_MOVE_LABEL = "DefaultMoveBPMNShapeFeature.SKIP_MOVE_LABEL";
	public static final String SKIP_MOVE_BENDPOINTS = "DefaultMoveBPMNShapeFeature.SKIP_MOVE_BENDPOINTS";
	public static final String SKIP_REPAIR_CONNECTIONS_AFTER_MOVE = "DefaultMoveBPMNShapeFeature.SKIP_RECONNECT_AFTER_MOVE";

	public static final String[] MOVE_PROPERTIES = {
		SKIP_MOVE_LABEL, SKIP_MOVE_BENDPOINTS, SKIP_REPAIR_CONNECTIONS_AFTER_MOVE
	};
	
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
	protected void postMoveShape(IMoveShapeContext context) {
		Shape shape = (Shape) context.getPictogramElement();

		sendToFront(shape);
		
		// layout shape after move 
		// (ie. reconnect, move children, labels...)
		layout(shape, context);

		// update di
		updateDi(shape);
	}

	/**
	 * Sends the element to the front after it has been moved
	 * @param shape
	 */
	protected void sendToFront(Shape shape) {
		GraphicsUtil.sendToFront(shape);
	}

	/**
	 * Update di after move
	 * @param shape
	 */
	protected void updateDi(Shape shape) {
		DIUtils.updateDI(shape);
	}

	/**
	 * Perform layout after the shape has been moved
	 * 
	 * @param shape
	 */
	protected void layout(Shape shape, IMoveShapeContext context) {
		Layouter.layoutShapeAfterMove(shape, isMoveLabel(context), isReconnectShapeAfterMove(context), getFeatureProvider());
	}
	
	protected boolean isReconnectShapeAfterMove(IMoveShapeContext context) {
		return isNot(context, SKIP_REPAIR_CONNECTIONS_AFTER_MOVE);
	}
	
	protected boolean isMoveBendpoints(IMoveShapeContext context) {
		return isNot(context, SKIP_MOVE_BENDPOINTS);
	}

	protected boolean isMoveLabel(IMoveShapeContext context) {
		Shape label = LabelUtil.getLabelShape(context.getPictogramElement(), getDiagram());
		
		// return is move label only if the label exists and is not an editor selection, too.
		return isNot(context, SKIP_MOVE_LABEL) && !isEditorSelection(label);
	}
	
	/**
	 * Return true if the given shape is currently connected in the editor.
	 * 
	 * @param shape
	 * @return
	 */
	protected boolean isEditorSelection(Shape shape) {
		List<PictogramElement> selection = Arrays.asList(getDiagramEditor().getSelectedPictogramElements());
		
		return selection.contains(shape);
	}
}