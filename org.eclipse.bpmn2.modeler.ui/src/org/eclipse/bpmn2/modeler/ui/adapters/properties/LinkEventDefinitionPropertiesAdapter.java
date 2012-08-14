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
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.LinkEventDefinition;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class LinkEventDefinitionPropertiesAdapter extends EventDefinitionPropertiesAdapter<LinkEventDefinition> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public LinkEventDefinitionPropertiesAdapter(AdapterFactory adapterFactory, LinkEventDefinition object) {
		super(adapterFactory, object);
		
    	setProperty(Bpmn2Package.eINSTANCE.getLinkEventDefinition_Source(), UI_CAN_CREATE_NEW, Boolean.FALSE);
    	setProperty(Bpmn2Package.eINSTANCE.getLinkEventDefinition_Source(), UI_CAN_EDIT, Boolean.TRUE);
    	setProperty(Bpmn2Package.eINSTANCE.getLinkEventDefinition_Target(), UI_CAN_CREATE_NEW, Boolean.FALSE);
    	setProperty(Bpmn2Package.eINSTANCE.getLinkEventDefinition_Target(), UI_CAN_EDIT, Boolean.FALSE);

    	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getLinkEventDefinition_Target();
    	setFeatureDescriptor(ref,
			new FeatureDescriptor<LinkEventDefinition>(adapterFactory,object,ref) {

			@Override
			public String getDisplayName(Object context) {
				final LinkEventDefinition led = adopt(context);
				String name = led.getName();
				if (name==null || name.isEmpty())
					name = led.getId();
				return name;
			}

			@Override
			public Hashtable<String, Object> getChoiceOfValues(Object context) {
				LinkEventDefinition object = adopt(context);
				// add all ItemDefinitions
				Hashtable<String,Object> choices = new Hashtable<String,Object>();
				String s;
				Definitions defs = ModelUtil.getDefinitions(object);
				List<LinkEventDefinition> links = (List)ModelUtil.getAllReachableObjects(defs, Bpmn2Package.eINSTANCE.getLinkEventDefinition());
				for (LinkEventDefinition link : links) {
					if (link!=object) {
						s = getDisplayName(link);
						choices.put(s,link);
					}
				}
				return choices;
			}

			@Override
			public String getChoiceString(Object value) {
				return super.getChoiceString(value);
			}
    		
		}
	);
	}

}
