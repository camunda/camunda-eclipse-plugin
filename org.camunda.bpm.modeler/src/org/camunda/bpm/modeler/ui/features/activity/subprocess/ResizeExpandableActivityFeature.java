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
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

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
}
