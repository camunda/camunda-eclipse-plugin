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
 * @author Bob Brodt
 ******************************************************************************/
package org.camunda.bpm.modeler.ui.features.activity.subprocess;

import org.camunda.bpm.modeler.core.layout.util.ConversionUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.ResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class ExpandFlowNodeFeature extends AbstractCustomFeature {

	private final static int EXPAND_PADDING = 20;

	private final static String NAME = "Expand";
	private final static String DESCRIPTION = "Expand the Activity and show contents";

	private String name = NAME;
	private String description = DESCRIPTION;

	public ExpandFlowNodeFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public String getName() {
	    return name;
	}
	
	@Override
	public String getDescription() {
	    return description;
	}

	@Override
	public String getImageId() {
		return Images.IMG_16_EXPAND;
	}

	@Override
	public boolean isAvailable(IContext context) {
		return true;
	}

	@Override
	public boolean canExecute(ICustomContext context) {

		PictogramElement[] elements = context.getPictogramElements();
		if (elements.length != 1) {
			return false;
		}

		PictogramElement pictogramElement = elements[0];
		BaseElement baseElement = BusinessObjectUtil.getFirstBaseElement(pictogramElement);

		if (AbstractExpandableActivityFeatureContainer.isExpandableElement(baseElement)) {
			return !FeatureSupport.isExpanded((ContainerShape) pictogramElement);
		} else {
			return false;
		}
	}

	@Override
	public void execute(ICustomContext context) {
		
		PictogramElement pictogramElement = context.getPictogramElements()[0];
		FlowNode flowNode = BusinessObjectUtil.getFirstElementOfType(pictogramElement, FlowNode.class);
		
		if (pictogramElement instanceof ContainerShape && flowNode != null) {
			ContainerShape containerShape = (ContainerShape) pictogramElement;

			// move to front
			GraphicsUtil.sendToFront(containerShape);
			
			BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(pictogramElement, BPMNShape.class);
			bpmnShape.setIsExpanded(true);

			// SubProcess is collapsed - resize to minimum size such that all children are visible
			// NOTE: children tasks will be set visible in UpdateExpandableActivityFeature

			GraphicsAlgorithm ga = containerShape.getGraphicsAlgorithm();
			ResizeShapeContext resizeContext = new ResizeShapeContext(containerShape);
			IResizeShapeFeature resizeFeature = getFeatureProvider().getResizeShapeFeature(resizeContext);
			int oldWidth = ga.getWidth();
			int oldHeight = ga.getHeight();

			IRectangle bounds = LayoutUtil.getChildrenBBox(containerShape, null, EXPAND_PADDING, EXPAND_PADDING);
			if (bounds == null) {
				bounds = ConversionUtil.rectangle(0, 0, 300, 200);
			}

			int newWidth = bounds.getWidth() + Math.abs(Math.min(0, bounds.getX()));
			int newHeight = bounds.getHeight() + Math.abs(Math.min(0, bounds.getY()));

			resizeContext.setX(ga.getX() + oldWidth / 2 - newWidth / 2);
			resizeContext.setY(ga.getY() + oldHeight / 2 - newHeight / 2);
			resizeContext.setWidth(newWidth);
			resizeContext.setHeight(newHeight);
			resizeFeature.resizeShape(resizeContext);
			
			UpdateContext updateContext = new UpdateContext(containerShape);
			
			IUpdateFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
			if (updateFeature.updateNeeded(updateContext).toBoolean()) {
				updateFeature.update(updateContext);
			}
		}
	}
}