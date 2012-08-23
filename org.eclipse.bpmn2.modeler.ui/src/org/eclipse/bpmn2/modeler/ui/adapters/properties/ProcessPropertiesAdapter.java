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
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Bob Brodt
 *
 */
public class ProcessPropertiesAdapter extends RootElementPropertiesAdapter<Process> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ProcessPropertiesAdapter(AdapterFactory adapterFactory, Process object) {
		super(adapterFactory, object);
		
		setObjectDescriptor(new ObjectDescriptor<Process>(adapterFactory, object) {
			@Override
			public Process createObject(Resource resource, Object context) {
				ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(
						Bpmn2Package.eINSTANCE.getRootElement(),
						ExtendedPropertiesAdapter.class);
				Process process = (Process) adapter.getObjectDescriptor().createObject(resource, object);
				return process;
			}
		});
	}

}
