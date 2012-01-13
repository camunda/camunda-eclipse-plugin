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

package org.eclipse.bpmn2.modeler.ui.adapters;

import java.util.Hashtable;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class provides "extended" BPMN2 model object properties for the UI that the
 * generated EMF edit.provider does not. Also, we can't easily extend the BPMN2 metamodel
 * edit.provider classes so this adapter sits on top of those classes.
 * 
 * Whenever necessary (i.e. classes for which the editor does not need extended properties),
 * this class will delegate to the BPMN2 edit.provider classes ItemProviderAdapter and
 * ItemPropertyDescriptor.
 * 
 * @see Bpmn2EditorItemProviderAdapterFactory
 * 
 * TODO: expose this framework as an extension point for runtime plugins
 */
public class Bpmn2ExtendedPropertiesAdapter extends AdapterImpl {

	// common property keys
	public final static String LONG_DESCRIPTION = "long.description";
	public final static String UI_CAN_EDIT = "ui.can.edit";
	public final static String UI_CAN_CREATE_NEW = "ui.can.create.new";
	public final static String UI_CAN_SET_NULL = "ui.can.set.null";
	public final static String UI_IS_MULTI_CHOICE = "ui.is.multi.choice";
	public final static String PROPERTY_DESCRIPTOR = "property.descriptor";
	
	protected Hashtable<
		Integer, // feature ID
		Hashtable<String,Object>> // property key and value
			featureProperties = new Hashtable<Integer, Hashtable<String,Object>>();
	protected Hashtable <
		String, // property key
		Object> // value
			objectProperties = new Hashtable <String,Object>();
	
	protected AdapterFactory adapterFactory;
	
	public Bpmn2ExtendedPropertiesAdapter(AdapterFactory adapterFactory, EObject target) {
		super();
		this.adapterFactory = adapterFactory;
		setTarget(target);
	}
	
	public void setObjectDescriptor(Bpmn2ObjectDescriptor pd) {
		setProperty(PROPERTY_DESCRIPTOR,pd);
	}

	public Bpmn2ObjectDescriptor getObjectDescriptor() {
		Bpmn2ObjectDescriptor pd = (Bpmn2ObjectDescriptor) getProperty(PROPERTY_DESCRIPTOR);
		if (pd==null) {
			pd = new Bpmn2ObjectDescriptor(adapterFactory, (EObject)getTarget());
			setProperty(PROPERTY_DESCRIPTOR,pd);
		}
		return pd;
	}

	public Bpmn2FeatureDescriptor getFeatureDescriptor(EStructuralFeature feature) {
		Bpmn2FeatureDescriptor pd = (Bpmn2FeatureDescriptor) getProperty(feature.getFeatureID(),PROPERTY_DESCRIPTOR);
		if (pd==null) {
			pd = new Bpmn2FeatureDescriptor(adapterFactory, (EObject)getTarget(), feature);
			setProperty(feature.getFeatureID(),PROPERTY_DESCRIPTOR,pd);
		}
		return pd;
	}
	
	public void setFeatureDescriptor(EStructuralFeature feature, Bpmn2FeatureDescriptor pd) {
		setProperty(feature.getFeatureID(),PROPERTY_DESCRIPTOR,pd);
	}
	
	public Object getProperty(String key) {
		return objectProperties.get(key);
	}
	
	public boolean getBooleanProperty(String key) {
		Object result = getProperty(key);
		if (result instanceof Boolean)
			return ((Boolean)result);
		return false;
	}

	public void setProperty(String key, Object value) {
		objectProperties.put(key, value);
	}

	public Object getProperty(EStructuralFeature feature, String key) {
		return getProperty(feature.getFeatureID(), key);
	}
	
	public boolean getBooleanProperty(EStructuralFeature feature, String key) {
		Object result = getProperty(feature, key);
		if (result instanceof Boolean)
			return ((Boolean)result);
		return false;
	}

	public void setProperty(EStructuralFeature feature, String key, Object value) {
		setProperty(feature.getFeatureID(), key, value);
	}

	public Object getProperty(int id, String key) {
		Integer idObject = Integer.valueOf(id);
		Hashtable<String,Object> props = featureProperties.get(idObject);
		if (props==null) {
			props = new Hashtable<String,Object>();
			featureProperties.put(idObject,props);
		}
		return props.get(key);
	}

	public void setProperty(int id, String key, Object value) {
		Integer idObject = Integer.valueOf(id);
		Hashtable<String,Object> props = featureProperties.get(idObject);
		if (props==null) {
			props = new Hashtable<String,Object>();
			featureProperties.put(idObject,props);
		}
		props.put(key, value);
	}
}
