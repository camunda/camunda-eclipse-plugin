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
package org.camunda.bpm.modeler.core.features.conversation;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import org.camunda.bpm.modeler.core.features.AbstractAddBpmnShapeFeature;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.Conversation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddConversationFeature extends AbstractAddBpmnShapeFeature<Conversation> {

	public AddConversationFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getTargetContainer().equals(getDiagram());
	}

	@Override
	protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		
		int w = bounds.getWidth();
		int h = bounds.getHeight();
		int x = bounds.getX();
		int y = bounds.getY();

		int w_5th = w / 5;
		
		Conversation conversation = getBusinessObject(context);

		ContainerShape newShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle rect = gaService.createInvisibleRectangle(newShape);
		gaService.setLocationAndSize(rect, x, y, w, h);

		int[] xy = { w_5th, 0, w_5th * 4, 0, w, h / 2, w_5th * 4, h, w_5th, h, 0, h / 2 };
		Polygon hexagon = gaService.createPolygon(rect, xy);

		StyleUtil.applyStyle(hexagon, conversation);
		
		return newShape;
	}

	@Override
	public int getDefaultHeight() {
		return 30;
	}

	@Override
	public int getDefaultWidth() {
		return 30;
	}

	@Override
	protected boolean isCreateExternalLabel() {
		return false;
	}
}