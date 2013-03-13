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

import static org.camunda.bpm.modeler.core.utils.ContextUtil.isNot;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.Layouter;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class DefaultMoveBPMNShapeFeature extends DefaultMoveShapeFeature {

	public static final String SKIP_MOVE_LABEL = "DefaultMoveBPMNShapeFeature.SKIP_MOVE_LABEL";
	public static final String SKIP_MOVE_BENDPOINTS = "DefaultMoveBPMNShapeFeature.SKIP_MOVE_BENDPOINTS";
	public static final String SKIP_REPAIR_CONNECTIONS_AFTER_MOVE = "DefaultMoveBPMNShapeFeature.SKIP_RECONNECT_AFTER_MOVE";

	public static final String[] MOVE_PROPERTIES = {
		SKIP_MOVE_LABEL, SKIP_MOVE_BENDPOINTS, 
		SKIP_REPAIR_CONNECTIONS_AFTER_MOVE
	};

	protected IRectangle preMoveBounds;
	
	public DefaultMoveBPMNShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void preMoveShape(IMoveShapeContext context) {
		super.preMoveShape(context);
		
		Shape shape = context.getShape();
		
		preMoveBounds = LayoutUtil.getAbsoluteBounds(shape);
	}
	
	/**
	 * Returns the business object handled by this move feature.
	 * 
	 * @param context
	 * @return
	 */
	protected <T extends EObject> T getBusinessObject(PictogramElement element, Class<T> cls) {
		return BusinessObjectUtil.getFirstElementOfType(element, cls);
	}
	
	/**
	 * Returns the business object for a given pictogram element
	 * @param pictogramElement
	 * @return
	 */
	protected BaseElement getBusinessObject(PictogramElement pictogramElement) {
		return (BaseElement) getBusinessObjectForPictogramElement(pictogramElement);
	}
	
	/**
	 * Reorganizing the moving of bendpoints after internal moving the shape.
	 */
	@Override
	public void moveShape(IMoveShapeContext context) {
		preMoveShape(context);
		internalMove(context);
		moveAllBendpoints(context);
		postMoveShape(context);
	}
	
	@Override
	protected void moveAllBendpoints(IMoveShapeContext context) {

		if (!isMoveBendpoints(context)) {
			return;
		}
		
		Shape shape = context.getShape();
		
		Set<Connection> connectionsToMove = new HashSet<Connection>();
		
		// move only container connections
		connectionsToMove.addAll(calculateContainerConnections(context));
		
		// move selected connections, i.e.
		// connections that are in between two selected (and thus moved) shapes
		Set<Connection> selectedConnections = calculateSelectedConnections(context);
		
		connectionsToMove.addAll(selectedConnections);
		
		IRectangle postMoveBounds = LayoutUtil.getAbsoluteBounds(shape);
		
		int deltaX = (postMoveBounds.getX() - preMoveBounds.getX());
		int deltaY = (postMoveBounds.getY() - preMoveBounds.getY());
		
		for (Connection connection: connectionsToMove) {
			
			if (connection instanceof FreeFormConnection) {
				boolean move = true;
				
				if (selectedConnections.contains(connection)) {
					List<PictogramElement> editorSelection = getEditorSelection();
					
					PictogramElement lastInSelection = getLastInSelection(
							editorSelection, 
							connection.getStart().getParent(), 
							connection.getEnd().getParent());
					
					// move if moved shape is last in selection
					move = (lastInSelection == shape);
				}
				
				// move only connections part of container connections
				// after start and end shape have already been moved
				// and thus layouting on the connection can safely be performed
				if (move) {
					moveConnectionBendpoints((FreeFormConnection) connection, deltaX, deltaY);
					Layouter.layoutConnectionAfterShapeMove(connection, getFeatureProvider());
				}
			}
		}
	}
	
	private <T, E extends T> T getLastInSelection(List<T> editorSelection, E ... elements) {

		int idx = -1;
		for (E e: elements) {
			idx = Math.max(idx, editorSelection.indexOf(e));
		}
		
		if (idx == -1) {
			return null;
		} else {
			return editorSelection.get(idx);
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
	 * Return the list of selected connections in the given context
	 * 
	 * @param shape
	 * @param context
	 */
	private Set<Connection> calculateSelectedConnections(IMoveShapeContext context) {
		Shape shape = context.getShape();
		
		return LayoutUtil.getSharedConnections(shape, getEditorSelection());
	}
	
	/**
	 * Returns all connections contained in a given container shape.
	 * 
	 * @param context
	 * @return
	 */
	private Set<FreeFormConnection> calculateContainerConnections(IMoveShapeContext context) {

		Shape shape = context.getShape();
		
		if (!(shape instanceof ContainerShape)) {
			return Collections.emptySet();
		}

		return LayoutUtil.getContainerConnections((ContainerShape) shape);
	}
	
	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		Shape shape = context.getShape();

		sendToFront(shape);
		
		// layout shape after move 
		// (i.e. reconnect, move children, labels...)
		layout(shape, context);

		// update di
		updateDi(shape);
		
		ScrollUtil.updateScrollShape(getDiagram());
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
		Layouter.layoutShapeAfterMove(shape, isMoveLabel(context), isRepairConnectionsAfterMove(context), getFeatureProvider());
	}
	
	protected boolean isRepairConnectionsAfterMove(IMoveShapeContext context) {
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