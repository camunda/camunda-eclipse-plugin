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
import org.camunda.bpm.modeler.core.adapters.FeatureDescriptor;
import org.camunda.bpm.modeler.core.adapters.ObjectDescriptor;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 * TODO: This was intended for elements that are both FlowElements and ItemAwareElements like
 * DataObject, DataObjectReference and DataStoreReference but alas, multiple inheritance ain't
 * happening in java. need to figure this out...
 */
public class FlowElementPropertiesAdapter<T extends FlowElement> extends ExtendedPropertiesAdapter<T> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public FlowElementPropertiesAdapter(AdapterFactory adapterFactory, T object) {
		super(adapterFactory, object);
    	EStructuralFeature f = Bpmn2Package.eINSTANCE.getFlowElement_Name();
		final FeatureDescriptor<T> fd = new FeatureDescriptor<T>(adapterFactory,object, f) {

			@Override
			public void setDisplayName(String text) {
				int i = text.lastIndexOf("/");
				if (i>=0)
					text = text.substring(i+1);
				text = text.trim();
				((T)object).setName(text);
			}

			@Override
			public String getChoiceString(Object context) {
				T flowElement = adopt(context);
				String text = flowElement.getName();
				if (text==null || text.isEmpty())
					text = flowElement.getId();
				
				EObject container = flowElement.eContainer();
				while (container!=null) {
					if (container instanceof Participant) {
						container = ((Participant)container).getProcessRef();
						if (container==null)
							break;
					}
					if (container instanceof Activity || container instanceof Process) {
						text = ModelUtil.getDisplayName(container) + "/" + text;
					}
					container = container.eContainer();
				}
				return text;
			}
			
		};
		setFeatureDescriptor(f, fd);
		
		setObjectDescriptor(new ObjectDescriptor<T>(adapterFactory, object) {

			@Override
			public void setDisplayName(String text) {
				fd.setDisplayName(text);
				ModelUtil.setID(object);
			}

			@Override
			public String getDisplayName(Object context) {
				return fd.getDisplayName(context);
			}
		});
	}

}
