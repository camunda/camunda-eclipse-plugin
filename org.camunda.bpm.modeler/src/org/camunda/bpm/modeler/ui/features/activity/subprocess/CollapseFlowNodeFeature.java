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

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.di.BPMNShape;
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

public class CollapseFlowNodeFeature extends AbstractCustomFeature {

	public CollapseFlowNodeFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "Collapse"; //$NON-NLS-1$
	}

	@Override
	public String getDescription() {

		return "Collapse the Activity and hide contents"; //$NON-NLS-1$
	}

	@Override
	public String getImageId() {
		return Images.IMG_16_COLLAPSE;
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
			return FeatureSupport.isExpanded((ContainerShape) pictogramElement);
		} else {
			return false;
		}
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe0 = pes[0];
			Object bo = getBusinessObjectForPictogramElement(pe0);
			if (pe0 instanceof ContainerShape && bo instanceof FlowNode) {
				ContainerShape containerShape = (ContainerShape)pe0;
				FlowNode flowNode = (FlowNode) bo;
				BPMNShape bpmnShape = (BPMNShape) ModelHandler.findDIElement(getDiagram(), flowNode);
				if (bpmnShape.isIsExpanded()) {

					// SubProcess is collapsed - resize to standard modelObject size
					// NOTE: children tasks will be set not-visible in UpdateExpandableActivityFeature

					bpmnShape.setIsExpanded(false);

					GraphicsAlgorithm ga = containerShape.getGraphicsAlgorithm();
					ResizeShapeContext resizeContext = new ResizeShapeContext(containerShape);
					IResizeShapeFeature resizeFeature = getFeatureProvider().getResizeShapeFeature(resizeContext);
					int oldWidth = ga.getWidth();
					int oldHeight = ga.getHeight();
					int newWidth = GraphicsUtil.getActivitySize(getDiagram()).getWidth();
					int newHeight = GraphicsUtil.getActivitySize(getDiagram()).getHeight();
					resizeContext.setX(ga.getX() + oldWidth/2 - newWidth/2);
					resizeContext.setY(ga.getY() + oldHeight/2 - newHeight/2);
					resizeContext.setWidth(newWidth);
					resizeContext.setHeight(newHeight);
					resizeFeature.resizeShape(resizeContext);
					
					UpdateContext updateContext = new UpdateContext(containerShape);
					IUpdateFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
					if (updateFeature.updateNeeded(updateContext).toBoolean())
						updateFeature.update(updateContext);
					
					getDiagramEditor().selectPictogramElements(new PictogramElement[] {});
				}
			}
		}
	}
}
