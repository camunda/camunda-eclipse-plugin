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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.property;

import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.modeler.core.runtime.ModelEnablementDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.PropertiesCompositeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Bob Brodt
 *
 */
public class JbpmScriptTaskPropertySection extends JbpmTaskPropertySection {
	static {
		PropertiesCompositeFactory.register(ScriptTask.class, JbpmScriptTaskPropertiesComposite.class);
	}

	@Override
	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		EObject bo = super.getBusinessObjectForPictogramElement(pe);
		if (bo instanceof ScriptTask)
			return bo;
		return null;
	}

	@Override
	protected AbstractBpmn2PropertiesComposite createSectionRoot() {
		return new JbpmScriptTaskPropertiesComposite(this);
	}
}
