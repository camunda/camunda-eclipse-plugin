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

import org.camunda.bpm.modeler.core.adapters.FeatureDescriptor;
import org.camunda.bpm.modeler.core.adapters.ObjectDescriptor;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class PropertyPropertiesAdapter extends ItemAwareElementPropertiesAdapter<Property> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public PropertyPropertiesAdapter(AdapterFactory adapterFactory, Property object) {
		super(adapterFactory, object);

    	EStructuralFeature f = Bpmn2Package.eINSTANCE.getProperty_Name();
		final FeatureDescriptor<Property> fd = new FeatureDescriptor<Property>(adapterFactory,object, f) {

			@Override
			public void setDisplayName(String text) {
				int i = text.lastIndexOf("/");
				if (i>=0)
					text = text.substring(i+1);
				text = text.trim();
				((Property)object).setName(text);
			}

			@Override
			public String getChoiceString(Object context) {
				Property property = adopt(context);
				String text = property.getName();
				if (text==null || text.isEmpty())
					text = property.getId();
				
				EObject container = property.eContainer();
				while (container!=null && !(container instanceof Definitions)) {
					if (container instanceof Participant) {
						container = ((Participant)container).getProcessRef();
						if (container==null)
							break;
					}
					else if (container instanceof Activity || container instanceof Process) {
						text = ModelUtil.getDisplayName(container) + "/" + text;
					}
					else if (container instanceof CatchEvent || container instanceof ThrowEvent) {
						text = ModelUtil.getDisplayName(container) + "/" + text;
					}
					container = container.eContainer();
				}
				return text;
			}
			
		};
		setFeatureDescriptor(f, fd);
		
		setObjectDescriptor(new ObjectDescriptor<Property>(adapterFactory, object) {

			@Override
			public void setDisplayName(String text) {
				fd.setDisplayName(text);
				ModelUtil.setID(object);
			}

			@Override
			public String getDisplayName(Object context) {
				return fd.getChoiceString(context);
			}
			
		});
	}

}
