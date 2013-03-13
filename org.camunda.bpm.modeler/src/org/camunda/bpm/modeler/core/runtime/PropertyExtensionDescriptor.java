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

package org.camunda.bpm.modeler.core.runtime;

import java.lang.reflect.Constructor;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Bob Brodt
 *
 */
public class PropertyExtensionDescriptor extends BaseRuntimeDescriptor {
	
	protected String type;
	protected String adapterClassName;

	/**
	 * @param rt
	 */
	public PropertyExtensionDescriptor(TargetRuntime rt) {
		super(rt);
	}

	public Class getInstanceClass() {
		try {
			ClassLoader cl = this.getRuntime().getRuntimeExtension().getClass().getClassLoader();
			Constructor ctor = null;
			return Class.forName(type, true, cl);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ExtendedPropertiesAdapter getAdapter(AdapterFactory adapterFactory, EObject object) {
		try {
			ClassLoader cl = this.getRuntime().getRuntimeExtension().getClass().getClassLoader();
			Constructor ctor = null;
			Class adapterClass = Class.forName(adapterClassName, true, cl);
			EClass eclass = null;
			if (object instanceof EClass) {
				eclass = (EClass)object;
				object = ModelUtil.getDummyObject(eclass);
			}
			else {
				eclass = object.eClass();
			}
			ctor = adapterClass.getConstructor(AdapterFactory.class, eclass.getInstanceClass());
			return (ExtendedPropertiesAdapter)ctor.newInstance(adapterFactory, object);
		} catch (Exception e) {
			Activator.logError(e);
		}
		return null;
	}
}
