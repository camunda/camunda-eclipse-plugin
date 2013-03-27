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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.Layouter;
import org.camunda.bpm.modeler.core.utils.AnchorUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class DefaultResizeBPMNShapeFeature extends DefaultResizeShapeFeature {

	private IRectangle preResizeBounds;

	public DefaultResizeBPMNShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void resizeShape(IResizeShapeContext context) {
		preResize(context);
		
		super.resizeShape(context);
		
		Shape shape = context.getShape();
		
		// either adjust the children to the old position
		// or adjust the bendpoint movement
		if (isCompensateMovementOnChildren()) {
			moveChildShapes(context);
		} else {
			moveAllBendpoints(context);
		}
		relocateAnchors(shape, context);
		
		updateDi(shape);
		
		postResize(context);
		
		layout(shape, context);
	}

	protected boolean isCompensateMovementOnChildren() {
		return false;
	}

	/**
	 * Perform a pre resize operation
	 * 
	 * (may be overridden by subclasses to perform actual work)
	 * 
	 * @param context
	 */
	protected void preResize(IResizeShapeContext context) {
		Shape shape = context.getShape();
		
		preResizeBounds = LayoutUtil.getAbsoluteBounds(shape);
	}

	/**
	 * Perform an operation after the resize has finished
	 * 
	 * @param context
	 */
	protected void postResize(IResizeShapeContext context) {
		ScrollUtil.updateScrollShape(getDiagram());
	}
	

	/**
	 * Move child shapes during a resize operation.
	 * 
	 * @param context
	 */
	protected void moveChildShapes(IResizeShapeContext context) {
		
		Shape shape = context.getShape();
		
		IRectangle postResizeBounds = LayoutUtil.getAbsoluteBounds(shape);
		
		if (postResizeBounds.getX() == preResizeBounds.getX() &&
			postResizeBounds.getY() == preResizeBounds.getY()) {
			
			// no resize from top left
			// no need to adjust bendpoints
			return;
		}
		
		int deltaX = (postResizeBounds.getX() - preResizeBounds.getX());
		int deltaY = (postResizeBounds.getY() - preResizeBounds.getY());
		
		if (shape instanceof ContainerShape) {
			moveChildren((ContainerShape) shape, deltaX, deltaY);
		}
	}
	
	protected void moveChildren(ContainerShape shape, int deltaX, int deltaY) {
		
		List<Shape> children = new ArrayList<Shape>(shape.getChildren());
		for (Shape child : children) {
			if (child instanceof ContainerShape) {
				BaseElement be = BusinessObjectUtil.getFirstBaseElement(child);
				if (be != null) {
					
					ILocation location = LayoutUtil.getRelativeBounds(child);
					
					MoveShapeContext moveContext = new MoveShapeContext(child);
					moveContext.setLocation(location.getX() - deltaX, location.getY() - deltaY);
					moveContext.setSourceContainer(shape);
					moveContext.setTargetContainer(shape);

					ContextUtil.set(moveContext, DefaultMoveBPMNShapeFeature.SKIP_REPAIR_CONNECTIONS_AFTER_MOVE);
					ContextUtil.set(moveContext, DefaultMoveBPMNShapeFeature.SKIP_MOVE_BENDPOINTS);
					
					IMoveShapeFeature moveShapeFeature = getFeatureProvider().getMoveShapeFeature(moveContext);
					if (moveShapeFeature.canExecute(moveContext)) {
						moveShapeFeature.execute(moveContext);
					}
				}
			}
		}
	}

	/**
	 * Move bendpoints after the resize operation took place
	 * @param context
	 */
	protected void moveAllBendpoints(IResizeShapeContext context) {
		
		Shape shape = context.getShape();
		
		IRectangle postResizeBounds = LayoutUtil.getAbsoluteBounds(shape);
		
		if (postResizeBounds.getX() == preResizeBounds.getX() &&
			postResizeBounds.getY() == preResizeBounds.getY()) {
			
			// no resize from top left
			// no need to adjust bendpoints
			return;
		}
		
		int deltaX = (postResizeBounds.getX() - preResizeBounds.getX());
		int deltaY = (postResizeBounds.getY() - preResizeBounds.getY());
		
		Set<Connection> connectionsToMove = new HashSet<Connection>();
		
		// move connections contained in the resized shape
		connectionsToMove.addAll(calculateContainerConnections(context));
		
		for (Connection connection: connectionsToMove) {
			
			if (connection instanceof FreeFormConnection) {
				Layouter.layoutConnectionAfterShapeMove(connection, getFeatureProvider());
				moveConnectionBendpoints((FreeFormConnection) connection, deltaX, deltaY);
			}
		}
	}
	
	protected void moveConnectionBendpoints(FreeFormConnection connection, int deltaX, int deltaY) {
		List<Point> points = connection.getBendpoints();
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			int oldX = point.getX();
			int oldY = point.getY();
			points.set(i, Graphiti.getGaCreateService().createPoint(oldX + deltaX, oldY + deltaY));
		}
	}
	
	/**
	 * Returns all connections contained in a given container shape.
	 * 
	 * @param context
	 * @return
	 */
	private Set<FreeFormConnection> calculateContainerConnections(IResizeShapeContext context) {

		Shape shape = context.getShape();
		
		if (!(shape instanceof ContainerShape)) {
			return Collections.emptySet();
		}

		return LayoutUtil.getContainerConnections((ContainerShape) shape, true);
	}
	
	/**
	 * Layout the shape after it has been resized.
	 * 
	 * @param shape
	 * @param context
	 */
	protected void layout(Shape shape, IResizeShapeContext context) {
		DiagramElement diagramElement = BusinessObjectUtil.getFirstElementOfType(shape, DiagramElement.class);

		if (diagramElement instanceof BPMNShape || diagramElement instanceof BPMNEdge) {
			Layouter.layoutShapeAfterResize(shape, getFeatureProvider());
		}
	}

	/**
	 * Relocate anchors after the shape has been resized
	 * 
	 * @param shape
	 * @param context
	 */
	protected void relocateAnchors(Shape shape, IResizeShapeContext context) {
		AnchorUtil.relocateFixPointAnchors(shape, context.getWidth(), context.getHeight());
	}

	/**
	 * Update di after feature has been executed
	 * 
	 * @param shape
	 */
	protected void updateDi(Shape shape) {
		DIUtils.updateDIShape(shape);
	}
	
	/**
	 * Returns true if the given shape is currently selected in the editor
	 * 
	 * @param pictogramElement
	 * @return
	 */
	protected boolean isEditorSelection(PictogramElement pictogramElement) {
		List<PictogramElement> selection = getEditorSelection();
		return selection.contains(pictogramElement);
	}

	/**
	 * Return the editor selection
	 * 
	 * @return
	 */
	protected List<PictogramElement> getEditorSelection() {
		return Arrays.asList(getDiagramEditor().getSelectedPictogramElements());
	}
}