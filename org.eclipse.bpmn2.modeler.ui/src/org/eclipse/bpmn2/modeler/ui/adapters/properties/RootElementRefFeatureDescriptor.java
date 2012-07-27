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

import java.util.Hashtable;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class RootElementRefFeatureDescriptor<T extends BaseElement> extends FeatureDescriptor<T> {

	/**
	 * @param adapterFactory
	 * @param object
	 * @param feature
	 */
	public RootElementRefFeatureDescriptor(AdapterFactory adapterFactory, T object, EStructuralFeature feature) {
		super(adapterFactory, object, feature);
	}
	
	@Override
	public EObject createObject(Object context, EClass eClass) {
		final T object = adopt(context);

		EObject rootElement = null;
		if (feature.getEType() != eClass) {
			ExtendedPropertiesAdapter<T> adapter = (ExtendedPropertiesAdapter<T>) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
			if (adapter!=null)
				rootElement = adapter.getFeatureDescriptor(feature).createObject(object, eClass);
		}
		if (rootElement==null)
			rootElement = Bpmn2ModelerFactory.getInstance().create(eClass);
		
		Definitions definitions = ModelUtil.getDefinitions(object);
		InsertionAdapter.add(definitions, Bpmn2Package.eINSTANCE.getDefinitions_RootElements(), rootElement);
		return rootElement;
	}
	
	@Override
	public Hashtable<String, Object> getChoiceOfValues(Object context) {
		final T object = adopt(context);
				
		Hashtable<String,Object> choices = new Hashtable<String,Object>();
		EObject rootElement = (EObject) object.eGet(feature);
		if (rootElement!=null)
			choices.put(PropertyUtil.getDisplayName(rootElement), rootElement);
		Definitions definitions = ModelUtil.getDefinitions(object);
		if (definitions!=null) {
			for (RootElement re : definitions.getRootElements()) {
				if (re.eClass() == feature.getEType()) {
					choices.put(PropertyUtil.getDisplayName(re), re);
				}
			}
		}
		return choices;
	}
}
