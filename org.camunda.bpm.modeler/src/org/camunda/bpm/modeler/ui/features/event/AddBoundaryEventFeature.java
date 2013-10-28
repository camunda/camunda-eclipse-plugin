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
package org.camunda.bpm.modeler.ui.features.event;

import static org.camunda.bpm.modeler.ui.features.event.BoundaryEventFeatureContainer.BOUNDARY_EVENT_CANCEL;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.features.event.AbstractUpdateEventFeature;
import org.camunda.bpm.modeler.core.features.event.AddEventFeature;
import org.camunda.bpm.modeler.core.layout.util.BoundaryEventUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.BoxingStrategy;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

public class AddBoundaryEventFeature extends AddEventFeature<BoundaryEvent> {

	public static final String BOUNDARY_EVENT_RELATIVE_Y = "boundary.event.relative.y";

	public AddBoundaryEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		if (!(getBusinessObject(context) instanceof BoundaryEvent)) {
			return false;
		}

		Object prop = context.getProperty(DIUtils.IMPORT);
		if (prop != null && (Boolean) prop) {
			return true;
		}

		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return bo != null && bo instanceof Activity;
	}
	
	@Override
	protected void updateAndLayout(ContainerShape newShape, IAddContext context) {
		
		// update only
		updatePictogramElement(newShape);
	}
	
	@Override
	protected void adjustLocation(IAddContext context, int width, int height) {
		
		// snap to line upon add
		if (context instanceof AddContext) {
			AddContext addContext = (AddContext) context;
			
			ContainerShape targetContainer = context.getTargetContainer();
			IRectangle targetBounds = LayoutUtil.getRelativeBounds(targetContainer);
			
			ILocation snapBounds = BoundaryEventUtil.snapToBounds(addContext.getX(), addContext.getY(), targetBounds);
			
			addContext.setLocation(snapBounds.getX(), snapBounds.getY());
		}
		
		super.adjustLocation(context, width, height);
	}

	@Override
	protected void decorate(Ellipse e) {
		Ellipse circle = GraphicsUtil.createIntermediateEventCircle(e);
		circle.setStyle(StyleUtil.getStyleForClass(getDiagram()));
	}

	protected ContainerShape getTargetContainer(IAddContext context) {
		boolean isImport = ContextUtil.is(context, DIUtils.IMPORT);
		
		// while it looks as if boundary events are contained in the shape they are attached to
		// they actually are not. We need to compensate that unless we perform an import
		return isImport ? 
			context.getTargetContainer() : 
			(ContainerShape) context.getTargetContainer().eContainer();
	}
	
	@Override
	protected void postAddHook(IAddContext context, ContainerShape boundaryShape) {
		super.postAddHook(context, boundaryShape);

		// send boundary event to front and element it is attached to to the back.
		BoundaryEventUtil.updateBoundaryAttachment(boundaryShape, getDiagram());
		
		GraphicsUtil.sendToFront(boundaryShape);
	}
	
	@Override
	protected void setProperties(IAddContext context, ContainerShape newShape) {
		super.setProperties(context, newShape);
		
		IPeService peService = Graphiti.getPeService();
		
		BoundaryEvent event = getBusinessObject(context);

		peService.setPropertyValue(newShape, BOUNDARY_EVENT_CANCEL, Boolean.toString(event.isCancelActivity()));
		peService.setPropertyValue(newShape, GraphicsUtil.EVENT_MARKER_CONTAINER, Boolean.toString(true));
		peService.setPropertyValue(newShape,
				UpdateBoundaryEventFeature.BOUNDARY_EVENT_MARKER,
				AbstractUpdateEventFeature.getEventDefinitionsValue(event));
	}
	
	@Override
	protected BoxingStrategy getBoxingStrategy(IAddContext context) {
		return BoxingStrategy.NONE;
	}
}