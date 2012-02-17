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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.property.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.FormalExpressionPropertiesAdapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class FormalExpressionPropertyAdapter extends FormalExpressionPropertiesAdapter {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public FormalExpressionPropertyAdapter(AdapterFactory adapterFactory, EObject object) {
		super(adapterFactory, object);

    	final EStructuralFeature language = Bpmn2Package.eINSTANCE.getFormalExpression_Language();
    	setFeatureDescriptor(language,
			new FeatureDescriptor(adapterFactory,object,language) {
				@Override
				public String getLabel(Object context) {
					return "Language";
				}

				@Override
				public Collection getChoiceOfValues(Object context) {
					List<String> choices = new ArrayList<String>();
					choices.add("http://www.java.com/java");
					choices.add("http://www.mvel.org/2.0");
					return choices;
				}
			}
    	);
	}

}
