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

import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultDetailComposite.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.ui.property.editors.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmScriptTaskDetailComposite extends JbpmTaskDetailComposite {

	/**
	 * @param section
	 */
	public JbpmScriptTaskDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmScriptTaskDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void createBindings(EObject be) {
		ObjectEditor editor;
		editor = new ComboObjectEditor(this,be,be.eClass().getEStructuralFeature("scriptFormat")) {

			@Override
			protected Hashtable<String, Object> getChoiceOfValues(EObject object, EStructuralFeature feature) {
				Hashtable<String, Object> choiceOfValues = new Hashtable<String, Object>();
				choiceOfValues.put("Java", "http://www.java.com/java");
				choiceOfValues.put("MVEL", "http://www.mvel.org/2.0");
				return choiceOfValues;
			}
			
		};
		editor.createControl(getAttributesParent(),"Script Language",SWT.NONE);

		editor = new TextObjectEditor(this,be,be.eClass().getEStructuralFeature("script"));
		editor.createControl(getAttributesParent(),"Script",SWT.MULTI);
	}
}
