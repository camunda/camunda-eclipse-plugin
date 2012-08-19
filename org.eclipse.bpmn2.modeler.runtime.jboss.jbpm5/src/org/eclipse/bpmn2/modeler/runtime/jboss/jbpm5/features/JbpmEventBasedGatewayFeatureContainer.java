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
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.features;

import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.modeler.ui.features.gateway.EventBasedGatewayFeatureContainer;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class JbpmEventBasedGatewayFeatureContainer extends EventBasedGatewayFeatureContainer {

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new JbpmCreateEventBasedGatewayFeature(fp);
	}

	public class JbpmCreateEventBasedGatewayFeature extends CreateEventBasedGatewayFeature {

		public JbpmCreateEventBasedGatewayFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public EventBasedGateway createBusinessObject(ICreateContext context) {
			EventBasedGateway gateway = super.createBusinessObject(context);
			gateway.setName("");
			return gateway;
		}
	}
}