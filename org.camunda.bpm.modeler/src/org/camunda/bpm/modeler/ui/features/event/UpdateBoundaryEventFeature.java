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
package org.camunda.bpm.modeler.ui.features.event;

import static org.camunda.bpm.modeler.ui.features.event.BoundaryEventFeatureContainer.BOUNDARY_EVENT_CANCEL;

import org.camunda.bpm.modeler.core.features.event.AbstractUpdateEventFeature;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class UpdateBoundaryEventFeature extends AbstractUpdateEventFeature {

	public static String BOUNDARY_EVENT_MARKER = "marker.boundary.event";

	public UpdateBoundaryEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		if (super.updateNeeded(context).toBoolean())
			return Reason.createTrueReason();
		
		String cancelProperty = Graphiti.getPeService().getPropertyValue(context.getPictogramElement(),
		        BOUNDARY_EVENT_CANCEL);
		BoundaryEvent event = (BoundaryEvent) getBusinessObjectForPictogramElement(context.getPictogramElement());
		boolean changed = Boolean.parseBoolean(cancelProperty) != event.isCancelActivity();
		IReason reason = changed ? Reason.createTrueReason("Boundary description changed") : Reason.createFalseReason();
		return reason;
	}

	@Override
	public boolean update(IUpdateContext context) {
		super.update(context);

		ContainerShape shape = (ContainerShape) context.getPictogramElement();

		BoundaryEvent event = (BoundaryEvent) getBusinessObjectForPictogramElement(shape);
		
		boolean cancelActivity = event.isCancelActivity();
		
		Graphiti.getPeService().setPropertyValue(shape, BOUNDARY_EVENT_CANCEL, Boolean.toString(cancelActivity));

		Shape child = shape.getChildren().get(0);

		Ellipse ellipse = (Ellipse) child.getGraphicsAlgorithm();
		Ellipse innerEllipse = (Ellipse) ellipse.getGraphicsAlgorithmChildren().get(0);
		LineStyle lineStyle = cancelActivity ? LineStyle.SOLID : LineStyle.DASH;

		ellipse.setLineStyle(lineStyle);
		innerEllipse.setLineStyle(lineStyle);

		return true;
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof BoundaryEvent;
	}

	@Override
	protected String getPropertyKey() {
		return BOUNDARY_EVENT_MARKER;
	}
}