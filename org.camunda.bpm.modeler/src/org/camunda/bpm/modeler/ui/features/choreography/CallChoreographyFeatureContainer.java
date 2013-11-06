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
package org.camunda.bpm.modeler.ui.features.choreography;

import org.camunda.bpm.modeler.core.features.AbstractCreateFlowElementFeature;
import org.camunda.bpm.modeler.core.features.MultiUpdateFeature;
import org.camunda.bpm.modeler.core.features.choreography.UpdateChoreographyNameFeature;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallChoreography;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;

public class CallChoreographyFeatureContainer extends AbstractChoreographyFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof CallChoreography;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateCallChoreographyFeature(fp);
	}
	
	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddCallChoreographyFeature(fp);
	}

	@Override
	public MultiUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(new UpdateChoreographyNameFeature(fp));
		multiUpdate.addUpdateFeature(new UpdateChoreographyParticipantRefsFeature(fp) {
			@Override
			protected boolean isShowNames() {
				return false;
			}
		});
		multiUpdate.addUpdateFeature(new UpdateChoreographyInitiatingParticipantFeature(fp));
		// multiUpdate.addUpdateFeature(new UpdateChoreographyMarkerFeature(fp)); use it when property editor supports
		// enums
		return multiUpdate;
	}

	public static class CreateCallChoreographyFeature extends AbstractCreateFlowElementFeature<CallChoreography> {

		public CreateCallChoreographyFeature(IFeatureProvider fp) {
			super(fp, "Call Choreography",
					"Identifies a point in the Process where a global Choreography or a Global Choreography Task is used");
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_CHOREOGRAPHY_TASK;
		}
		
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getCallChoreography();
		}
	}
}
