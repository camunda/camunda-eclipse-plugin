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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.OnEntryScriptType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.OnExitScriptType;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.editors.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.tasks.TaskPropertiesComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @author Bob Brodt
 *
 */
public class JbpmScriptTaskPropertiesComposite extends JbpmTaskPropertiesComposite {

	/**
	 * @param section
	 */
	public JbpmScriptTaskPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmScriptTaskPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void createBindings(EObject be) {
		ObjectEditor editor;
		
		editor = new ComboObjectEditor(this,be,be.eClass().getEStructuralFeature("scriptFormat")) {
			
			@Override
			protected Hashtable getChoiceOfValues(EObject object, EStructuralFeature feature) {
				Hashtable<String, Object> values = new Hashtable<String, Object>();
				values.put("Java", "http://www.java.com/java");
				values.put("MVEL", "http://www.mvel.org/2.0");
				return values;
			}
		};
		editor.createControl(getAttributesParent(),"Script Language");
		
		editor = new TextObjectEditor(this,be,be.eClass().getEStructuralFeature("script"));
		editor.createControl(getAttributesParent(),"Script",SWT.MULTI);
	}
}
