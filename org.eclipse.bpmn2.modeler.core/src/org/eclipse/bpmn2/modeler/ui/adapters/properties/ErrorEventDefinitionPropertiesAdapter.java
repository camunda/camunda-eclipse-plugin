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
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * @author Bob Brodt
 *
 */
public class ErrorEventDefinitionPropertiesAdapter extends EventDefinitionPropertiesAdapter<ErrorEventDefinition> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ErrorEventDefinitionPropertiesAdapter(AdapterFactory adapterFactory, ErrorEventDefinition object) {
		super(adapterFactory, object);
		
    	setProperty(Bpmn2Package.eINSTANCE.getErrorEventDefinition_ErrorRef(), UI_CAN_CREATE_NEW, Boolean.TRUE);
    	setProperty(Bpmn2Package.eINSTANCE.getErrorEventDefinition_ErrorRef(), UI_CAN_EDIT, Boolean.TRUE);
	}

}
