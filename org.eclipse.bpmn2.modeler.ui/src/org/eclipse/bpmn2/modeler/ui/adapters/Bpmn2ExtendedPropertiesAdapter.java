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
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class Bpmn2ExtendedPropertiesAdapter extends AdapterImpl {

	// common property keys
	public final static String LONG_DESCRIPTION = "long.description";
	public final static String UI_CAN_EDIT = "ui.can.edit";
	public final static String UI_CAN_CREATE_NEW = "ui.can.create.new";
	public final static String UI_CAN_SET_NULL = "ui.can.set.null";
	
	
	private Hashtable<Integer, Hashtable<String,Object>> featureProperties = new Hashtable<Integer, Hashtable<String,Object>>();
	protected Hashtable <String,Object> objectProperties = new Hashtable <String,Object>();
	
	protected AdapterFactory adapterFactory;
	
	public Bpmn2ExtendedPropertiesAdapter(AdapterFactory adapterFactory, Notifier target) {
		super();
		this.adapterFactory = adapterFactory;
		setTarget(target);
	}

	public Object getProperty(String key) {
		return objectProperties.get(key);
	}

	public void setProperty(String key, Object value) {
		objectProperties.put(key, value);
	}

	public Object getProperty(EStructuralFeature feature, String key) {
		return getProperty(feature.getFeatureID(), key);
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
