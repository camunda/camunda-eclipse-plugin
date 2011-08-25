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
package org.eclipse.bpmn2.modeler.core.features.activity.subprocess;

import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.modeler.core.features.activity.AbstractCreateExpandableActivityFeature;
import org.eclipse.graphiti.features.IFeatureProvider;

public abstract class AbstractCreateSubProcessFeature extends AbstractCreateExpandableActivityFeature<SubProcess> {

	public AbstractCreateSubProcessFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }
}