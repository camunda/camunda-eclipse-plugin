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
package org.eclipse.bpmn2.modeler.core.features.activity.task;

import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.graphiti.features.IFeatureProvider;

public interface ICustomTaskFeature {

	public final static String CUSTOM_TASK_ID = "custom.task.id";

	public abstract void setId(String id);

	public abstract String getId();

	public abstract void setCustomTaskDescriptor(CustomTaskDescriptor customTaskDescriptor);

}