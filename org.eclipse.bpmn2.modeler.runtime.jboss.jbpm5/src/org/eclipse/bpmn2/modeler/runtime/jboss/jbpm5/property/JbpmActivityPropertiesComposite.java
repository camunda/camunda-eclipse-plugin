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

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.OnEntryScriptType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.OnExitScriptType;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.editors.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ActivityPropertiesComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @author Bob Brodt
 *
 */
public class JbpmActivityPropertiesComposite extends ActivityPropertiesComposite {

	/**
	 * @param section
	 */
	public JbpmActivityPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmActivityPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}


	@Override
	public void createBindings(EObject be) {
		super.createBindings(be);
		Composite composite = this; //getAttributesParent();
		
		Activity activity = (Activity)be;
		
		// TODO: handle extension values in a generic way in AbstractBpmn2propertiesComposite
		for (ExtensionAttributeValue eav : activity.getExtensionValues()) {
			FeatureMap fm = eav.getValue();
			for (Entry entry : fm) {
				createEntryExitScriptSection(activity, entry);
			}
		}
	}
	
	private void createEntryExitScriptSection(Activity activity, Entry entry) {

		String title;
		EStructuralFeature feature = entry.getEStructuralFeature();
		if ("onEntryScript".equals(feature.getName()))
			title = "On Entry Script";
		else if ("onExitScript".equals(feature.getName()))
			title = "On Exit Script";
		else
			return;
		
		Section section = this.createSection(this, title);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		Composite sectionComposite = toolkit.createComposite(section);
		section.setClient(sectionComposite);
		sectionComposite.setLayout(new GridLayout(3,false));
		EObject est = (EObject)entry.getValue();

		ObjectEditor editor;
		editor = new ComboObjectEditor(this,est,est.eClass().getEStructuralFeature("scriptFormat")) {

			@Override
			protected Hashtable<String, Object> getChoiceOfValues(EObject object, EStructuralFeature feature) {
				Hashtable<String, Object> choiceOfValues = new Hashtable<String, Object>();
				choiceOfValues.put("Java", "http://www.java.com/java");
				choiceOfValues.put("MVEL", "http://www.mvel.org/2.0");
				return choiceOfValues;
			}
			
		};
		editor.createControl(sectionComposite,"Script Language",SWT.NONE);

		editor = new TextObjectEditor(this,est,est.eClass().getEStructuralFeature("script"));
		editor.createControl(sectionComposite,"Script",SWT.MULTI);
	}
}
