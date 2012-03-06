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

import static org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil.EVENT_SIZE;
import static org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil.createEventShape;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddEventFeature extends AbstractAddBPMNShapeFeature {

	public static final String EVENT_ELEMENT = "event.graphics.element";
	public static final String EVENT_CIRCLE = "event.graphics.element.circle";

	public AddEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoFlowELementContainer = BusinessObjectUtil.containsElementOfType(context.getTargetContainer(),
		        FlowElementsContainer.class);
		return intoDiagram || intoLane || intoParticipant || intoFlowELementContainer;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Event e = (Event) context.getNewObject();

		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		int eventWidth = this.getWidth(context);
		int eventHeight = this.getHeight(context);
		// for backward compatibility with older files that included
		// the label height in the figure height
		if (eventWidth!=eventHeight) {
			eventWidth = eventHeight = Math.min(eventWidth, eventHeight);
		}

		ContainerShape eventContainerShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle invisibleRect = gaService.createInvisibleRectangle(eventContainerShape);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), eventWidth, eventHeight);

		Shape ellipseShape = peService.createShape(eventContainerShape, false);
		peService.setPropertyValue(ellipseShape, EVENT_ELEMENT, EVENT_CIRCLE);
		peService.setPropertyValue(eventContainerShape, GraphicsUtil.ACTIVITY_MARKER_CONTAINER, Boolean.toString(true));
		Ellipse ellipse = createEventShape(ellipseShape, eventWidth, eventHeight);
		StyleUtil.applyBGStyle(ellipse, this);
		decorateEllipse(ellipse);

//		Shape textShape = peService.createShape(eventContainerShape, false);
//		peService.setPropertyValue(textShape, UpdateBaseElementNameFeature.TEXT_ELEMENT, Boolean.toString(true));
//		Text text = gaService.createDefaultText(getDiagram(), textShape, e.getName());
//		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
//		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
//		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
//		gaService.setLocationAndSize(text, 0, EVENT_SIZE, EVENT_SIZE, EVENT_TEXT_AREA);

		peService.createChopboxAnchor(eventContainerShape);
		AnchorUtil.addFixedPointAnchors(eventContainerShape, ellipse);
		createDIShape(eventContainerShape, e);
		hook(eventContainerShape);
		updatePictogramElement(eventContainerShape);
		layoutPictogramElement(eventContainerShape);
		
		this.prepareAddContext(context, eventWidth, eventHeight);
		this.getFeatureProvider().getAddFeature(context).add(context);
		
		return eventContainerShape;
	}

	protected void decorateEllipse(Ellipse ellipse) {
	}

	protected void hook(ContainerShape container) {
	}

	@Override
	protected int getHeight() {
		return GraphicsUtil.getEventSize(this.getDiagram()).getHeight();
	}

	@Override
	protected int getWidth() {
		return GraphicsUtil.getEventSize(this.getDiagram()).getWidth();
	}
}