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
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;

public class ServiceTaskFeatureContainer extends AbstractTaskFeatureContainer {

	private static final String TASK_ICON = Images.IMG_16_SERVICE_TASK;

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
				return TASK_ICON;
			}
		};
	}

	public static class CreateServiceTaskFeature extends AbstractCreateTaskFeature<ServiceTask> {

		public CreateServiceTaskFeature(IFeatureProvider fp) {
			super(fp, "Service Task", "Task that uses some kind of service");
		}

		@Override
		protected String getStencilImageId() {
			return TASK_ICON;
		}

		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getServiceTask();
		}
	}
}
