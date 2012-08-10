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

import java.util.Hashtable;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.connectors.SequenceFlowDetailComposite;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmSequenceFlowDetailComposite extends SequenceFlowDetailComposite {

	/**
	 * @param section
	 */
	public JbpmSequenceFlowDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmSequenceFlowDetailComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	public void createBindings(EObject be) {
		bindAttribute(this, be, "priority");
		super.createBindings(be);
	}
}
