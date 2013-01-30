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
package org.eclipse.bpmn2.modeler.ui.features.activity.subprocess;

import static org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.IS_EXPANDED;
import static org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.TRIGGERED_BY_EVENT;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.features.activity.AbstractAddActivityFeature;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddExpandableActivityFeature<T extends Activity>
	extends AbstractAddActivityFeature<T> {

	public AddExpandableActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postCreateHook(IAddContext context, IRectangle newShapeBounds, ContainerShape newShape) {
		super.postCreateHook(context, newShapeBounds, newShape);
		
		IPeService peService = Graphiti.getPeService();
		IGaService gaService = Graphiti.getGaService();

		T activity = getBusinessObject(context);
		
		int width = newShapeBounds.getWidth();
		
		boolean isTriggeredByEvent = false;
		boolean isExpanded = true;
		
		if (activity instanceof SubProcess) {
			SubProcess subprocess = (SubProcess) activity;
			isTriggeredByEvent = subprocess.isTriggeredByEvent();
			
			BPMNShape bpmnShape = (BPMNShape) BusinessObjectUtil.getFirstElementOfType(newShape, BPMNShape.class);
			if (bpmnShape != null) {
				isExpanded = bpmnShape.isIsExpanded();
			}
		}
		
		peService.setPropertyValue(newShape, TRIGGERED_BY_EVENT, Boolean.toString(isTriggeredByEvent));
		peService.setPropertyValue(newShape, IS_EXPANDED, Boolean.toString(isExpanded));

		Shape textShape = peService.createShape(newShape, false);
		Text text = gaService.createDefaultText(getDiagram(), textShape, activity.getName());
		gaService.setLocationAndSize(text, 5, 5, width - 10, 15);
		StyleUtil.applyStyle(text, activity);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		link(textShape, activity);
		
		if (isExpanded) {
			GraphicsUtil.hideActivityMarker(newShape, GraphicsUtil.ACTIVITY_MARKER_EXPAND);
		} else {
			GraphicsUtil.showActivityMarker(newShape, GraphicsUtil.ACTIVITY_MARKER_EXPAND);
		}
	}

	@Override
	public int getWidth() {
		if (Bpmn2Preferences.getInstance().isExpandedDefault())
			return GraphicsUtil.SUB_PROCEESS_DEFAULT_WIDTH;
		return GraphicsUtil.TASK_DEFAULT_WIDTH;
	}

	@Override
	public int getHeight() {
		if (Bpmn2Preferences.getInstance().isExpandedDefault())
			return GraphicsUtil.SUB_PROCESS_DEFAULT_HEIGHT;
		return GraphicsUtil.TASK_DEFAULT_HEIGHT;
	}

	@Override
	protected boolean isCreateExternalLabel() {
		return false;
	}
}