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
package org.eclipse.bpmn2.modeler.core.features.gateway;

import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class DefaultAddGatewayFeature extends AbstractAddBPMNShapeFeature {

	public DefaultAddGatewayFeature(IFeatureProvider fp) {
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
		Gateway addedGateway = (Gateway) context.getNewObject();
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		int gatewayWidth = this.getWidth(context);
		int gatewayHeight = this.getHeight(context);

		// Create a container for the gateway-symbol
		final ContainerShape gatewayContainerShape = peService.createContainerShape(context.getTargetContainer(), true);
		final Rectangle gatewayRect = gaService.createInvisibleRectangle(gatewayContainerShape);
		gaService.setLocationAndSize(gatewayRect, context.getX(), context.getY(), gatewayWidth, gatewayHeight);

		Shape gatewayShape = peService.createShape(gatewayContainerShape, false);
		Polygon gateway = GraphicsUtil.createGateway(gatewayShape, gatewayWidth, gatewayHeight);
		StyleUtil.applyBGStyle(gateway, this);
		gaService.setLocationAndSize(gateway, 0, 0, gatewayWidth, gatewayHeight);
		decorateGateway(gatewayContainerShape);

		createDIShape(gatewayContainerShape, addedGateway);
		peService.createChopboxAnchor(gatewayContainerShape);
		AnchorUtil.addFixedPointAnchors(gatewayContainerShape, gateway);
		layoutPictogramElement(gatewayContainerShape);
		
		// Use context for labeling! 
		this.prepareAddContext(context, gatewayWidth, gatewayHeight);
		this.getFeatureProvider().getAddFeature(context).add(context);
		
		return gatewayContainerShape;
	}

	protected void decorateGateway(ContainerShape container) {
	}

	@Override
	protected int getHeight() {
		return GraphicsUtil.getGatewaySize(this.getDiagram()).getHeight();
	}

	@Override
	protected int getWidth() {
		return GraphicsUtil.getGatewaySize(this.getDiagram()).getWidth();
	}
	
}