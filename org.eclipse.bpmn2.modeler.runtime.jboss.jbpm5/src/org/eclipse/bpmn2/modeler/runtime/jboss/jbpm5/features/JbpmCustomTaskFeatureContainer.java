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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.ItemKind;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.IBpmn2RuntimeExtension;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.runtime.ModelExtensionDescriptor.Property;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.JBPM5RuntimeExtension;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.customeditor.SampleCustomEditor;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.ParameterDefinition;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.WorkDefinition;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.DataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.DataTypeFactory;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.DataTypeRegistry;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.impl.type.UndefinedDataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.impl.ParameterDefinitionImpl;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.impl.WorkDefinitionImpl;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.impl.WorkImpl;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.wid.WorkItemDefinition;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomTaskFeatureContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
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
		if (iconPath != null && iconPath.trim().length() > 0) {
			return new JbpmCreateCustomTaskFeature(fp,
					customTaskDescriptor.getName(),
					customTaskDescriptor.getDescription(),
					iconPath);
		}
		return new JbpmCreateCustomTaskFeature(fp,
				customTaskDescriptor.getName(),
				customTaskDescriptor.getDescription());
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
	
	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider fp) {
		return new ICustomFeature[] {new DoubleClickFeature(fp)};
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

	public class DoubleClickFeature implements ICustomFeature {

		protected IFeatureProvider fp;
		boolean hasChanges = false;
		
		/**
		 * @param fp
		 */
		public DoubleClickFeature(IFeatureProvider fp) {
			this.fp = fp;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.features.IFeature#isAvailable(org.eclipse.graphiti.features.context.IContext)
		 */
		@Override
		public boolean isAvailable(IContext context) {
			return true;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.features.custom.ICustomFeature#canExecute(org.eclipse.graphiti.features.context.ICustomContext)
		 */
		@Override
		public boolean canExecute(ICustomContext context) {
			return canExecute((IContext)context);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.features.IFeature#canExecute(org.eclipse.graphiti.features.context.IContext)
		 */
		@Override
		public boolean canExecute(IContext context) {
			BPMN2Editor editor = (BPMN2Editor)getFeatureProvider().getDiagramTypeProvider().getDiagramEditor();
			IBpmn2RuntimeExtension rte = editor.getTargetRuntime().getRuntimeExtension();
			if (rte instanceof JBPM5RuntimeExtension && context instanceof ICustomContext) {
				PictogramElement[] pes = ((ICustomContext) context).getPictogramElements();
				if (pes.length==1) {
					Object o = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pes[0]);
					if (o instanceof Task) {
						Task task = (Task)o;
						List<EStructuralFeature> features = ModelUtil.getAnyAttributes(task);
						for (EStructuralFeature f : features) {
							if ("taskName".equals(f.getName())) {
								// make sure the Work Item Definition exists
								String taskName = (String)task.eGet(f);
								return ((JBPM5RuntimeExtension)rte).getWorkItemDefinition(taskName) != null;
							}
						}
					}
				}
			}
			return false;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.features.custom.ICustomFeature#execute(org.eclipse.graphiti.features.context.ICustomContext)
		 */
		@Override
		public void execute(ICustomContext context) {
			execute((IContext)context);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.features.IFeature#execute(org.eclipse.graphiti.features.context.IContext)
		 */
		@Override
		public void execute(IContext context) {
			BPMN2Editor editor = (BPMN2Editor)getFeatureProvider().getDiagramTypeProvider().getDiagramEditor();
			PictogramElement pe = ((ICustomContext) context).getPictogramElements()[0];
			final Task task = (Task)Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			String taskName = "";
			List<EStructuralFeature> features = ModelUtil.getAnyAttributes(task);
			for (EStructuralFeature f : features) {
				if ("taskName".equals(f.getName())) {
					taskName = (String)task.eGet(f);
					break;
				}
			}
			
			IBpmn2RuntimeExtension rte = editor.getTargetRuntime().getRuntimeExtension();
			WorkItemDefinition workItemDefinition = ((JBPM5RuntimeExtension)rte).getWorkItemDefinition(taskName);
			
			WorkDefinitionImpl wd = new WorkDefinitionImpl();
			for (String name : workItemDefinition.getParameters().keySet()) {
				String type = workItemDefinition.getParameters().get(name);
				DataTypeFactory factory = DataTypeRegistry.getFactory(type);
				wd.addParameter( new ParameterDefinitionImpl(name,factory.createDataType()) );
			}
			
			WorkImpl w = new WorkImpl();
			w.setName(taskName);
			w.setParameterDefinitions(wd.getParameters());
			for (DataInputAssociation dia : task.getDataInputAssociations()) {
				DataInput dataInput = (DataInput)dia.getTargetRef();
				if (dataInput!=null) {
					String name = dataInput.getName();
					ItemDefinition itemDefinition = dataInput.getItemSubjectRef();
					if (itemDefinition!=null) {
						Object structureRef = itemDefinition.getStructureRef();
						if (ModelUtil.isStringWrapper(structureRef)) {
							ParameterDefinition parameterDefinition = w.getParameterDefinition(name);
							try {
								Object value = parameterDefinition.getType().readValue(ModelUtil.getStringWrapperValue(structureRef));
								w.setParameter(name, value);
							}
							catch (Exception e) {
							}
						}
					}
				}
			}

			SampleCustomEditor dialog = new SampleCustomEditor(editor.getSite().getShell());
			dialog.setWorkDefinition(wd);
			dialog.setWork(w);
			dialog.show();
			
			hasChanges = dialog.getWork() != w;
			if (hasChanges) {
				w = (WorkImpl) dialog.getWork();
				for (DataInputAssociation dia : task.getDataInputAssociations()) {
					DataInput dataInput = (DataInput)dia.getTargetRef();
					if (dataInput!=null) {
						String name = dataInput.getName();
						ItemDefinition itemDefinition = dataInput.getItemSubjectRef();
						// this always comes back as a String from the SampleCustomEditor dialog
						String value = (String)w.getParameter(name);
						if (value!=null && !value.isEmpty()) {
							EObject structureRef = ModelUtil.createStringWrapper(value);
							if (itemDefinition==null) {
								itemDefinition = Bpmn2Factory.eINSTANCE.createItemDefinition();
								ModelUtil.getDefinitions(task).getRootElements().add(itemDefinition);
								ModelUtil.setID(itemDefinition);
							}
							itemDefinition.setItemKind(ItemKind.PHYSICAL);
							itemDefinition.setStructureRef(structureRef);
							dataInput.setItemSubjectRef(itemDefinition);
						}
						else if (itemDefinition!=null) {
							// TODO: remove Item Definition if it is on longer referenced anywhere
//							ModelUtil.getDefinitions(task).getRootElements().remove(itemDefinition);
							dataInput.setItemSubjectRef(null);
						}
					}
				}
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.features.IFeature#canUndo(org.eclipse.graphiti.features.context.IContext)
		 */
		@Override
		public boolean canUndo(IContext context) {
			return true;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.features.IFeature#hasDoneChanges()
		 */
		@Override
		public boolean hasDoneChanges() {
			return hasChanges;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.IName#getName()
		 */
		@Override
		public String getName() {
			return "Configure Work Item";
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.IDescription#getDescription()
		 */
		@Override
		public String getDescription() {
			return "Configure the Parameters for this Custom Task";
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.features.IFeatureProviderHolder#getFeatureProvider()
		 */
		@Override
		public IFeatureProvider getFeatureProvider() {
			return fp;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.features.custom.ICustomFeature#getImageId()
		 */
		@Override
		public String getImageId() {
			return ImageProvider.IMG_16_CONFIGURE;
		}
	}
}
