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

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ResourceParameterBinding;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * @author Bob Brodt
 *
 */
public class ResourceParameterBindingPropertiesAdapter extends ExtendedPropertiesAdapter<ResourceParameterBinding> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ResourceParameterBindingPropertiesAdapter(AdapterFactory adapterFactory, ResourceParameterBinding object) {
		super(adapterFactory, object);

		// ResourceParameters are contained in Resource, a root element
    	setProperty(Bpmn2Package.eINSTANCE.getResourceParameterBinding_ParameterRef(), UI_IS_MULTI_CHOICE, Boolean.TRUE);
    	setProperty(Bpmn2Package.eINSTANCE.getResourceParameterBinding_ParameterRef(), UI_CAN_CREATE_NEW, Boolean.FALSE);
    	setProperty(Bpmn2Package.eINSTANCE.getResourceParameterBinding_ParameterRef(), UI_CAN_EDIT, Boolean.FALSE);
    	setProperty(Bpmn2Package.eINSTANCE.getResourceParameterBinding_ParameterRef(), UI_CAN_SET_NULL, Boolean.TRUE);
	}

}
