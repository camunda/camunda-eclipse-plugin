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

import static org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.TRIGGERED_BY_EVENT;
import static org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.IS_EXPANDED;

import java.io.IOException;

import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil.Expand;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;

public class UpdateSubProcessFeature extends AbstractUpdateFeature {

	public UpdateSubProcessFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return bo != null && bo instanceof SubProcess;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		Property triggerProperty = Graphiti.getPeService().getProperty(context.getPictogramElement(),
		        TRIGGERED_BY_EVENT);
		Property expandedProperty = Graphiti.getPeService().getProperty(context.getPictogramElement(),
		        IS_EXPANDED);
		
		SubProcess process = (SubProcess) getBusinessObjectForPictogramElement(context.getPictogramElement());
		try {
			BPMNShape bpmnShape = (BPMNShape) ModelHandlerLocator.getModelHandler(getDiagram().eResource()).findDIElement(process);
			if (expandedProperty != null && Boolean.parseBoolean(expandedProperty.getValue()) != bpmnShape.isIsExpanded()) {
				return Reason.createTrueReason("Expanded property changed");
			}
			
		} catch (Exception e) {
			throw new IllegalStateException("Could not get DI shape for subprocess:"+process);
		}

		if (triggerProperty != null && Boolean.parseBoolean(triggerProperty.getValue()) != process.isTriggeredByEvent()) {
			return Reason.createTrueReason("Trigger property changed");
		}
			
		return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		SubProcess process = (SubProcess) getBusinessObjectForPictogramElement(context.getPictogramElement());
		ContainerShape container = (ContainerShape) context.getPictogramElement();
		ContainerShape markerContainer = (ContainerShape) GraphicsUtil.getShape(container,
				GraphicsUtil.ACTIVITY_MARKER_CONTAINER);
		boolean isExpanded = false;
		
		try {
			BPMNShape bpmnShape = (BPMNShape) ModelHandlerLocator.getModelHandler(getDiagram().eResource()).findDIElement(process);
			isExpanded =bpmnShape.isIsExpanded();
		} catch (IOException e) {
			throw new IllegalStateException("Could not get DI shape for subprocess:"+process);
		}
		Graphiti.getPeService().setPropertyValue(context.getPictogramElement(), TRIGGERED_BY_EVENT,
		        Boolean.toString(process.isTriggeredByEvent()));
		Graphiti.getPeService().setPropertyValue(context.getPictogramElement(), IS_EXPANDED,
		        Boolean.toString(isExpanded));

		RoundedRectangle rectangle = (RoundedRectangle) Graphiti.getPeService()
		        .getAllContainedPictogramElements(context.getPictogramElement()).iterator().next()
		        .getGraphicsAlgorithm();
		LineStyle lineStyle = process.isTriggeredByEvent() ? LineStyle.DOT : LineStyle.SOLID;
		rectangle.setLineStyle(lineStyle);

		if(!isExpanded){
			FeatureSupport.setContainerChildrenVisible(container, false);
			
			Expand expand = GraphicsUtil.createActivityMarkerExpand(markerContainer);
			expand.rect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			expand.horizontal.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			expand.vertical.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		}else{
			GraphicsUtil.clearActivityMarker(markerContainer, GraphicsUtil.ACTIVITY_MARKER_EXPAND);
			FeatureSupport.setContainerChildrenVisible(container, true);
		}
		
		return true;
	}
}