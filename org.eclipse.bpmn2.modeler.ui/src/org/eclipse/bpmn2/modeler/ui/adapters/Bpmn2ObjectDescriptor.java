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

package org.eclipse.bpmn2.modeler.ui.adapters;

import java.util.Collection;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * @author Bob Brodt
 *
 */
public class Bpmn2ObjectDescriptor {

	protected EObject object;
	protected String label;
	protected String text;
	
	public Bpmn2ObjectDescriptor(EObject object) {
		this.object = object;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel(Object context) {
		if (label==null) {
			label = ModelUtil.toDisplayName(object.eClass().getName());
		}
		return label;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText(Object context) {
		if (text==null) {
			// derive text from feature's value: default behavior is
			// to use the "name" attribute if there is one;
			// if not, use the "id" attribute;
			// fallback is to use the feature's toString()
			EStructuralFeature f = null;
			if (text==null) {
				f = object.eClass().getEStructuralFeature("name");
				if (f!=null) {
					String name = (String)object.eGet(f);
					if (name!=null && !name.isEmpty())
						text = name;
				}
			}
			if (text==null) {
				f = object.eClass().getEStructuralFeature("id");
				if (f!=null) {
					Object id = object.eGet(f);
					if (id!=null && !id.toString().isEmpty())
						text = id.toString();
				}
			}
		}
		return text;
	}

	protected IItemPropertyDescriptor getPropertyDescriptor(EStructuralFeature feature) {
		ItemProviderAdapter adapter =
				(ItemProviderAdapter) AdapterUtil.adapt((Notifier)object, ItemProviderAdapter.class);
		if (adapter!=null)
			return adapter.getPropertyDescriptor(object, feature);
		return null;
	}
}
