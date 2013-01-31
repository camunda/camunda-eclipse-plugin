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

import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.modeler.core.features.activity.AbstractAddFlowElementFeature;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddGatewayFeature<T extends Gateway>
	extends AbstractAddFlowElementFeature<T> {

	public AddGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {
		
		T addedGateway = getBusinessObject(context);
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		int x = bounds.getX();
		int y = bounds.getY();
		int width = bounds.getWidth();
		int height = bounds.getHeight();
		
		// Create a container for the gateway-symbol
		final ContainerShape newShape = peService.createContainerShape(context.getTargetContainer(), true);
		final Rectangle gatewayRect = gaService.createInvisibleRectangle(newShape);
		gaService.setLocationAndSize(gatewayRect, x, y, width, height);

		Shape gatewayShape = peService.createShape(newShape, false);
		Polygon gateway = GraphicsUtil.createGateway(gatewayShape, width, height);
		StyleUtil.applyStyle(gateway, addedGateway);
		gaService.setLocationAndSize(gateway, 0, 0, width, height);
		
		return newShape;
	}

	@Override
	protected void postAddHook(IAddContext context, ContainerShape newShape) {
		super.postAddHook(context, newShape);

		decorate(newShape);
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
	
	protected void decorate(ContainerShape newShape) {
		
	}

	@Override
	public int getDefaultHeight() {
		return GraphicsUtil.getGatewaySize(this.getDiagram()).getHeight();
	}

	@Override
	public int getDefaultWidth() {
		return GraphicsUtil.getGatewaySize(this.getDiagram()).getWidth();
	}
	
	@Override
	protected boolean isCreateExternalLabel() {
		return true;
	}
}