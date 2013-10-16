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
import java.util.Iterator;
import java.util.List;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.features.AbstractCreateFlowElementFeature;
import org.camunda.bpm.modeler.core.features.MultiUpdateFeature;
import org.camunda.bpm.modeler.core.features.UpdateBaseElementNameFeature;
import org.camunda.bpm.modeler.core.features.data.AddDataFeature;
import org.camunda.bpm.modeler.core.features.data.Properties;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Display;

public class DataObjectFeatureContainer extends AbstractDataFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof DataObject;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataObjectFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddDataObjectFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(new UpdateMarkersFeature(fp));
		multiUpdate.addUpdateFeature(new UpdateBaseElementNameFeature(fp));
		return multiUpdate;
	}

	private static class AddDataObjectFeature extends AddDataFeature<DataObject> {
		private AddDataObjectFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public String getName(DataObject t) {
			return t.getName();
		}
	}

	private class UpdateMarkersFeature extends AbstractUpdateFeature {

		public UpdateMarkersFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canUpdate(IUpdateContext context) {
			Object o = getBusinessObjectForPictogramElement(context.getPictogramElement());
			return o != null && o instanceof BaseElement && canApplyTo(o);
		}

		@Override
		public IReason updateNeeded(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			ContainerShape container = (ContainerShape) context.getPictogramElement();
			DataObject data = (DataObject) getBusinessObjectForPictogramElement(container);
			boolean isCollection = Boolean.parseBoolean(peService.getPropertyValue(container,
					Properties.COLLECTION_PROPERTY));
			return data.isIsCollection() != isCollection ? Reason.createTrueReason() : Reason.createFalseReason();
		}

		@Override
		public boolean update(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			ContainerShape container = (ContainerShape) context.getPictogramElement();
			DataObject data = (DataObject) getBusinessObjectForPictogramElement(container);

			boolean drawCollectionMarker = data.isIsCollection();

			Iterator<Shape> iterator = peService.getAllContainedShapes(container).iterator();
			while (iterator.hasNext()) {
				Shape shape = iterator.next();
				String prop = peService.getPropertyValue(shape, Properties.HIDEABLE_PROPERTY);
				if (prop != null && new Boolean(prop)) {
					Polyline line = (Polyline) shape.getGraphicsAlgorithm();
					line.setLineVisible(drawCollectionMarker);
				}
			}

			peService.setPropertyValue(container, Properties.COLLECTION_PROPERTY,
					Boolean.toString(data.isIsCollection()));
			return true;
		}
	}

	public static class CreateDataObjectFeature extends AbstractCreateFlowElementFeature<FlowElement> {

		@Override
		public boolean canCreate(ICreateContext context) {
			// data objects may be added on diagram, too
			return 
				super.canCreate(context) || 
				getDiagram().equals(context.getTargetContainer());
		}
		
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

		public static String CREATE_MODE = CreateDataObjectFeature.class.getName() + "_CREATE_MODE";
		
		public static Option CREATE_NEW_REFERENCE = new Option("Create Data Object Reference");
		public static Option CREATE_NEW_REFERENCE_WITH_ATTACHED_DATA_OBJECT = new Option("Create Data Object Reference and referenced Data Object");
		
		
		public CreateDataObjectFeature(IFeatureProvider fp) {
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
			return Bpmn2Package.eINSTANCE.getDataObject();
		}

		@Override
		public FlowElement createBusinessObject(ICreateContext context) {
			
			ModelHandler modelHandler = ModelHandler.getInstance(getDiagram());
			
			// get selection from context (during tests)
			Option selection = (Option) ContextUtil.get(context, CREATE_MODE);
			
			if (selection == null) {
				List<Option> options = new ArrayList<Option>();
				options.add(CREATE_NEW_REFERENCE);
				options.add(CREATE_NEW_REFERENCE_WITH_ATTACHED_DATA_OBJECT);
				
				TreeIterator<EObject> contentIterator = modelHandler.getDefinitions().eAllContents();
				while (contentIterator.hasNext()) {
					EObject obj = contentIterator.next();
					if (obj instanceof DataObject) {
						options.add(new Option("Reference existing Data Object", (DataObject) obj));
					}
				}
				
				PopupMenu popupMenu = new PopupMenu(options, new LabelProvider());
				boolean showPopup = popupMenu.show(Display.getCurrent().getActiveShell());
				if (showPopup) {
					selection = (Option) popupMenu.getResult();
				}
			}
			
			DataObjectReference newDataObjectReference = Bpmn2Factory.eINSTANCE.createDataObjectReference();
			DataObject newDataObject = null;
			
			DataObject referencedDataObject = null;
			
			if (selection == null || selection == CREATE_NEW_REFERENCE) {
				
			} else
			if (selection == CREATE_NEW_REFERENCE_WITH_ATTACHED_DATA_OBJECT) {
				newDataObject = Bpmn2Factory.eINSTANCE.createDataObject();
				newDataObjectReference.setDataObjectRef(newDataObject);
				referencedDataObject = newDataObject;
			} else {
				referencedDataObject = selection.getElement();
				newDataObjectReference.setDataObjectRef(referencedDataObject);
			}

			Object container = getBusinessObjectForPictogramElement(context.getTargetContainer());
			
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