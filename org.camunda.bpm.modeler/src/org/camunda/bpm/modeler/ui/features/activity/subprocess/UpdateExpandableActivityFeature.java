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

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

public class UpdateExpandableActivityFeature extends AbstractUpdateFeature {

	public static final String TRIGGERED_BY_EVENT = "triggered-by-event";
	public static final String IS_EXPANDED = "is-expanded";

	public UpdateExpandableActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();

		if (!FeatureSupport.isBpmnShape(pictogramElement)) {
			return false;
		}
		
		Object bo = getBusinessObjectForPictogramElement(pictogramElement);
		return AbstractExpandableActivityFeatureContainer.isExpandableElement(bo);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		PictogramElement pe = context.getPictogramElement();

		Property triggerProperty = Graphiti.getPeService().getProperty(pe, TRIGGERED_BY_EVENT);
		Property expandedProperty = Graphiti.getPeService().getProperty(pe, IS_EXPANDED);

		SubProcess process = (SubProcess) getBusinessObjectForPictogramElement(pe);

		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(pe, BPMNShape.class);
		if (expandedProperty == null || Boolean.parseBoolean(expandedProperty.getValue()) != bpmnShape.isIsExpanded()) {
			return Reason.createTrueReason("Expanded property changed");
		}

		if (triggerProperty == null || Boolean.parseBoolean(triggerProperty.getValue()) != process.isTriggeredByEvent()) {
			return Reason.createTrueReason("Trigger property changed");
		}

		return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		PictogramElement pe = context.getPictogramElement();
		SubProcess process = (SubProcess) getBusinessObjectForPictogramElement(pe);
		ContainerShape container = (ContainerShape) pe;

		boolean isExpanded = false;

		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(pe, BPMNShape.class);
		isExpanded = bpmnShape.isIsExpanded();

		Graphiti.getPeService().setPropertyValue(pe, TRIGGERED_BY_EVENT, Boolean.toString(process.isTriggeredByEvent()));
		Graphiti.getPeService().setPropertyValue(pe, IS_EXPANDED, Boolean.toString(isExpanded));

		RoundedRectangle rectangle = (RoundedRectangle) Graphiti.getPeService()
		        .getAllContainedPictogramElements(pe).iterator().next()
		        .getGraphicsAlgorithm();
		
		LineStyle lineStyle = process.isTriggeredByEvent() ? LineStyle.DOT : LineStyle.SOLID;
		rectangle.setLineStyle(lineStyle);

		if (!isExpanded) {
			FeatureSupport.setContainerChildrenVisible(container, false);
			GraphicsUtil.showActivityMarker(container, GraphicsUtil.ACTIVITY_MARKER_EXPAND);
		} else {
			FeatureSupport.setContainerChildrenVisible(container, true);
			GraphicsUtil.hideActivityMarker(container, GraphicsUtil.ACTIVITY_MARKER_EXPAND);
		}
		
		return true;
	}
}