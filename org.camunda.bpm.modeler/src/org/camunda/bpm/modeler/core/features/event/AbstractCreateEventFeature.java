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
package org.camunda.bpm.modeler.core.features.event;

import org.camunda.bpm.modeler.core.features.AbstractCreateFlowElementFeature;
import org.eclipse.bpmn2.Event;
import org.eclipse.graphiti.features.IFeatureProvider;

public abstract class AbstractCreateEventFeature<T extends Event> extends AbstractCreateFlowElementFeature<T> {
	
	public AbstractCreateEventFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }
}