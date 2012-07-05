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

package org.eclipse.bpmn2.modeler.ui.features.lane;

import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.ui.features.AbstractDefaultDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

/**
 * @author Bob Brodt
 *
 */
public class DeleteLaneFeature extends AbstractDefaultDeleteFeature {

	/**
	 * @param fp
	 */
	public DeleteLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void delete(IDeleteContext context) {
		ContainerShape laneContainerShape = (ContainerShape) context.getPictogramElement();
		ContainerShape parentContainerShape = laneContainerShape.getContainer();

		super.delete(context);
		
		if (FeatureSupport.isLane(parentContainerShape)) {
			// resize containing Lane
		}
		else if (FeatureSupport.isParticipant(parentContainerShape)) {
			// resize containing Pool
		}
	}
}
