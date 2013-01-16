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

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.ui.features.AbstractDefaultDeleteFeature;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.impl.ResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;

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
		
		if (parentContainerShape != null) {
			
			boolean before = false;
			ContainerShape neighborContainerShape = FeatureSupport.getLaneAfter(laneContainerShape);
			
			if (neighborContainerShape == null) {
				neighborContainerShape = FeatureSupport.getLaneBefore(laneContainerShape);
				if (neighborContainerShape == null) {
					super.delete(context);
					return;
				} else {
					before = true;
				}
			}
			
			boolean isHorizontal = FeatureSupport.isHorizontal(laneContainerShape);
			GraphicsAlgorithm ga = laneContainerShape.getGraphicsAlgorithm();
			GraphicsAlgorithm neighborGA = neighborContainerShape.getGraphicsAlgorithm();
			ResizeShapeContext newContext = new ResizeShapeContext(neighborContainerShape);
			if (!before) {
				Graphiti.getGaService().setLocation(neighborGA, ga.getX(), ga.getY());
			}
			newContext.setLocation(neighborGA.getX(), neighborGA.getY());
			if (isHorizontal) {
				newContext.setHeight(neighborGA.getHeight() + ga.getHeight());
				newContext.setWidth(neighborGA.getWidth());
			} else {
				newContext.setHeight(neighborGA.getHeight());
				newContext.setWidth(neighborGA.getWidth() + ga.getWidth());
			}
			
			IResizeShapeFeature resizeFeature = getFeatureProvider().getResizeShapeFeature(newContext);
			if (resizeFeature.canResizeShape(newContext)) {
				super.delete(context);
				resizeFeature.resizeShape(newContext);
				return;
			}
		}
		super.delete(context);
	}
	
	@Override
	protected void deleteBusinessObject(Object bo) {
		
		// remove lane AND empty lanesets
		if (bo instanceof Lane) {
			Lane lane = (Lane) bo;
			LaneSet laneSet = (LaneSet) lane.eContainer();
			
			super.deleteBusinessObject(bo);
			
			// delete lane set, too if it has no lanes
			if (laneSet.getLanes().isEmpty()) {
				super.deleteBusinessObject(laneSet);
			}
		} else {
			super.deleteBusinessObject(bo);
		}
	}
}
