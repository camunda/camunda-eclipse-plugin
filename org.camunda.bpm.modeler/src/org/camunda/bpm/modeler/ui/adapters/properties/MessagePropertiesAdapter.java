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

import org.camunda.bpm.modeler.core.adapters.AdapterUtil;
import org.camunda.bpm.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.camunda.bpm.modeler.core.adapters.FeatureDescriptor;
import org.camunda.bpm.modeler.ui.features.choreography.ChoreographyUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.Message;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Gary Brown
 *
 */
public class MessagePropertiesAdapter extends RootElementPropertiesAdapter<Message> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public MessagePropertiesAdapter(AdapterFactory adapterFactory, Message object) {
		super(adapterFactory, object);

		EStructuralFeature ref = Bpmn2Package.eINSTANCE.getMessage_ItemRef();
    	
    	setFeatureDescriptor(ref, new FeatureDescriptor<Message>(adapterFactory, object, ref) {

    		@Override
    		public String getDisplayName(Object context) {
    			EObject object = this.object;
    			ItemDefinition itemDefinition = null;
    			if (object instanceof Message) {
    				itemDefinition = (ItemDefinition) object.eGet(feature);
    			}
    			if (itemDefinition!=null) {
    				ExtendedPropertiesAdapter<ItemDefinition> adapter =
    						(ExtendedPropertiesAdapter<ItemDefinition>) AdapterUtil.adapt(itemDefinition, ExtendedPropertiesAdapter.class);
    				return adapter.getFeatureDescriptor(Bpmn2Package.eINSTANCE.getItemDefinition_StructureRef()).getDisplayName(itemDefinition);
    			}
    			return super.getDisplayName(context);
    		}
    		
    	});

    	setObjectDescriptor(new RootElementObjectDescriptor<Message>(adapterFactory, object) {
			@Override
			public String getDisplayName(Object context) {
				final Message mesg = adopt(context);
				String text = ChoreographyUtil.getMessageName(mesg);
				return text;
			}
    	});
	}

}
