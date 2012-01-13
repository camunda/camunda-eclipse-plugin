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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * @author Bob Brodt
 *
 */
public class Bpmn2FeatureDescriptor extends Bpmn2ObjectDescriptor {

	protected EStructuralFeature feature;
	protected int multiline = 0; // -1 = false, +1 = true, 0 = unset
	protected Collection choiceOfValues; // for static lists
	
	public Bpmn2FeatureDescriptor(AdapterFactory adapterFactory, EObject object, EStructuralFeature feature) {
		super(adapterFactory, object);
		this.feature = feature;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel(Object context) {
		if (label==null) {
			IItemPropertyDescriptor propertyDescriptor = getPropertyDescriptor(feature);
			if (propertyDescriptor != null)
				label = propertyDescriptor.getDisplayName(object);
			else
				label = ModelUtil.toDisplayName(feature.getName());
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
			EObject o = object;
			EStructuralFeature f = null;
			if (feature!=null) {
				Object value = object.eGet(feature); 
				if (value instanceof EObject) {
					o = (EObject)object.eGet(feature);
				}
				else if (value!=null)
					text = value.toString();
			}
			if (text==null) {
				f = o.eClass().getEStructuralFeature("name");
				if (f!=null) {
					String name = (String)o.eGet(f);
					if (name!=null && !name.isEmpty())
						text = name;
				}
			}
//			if (text==null) {
//				f = o.eClass().getEStructuralFeature("id");
//				if (f!=null) {
//					Object id = o.eGet(f);
//					if (id!=null && !id.toString().isEmpty())
//						text = id.toString();
//				}
//			}
		}
		return text == null ? "" : text;
	}

	public void setChoiceOfValues(Collection choiceOfValues) {
		this.choiceOfValues = choiceOfValues;
	}
	
	public Collection getChoiceOfValues(Object context) {
		EObject object = context instanceof EObject ? (EObject)context : this.object;
		if (choiceOfValues==null) {
			try {
				IItemPropertyDescriptor propertyDescriptor = getPropertyDescriptor(feature);
				if (propertyDescriptor!=null) {
					return propertyDescriptor.getChoiceOfValues(object);
				}
			}
			catch (Exception e) {
				// ignore exceptions if we fail to resolve proxies;
				// e.g. and instance of a DynamicEObjectImpl with a bogus
				// URI is used for ItemDefinition.structureRef
				// fallback is to do our own search
			}
			return ModelUtil.getAllReachableObjects(object, feature);
		}
		return choiceOfValues;
	}

	public void setMultiLine(boolean multiline) {
		this.multiline = multiline ? 1 : -1;
	}
	
	public boolean isMultiLine(Object context) {
		if (multiline==0) {
			IItemPropertyDescriptor propertyDescriptor = getPropertyDescriptor(feature);
			if (propertyDescriptor!=null)
				multiline = propertyDescriptor.isMultiLine(object) ? 1 : -1;
		}
		return multiline == 1;
	}
	
	public void setValue(EObject context, Object value) {
		EObject object = this.object;
		if (context!=null)
			object = context;
		
		IItemPropertyDescriptor propertyDescriptor = getPropertyDescriptor(object, feature);
		if (propertyDescriptor != null) {
			propertyDescriptor.setPropertyValue(object, value);
			return;
		}

		EditingDomain editingDomain = getEditingDomain(object);
		if (editingDomain == null) {
			object.eSet(feature, value);
		} else {
			editingDomain.getCommandStack().execute(SetCommand.create(editingDomain, object, feature, value));
		}
	}

}
