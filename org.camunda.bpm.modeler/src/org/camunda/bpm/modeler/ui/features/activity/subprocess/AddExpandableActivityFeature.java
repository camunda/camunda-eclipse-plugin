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
package org.camunda.bpm.modeler.ui.features.activity.subprocess;

import static org.camunda.bpm.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.IS_EXPANDED;
import static org.camunda.bpm.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.TRIGGERED_BY_EVENT;

import org.camunda.bpm.modeler.core.features.activity.AbstractAddActivityFeature;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences.BPMNDIAttributeDefault;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
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
				
		// set to default value so that update pics up the actual drawing
		peService.setPropertyValue(newShape, TRIGGERED_BY_EVENT, "false");

		Shape textShape = peService.createShape(newShape, false);
		Text text = gaService.createDefaultText(getDiagram(), textShape, activity.getName());
		gaService.setLocationAndSize(text, 5, 5, width - 10, 15);
		StyleUtil.applyStyle(text, activity);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		link(textShape, activity);
	}
	
	@Override
	protected void postAddHook(IAddContext context, ContainerShape newShape) {
		super.postAddHook(context, newShape);

		T activity = getBusinessObject(context);

		boolean isExpanded = true;

		if (activity instanceof SubProcess) {
			BPMNShape bpmnShape = (BPMNShape) BusinessObjectUtil.getFirstElementOfType(newShape, BPMNShape.class);
			if (bpmnShape != null) {
				isExpanded = bpmnShape.isIsExpanded();
			}
		}

		if (activity instanceof CallActivity) {
			BPMNShape bpmnShape = (BPMNShape) BusinessObjectUtil.getFirstElementOfType(newShape, BPMNShape.class);
			if (bpmnShape != null) {
				isExpanded = bpmnShape.isIsExpanded();
			}
		}

		IPeService peService = Graphiti.getPeService();
		peService.setPropertyValue(newShape, IS_EXPANDED, Boolean.toString(isExpanded));

		if (isExpanded) {
			GraphicsUtil.hideActivityMarker(newShape, GraphicsUtil.ACTIVITY_MARKER_EXPAND);
		} else {
			GraphicsUtil.showActivityMarker(newShape, GraphicsUtil.ACTIVITY_MARKER_EXPAND);
		}

		// set a property on the decorators so we can distinguish them from the
		// real children (i.e. tasks, etc.)
		for (PictogramElement pe : newShape.getChildren()) {
			Graphiti.getPeService().setPropertyValue(pe, ACTIVITY_DECORATOR, "true");
		}
	}
	
	@Override
	protected BPMNShape createDi(Shape shape, BaseElement baseElement, IAddContext context) {
		Bpmn2Preferences preferences = Bpmn2Preferences.getInstance();
		// check whether it is an import or a creation of an BPMN element
		boolean isImport = isImport(context);
		if (!isImport) {
			// manipulate BPMN2 global preferences for a subprocess creation
			// because we want always create an expanded subprocess
			// Note: preferences.setIsExpanded throws an exception during the test case execution
			// because the project preferences are null
			preferences.getGlobalPreferences().setValue(Bpmn2Preferences.PREF_IS_EXPANDED, BPMNDIAttributeDefault.DEFAULT_TRUE.name());
		}
		BPMNShape bpmnShape = createDIShape(shape, baseElement, findDIShape(baseElement), isImport);
		// change back to the default BPMN2 preferences
		preferences.getGlobalPreferences().setValue(Bpmn2Preferences.PREF_IS_EXPANDED, BPMNDIAttributeDefault.USE_DI_VALUE.name());
		return bpmnShape;
	}

	@Override
	public int getDefaultWidth() {
		if (Bpmn2Preferences.getInstance().isExpandedDefault())
			return GraphicsUtil.SUB_PROCEESS_DEFAULT_WIDTH;
		return GraphicsUtil.TASK_DEFAULT_WIDTH;
	}

	@Override
	public int getDefaultHeight() {
		if (Bpmn2Preferences.getInstance().isExpandedDefault())
			return GraphicsUtil.SUB_PROCESS_DEFAULT_HEIGHT;
		return GraphicsUtil.TASK_DEFAULT_HEIGHT;
	}

	@Override
	protected boolean isCreateExternalLabel() {
		return false;
	}
}