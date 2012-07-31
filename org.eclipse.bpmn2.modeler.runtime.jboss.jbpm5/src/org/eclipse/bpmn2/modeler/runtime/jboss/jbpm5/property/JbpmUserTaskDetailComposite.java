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

import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.PotentialOwner;
import org.eclipse.bpmn2.ResourceAssignmentExpression;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmUserTaskDetailComposite extends JbpmTaskDetailComposite {

	public JbpmUserTaskDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmUserTaskDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected AbstractListComposite bindList(EObject object, EStructuralFeature feature, EClass listItemClass) {
		if (feature.getName().equals("resources")) {
			if (modelEnablement.isEnabled(object.eClass(), feature)) {
				UserTask task = (UserTask)object;
				PotentialOwner owner = null;
				ResourceAssignmentExpression resourceAssignment = null;
				FormalExpression expression = null;
				if (task.getResources().size() == 0) {
					owner = getDiagramEditor().getModelHandler().createStandby(
							task, PACKAGE.getActivity_Resources(), PotentialOwner.class);
					resourceAssignment = getDiagramEditor().getModelHandler().createStandby(
							owner, PACKAGE.getResourceRole_ResourceAssignmentExpression(),
							ResourceAssignmentExpression.class);
					expression = getDiagramEditor().getModelHandler().createStandby(
							 resourceAssignment, PACKAGE.getResourceAssignmentExpression_Expression(),
							 FormalExpression.class);
				}
				else if (task.getResources().get(0) instanceof PotentialOwner){
					owner = (PotentialOwner)task.getResources().get(0);
					resourceAssignment = owner.getResourceAssignmentExpression();
					expression = (FormalExpression)resourceAssignment.getExpression();
				}
				TextObjectEditor editor = new TextObjectEditor(this, expression, PACKAGE.getFormalExpression_Body());
				editor.createControl(getAttributesParent(), "Actor", SWT.NONE);
			}
			return null;
		}
		else
			return super.bindList(object, feature, listItemClass);
	}
	
	
}
