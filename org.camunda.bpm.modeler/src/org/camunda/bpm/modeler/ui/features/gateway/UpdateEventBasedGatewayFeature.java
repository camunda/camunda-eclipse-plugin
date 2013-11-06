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
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.ui.features.gateway;

import org.camunda.bpm.modeler.core.features.AbstractBpmn2UpdateFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.EventBasedGatewayType;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * An update feature that takes care of updating an event based gateway based 
 * on its instantiate and type properties.
 * 
 * @author nico.rehwaldt
 */
public class UpdateEventBasedGatewayFeature extends AbstractBpmn2UpdateFeature {

	static final String INSTANTIATE_PROPERTY = "eventBased.instantiate";
	static final String GATEWAY_TYPE_PROPERTY = "eventBased.type";
	
	public UpdateEventBasedGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object o = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return o != null && o instanceof EventBasedGateway;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {

		PictogramElement pictogramElement = context.getPictogramElement();
		
		Boolean instantiateProperty = getBooleanProperty(pictogramElement, INSTANTIATE_PROPERTY);
		String gatewayTypeProperty = getProperty(pictogramElement, GATEWAY_TYPE_PROPERTY);

		if (instantiateProperty == null || gatewayTypeProperty == null) {
			return Reason.createTrueReason();
		}

		EventBasedGatewayType gatewayType = EventBasedGatewayType.getByName(gatewayTypeProperty);

		EventBasedGateway gateway = (EventBasedGateway) getBusinessObjectForPictogramElement(pictogramElement);

		if (gateway.isInstantiate() != instantiateProperty) {
			return Reason.createTrueReason();
		}
		
		if (gatewayType != gateway.getEventGatewayType()) {
			return Reason.createTrueReason();
		}

		return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		Shape pictogramElement = (Shape) context.getPictogramElement();
		
		EventBasedGateway gateway = (EventBasedGateway) getBusinessObjectForPictogramElement(pictogramElement);

		clearGateway(pictogramElement);

		if (gateway.isInstantiate()) {
			if (gateway.getEventGatewayType() == EventBasedGatewayType.PARALLEL) {
				drawParallelMultipleEventBased((ContainerShape) pictogramElement);
			} else {
				drawExclusiveEventBased((Shape) pictogramElement);
			}
		} else {
			drawEventBased((Shape) pictogramElement);
		}

		setProperty(pictogramElement, INSTANTIATE_PROPERTY, gateway.isInstantiate());
		setProperty(pictogramElement, GATEWAY_TYPE_PROPERTY, gateway.getEventGatewayType().getName());

		return true;
	}

	private void clearGateway(PictogramElement element) {
		GraphicsUtil.clearGateway(element);
	}

	// TODO: Move to decorate feature

	private void drawEventBased(Shape container) {
		GraphicsAlgorithm graphicsAlgorithm = container.getGraphicsAlgorithm();

		Ellipse outer = GraphicsUtil.createGatewayOuterCircle(graphicsAlgorithm);
		Ellipse inner = GraphicsUtil.createGatewayInnerCircle(outer);

		Polygon pentagon = GraphicsUtil.createGatewayPentagon(graphicsAlgorithm);
		pentagon.setFilled(false);
	}

	private void drawExclusiveEventBased(Shape container) {
		GraphicsAlgorithm graphicsAlgorithm = container.getGraphicsAlgorithm();

		Ellipse ellipse = GraphicsUtil.createGatewayOuterCircle(graphicsAlgorithm);
		Polygon pentagon = GraphicsUtil.createGatewayPentagon(graphicsAlgorithm);
		pentagon.setFilled(false);
	}

	private void drawParallelMultipleEventBased(Shape container) {
		GraphicsAlgorithm graphicsAlgorithm = container.getGraphicsAlgorithm();

		Ellipse ellipse = GraphicsUtil.createGatewayOuterCircle(graphicsAlgorithm);
		Polygon cross = GraphicsUtil.createEventGatewayParallelCross(graphicsAlgorithm);
	}
}