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
package org.camunda.bpm.modeler.core.features.activity;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

public abstract class AbstractUpdateMarkerFeature<T extends FlowElement> extends AbstractUpdateFeature {

	public AbstractUpdateMarkerFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canUpdate(IUpdateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return bo != null && bo instanceof Activity && context.getPictogramElement() instanceof ContainerShape;
    }

	@Override
    public IReason updateNeeded(IUpdateContext context) {
		IPeService peService = Graphiti.getPeService();
		PictogramElement element = context.getPictogramElement();
		String property = peService.getPropertyValue(element, getPropertyKey());
		if(property == null) {
			return Reason.createFalseReason();
		}
		T activity = (T) getBusinessObjectForPictogramElement(context.getPictogramElement());
		boolean changed = isPropertyChanged(activity, property);
		return changed ? Reason.createTrueReason() : Reason.createFalseReason();
    }

	@Override
    public boolean update(IUpdateContext context) {
		IPeService peService = Graphiti.getPeService();
		ContainerShape container = (ContainerShape) context.getPictogramElement();
		T element = (T) getBusinessObjectForPictogramElement(context.getPictogramElement());

		doUpdate(element, container);
		peService.setPropertyValue(container, getPropertyKey(), convertPropertyToString(element));
		return true;
    }
	
	protected abstract String getPropertyKey();
	
	protected abstract boolean isPropertyChanged(T element, String propertyValue);

	protected abstract void doUpdate(T element, ContainerShape markerContainer);
	
	protected abstract String convertPropertyToString(T element);
}
