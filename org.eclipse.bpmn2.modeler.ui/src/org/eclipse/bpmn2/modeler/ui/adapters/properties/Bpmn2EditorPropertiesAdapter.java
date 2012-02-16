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

import java.lang.reflect.Field;

import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.Messages;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;

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
public class Bpmn2EditorPropertiesAdapter extends ExtendedPropertiesAdapter {

	// common property keys
	public final static String LONG_DESCRIPTION = "long.description";
	public final static String UI_CAN_EDIT = "ui.can.edit";
	public final static String UI_CAN_CREATE_NEW = "ui.can.create.new";
	public final static String UI_CAN_SET_NULL = "ui.can.set.null";
	public final static String UI_IS_MULTI_CHOICE = "ui.is.multi.choice";
	
	public Bpmn2EditorPropertiesAdapter(AdapterFactory adapterFactory, EObject object) {
		super(adapterFactory,object);

    	try {
        	String fieldName = "UI_" + object.eClass().getName().replaceAll("Impl$", "") + "_long_description";
			Field field = Messages.class.getField(fieldName);
			String text = (String)field.get(null);
			setProperty(Bpmn2EditorPropertiesAdapter.LONG_DESCRIPTION, text);
		} catch (Exception e) {
		}
	}
}
