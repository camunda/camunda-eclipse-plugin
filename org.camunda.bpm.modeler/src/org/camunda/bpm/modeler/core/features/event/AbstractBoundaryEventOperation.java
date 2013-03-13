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
package org.camunda.bpm.modeler.core.features.event;

import java.util.Collection;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * Operation to be executed on all boundary events of a given shape
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractBoundaryEventOperation {

	public void execute(Shape shape) {
		execute(shape, shape.getContainer());
	}
	
	public void execute(Shape shape, Shape eventContainer) {
		
		Assert.isNotNull(eventContainer);
		
		Collection<PictogramElement> elements = Graphiti.getPeService().getAllContainedPictogramElements(eventContainer);

		Activity activity = BusinessObjectUtil.getFirstElementOfType(shape, Activity.class);
		List<BoundaryEvent> boundaryEvents = activity.getBoundaryEventRefs();
		
		for (PictogramElement e : elements) {
			BoundaryEvent boundaryEvent = BusinessObjectUtil.getFirstElementOfType(e, BoundaryEvent.class);
			if (boundaryEvents.contains(boundaryEvent) && !LabelUtil.isLabel(e)) {
				ContainerShape boundaryShape = (ContainerShape) e;
				applyTo(boundaryShape);
			}
		}
	}

	protected abstract void applyTo(ContainerShape boundaryShape);
}