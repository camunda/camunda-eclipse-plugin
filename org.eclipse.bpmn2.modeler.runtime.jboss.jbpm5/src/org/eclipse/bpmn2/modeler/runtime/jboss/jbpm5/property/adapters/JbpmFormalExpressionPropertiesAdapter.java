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

import java.util.Hashtable;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.FormalExpressionPropertiesAdapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class JbpmFormalExpressionPropertiesAdapter extends FormalExpressionPropertiesAdapter {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public JbpmFormalExpressionPropertiesAdapter(AdapterFactory adapterFactory, EObject object) {
		super(adapterFactory, object);

    	final EStructuralFeature language = Bpmn2Package.eINSTANCE.getFormalExpression_Language();
    	FeatureDescriptor fd = new FeatureDescriptor(adapterFactory,object,language) {
			@Override
			public String getLabel(Object context) {
				return "Script Language";
			}
		};
		Hashtable<String, Object> choiceOfValues = new Hashtable<String, Object>();
		choiceOfValues.put("Java", "http://www.java.com/java");
		choiceOfValues.put("MVEL", "http://www.mvel.org/2.0");
		if (object.eContainer() instanceof SequenceFlow)
			choiceOfValues.put("Rule", "http://www.jboss.org/drools/rule");
    	fd.setChoiceOfValues(choiceOfValues);
    	setFeatureDescriptor(language,fd);
    	
    	final EStructuralFeature body = Bpmn2Package.eINSTANCE.getFormalExpression_Body();
    	setFeatureDescriptor(body,
			new FeatureDescriptor(adapterFactory,object,body) {
				@Override
				public String getLabel(Object context) {
					EObject object = this.object;
					if (context instanceof EObject)
						object = (EObject)context;
					if (object.eContainer() instanceof SequenceFlow)
						return "Constraint";
					return "Script";
				}

				@Override
				public boolean isMultiLine(Object context) {
					// formal expression body is always a multiline text field
					return true;
				}
			}
    	);
	}

}
