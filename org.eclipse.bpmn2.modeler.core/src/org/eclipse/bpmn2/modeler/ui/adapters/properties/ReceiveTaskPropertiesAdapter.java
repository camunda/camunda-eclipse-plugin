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
import org.eclipse.bpmn2.ReceiveTask;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class ReceiveTaskPropertiesAdapter extends TaskPropertiesAdapter<ReceiveTask> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ReceiveTaskPropertiesAdapter(AdapterFactory adapterFactory, ReceiveTask object) {
		super(adapterFactory, object);

    	EStructuralFeature ref = Bpmn2Package.eINSTANCE.getReceiveTask_MessageRef();
    	setFeatureDescriptor(ref, new RootElementRefFeatureDescriptor<ReceiveTask>(adapterFactory,object,ref));

    	ref = Bpmn2Package.eINSTANCE.getReceiveTask_OperationRef();
    	setFeatureDescriptor(ref, new RootElementRefFeatureDescriptor<ReceiveTask>(adapterFactory,object,ref));
	}

}
