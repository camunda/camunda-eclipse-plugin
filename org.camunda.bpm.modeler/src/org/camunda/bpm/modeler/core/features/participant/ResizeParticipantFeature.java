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
package org.camunda.bpm.modeler.core.features.participant;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import java.util.List;

import org.camunda.bpm.modeler.core.features.DefaultResizeBPMNShapeFeature;
import org.camunda.bpm.modeler.core.features.lane.ResizeLaneSetFeature;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.CollaborationResizeSupport;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.BBox;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.core.layout.util.RectangleUtil.ResizeDiff;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.Lane;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.ResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class ResizeParticipantFeature extends ResizeLaneSetFeature {

	public static final String POOL_RESIZE_PROPERTY = "pool.resize";
	public static final String RESIZE_FIRST_LANE = "resize.first.lane";
	
	public ResizeParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		EObject container = context.getShape().eContainer();
		if (container instanceof PictogramElement) {
			PictogramElement containerElem = (PictogramElement) container;
			if (BusinessObjectUtil.containsElementOfType(containerElem, ChoreographyActivity.class)) {
				return false;
			}
		}
		return super.canResizeShape(context);
	}
}
