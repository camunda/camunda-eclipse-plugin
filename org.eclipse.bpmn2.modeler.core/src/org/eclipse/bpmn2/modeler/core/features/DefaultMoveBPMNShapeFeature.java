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

import java.util.Iterator;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.layout.ConnectionService;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class DefaultMoveBPMNShapeFeature extends DefaultMoveShapeFeature {

	public static final String SKIP_MOVE_LABEL = "DefaultMoveBPMNShapeFeature.SKIP_MOVE_LABEL";
	
	private Point preMovePosition = null;
	
	public DefaultMoveBPMNShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void preMoveShape(IMoveShapeContext context) {
		super.preMoveShape(context);

		Shape shape = context.getShape();
		
		preMovePosition = point(LayoutUtil.getAbsoluteBounds(shape));
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		Shape shape = (Shape) context.getPictogramElement();

		GraphicsUtil.sendToFront(shape);
				
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(shape, BPMNShape.class);
		
		Point movementDiff = getMovementDiff(shape);
		
		if (isBpmnContainer(shape)) {
			moveChildren(context, movementDiff);
		}
		
		if (isMoveLabel(context)) {
			// move label after the shape has been moved
			moveLabel(shape, bpmnShape, movementDiff);
		}

		ConnectionService.reconnectShapeAfterMove(shape);

		// update di
		DIUtils.updateDIShape(shape, bpmnShape);
	}

	private boolean isMoveLabel(IMoveShapeContext context) {
		return context.getProperty(DefaultMoveBPMNShapeFeature.SKIP_MOVE_LABEL) == null;
	}

	/**
	 * Return true if the shape represents a bpmn container
	 * 
	 * @param shape
	 * @return
	 */
	private boolean isBpmnContainer(Shape shape) {
		BaseElement baseElement = BusinessObjectUtil.getFirstElementOfType(shape, BaseElement.class);
		
		return 
				baseElement instanceof Participant ||
				baseElement instanceof Lane ||
				baseElement instanceof SubProcess;
	}

	private Point getMovementDiff(Shape shape) {
		IRectangle newBounds = LayoutUtil.getAbsoluteBounds(shape);
		
		return point(newBounds.getX() - preMovePosition.getX(), newBounds.getY() - preMovePosition.getY());
	}

	protected void moveLabel(Shape shape, BPMNShape bpmnShape, Point movementDiff) {
		
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
			
			IRectangle shapeBounds = LayoutUtil.getAbsoluteBounds(shape);
			
			// reconstruct pre movement position from shape bounds (post move) and diff
			Point preMovePos = point(shapeBounds.getX() - movementDiff.getX(), shapeBounds.getY() - movementDiff.getY());
			
			// only align when not selected, the move feature of the label will
			// do the job when selected
			if (!isEditorSelection(label)) {
				GraphicsUtil.alignWithShape(text, label, shapeBounds.getWidth(), shapeBounds.getHeight(),
						point(shapeBounds), preMovePos);
				
				GraphicsUtil.sendToFront(label);
				
				DIUtils.updateDILabel(label, bpmnShape);
			}
			
			// do not adjust label container 
			// (labels always on top)
		}
	}

	/**
	 * Propagate move to children
	 * 
	 * @param context
	 * @param movementDiff 
	 */
	private void moveChildren(IMoveShapeContext context, Point movementDiff) {
		ContainerShape containerShape = (ContainerShape) context.getShape();
		
		Iterator<EObject> allSubShapes = containerShape.eAllContents();
		
		while (allSubShapes.hasNext()) {
			EObject s = allSubShapes.next();
			
			if (s instanceof Shape) {
				Shape shape = (Shape) s;
				
				BaseElement baseElement = BusinessObjectUtil.getFirstElementOfType(shape, BaseElement.class);
				BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(shape, BPMNShape.class);
				
				if (baseElement != null) {
					// reconnect shape
					ConnectionService.reconnectShapeAfterMove(shape);
					
					if (bpmnShape != null) {
						// move label
						moveLabel(shape, bpmnShape, movementDiff);
					}
				}
			}
		}
	}
	
	/**
	 * Returns true if the given shape is currently selected in the editor
	 * 
	 * @param shape
	 * @return
	 */
	private boolean isEditorSelection(ContainerShape shape) {
		
		PictogramElement[] selection = getDiagramEditor().getSelectedPictogramElements();
		
		for (PictogramElement e: selection) {
			if (e == shape) {
				return true;
			}
		}
		
		return false;
	}
}