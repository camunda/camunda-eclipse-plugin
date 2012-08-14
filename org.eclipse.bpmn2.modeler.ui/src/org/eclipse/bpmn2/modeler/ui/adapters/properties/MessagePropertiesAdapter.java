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
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.bpmn2.modeler.ui.features.choreography.ChoreographyUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Gary Brown
 *
 */
public class MessagePropertiesAdapter extends RootElementPropertiesAdapter<Message> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public MessagePropertiesAdapter(AdapterFactory adapterFactory, Message object) {
		super(adapterFactory, object);
		
    	setObjectDescriptor(new RootElementObjectDescriptor<Message>(adapterFactory, object) {
			@Override
			public String getDisplayName(Object context) {
				final Message mesg = adopt(context);
				String text = ChoreographyUtil.getMessageName(mesg);
				return text;
			}
    	});
	}

}
