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
/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.features;

import java.util.List;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.runtime.ModelExtensionDescriptor.Property;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomTaskFeatureContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class JbpmCustomTaskFeatureContainer extends CustomTaskFeatureContainer {
	
	@Override
	public Object getApplyObject(IContext context) {
		if (context instanceof IAddContext) {
			Object obj = ((IAddContext)context).getNewObject();
			if (obj instanceof Task) {
				if ( getId((Task)obj)!=null )
					return obj;
//				Task task = (Task)obj;
//				EAttribute taskNameAttr = ModelPackage.eINSTANCE.getDocumentRoot_TaskName();
//				String taskName = (String)task.eGet(taskNameAttr);
//				String myName = (String)customTaskDescriptor.getProperty("taskName");
//				if (taskName!=null && myName!=null && taskName.equals(myName))
//					return obj;
			}
		}
		else
			return super.getApplyObject(context);
		
		return null;
	}
	
	public String getId(EObject object) {
		List<EStructuralFeature> features = ModelUtil.getAnyAttributes(object);
		for (EStructuralFeature f : features) {
			if ("taskName".equals(f.getName())) {
				Object attrValue = object.eGet(f);
				if (attrValue!=null) {
					// search the extension attributes for a "taskName" and compare it
					// against the new object's taskName value
					for (Property p : customTaskDescriptor.getProperties()) {
						String propValue = p.getFirstStringValue();
						if (attrValue.equals(propValue))
							return getId();
					}
				}
			}
		}
		return null;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		String iconPath = (String) customTaskDescriptor.getProperty("icon");
		return new JbpmCreateCustomTaskFeature(fp,
				customTaskDescriptor.getName(),
				customTaskDescriptor.getDescription(),
				iconPath);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		final String iconPath = (String) customTaskDescriptor.getProperty("icon"); 
		if (iconPath != null && iconPath.trim().length() > 0) {
			return new AddCustomTaskFeature(fp) {

				@Override
				protected void decorateActivityRectangle(RoundedRectangle rect) {
					IGaService service = Graphiti.getGaService();
					Image img = service.createImage(rect, iconPath);
					service.setLocationAndSize(img, 2, 2, GraphicsUtil.TASK_IMAGE_SIZE, 
								GraphicsUtil.TASK_IMAGE_SIZE);
				}
			};
			
		}
		return new AddCustomTaskFeature(fp) {

			@Override
			protected void decorateActivityRectangle(RoundedRectangle rect) {
				IGaService service = Graphiti.getGaService();
				Image img = service.createImage(rect, ImageProvider.IMG_16_USER_TASK);
				service.setLocationAndSize(img, 2, 2, GraphicsUtil.TASK_IMAGE_SIZE, 
							GraphicsUtil.TASK_IMAGE_SIZE);
			}
		};
	}
	
	public class JbpmCreateCustomTaskFeature extends CreateCustomTaskFeature {
		
		private String imagePath = null;

		public JbpmCreateCustomTaskFeature(IFeatureProvider fp, String name,
				String description) {
			super(fp, name, description);
		}
		
		public JbpmCreateCustomTaskFeature(IFeatureProvider fp, String name,
				String description, String imagePath) {
			this(fp, name, description);
			this.imagePath = imagePath;
		}

		@Override
		public String getCreateImageId() {
			return this.imagePath;
		}
	}

}
