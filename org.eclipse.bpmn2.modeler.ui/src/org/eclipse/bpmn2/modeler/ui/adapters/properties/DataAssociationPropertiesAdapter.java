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

package org.eclipse.bpmn2.modeler.ui.adapters.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.DataStore;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.runtime.ModelEnablementDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * @author Bob Brodt
 *
 */
public class DataAssociationPropertiesAdapter extends ExtendedPropertiesAdapter<DataAssociation> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public DataAssociationPropertiesAdapter(AdapterFactory adapterFactory, DataAssociation object) {
		super(adapterFactory, object);

    	EStructuralFeature ref;
    	
    	ref = Bpmn2Package.eINSTANCE.getDataAssociation_SourceRef();
    	setFeatureDescriptor(ref, new SourceTargetFeatureDescriptor(adapterFactory,object,ref));
		setProperty(ref, UI_CAN_EDIT_INLINE, Boolean.TRUE);
		setProperty(ref, UI_CAN_EDIT, Boolean.FALSE);
		setProperty(ref, UI_CAN_CREATE_NEW, Boolean.FALSE);

		ref = Bpmn2Package.eINSTANCE.getDataAssociation_TargetRef();
    	setFeatureDescriptor(ref, new SourceTargetFeatureDescriptor(adapterFactory,object,ref));
		setProperty(ref, UI_CAN_EDIT_INLINE, Boolean.TRUE);
		setProperty(ref, UI_CAN_EDIT, Boolean.FALSE);
		setProperty(ref, UI_CAN_CREATE_NEW, Boolean.FALSE);
	}

	public class SourceTargetFeatureDescriptor extends FeatureDescriptor<DataAssociation> {

		public SourceTargetFeatureDescriptor(AdapterFactory adapterFactory, DataAssociation object, EStructuralFeature feature) {
			super(adapterFactory, object, feature);
		}
		
		@Override
		public String getLabel(Object context) {
			return "Mapped To";
		}

		@Override
		public Hashtable<String, Object> getChoiceOfValues(Object context) {
			List<EObject> values = new ArrayList<EObject>();
			// search for all Properties and DataStores
			// Properties are contained in the nearest enclosing Process or Event;
			// DataStores are contained in the DocumentRoot
			DataAssociation association = adopt(context);
			values.addAll( ModelUtil.collectAncestorObjects(association, "properties", new Class[] {Activity.class}) );
			values.addAll( ModelUtil.collectAncestorObjects(association, "properties", new Class[] {Process.class}) );
			values.addAll( ModelUtil.collectAncestorObjects(association, "properties", new Class[] {Event.class}) );
			values.addAll( ModelUtil.collectAncestorObjects(association, "dataStore", new Class[] {DocumentRoot.class}) );
			values.addAll( ModelUtil.collectAncestorObjects(association, "flowElements", new Class[] {FlowElementsContainer.class}, new Class[] {ItemAwareElement.class}));
			
			Activity activity = (Activity)ModelUtil.findNearestAncestor(association.eContainer(), new Class[] {Activity.class});
			if (activity!=null) {
				InputOutputSpecification ioSpec = activity.getIoSpecification();
				if (ioSpec!=null) {
					values.addAll(ioSpec.getDataInputs());
					values.addAll(ioSpec.getDataOutputs());
				}
			}
			
			CallableElement callable = (CallableElement)ModelUtil.findNearestAncestor(association.eContainer(), new Class[] {CallableElement.class});
			if (callable!=null) {
				InputOutputSpecification ioSpec = callable.getIoSpecification();
				if (ioSpec!=null) {
					values.addAll(ioSpec.getDataInputs());
					values.addAll(ioSpec.getDataOutputs());
				}
			}
			
			super.setChoiceOfValues(values);
			return super.getChoiceOfValues(context);
		}
		
		@Override
		public EObject createFeature(Resource resource, Object context, EClass eClass) {
			DataAssociation association = adopt(context);
			// what kind of object should we create? Property or DataStore?
			if (eClass==null) {
				if (ModelUtil.findNearestAncestor(association, new Class[] {Process.class, Event.class}) != null)
					// nearest ancestor is a Process or Event, so new object will be a Property
					eClass = Bpmn2Package.eINSTANCE.getProperty();
				else if(ModelUtil.findNearestAncestor(association, new Class[] {DocumentRoot.class}) != null)
					eClass = Bpmn2Package.eINSTANCE.getDataStore();
			}			
			if (eClass!=null) {
				return ModelUtil.createObject(resource, eClass);
			}
			return null;
		}
		
		@Override
		public void setValue(Object context, Object value) {
			final DataAssociation association = adopt(context);

			EObject container = null;
			EStructuralFeature containerFeature = null;
			if (value instanceof Property) {
				if (((Property)value).eContainer()==null) {
					// this Property isn't owned by anything yet - figure out who the owner is
					container = ModelUtil.findNearestAncestor(association, new Class[] {Activity.class});
					if (container==null)
						container = ModelUtil.findNearestAncestor(association, new Class[] {Event.class});
					if (container==null)
						container = ModelUtil.findNearestAncestor(association, new Class[] {Process.class});
					containerFeature = container.eClass().getEStructuralFeature("properties");
				}
			}
			else if (value instanceof DataStore) {
				if (((DataStore)value).eContainer()==null) {
					// this DataStore isn't owned by anything yet - figure out who the owner is
					container = ModelUtil.findNearestAncestor(association, new Class[] {DocumentRoot.class});
					containerFeature = container.eClass().getEStructuralFeature("dataStore");
				}
			}
			else if (value instanceof String) {
				// first check if a property with this name already exists
				Hashtable<String, Object> choices = getChoiceOfValues(object);
				Property property = (Property)choices.get(value);
				if (property==null) {
					// need to create a new one!
					ModelEnablementDescriptor modelEnablement = BPMN2Editor.getActiveEditor().getTargetRuntime().getModelEnablements(object);
					// find nearest element that can contain a Property and create one
					container = association;
					for (;;) {
						container = ModelUtil.findNearestAncestor(container, new Class[] {Activity.class, Event.class, Process.class});
						if (container==null)
							return;
						containerFeature = container.eClass().getEStructuralFeature("properties");
						if (modelEnablement.isEnabled(container.eClass(), containerFeature))
							break;
					}
						
					containerFeature = container.eClass().getEStructuralFeature("properties");
					property = Bpmn2ModelerFactory.create(Property.class);
					ExtendedPropertiesAdapter<Property> adapter = AdapterUtil.adapt(property, ExtendedPropertiesAdapter.class);
					adapter.getObjectDescriptor().setDisplayName((String)value);
					InsertionAdapter.add(container, containerFeature, property);
				}
				value = property;
			}

			final EObject c = container;
			final EStructuralFeature cf = containerFeature;
			final ItemAwareElement v = (ItemAwareElement)value;
			
			TransactionalEditingDomain editingDomain = getEditingDomain(association);
			if (feature == Bpmn2Package.eINSTANCE.getDataAssociation_SourceRef()) {
				if (association.getSourceRef().size()==0) {
					if (editingDomain == null) {
						if (c!=null) {
							if (c.eGet(cf) instanceof List)
								((List)c.eGet(cf)).add(v);
							else
								c.eSet(cf, v);
						}
						association.getSourceRef().add(v);
					} else {
						editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
							@Override
							protected void doExecute() {
								if (c!=null) {
									if (c.eGet(cf) instanceof List)
										((List)c.eGet(cf)).add(v);
									else
										c.eSet(cf, v);
								}
								association.getSourceRef().add(v);
							}
						});
					}
				}
				else {
					if (editingDomain == null) {
						if (c!=null) {
							if (c.eGet(cf) instanceof List)
								((List)c.eGet(cf)).add(v);
							else
								c.eSet(cf, v);
						}
						association.getSourceRef().set(0,v);
					} else {
						editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
							@Override
							protected void doExecute() {
								if (c!=null) {
									if (c.eGet(cf) instanceof List)
										((List)c.eGet(cf)).add(v);
									else
										c.eSet(cf, v);
								}
								association.getSourceRef().set(0,v);
							}
						});
					}
				}
			}
			else {
				if (editingDomain == null) {
					if (c!=null) {
						if (c.eGet(cf) instanceof List)
							((List)c.eGet(cf)).add(v);
						else
							c.eSet(cf, v);
					}
					association.setTargetRef(v);
				} else {
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							if (c!=null) {
								if (c.eGet(cf) instanceof List)
									((List)c.eGet(cf)).add(v);
								else
									c.eSet(cf, v);
							}
							association.setTargetRef(v);
						}
					});
				}
			}
		}
	}
}
