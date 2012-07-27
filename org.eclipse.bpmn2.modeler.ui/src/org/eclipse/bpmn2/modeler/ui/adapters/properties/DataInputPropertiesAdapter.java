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

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class DataInputPropertiesAdapter extends ItemAwareElementPropertiesAdapter {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public DataInputPropertiesAdapter(AdapterFactory adapterFactory, EObject object) {
		super(adapterFactory, object);
    	EStructuralFeature f = Bpmn2Package.eINSTANCE.getDataInput_Name();
		final FeatureDescriptor fd = new FeatureDescriptor(adapterFactory,object, f) {

			@Override
			public void setDisplayName(String text) {
				int i = text.lastIndexOf("/");
				if (i>=0)
					text = text.substring(i+1);
				text = text.trim();
				((DataInput)object).setName(text);
			}

			@Override
			public String getChoiceString(Object context) {
				DataInput dataInput = (DataInput)(context instanceof DataInput ? context : this.object);
				String text = dataInput.getName();
				if (text==null || text.isEmpty())
					text = dataInput.getId();
				
				EObject container = dataInput.eContainer();
				while (container!=null) {
					if (container instanceof Participant) {
						container = ((Participant)container).getProcessRef();
						if (container==null)
							break;
					}
					if (container instanceof Activity || container instanceof Process) {
						text = PropertyUtil.getDisplayName(container) + "/" + text;
					}
					container = container.eContainer();
				}
				return text;
			}
			
		};
		setFeatureDescriptor(f, fd);
		
		setObjectDescriptor(new ObjectDescriptor(adapterFactory, object) {

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
