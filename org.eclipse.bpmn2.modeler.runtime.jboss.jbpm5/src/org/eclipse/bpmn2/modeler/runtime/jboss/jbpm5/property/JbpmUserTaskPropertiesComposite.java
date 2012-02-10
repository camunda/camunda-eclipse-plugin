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

import org.eclipse.bpmn2.PotentialOwner;
import org.eclipse.bpmn2.ResourceRole;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmUserTaskPropertiesComposite extends JbpmTaskPropertiesComposite {

	public JbpmUserTaskPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmUserTaskPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void createBindings(EObject be) {
		UserTask task = (UserTask)be;
		PotentialOwner owner = null;
		if (task.getResources().size() == 0) {
			owner = getDiagramEditor().getModelHandler().createStandby(
					task, PACKAGE.getActivity_Resources(), PotentialOwner.class);
		}
		else if (task.getResources().get(0) instanceof PotentialOwner){
			owner = (PotentialOwner)task.getResources().get(0);
		}
		TextObjectEditor editor = new TextObjectEditor(this, owner, PACKAGE.getResourceRole_Name());
		editor.createControl("Actor");
	}
}
