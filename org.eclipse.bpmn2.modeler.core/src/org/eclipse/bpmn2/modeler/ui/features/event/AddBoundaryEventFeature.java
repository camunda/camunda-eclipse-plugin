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
package org.eclipse.bpmn2.modeler.ui.features.event;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.eclipse.bpmn2.modeler.ui.features.event.BoundaryEventFeatureContainer.BOUNDARY_EVENT_CANCEL;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBpmnShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractUpdateEventFeature;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddBoundaryEventFeature extends AbstractAddBpmnShapeFeature<BoundaryEvent> {

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
	protected void createAnchors(IAddContext context, ContainerShape newShape) {

		// per default, create chopbox anchor and
		// four fix point anchors on all four sides of the shape (North-East-South-West)
		ChopboxAnchor addChopboxAnchor = AnchorUtil.addChopboxAnchor(newShape);
		addChopboxAnchor.setReferencedGraphicsAlgorithm(newShape.getGraphicsAlgorithm());
		
		AnchorUtil.addFixedPointAnchors(newShape);
	}
	
	@Override
	protected void updateAndLayout(ContainerShape newShape) {
		
		// update only
		updatePictogramElement(newShape);
	}
	
	@Override
	protected IRectangle getAddBounds(IAddContext context) {
		IRectangle bounds = super.getAddBounds(context);
		
		if (isImport(context)) {
			return bounds;
		} else {
			ContainerShape targetContainer = context.getTargetContainer();
			IRectangle targetBounds = LayoutUtil.getRelativeBounds(targetContainer);
			
			// if not importing place it in the center of shape for user to adjust it
			return rectangle(
					targetBounds.getX() + context.getX() + bounds.getWidth(), 
					targetBounds.getY() + context.getY() + bounds.getWidth(), 
					bounds.getWidth(), 
					bounds.getHeight());
		}
	}
	
	@Override
	protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {
		BoundaryEvent event = getBusinessObject(context);

		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		int x = bounds.getX();
		int y = bounds.getY();
		int width = bounds.getWidth();
		int height = bounds.getHeight();
		
		boolean isImport = ContextUtil.is(context, DIUtils.IMPORT);
		
		// while it looks as if boundary events are contained in the shape they are attached to
		// they actually are not. We need to compensate that unless we perform an import
		ContainerShape newShapeContainer = 
			isImport ? context.getTargetContainer() : (ContainerShape) context.getTargetContainer().eContainer();

		ContainerShape newShape = peService.createContainerShape(newShapeContainer, true);
		Ellipse ellipse = gaService.createEllipse(newShape);
		StyleUtil.applyStyle(ellipse, event);

		gaService.setLocationAndSize(ellipse, x, y, width, height);

		Ellipse circle = GraphicsUtil.createIntermediateEventCircle(ellipse);
		circle.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		
		return newShape;
	}

	@Override
	protected void postAddHook(IAddContext context, ContainerShape newShape) {
		super.postAddHook(context, newShape);
		
		IPeService peService = Graphiti.getPeService();
		
		BoundaryEvent event = getBusinessObject(context);

		// send boundary event to front and element it is attached to to the back.
		
		Activity activity = event.getAttachedToRef();
		PictogramElement foundElem = BusinessObjectUtil.getLinkingPictogramElement(getDiagram(), activity);
		if (foundElem != null && foundElem instanceof ContainerShape) {
			ContainerShape activityContainer = (ContainerShape) foundElem;
			PositionOnLine pos = BoundaryEventPositionHelper.getPositionOnLineUsingBPMNShape(newShape, activityContainer);
			BoundaryEventPositionHelper.assignPositionOnLineProperty(newShape, pos);
			peService.sendToBack((Shape) foundElem);
			peService.sendToFront(newShape);
		}
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
	public int getDefaultHeight() {
		return GraphicsUtil.getEventSize(getDiagram()).getHeight();
	}

	@Override
	public int getDefaultWidth() {
		return GraphicsUtil.getEventSize(getDiagram()).getWidth();
	}

	@Override
	protected boolean isCreateExternalLabel() {
		return true;
	}
}