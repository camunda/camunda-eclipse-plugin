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
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.GlobalType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelFactory;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.OnEntryScriptType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.OnExitScriptType;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2TableComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.ExtensionValueTableComposite;
import org.eclipse.bpmn2.modeler.ui.property.editors.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ActivityPropertiesComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
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
		
		ScriptTableComposite onEntryScriptTable = new ScriptTableComposite(this);
		onEntryScriptTable.bindList(be, ModelPackage.eINSTANCE.getDocumentRoot_OnEntryScript());
		onEntryScriptTable.setTitle("On Entry Scripts");
		
		ScriptTableComposite onExitScriptTable = new ScriptTableComposite(this);
		onExitScriptTable.bindList(be, ModelPackage.eINSTANCE.getDocumentRoot_OnExitScript());
		onExitScriptTable.setTitle("On Exit Scripts");
	}
	
	private class ScriptTableComposite extends ExtensionValueTableComposite {

		/**
		 * @param parent
		 * @param style
		 */
		public ScriptTableComposite(Composite parent) {
			super(parent, AbstractBpmn2TableComposite.DEFAULT_STYLE);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.ui.property.ExtensionValueTableComposite#addListItem(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
		 */
		@Override
		protected EObject addListItem(EObject object, EStructuralFeature feature) {
			EObject newScript = ModelFactory.eINSTANCE.create(listItemClass);
			EStructuralFeature f = newScript.eClass().getEStructuralFeature("script");
			if (f!=null)
				newScript.eSet(feature, "");
			f = newScript.eClass().getEStructuralFeature("scriptFormat");
			if (f!=null)
				newScript.eSet(f,"http://www.java.com/java");
			addExtensionValue(newScript);
			return newScript;
		}

		@Override
		public AbstractBpmn2PropertiesComposite createDetailComposite(final Composite parent, Class eClass) {
			return new JbpmScriptTaskPropertiesComposite(parent, SWT.NONE) {
				@Override
				public Composite getAttributesParent() {
					((Section)parent).setText("Script Details");
					return (Composite) ((Section)parent).getClient();
				}
			};
		}
	}
}
