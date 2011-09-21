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
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.property;

import org.eclipse.bpmn2.modeler.core.preferences.ToolEnablementPreferences;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.swt.widgets.Composite;

public class MainPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MainPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void createBindings(EObject be) {
		
		ItemProviderAdapter itemProviderAdapter = (ItemProviderAdapter) new Bpmn2ItemProviderAdapterFactory().adapt(be,
				ItemProviderAdapter.class);

		for (EStructuralFeature a : be.eClass().getEAllStructuralFeatures()) {

			Object value = be.eGet(a); // the EList<cl>
			
			if (a instanceof EAttribute) {
				bindAttribute(be,(EAttribute)a,itemProviderAdapter);
			}
			else if (value instanceof EList) {
				bindList(be, a, itemProviderAdapter);
			}
		}

		bindReferences(be,itemProviderAdapter);
		
		itemProviderAdapter.dispose();
	}

	@Override
	protected boolean canBindAttribute(EObject object, EAttribute attribute) {
		ToolEnablementPreferences preferences = ToolEnablementPreferences.getPreferences(project);
		return preferences.isEnabled(object.eClass(), attribute);
	}
	
	@Override
	protected boolean canBindList(EObject object, EStructuralFeature feature) {
		// Don't render any list features by default - it's just too messy
		return false;
	}

	@Override
	protected int getListStyleFlags(EObject object, EStructuralFeature feature) {
		return SHOW_LIST_LABEL | EDITABLE_LIST | ORDERED_LIST;
	}

	@Override
	protected boolean canBindListColumn(EObject object, EAttribute attribute) {
		return false;
	}

	@Override
	protected boolean canModifyListColumn(EObject object, EAttribute attribute, Object element) {
		return true;
	}

	@Override
	protected boolean canBindReference(EObject object, EReference reference) {
		return true;
	}
}
