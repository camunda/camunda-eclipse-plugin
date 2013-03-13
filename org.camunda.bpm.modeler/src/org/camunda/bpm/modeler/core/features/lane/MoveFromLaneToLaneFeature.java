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
package org.camunda.bpm.modeler.core.features.lane;

import java.io.IOException;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.model.Bpmn2ModelerFactory;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class MoveFromLaneToLaneFeature extends MoveLaneFeature {

	public MoveFromLaneToLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		Lane targetLane = getTargetLane(context);

		if (targetLane.getFlowNodeRefs() != null && targetLane.getFlowNodeRefs().size() > 0) {
			return false;
		}

		Lane movedLane = getMovedLane(context);

		if (targetLane.getChildLaneSet() != null && targetLane.getChildLaneSet().getLanes().contains(movedLane)) {
			return false;
		}

		return true;
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		Lane movedLane = getMovedLane(context);
		Lane targetLane = getTargetLane(context);
		Lane sourceLane = getSourceLane(context);

		modifyModelStructure(sourceLane, targetLane, movedLane);

		FeatureSupport.redraw(context.getSourceContainer());
		FeatureSupport.redraw(context.getTargetContainer());
	}

	private void modifyModelStructure(Lane sourceLane, Lane targetLane, Lane movedLane) {
		if (targetLane.getChildLaneSet() == null) {
			LaneSet createLaneSet = Bpmn2ModelerFactory.create(LaneSet.class);
//			createLaneSet.setId(EcoreUtil.generateUUID());
			targetLane.setChildLaneSet(createLaneSet);
			ModelUtil.setID(createLaneSet);
		}

		ModelHandler handler = ModelHandler.getInstance(getDiagram());
		Participant sourceParticipant = handler.getParticipant(sourceLane);
		Participant targetParticipant = handler.getParticipant(targetLane);
		if (!sourceParticipant.equals(targetParticipant)) {
			handler.moveLane(movedLane, sourceParticipant, targetParticipant);
		}

		targetLane.getChildLaneSet().getLanes().add(movedLane);
		sourceLane.getChildLaneSet().getLanes().remove(movedLane);

		if (sourceLane.getChildLaneSet().getLanes().isEmpty()) {
			sourceLane.setChildLaneSet(null);
		}
	}

	private Lane getTargetLane(ITargetContext context) {
		ContainerShape targetContainer = context.getTargetContainer();
		return (Lane) getBusinessObjectForPictogramElement(targetContainer);
	}

	private Lane getSourceLane(IMoveShapeContext context) {
		ContainerShape sourceContainer = context.getSourceContainer();
		return (Lane) getBusinessObjectForPictogramElement(sourceContainer);
	}
}
