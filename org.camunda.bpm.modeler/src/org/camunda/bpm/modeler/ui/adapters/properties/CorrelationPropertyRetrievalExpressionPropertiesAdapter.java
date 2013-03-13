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

import org.camunda.bpm.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CorrelationPropertyRetrievalExpression;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class CorrelationPropertyRetrievalExpressionPropertiesAdapter extends ExtendedPropertiesAdapter<CorrelationPropertyRetrievalExpression> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public CorrelationPropertyRetrievalExpressionPropertiesAdapter(AdapterFactory adapterFactory, CorrelationPropertyRetrievalExpression object) {
		super(adapterFactory, object);

    	EStructuralFeature ref = Bpmn2Package.eINSTANCE.getCorrelationPropertyRetrievalExpression_MessageRef();
    	setFeatureDescriptor(ref, new RootElementRefFeatureDescriptor<CorrelationPropertyRetrievalExpression>(adapterFactory,object,ref));
	}

}
