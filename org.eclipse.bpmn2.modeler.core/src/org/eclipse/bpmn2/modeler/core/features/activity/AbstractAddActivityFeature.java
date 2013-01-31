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

import static org.eclipse.bpmn2.modeler.core.features.activity.UpdateActivityCompensateMarkerFeature.IS_COMPENSATE_PROPERTY;
import static org.eclipse.bpmn2.modeler.core.features.activity.UpdateActivityLoopAndMultiInstanceMarkerFeature.IS_LOOP_OR_MULTI_INSTANCE;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.modeler.core.features.activity.UpdateActivityLoopAndMultiInstanceMarkerFeature.LoopCharacteristicType;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public abstract class AbstractAddActivityFeature<T extends Activity> extends AbstractAddFlowElementFeature<T> {

	public static final String ACTIVITY_DECORATOR = "ACTIVITY_DECORATOR";
	public static final String IS_ACTIVITY = "IS_ACTIVITY";
	
	public AbstractAddActivityFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {

		IPeService peService = Graphiti.getPeService();
		IGaService gaService = Graphiti.getGaService();
		
		T baseElement = getBusinessObject(context);
		
		ContainerShape newShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle invisibleRect = gaService.createInvisibleRectangle(newShape);
		
		gaService.setLocationAndSize(
			invisibleRect, 
			bounds.getX(), 
			bounds.getY(), 
			bounds.getWidth(), 
			bounds.getHeight());

		Shape rectShape = peService.createShape(newShape, false);
		RoundedRectangle rect = gaService.createRoundedRectangle(rectShape, 5, 5);
		
		StyleUtil.applyStyle(rect, baseElement);
		
		gaService.setLocationAndSize(rect, 0, 0, bounds.getWidth(), bounds.getHeight());
		
		decorate(rect);
		
		peService.setPropertyValue(rectShape, IS_ACTIVITY, Boolean.toString(true));
		
		return newShape;
	}

	@Override
	protected void setProperties(IAddContext context, ContainerShape newShape) {

		Graphiti.getPeService().setPropertyValue(newShape, IS_COMPENSATE_PROPERTY, Boolean.toString(false));
		Graphiti.getPeService().setPropertyValue(newShape, IS_LOOP_OR_MULTI_INSTANCE, LoopCharacteristicType.NULL.getName());
		
		// set a property on the decorators so we can distinguish them from the real children (i.e. tasks, etc.)
		for (PictogramElement pe : newShape.getChildren()) {
			Graphiti.getPeService().setPropertyValue(pe, ACTIVITY_DECORATOR, "true");
		}
	}

	@Override
	protected IRectangle getAddBounds(IAddContext context) {

		int x, y, width, height;
		
		ContainerShape targetContainer = context.getTargetContainer();
		IRectangle targetBounds = LayoutUtil.getRelativeBounds(targetContainer);

		width = context.getWidth() > 0 ? context.getWidth() : getWidth();
		height = context.getHeight() > 0 ? context.getHeight() : getHeight();
		
		width = Math.min(targetBounds.getWidth(), width);
		height = Math.min(targetBounds.getHeight(), height);
		
		if (context.getTargetConnection() != null) {
			// adjust x and y coordinates to compensate drop on connection
			
		}
		
		adjustLocation(context, width, height);
		
		x = context.getX();
		y = context.getY();
		
		return rectangle(x, y, width, height);
	}
	
	/**
	 * Decorates the graphical element
	 * 
	 * @param rect
	 */
	protected void decorate(RoundedRectangle rect) {
		
	}
	
	protected int getMarkerContainerOffset() {
		return 0;
	}

	public abstract int getWidth();

	public abstract int getHeight();
}