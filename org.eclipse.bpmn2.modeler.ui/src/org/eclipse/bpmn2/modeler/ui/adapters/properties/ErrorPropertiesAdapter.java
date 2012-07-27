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
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class ErrorPropertiesAdapter extends RootElementPropertiesAdapter<Error> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ErrorPropertiesAdapter(AdapterFactory adapterFactory, Error object) {
		super(adapterFactory, object);
		
    	setObjectDescriptor(new ObjectDescriptor<Error>(adapterFactory, object) {
			@Override
			public String getDisplayName(Object context) {
				final Error error = adopt(context);
				String text = "";
				if (error.getName()!=null) {
					text += error.getName();
				}
				else if (error.getErrorCode()!=null) {
					text += "Error Code: " + error.getErrorCode();
				}
				if (text.isEmpty())
					text = "ID: " + error.getId();
				return text;
			}
    	});
	}

}
