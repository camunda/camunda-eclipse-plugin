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
import org.camunda.bpm.modeler.core.adapters.ObjectDescriptor;
import org.camunda.bpm.modeler.ui.features.choreography.ChoreographyUtil;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.emf.common.notify.AdapterFactory;

/**
 * @author Gary Brown
 *
 */
public class MessageFlowPropertiesAdapter extends ExtendedPropertiesAdapter<MessageFlow> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public MessageFlowPropertiesAdapter(AdapterFactory adapterFactory, MessageFlow object) {
		super(adapterFactory, object);
		
    	setObjectDescriptor(new ObjectDescriptor<MessageFlow>(adapterFactory, object) {
			@Override
			public String getDisplayName(Object context) {
				final MessageFlow flow = adopt(context);
				String text = "";
				if (flow.getMessageRef()!=null) {
					text += ChoreographyUtil.getMessageFlowName(flow);
				}
				
				if (flow.getSourceRef() instanceof Participant) {
					text += " "+((Participant)flow.getSourceRef()).getName()+"->";
					
					if (flow.getTargetRef() instanceof Participant) {
						text += ((Participant)flow.getTargetRef()).getName();
					}
				}
				return text;
			}
    	});
	}

}
