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
package org.eclipse.bpmn2.modeler.core.features.event;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil.createEventShape;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.modeler.core.features.activity.AbstractAddFlowElementFeature;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddEventFeature<T extends Event>
	extends AbstractAddFlowElementFeature<T> {

	public static final String EVENT_ELEMENT = "event.graphics.element";
	public static final String EVENT_CIRCLE = "event.graphics.element.circle";

	public AddEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {
		T e = getBusinessObject(context);

		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		int x = bounds.getX();
		int y = bounds.getY();
		int width = bounds.getWidth();
		int height = bounds.getHeight();
		
		ContainerShape newShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle invisibleRect = gaService.createInvisibleRectangle(newShape);
		gaService.setLocationAndSize(invisibleRect, x, y, width, height);

		Shape ellipseShape = peService.createShape(newShape, false);
		peService.setPropertyValue(ellipseShape, EVENT_ELEMENT, EVENT_CIRCLE);
		peService.setPropertyValue(newShape, GraphicsUtil.EVENT_MARKER_CONTAINER, Boolean.toString(true));
		Ellipse ellipse = createEventShape(ellipseShape, width, height);
		StyleUtil.applyStyle(ellipse, e);
		decorate(ellipse);
		
		return newShape;
	}

	@Override
	protected IRectangle getAddBounds(IAddContext context) {
		
		int width = this.getWidth(context);
		int height = this.getHeight(context);
		
		// for backward compatibility with older files that included
		// the label height in the figure height
		if (width != height) {
			width = height = Math.min(width, height);
		}
		
		adjustLocation(context, width, height);
		
		int x = context.getX();
		int y = context.getY();
		
		return rectangle(x, y, width, height);
	}
	
	protected void decorate(Ellipse ellipse) {
		
	}

	@Override
	public int getHeight() {
		return GraphicsUtil.getEventSize(this.getDiagram()).getHeight();
	}

	@Override
	public int getWidth() {
		return GraphicsUtil.getEventSize(this.getDiagram()).getWidth();
	}

	@Override
	protected boolean isCreateExternalLabel() {
		return true;
	}
}