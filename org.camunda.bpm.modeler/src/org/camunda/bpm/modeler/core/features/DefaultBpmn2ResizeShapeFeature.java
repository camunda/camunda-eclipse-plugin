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

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.RectangleUtil.translate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.Layouter;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.core.utils.AnchorUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IReason;
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

/**
 * 
 * @author nico.rehwaldt
 */
public class DefaultBpmn2ResizeShapeFeature extends DefaultResizeShapeFeature {

	public final static int PADDING = 5;

	private IRectangle preResizeBounds;

	public DefaultBpmn2ResizeShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	/**
	 * Returns true if the resize operation is permitted
	 * 
	 * @param context
	 */
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {

		boolean minBoundsContained = true;
		
		Sector direction = Sector.fromResizeDirection(context.getDirection());
		
		// see if we are before the actual resize operation because no resize 
		// has been carried out yet.
		
		// when actually resizing, check the minimum bounds of the shape
		// and allow the resize operation if the post resize bounds would contain
		// the minimum bounds
		if (direction != Sector.UNDEFINED) {
			
			// check if children still fit into element
			IRectangle minimumBounds = getMinimumBounds(context);
			if (minimumBounds != null) {
				
				IRectangle postResizeBounds = getPostResizeBounds(context);
				minBoundsContained = LayoutUtil.isContained(minimumBounds, postResizeBounds, -1, direction);
			}
		}
		
		return minBoundsContained && super.canResizeShape(context);
	}

	public IRectangle getPreResizeBounds() {
		return preResizeBounds;
	}
	
	/**
	 * Returns the bounds after resize as indicated in the given context
	 * 
	 * @param context
	 * @return
	 */
	protected IRectangle getPostResizeBounds(IResizeShapeContext context) {
		return rectangle(
			context.getX(), 
			context.getY(), 
			context.getWidth(), 
			context.getHeight());
	}
	
	@Override
	public void resizeShape(IResizeShapeContext context) {
		preResize(context);
		
		internalResize(context);
		
		Shape shape = context.getShape();
		
		// move child shapes to compensate the in-parent-movement
		// results in a visually fixed position for the children
		moveChildShapes(context);
		
		relocateAnchors(shape, context);
		
		updateDi(shape);
		
		postResize(context);
		
		layout(shape, context);
	}

	protected void internalResize(IResizeShapeContext context) {
		Shape shape = context.getShape();
		int x = context.getX();
		int y = context.getY();
		int width = context.getWidth();
		int height = context.getHeight();

		if (shape.getGraphicsAlgorithm() != null) {
			Graphiti.getGaService().setLocationAndSize(shape.getGraphicsAlgorithm(), x, y, width, height);
		}
	}

	/**
	 * Get minimum resize bounds for this shape
	 * 
	 * @param context
	 * @return
	 */
	protected IRectangle getMinimumBounds(IResizeShapeContext context) {
		
		ContainerShape containerShape = (ContainerShape) context.getShape();
		
		IRectangle containerRelativeBounds = LayoutUtil.getRelativeBounds(containerShape);
		
		IRectangle childrenBBox = LayoutUtil.getChildrenBBox(containerShape, null, PADDING, PADDING);
		
		if (childrenBBox != null) {
			return translate(childrenBBox, point(containerRelativeBounds));
		} else {
			return null;
		}
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
		

		if (!(shape instanceof ContainerShape)) {
			return;
		}
		
		IRectangle postResizeBounds = LayoutUtil.getAbsoluteBounds(shape);
		
		if (postResizeBounds.getX() == preResizeBounds.getX() &&
			postResizeBounds.getY() == preResizeBounds.getY()) {
			
			// no resize from top left
			// no need to adjust bendpoints / children positions
			return;
		}
		
		int deltaX = preResizeBounds.getX() - postResizeBounds.getX();
		int deltaY = preResizeBounds.getY() - postResizeBounds.getY();
		
		FeatureSupport.moveChildren((ContainerShape) shape, point(deltaX, deltaY), getFeatureProvider());
	}

	/**
	 * Move bendpoints after the resize operation took place
	 * @param context
	 */
	@Deprecated
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

	@Deprecated
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
	@Deprecated
	private Set<Connection> calculateContainerConnections(IResizeShapeContext context) {

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
		internalLayout(shape);
	}
	
	protected void internalLayout(Shape shape) {
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
		return Arrays.asList(getDiagramBehavior().getDiagramContainer().getSelectedPictogramElements());
	}
}