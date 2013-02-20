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
package org.eclipse.bpmn2.modeler.core.features.activity;

import static org.eclipse.bpmn2.modeler.core.features.activity.AbstractAddActivityFeature.ACTIVITY_RECT;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.features.LayoutBpmnShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractBoundaryEventOperation;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class LayoutActivityFeature extends LayoutBpmnShapeFeature {

	public LayoutActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
		return bo != null && bo instanceof Activity;
	}

	@Override
	protected void layoutShape(ContainerShape container) {

		super.layoutShape(container);
		
		IRectangle bounds = LayoutUtil.getRelativeBounds(container);
		
		GraphicsUtil.setActivityMarkerOffest(container, getMarkerContainerOffset());
		GraphicsUtil.layoutActivityMarkerContainer(container);
		
		layoutActivity(container, bounds);
	}
	
	@Override
	protected void postLayoutShapeAndChildren(ContainerShape shape, final ILayoutContext context) {
		
		new AbstractBoundaryEventOperation() {
			@Override
			protected void applyTo(ContainerShape boundaryShape) {
				layoutChild(context, boundaryShape);
			}
		}.execute(shape);
	}
	
	protected void layoutActivity(ContainerShape container, IRectangle bounds) {
		for (Shape childShape: container.getChildren()) {
			
			if (isRect(childShape)) {
				layoutActivityRectangle(container, childShape, bounds);
			}
			
			if (isLabel(childShape)) {
				layoutLabel(container, childShape, bounds);
			}
		}
	}
	
	protected boolean isRect(Shape childShape) {
		return Graphiti.getPeService().getPropertyValue(childShape, ACTIVITY_RECT) != null;

	}
	/**
	 * Return true if the given shape is a label
	 * 
	 * @param subShape
	 * @return
	 */
	protected boolean isLabel(Shape subShape) {
		return getBusinessObjectForPictogramElement(subShape) != null && subShape.getGraphicsAlgorithm() instanceof Text;
	}

	/**
	 * Layout the label
	 * 
	 * @param container
	 * @param labelShape
	 * @param bounds 
	 */
	protected void layoutLabel(ContainerShape container, Shape labelShape, IRectangle bounds) {
		
	}

	/**
	 * Layout the activity rectangle shape
	 * 
	 * @param activityRectangle
	 * 
	 * @param bounds 
	 */
	protected void layoutActivityRectangle(ContainerShape container, Shape activityRectangle, IRectangle bounds) {
		GraphicsUtil.setSize(activityRectangle, bounds.getWidth(), bounds.getHeight());
	}
	
	protected int getMarkerContainerOffset() {
		return 0;
	}
}