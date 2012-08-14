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

import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.Escalation;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Bob Brodt
 *
 */
public class EscalationPropertiesAdapter extends RootElementPropertiesAdapter<Escalation> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public EscalationPropertiesAdapter(AdapterFactory adapterFactory, Escalation object) {
		super(adapterFactory, object);
		
    	setObjectDescriptor(new RootElementObjectDescriptor<Escalation>(adapterFactory, object) {
			@Override
			public String getDisplayName(Object context) {
				final Escalation escalation = adopt(context);
				String text = "";
				if (escalation.getName()!=null) {
					text += escalation.getName();
				}
				else if (escalation.getEscalationCode()!=null) {
					text += "Escalation Code: " + escalation.getEscalationCode();
				}
				if (text.isEmpty())
					text = "ID: " + escalation.getId();
				return text;
			}
    	});
	}

}
