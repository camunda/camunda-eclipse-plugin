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
package org.camunda.bpm.modeler.ui.features.activity.task;

import org.camunda.bpm.modeler.core.features.activity.task.AbstractCreateTaskFeature;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;

public class ServiceTaskFeatureContainer extends AbstractTaskFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof ServiceTask;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateServiceTaskFeature(fp);
	}
	
	@Override
	public IDecorateFeature getDecorateFeature(IFeatureProvider fp) {
		return new AbstractTaskDecorateFeature(fp) {
			@Override
			public String getIconId() {
				return ImageProvider.IMG_16_SERVICE_TASK;
			}
		};
	}

	public static class CreateServiceTaskFeature extends AbstractCreateTaskFeature<ServiceTask> {

		public CreateServiceTaskFeature(IFeatureProvider fp) {
			super(fp, "Service Task", "Task that uses some kind of service");
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_SERVICE_TASK;
		}

		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getServiceTask();
		}
	}
}
