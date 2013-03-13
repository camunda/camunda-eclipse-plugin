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

import org.eclipse.bpmn2.Signal;
import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * @author Bob Brodt
 *
 */
public class SignalPropertiesAdapter extends RootElementPropertiesAdapter<Signal> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public SignalPropertiesAdapter(AdapterFactory adapterFactory, Signal object) {
		super(adapterFactory, object);
		
    	setObjectDescriptor(new RootElementObjectDescriptor<Signal>(adapterFactory, object) {
			@Override
			public String getDisplayName(Object context) {
				final Signal signal = adopt(context);
				String text = "";
				if (signal.getName()!=null) {
					text += signal.getName();
				}
				if (text.isEmpty())
					text = "ID: " + signal.getId();
				return text;
			}
    	});
	}

}
