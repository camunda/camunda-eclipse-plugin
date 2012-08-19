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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.features;

import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.ErrorEventDefinitionContainer;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

/**
 * @author Bob Brodt
 *
 */
public class JbpmErrorEventDefinitionFeatureContainer extends ErrorEventDefinitionContainer {

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateErrorEventDefinition(fp) {
			public boolean canCreate(ICreateContext context) {
				if (super.canCreate(context)) {
					Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
					if (bo instanceof ThrowEvent) {
						ThrowEvent te = (ThrowEvent)bo;
						return te.getEventDefinitions().size()==0;
					}
					if (bo instanceof CatchEvent) {
						CatchEvent ce = (CatchEvent)bo;
						return ce.getEventDefinitions().size()==0;
					}
				}
				return false;
			}
		};
	}

}
