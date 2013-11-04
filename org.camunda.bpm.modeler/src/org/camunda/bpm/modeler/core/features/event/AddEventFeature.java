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
package org.camunda.bpm.modeler.core.features.event;

import static org.camunda.bpm.modeler.core.utils.GraphicsUtil.createEventShape;

import org.camunda.bpm.modeler.core.features.activity.AbstractAddFlowElementFeature;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.BoxingStrategy;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.Event;
import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
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
		
		ContainerShape targetContainer = getTargetContainer(context);
		
		ContainerShape newShape = peService.createContainerShape(targetContainer, true);
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

	protected ContainerShape getTargetContainer(IAddContext context) {
		return context.getTargetContainer();
	}
	
	@Override
	protected IAddContext getAddLabelContext(IAddContext context, ContainerShape newShape, IRectangle newShapeBounds) {
		IAddContext addContext = super.getAddLabelContext(context, newShape, newShapeBounds);
		
		Assert.isLegal(addContext instanceof AddContext);
		
		AddContext addLabelContext = (AddContext) addContext;
		
		// set actual target container
		addLabelContext.setTargetContainer(newShape.getContainer());
		
		return addLabelContext;
	}
	
	@Override
	protected void adjustLocationAndSize(IAddContext context, int width, int height) {
		
		if (context.getTargetConnection() != null) {
			adjustLocationForDropOnConnection(context);
		}
		
		super.adjustLocationAndSize(context, width, height);
		
		if (isImport(context)) {
			return;
		}
		
		// for backward compatibility with older files that included
		// the label height in the figure height
		if (width != height) {
			width = height = Math.min(width, height);
		}
		
		if (context instanceof AddContext) {
			AddContext addContext = (AddContext) context;
			
			addContext.setSize(width, height);
		}
	}
	
	@Override
	protected BoxingStrategy getBoxingStrategy(IAddContext context) {
		return BoxingStrategy.POSITION;
	}
	
	@Override
	protected void setProperties(IAddContext context, ContainerShape newShape) {
		super.setProperties(context, newShape);
	
		Event event = getBusinessObject(context);

		IPeService peService = Graphiti.getPeService();
		peService.setPropertyValue(newShape,
				AbstractUpdateEventFeature.EVENT_DEFINITIONS_MARKER,
				AbstractUpdateEventFeature.getEventDefinitionsValue(event));
	}

	protected void decorate(Ellipse ellipse) {
		
	}

	@Override
	public int getDefaultHeight() {
		return GraphicsUtil.getEventSize(this.getDiagram()).getHeight();
	}

	@Override
	public int getDefaultWidth() {
		return GraphicsUtil.getEventSize(this.getDiagram()).getWidth();
	}

	@Override
	protected boolean isCreateExternalLabel() {
		return true;
	}
}