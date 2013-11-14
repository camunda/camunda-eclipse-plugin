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
package org.camunda.bpm.modeler.ui.features.lane;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.features.AbstractBpmn2CreateFeature;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Lane;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class CreateLaneFeature extends AbstractBpmn2CreateFeature<Lane> {

	private static int index = 1;

	public CreateLaneFeature(IFeatureProvider fp) {
		super(fp, "Lane", "A sub-partition in a process that helps to organize and categorize activities");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoSubprocess = FeatureSupport.isTargetSubProcess(context);
		
		return !intoDiagram && !intoSubprocess && (intoLane || intoParticipant);
	}

	@Override
	public Object[] create(ICreateContext context) {
		Lane lane = createBusinessObject(context);
		lane.setName("Lane "+ModelUtil.getIDNumber(lane.getId()));
		addGraphicalRepresentation(context, lane);
		return new Object[] { lane };
	}

	@Override
	public String getCreateImageId() {
		return Images.IMG_16_LANE;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}

	/* (non-Javadoc)
	 * @see org.camunda.bpm.modeler.features.AbstractBpmn2CreateFeature#getBusinessObjectClass()
	 */
	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getLane();
	}

	@Override
	public Lane createBusinessObject(ICreateContext context) {
		Lane bo = null;
		ModelHandler mh = ModelHandler.getInstance(getDiagram());
		Object o = getBusinessObjectForPictogramElement(context.getTargetContainer());
		if (FeatureSupport.isTargetLane(context)) {
			Lane targetLane = (Lane) o;
			bo = ModelHandler.createLane(targetLane);
		} else {
			bo = mh.createLane(o);
		}
		putBusinessObject(context, bo);
		
		return bo;
	}
}