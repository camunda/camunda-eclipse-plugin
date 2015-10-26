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

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.ui.features.activity.ResizeActivityFeature;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

/**
 * Expandable activities resize feature.
 * 
 * @author nico.rehwaldt
 */
public class ResizeExpandableActivityFeature extends ResizeActivityFeature {
	
	public ResizeExpandableActivityFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		ContainerShape containerShape = (ContainerShape) context.getShape();
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(containerShape, BPMNShape.class);
		
		// can only resize non-expanded containers
		if (!bpmnShape.isIsExpanded()) {
			return false;
		}
		
		return super.canResizeShape(context);
	}


	@Override
	protected void postResize(IResizeShapeContext context) {
		super.postResize(context);

		IGaService gaService = Graphiti.getGaService();

		ContainerShape containerShape = (ContainerShape) context.getShape();
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(containerShape, BPMNShape.class);

		// resize the MultiText element (holding the name of the element) appropriately
		for (PictogramElement pe : containerShape.getChildren()) {
			GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();
			if (ga instanceof MultiText) {
				gaService.setLocationAndSize(ga, 5, 5, (int) bpmnShape.getBounds().getWidth() - 10, (int) bpmnShape.getBounds().getHeight() - 10);
				break;
			}
		}
	}

}
