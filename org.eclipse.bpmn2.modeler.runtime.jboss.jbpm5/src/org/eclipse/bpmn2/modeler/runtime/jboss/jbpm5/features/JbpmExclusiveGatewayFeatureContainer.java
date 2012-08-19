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

import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.modeler.ui.features.gateway.ExclusiveGatewayFeatureContainer;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class JbpmExclusiveGatewayFeatureContainer extends ExclusiveGatewayFeatureContainer {

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new JbpmCreateExclusiveGatewayFeature(fp);
	}

	public class JbpmCreateExclusiveGatewayFeature extends CreateExclusiveGatewayFeature {

		public JbpmCreateExclusiveGatewayFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public ExclusiveGateway createBusinessObject(ICreateContext context) {
			ExclusiveGateway gateway = super.createBusinessObject(context);
			gateway.setName("");
			return gateway;
		}
	}
}