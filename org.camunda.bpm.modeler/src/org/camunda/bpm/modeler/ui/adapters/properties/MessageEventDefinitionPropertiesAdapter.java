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

package org.camunda.bpm.modeler.ui.adapters.properties;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * @author Bob Brodt
 *
 */
public class MessageEventDefinitionPropertiesAdapter extends EventDefinitionPropertiesAdapter<MessageEventDefinition> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public MessageEventDefinitionPropertiesAdapter(AdapterFactory adapterFactory, MessageEventDefinition object) {
		super(adapterFactory, object);
		
    	setProperty(Bpmn2Package.eINSTANCE.getMessageEventDefinition_OperationRef(), UI_CAN_CREATE_NEW, Boolean.FALSE);
    	setProperty(Bpmn2Package.eINSTANCE.getMessageEventDefinition_OperationRef(), UI_CAN_EDIT, Boolean.FALSE);
	}

}
