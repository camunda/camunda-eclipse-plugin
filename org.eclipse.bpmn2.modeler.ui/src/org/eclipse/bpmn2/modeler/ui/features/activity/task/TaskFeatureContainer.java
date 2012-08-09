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
package org.eclipse.bpmn2.modeler.ui.features.activity.task;

import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.impl.TaskImpl;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AbstractCreateTaskFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AddTaskFeature;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.FeatureEditingDialog;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.ObjectEditingDialog;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureAndContext;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.jface.window.Window;

public class TaskFeatureContainer extends AbstractTaskFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o.getClass().isAssignableFrom(TaskImpl.class);
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateTaskFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddTaskFeature(fp);
	}

	public static class CreateTaskFeature extends AbstractCreateTaskFeature<Task> {

		public CreateTaskFeature(IFeatureProvider fp) {
			super(fp, "Task", "Create Task");
		}

//		@Override
//		public Task createBusinessObject(ICreateContext context) {
//			Task task = Bpmn2ModelerFactory.create(Task.class);
//			task.setName("Task Name");
//			return task;
//		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_TASK;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getTask();
		}
		
// the object editing dialog stuff needs to go into the AddFeature, not here in CreateFeature
		public void postExecute(IExecutionInfo executionInfo) {
			for (IFeatureAndContext fc : executionInfo.getExecutionList()) {
				IContext context = fc.getContext();
				if (context instanceof ICreateContext) {
					ICreateContext cc = (ICreateContext)context;
					Task businessObject = getBusinessObject(cc);
					if (businessObject!=null) {
						ObjectEditingDialog dialog =
								new ObjectEditingDialog((BPMN2Editor)getDiagramEditor(), businessObject);
						dialog.open();
					}
				}
			}
		}
	}
}