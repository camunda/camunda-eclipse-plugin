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

package org.camunda.bpm.modeler.core.features.event;

import java.util.List;

import org.camunda.bpm.modeler.core.features.activity.AbstractUpdateMarkerFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

/**
 * @author Bob Brodt
 * @author nico.rehwaldt
 */
public abstract class AbstractUpdateEventFeature extends AbstractUpdateMarkerFeature<Event> {

	public static final String EVENT_DEFINITIONS_MARKER = "EVENT_DEFINITIONS_MARKER";
	
	/**
	 * @param fp
	 */
	public AbstractUpdateEventFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canUpdate(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		return getBusinessObjectForPictogramElement(pictogramElement) instanceof Event;
	}
	
	@Override
    public IReason updateNeeded(IUpdateContext context) {
		if (super.updateNeeded(context).toBoolean()) {
			return Reason.createTrueReason();
		}
		
		return Reason.createFalseReason();
	}
	
	@Override
	protected boolean isPropertyChanged(Event element, String propertyValue) {
		return !getEventDefinitionsValue(element).equals(propertyValue);
	}

	@Override
	protected final String getPropertyKey() {
		return EVENT_DEFINITIONS_MARKER;
	}
	
	@Override
	protected void doUpdate(Event event, ContainerShape container) {
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(event);
		int size = eventDefinitions.size();
		
		GraphicsUtil.deleteEventShape(container);
		if (size!=0) {
			EventDefinition eventDefinition = eventDefinitions.get(0);

			// either find the existing Shape that is linked with an EventDefinition...
			PictogramElement eventDefinitionShape = GraphicsUtil.getEventShape(container);
			
			if (eventDefinitionShape == null) {
				// ...or create a temporary Shape that we can link
				// with the event definition business object... 
				eventDefinitionShape = Graphiti.getPeService().createShape(container, true);
				link(eventDefinitionShape, eventDefinition);
			}
			
			// ...so we can create an UpdateContext...
			UpdateContext context = new UpdateContext(eventDefinitionShape);
			// ...to look up the EventDefinitionUpdateFeature
			IUpdateFeature upateFeature = getFeatureProvider().getUpdateFeature(context);
			if (upateFeature!=null) {
				// and do the update with the Event object (not the EventDefinition!)
				context = new UpdateContext(container);
				upateFeature.update(context);
			}
		}
	}

	@Override
	protected String convertPropertyToString(Event element) {
		return getEventDefinitionsValue(element);
	}
	
	public static String getEventDefinitionsValue(Event element) {
		String result = element.getClass().getSimpleName() + "#";
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(element);
		for (EventDefinition ed : eventDefinitions) {
			if (!result.isEmpty())
				result += " ";
			result += ed.getClass().getSimpleName() + ":" + ed.getId();
		}
		// Parallel Multiple has a different visual than Multiple for Catch Events
		if (element instanceof CatchEvent) {
			if (((CatchEvent)element).isParallelMultiple())
				result += "+";
		}
		return result;
	}
}
