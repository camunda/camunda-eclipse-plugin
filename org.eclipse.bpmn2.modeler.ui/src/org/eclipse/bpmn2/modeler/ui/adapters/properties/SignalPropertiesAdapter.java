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
import org.eclipse.bpmn2.Signal;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class SignalPropertiesAdapter extends RootElementPropertiesAdapter {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public SignalPropertiesAdapter(AdapterFactory adapterFactory, EObject object) {
		super(adapterFactory, object);
		
    	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getResourceAssignmentExpression_Expression();
    	setObjectDescriptor(new ObjectDescriptor(adapterFactory, object) {
			@Override
			public String getText(Object context) {
				final Signal error = context instanceof Signal ?
						(Signal)context :
						(Signal)this.object;
				String text = "";
				if (error.getName()!=null) {
					text += error.getName();
				}
				if (text.isEmpty())
					text = "ID: " + error.getId();
				return text;
			}
    	});
	}

}
