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
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core.features;

import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.graphiti.features.IFeatureProvider;

public class DirectEditNamedConnectionFeature extends DirectEditNamedElementFeature {

	public DirectEditNamedConnectionFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	protected boolean isNamedBusinessObject(Object bo) {
		 return (bo instanceof SequenceFlow) || (bo instanceof MessageFlow) ;
	}
	
	@Override
	protected void setNameValue(Object businessObject, String value) {
		if (businessObject instanceof MessageFlow) {
			((MessageFlow) businessObject).setName(value);
		}else {
			super.setNameValue(businessObject, value);
		}
	}
	
	@Override
	protected String getNameValue(Object businessObject) {
		if (businessObject instanceof MessageFlow) {
			return ((MessageFlow) businessObject).getName();
		}
		return super.getNameValue(businessObject);
	}
	
}
