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

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelFactory;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.ExtensionValueListComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.ActivityDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @author Bob Brodt
 *
 */
public class JbpmActivityDetailComposite extends ActivityDetailComposite {

	ScriptTableComposite onEntryScriptTable;
	ScriptTableComposite onExitScriptTable;
	
	/**
	 * @param section
	 */
	public JbpmActivityDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmActivityDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void cleanBindings() {
		super.cleanBindings();
		onEntryScriptTable = null;
		onExitScriptTable = null;
	}

	@Override
	public void createBindings(EObject be) {
		super.createBindings(be);
		
		onEntryScriptTable = new ScriptTableComposite(this);
		onEntryScriptTable.bindList(be, ModelPackage.eINSTANCE.getDocumentRoot_OnEntryScript());
		onEntryScriptTable.setTitle("On Entry Scripts");
		
		onExitScriptTable = new ScriptTableComposite(this);
		onExitScriptTable.bindList(be, ModelPackage.eINSTANCE.getDocumentRoot_OnExitScript());
		onExitScriptTable.setTitle("On Exit Scripts");
	}
	
	private class ScriptTableComposite extends ExtensionValueListComposite {

		/**
		 * @param parent
		 * @param style
		 */
		public ScriptTableComposite(Composite parent) {
			super(parent, AbstractListComposite.DEFAULT_STYLE);
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
		public AbstractDetailComposite createDetailComposite(final Composite parent, Class eClass) {
			return new JbpmScriptTaskDetailComposite(parent, SWT.NONE) {
				@Override
				public Composite getAttributesParent() {
					((Section)parent).setText("Script Details");
					return (Composite) ((Section)parent).getClient();
				}
			};
		}
	}
}
