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

import static org.eclipse.bpmn2.modeler.core.features.activity.AbstractAddActivityFeature.IS_ACTIVITY;

import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.DefaultLayoutBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractBoundaryEventOperation;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class LayoutActivityFeature extends DefaultLayoutBPMNShapeFeature {

	public LayoutActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
		return bo != null && bo instanceof Activity;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape container = (ContainerShape) context.getPictogramElement();
		
		IRectangle bounds = LayoutUtil.getAbsoluteBounds(container);
		
		int width = bounds.getWidth();
		int height = bounds.getHeight();
		
		GraphicsUtil.setActivityMarkerOffest(container, getMarkerContainerOffset());
		GraphicsUtil.layoutActivityMarkerContainer(container);
		
		Collection<Shape> allContainedShapes = Graphiti.getPeService().getAllContainedShapes(container);
		for (Shape shape: allContainedShapes) {
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			IGaService gaService = Graphiti.getGaService();
			
			Shape rectShape = FeatureSupport.getShape(container, IS_ACTIVITY, Boolean.toString(true));
			gaService.setSize(rectShape.getGraphicsAlgorithm(), width, height);
			layoutInRectangle((RoundedRectangle) rectShape.getGraphicsAlgorithm());

			Object[] objects = getAllBusinessObjectsForPictogramElement(shape);
			for (Object bo : objects) {
				layoutHook(shape, ga, bo, width, height);
			}
		}

		new AbstractBoundaryEventOperation() {
			@Override
			protected void applyTo(ContainerShape container) {
				layoutPictogramElement(container);
			}
		}.execute(container);
		
		if (container.eContainer() instanceof ContainerShape) {
			PictogramElement pe = (PictogramElement) container.eContainer();
			if (BusinessObjectUtil.containsElementOfType(pe, SubProcess.class)) {
				layoutPictogramElement(pe);
			}
		}

		DIUtils.updateDIShape(container);
		return true;
	}

	protected int getMarkerContainerOffset() {
		return 0;
	}

	protected void layoutInRectangle(RoundedRectangle rect) {
	}

	protected boolean layoutHook(Shape shape, GraphicsAlgorithm ga, Object bo, int newWidth, int newHeight) {
		return false;
	}
}