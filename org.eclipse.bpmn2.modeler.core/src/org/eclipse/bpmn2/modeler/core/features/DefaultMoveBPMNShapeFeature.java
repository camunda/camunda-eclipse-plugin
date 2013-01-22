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

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.layout.ConnectionService;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DefaultMoveBPMNShapeFeature extends DefaultMoveShapeFeature {

	private Point oldShapePosition = null;
	
	public DefaultMoveBPMNShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void preMoveShape(IMoveShapeContext context) {
		super.preMoveShape(context);

		if (context.getShape().getGraphicsAlgorithm() != null) {
			oldShapePosition = point(
				context.getShape().getGraphicsAlgorithm().getX(), 
				context.getShape().getGraphicsAlgorithm().getY());
		}
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		Shape shape = (Shape) context.getPictogramElement();
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(shape, BPMNShape.class);
		
		// move label after the shape has been moved
		moveLabel(shape, bpmnShape);

		ConnectionService.reconnectShapeAfterMove(shape);

		// update di
		DIUtils.updateDIShape(shape, bpmnShape);
	}

	private void moveLabel(Shape s, BPMNShape bpmnShape) {

		ContainerShape shape = (ContainerShape) s;
		
		if (bpmnShape == null) {
			throw new IllegalArgumentException("Argument bpmnShape must not be null");
		}

		ContainerShape label = GraphicsUtil.getLabelShape(shape, getDiagram());
		
		// no label, no work to do
		if (label == null) {
			return;
		}

		// align shape and label if the label
		// lies outside the shape
		if (shape != label) {
			AbstractText text = GraphicsUtil.getLabelShapeText(label);
			
			IRectangle shapeBounds = LayoutUtil.getRelativeBounds(shape);
			
			// only align when not selected, the move feature of the label will
			// do the job when selected
			GraphicsUtil.alignWithShape(text, label, shapeBounds.getWidth(), shapeBounds.getHeight(),
					point(shapeBounds), oldShapePosition);
			
			// do not adjust label container 
			// (labels always on top)
		}
		
		DIUtils.updateDILabel(label, bpmnShape);
	}
}