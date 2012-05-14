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

package org.eclipse.bpmn2.modeler.runtime.example;

import java.util.List;

import org.eclipse.bpmn2.modeler.core.runtime.ModelExtensionDescriptor.Property;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomTaskFeatureContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.bpmn2.Task;

/**
 * @author Bob Brodt
 *
 */
public class SampleCustomTaskFeatureContainer extends CustomTaskFeatureContainer {

	/**
	 * 
	 */
	public SampleCustomTaskFeatureContainer() {
	}
	
	public String getId(EObject object) {
		if (object==null)
			return null;
		List<EStructuralFeature> features = ModelUtil.getAnyAttributes(object);
		for (EStructuralFeature f : features) {
			if ("sampleCustomTaskId".equals(f.getName())) {
				Object attrValue = object.eGet(f);
				return (String)attrValue;
			}
		}
		return null;
	}
}
