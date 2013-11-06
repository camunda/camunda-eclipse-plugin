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
package org.camunda.bpm.modeler.ui.features.data;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.features.AbstractCreateFlowElementFeature;
import org.camunda.bpm.modeler.core.features.DefaultBpmn2MoveShapeFeature;
import org.camunda.bpm.modeler.core.features.UpdateBaseElementNameFeature;
import org.camunda.bpm.modeler.core.features.data.AddDataFeature;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Display;

public class DataObjectReferenceFeatureContainer extends AbstractDataFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof DataObjectReference;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataObjectReferenceFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddDataFeature<DataObjectReference>(fp) {

			@Override
			public String getName(DataObjectReference t) {
				return t.getName();
			}
		};
	}
	
	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new MoveDataObjectReferenceFeature(fp);
	}	

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new UpdateBaseElementNameFeature(fp);
	}
	
	public static class MoveDataObjectReferenceFeature extends DefaultBpmn2MoveShapeFeature {

		public MoveDataObjectReferenceFeature(IFeatureProvider fp) {
			super(fp);
		}
		
		@Override
		public boolean canMoveShape(IMoveShapeContext context) {
			EObject elem = (EObject) getBusinessObjectForPictogramElement(context.getShape());
			
			if (!(elem instanceof DataObjectReference)) {
				return false;
			}
			
			if (context.getTargetConnection() != null) {
				return false;
			}
			
			EObject be = (EObject) getBusinessObjectForPictogramElement(context.getTargetContainer());
			
			DataObjectReference reference = (DataObjectReference) elem;
			
			DataObject dataObject = reference.getDataObjectRef();
			
			List<DataObject> reachableDataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);
			
			return reachableDataObjects.contains(dataObject);
		}
		
		@Override
		protected void postMoveShape(IMoveShapeContext context) {
			DataObjectReference element = (DataObjectReference) getBusinessObject(context.getShape());
			
			ModelHandler handler = ModelHandler.getInstance(element);
			handler.moveDataObjectReference(element, getBusinessObject(context.getSourceContainer()), getBusinessObject(context.getTargetContainer()));
			
			super.postMoveShape(context);
		}
	}

	public static class CreateDataObjectReferenceFeature extends AbstractCreateFlowElementFeature<FlowElement> {

		public static class Option {
			
			private String name;
			private DataObject element;

			public Option(String name, DataObject element) {
				this.name = name;
				this.element = element;
			}
			
			public Option(String name) {
				this(name, null);
			}
			
			@Override
			public String toString() {
				return name + (element != null ? " " + element.getName() : "");
			}
			
			public DataObject getElement() {
				return element;
			}
		}

		public static String CREATE_MODE = CreateDataObjectReferenceFeature.class.getName() + "_CREATE_MODE";
		
		public static Option CREATE_NEW_REFERENCE_WITH_ATTACHED_DATA_OBJECT = new Option("Create Data Object Reference and referenced Data Object");
		
		
		public CreateDataObjectReferenceFeature(IFeatureProvider fp) {
			super(fp, "Data Object",
					"Provides information about what activities require to be performed or what they produce");
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_DATA_OBJECT;
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getDataObjectReference();
		}

		@Override
		public FlowElement createBusinessObject(ICreateContext context) {
			
			EObject container = (EObject) getBusinessObjectForPictogramElement(context.getTargetContainer());
			
			ModelHandler modelHandler = ModelHandler.getInstance(getDiagram());
			
			// get selection from context (during tests)
			Option selection = (Option) ContextUtil.get(context, CREATE_MODE);
			
			if (selection == null) {
				List<Option> options = new ArrayList<Option>();
				options.add(CREATE_NEW_REFERENCE_WITH_ATTACHED_DATA_OBJECT);
				
				List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(container, DataObject.class);
				
				for (DataObject dataObject : dataObjects) {
					options.add(new Option("Reference existing Data Object", dataObject));
				}
				
				if (options.size() > 1) {
					PopupMenu popupMenu = new PopupMenu(options, new LabelProvider());
					boolean showPopup = popupMenu.show(Display.getCurrent().getActiveShell());
					if (showPopup) {
						selection = (Option) popupMenu.getResult();
					}					
				}
			}
			
			DataObjectReference newDataObjectReference = Bpmn2Factory.eINSTANCE.createDataObjectReference();
			DataObject newDataObject = null;
			
			DataObject referencedDataObject = null;
			
			if (selection == null || selection == CREATE_NEW_REFERENCE_WITH_ATTACHED_DATA_OBJECT) {
				newDataObject = Bpmn2Factory.eINSTANCE.createDataObject();
				newDataObjectReference.setDataObjectRef(newDataObject);
				referencedDataObject = newDataObject;
			} else {
				referencedDataObject = selection.getElement();
				newDataObjectReference.setDataObjectRef(referencedDataObject);
			}

			// add new data object
			if (newDataObject != null) {
				modelHandler.addFlowElement(container, newDataObject);
				ModelUtil.setID(newDataObject);
				newDataObject.setIsCollection(false);
				newDataObject.setName(ModelUtil.toDisplayName(newDataObject.getId()));
			}

			// add new data object reference
			modelHandler.addFlowElement(container, newDataObjectReference);
			ModelUtil.setID(newDataObjectReference);
			if (referencedDataObject != null) {
				newDataObjectReference.setName(referencedDataObject.getName() + " Reference");
			} else {
				newDataObjectReference.setName(ModelUtil.toDisplayName(newDataObjectReference.getId()));
			}
			
			putBusinessObject(context, newDataObjectReference);
			
			return newDataObjectReference;
		}
	}
}