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

package org.eclipse.bpmn2.modeler.core.adapters;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * @author Bob Brodt
 *
 */
public class ObjectDescriptor {

	protected EObject object;
	protected String label;
	protected String text;
	protected AdapterFactory adapterFactory;
	
	public ObjectDescriptor(AdapterFactory adapterFactory, EObject object) {
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
			EObject object = context instanceof EObject ? (EObject)context : this.object;
			// derive text from feature's value: default behavior is
			// to use the "name" attribute if there is one;
			// if not, use the "id" attribute;
			// fallback is to use the feature's toString()
			String text = ModelUtil.toDisplayName(object.eClass().getName());
			Object name = null;
			EStructuralFeature f = null;
			f = object.eClass().getEStructuralFeature("name");
			if (f!=null) {
				name = object.eGet(f);
				if (name==null || name.toString().isEmpty())
					name = null;
			}
			if (name==null) {
				f = object.eClass().getEStructuralFeature("id");
				if (f!=null) {
					name = object.eGet(f);
					if (name==null || name.toString().isEmpty())
						name = null;
				}
			}
			if (name==null)
				name = "Unnamed " + text;
			return (String)name;
		}
		return text;
	}

	protected IItemPropertyDescriptor getPropertyDescriptor(EStructuralFeature feature) {
		return getPropertyDescriptor(object, feature);
	}

	protected IItemPropertyDescriptor getPropertyDescriptor(EObject object, EStructuralFeature feature) {
		ItemProviderAdapter adapter = null;
		for (Adapter a : object.eAdapters()) {
			if (a instanceof ItemProviderAdapter) {
				adapter = (ItemProviderAdapter)a;
				break;
			}
		}
		if (adapter!=null)
			return adapter.getPropertyDescriptor(object, feature);
		return null;
	}
	
	protected EObject clone(EObject oldObject) {
		EObject newObject = null;
		if (oldObject!=null) {
			EClass eClass = oldObject.eClass();
			newObject = eClass.getEPackage().getEFactoryInstance().create(eClass);
			for (EStructuralFeature f : eClass.getEAllAttributes()) {
				newObject.eSet(f, oldObject.eGet(f));
			}
		}
		return newObject;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EObject && ((EObject) obj).eClass() == object.eClass()) {
			// compare feature values of both EObjects:
			// this should take care of most of the BPMN2 elements
			for (EStructuralFeature f : object.eClass().getEAllStructuralFeatures()) {
				// IDs are allowed to be different
				if ("id".equals(f.getName()))
					continue;
				Object v1 = ((EObject)obj).eGet(f);
				Object v2 = object.eGet(f);
				// both null? equal!
				if (v1==null && v2==null)
					continue;
				// one or the other null? not equal!
				if (v1==null || v2==null)
					return false;
				// both not null? do a default compare...
				if (!v1.equals(v2)) {
					// the default Object.equals(obj) fails:
					// for Dynamic EObjects (used here as "proxies") only compare their proxy URIs 
					if (v1 instanceof DynamicEObjectImpl && v2 instanceof DynamicEObjectImpl) {
						v1 = ((DynamicEObjectImpl)v1).eProxyURI();
						v2 = ((DynamicEObjectImpl)v2).eProxyURI();
						if (v1==null && v2==null)
							continue;
						if (v1==null || v2==null)
							return false;
						if (v1.equals(v2))
							continue;
					}
					else if (v1 instanceof EObject && v2 instanceof EObject) {
						// for all other EObjects, do a deep compare...
						ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt((EObject)v1, ExtendedPropertiesAdapter.class);
						if (adapter!=null) {
							if (adapter.getObjectDescriptor().equals(v2))
								continue;
						}
					}
					return false;
				}
			}
			return true;
		}
		return super.equals(obj);
	}

	public TransactionalEditingDomain getEditingDomain(Object object) {
		EObject eObject = (EObject) object;
		EditingDomain result = AdapterFactoryEditingDomain.getEditingDomainFor(eObject);
		if (result == null) {
			if (adapterFactory instanceof IEditingDomainProvider) {
				result = ((IEditingDomainProvider) adapterFactory).getEditingDomain();
			}

			if (result == null && adapterFactory instanceof ComposeableAdapterFactory) {
				AdapterFactory rootAdapterFactory = ((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory();
				if (rootAdapterFactory instanceof IEditingDomainProvider) {
					result = ((IEditingDomainProvider) rootAdapterFactory).getEditingDomain();
				}
			}
		}
		if (result instanceof TransactionalEditingDomain)
			return (TransactionalEditingDomain)result;
		return null;
	}
}
