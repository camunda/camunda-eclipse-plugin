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
import org.eclipse.emf.ecore.resource.Resource;
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
public class ObjectDescriptor<T extends EObject> {

	protected T object;
	protected String label;
	protected String name;
	protected AdapterFactory adapterFactory;
	
	public ObjectDescriptor(AdapterFactory adapterFactory, T object) {
		this.object = object;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel(Object context) {
		EObject object = (context instanceof EObject) ?
				(EObject)context :
				this.object;
		EClass eclass = (object instanceof EClass) ?
				(EClass)object :
				object.eClass();
		if (label==null) {
			label = ModelUtil.toDisplayName(eclass.getName());
		}
		return label;
	}
	
	public void setDisplayName(String name) {
		this.name = name;
	}
	
	public String getDisplayName(Object context) {
		if (name==null) {
			T object = adopt(context);
			
			// derive text from feature's value: default behavior is
			// to use the "name" attribute if there is one;
			// if not, use the "id" attribute;
			// fallback is to use the feature's toString()
			String text = ModelUtil.toDisplayName(object.eClass().getName());
			Object value = null;
			EStructuralFeature f = null;
			f = object.eClass().getEStructuralFeature("name");
			if (f!=null) {
				value = object.eGet(f);
				if (value==null || value.toString().isEmpty())
					value = null;
			}
			if (value==null) {
				f = object.eClass().getEStructuralFeature("id");
				if (f!=null) {
					value = object.eGet(f);
					if (value==null || value.toString().isEmpty())
						value = null;
				}
			}
			if (value==null)
				value = "Unnamed " + text;
			
			String displayName = (String) value;

			return ModelUtil.beautifyName(displayName);
		}
		return name;
	}

	protected IItemPropertyDescriptor getPropertyDescriptor(EStructuralFeature feature) {
		return getPropertyDescriptor(object, feature);
	}

	protected IItemPropertyDescriptor getPropertyDescriptor(T object, EStructuralFeature feature) {
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
	
	protected EObject clone(T oldObject) {
		T newObject = null;
		if (oldObject!=null) {
			EClass eClass = oldObject.eClass();
			newObject = (T) eClass.getEPackage().getEFactoryInstance().create(eClass);
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
				Object v1 = ((T)obj).eGet(f);
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
						ExtendedPropertiesAdapter<T> adapter = (ExtendedPropertiesAdapter<T>) AdapterUtil.adapt((EObject)v1, ExtendedPropertiesAdapter.class);
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
	
	/**
	 * Many methods accept java Objects as a context variable. In many cases (especially the
	 * default implementations) the context object must have the same type as the specialized
	 * class. 
	 * 
	 * @param context
	 * @return the context variable if it has the same type as this.object, or this.object if not.
	 */
	protected T adopt(Object context) {
		return (this.object.getClass().isInstance(context)) ? (T)context : this.object;
	}

	public TransactionalEditingDomain getEditingDomain(Object context) {
		T object = adopt(context);
		EditingDomain result = AdapterFactoryEditingDomain.getEditingDomainFor(object);
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

	public T createObject(Object context) {
		return createObject(null,context);
	}
	
	public T createObject(Resource resource, Object context) {
	
		EClass eClass = null;
		if (context instanceof EClass) {
			eClass = (EClass)context;
		}
		else if (context instanceof EObject) {
			eClass = ((EObject)context).eClass();
			if (resource==null)
				resource = ((EObject)context).eResource();
		}
		else {
			eClass = object.eClass();
		}
		assert(object.eClass().isSuperTypeOf(eClass));

		T newObject = (T) eClass.getEPackage().getEFactoryInstance().create(eClass);
		
		if (resource==null)
			resource = object.eResource();
		
		// if the object has an "id", assign it now.
		String id = ModelUtil.setID(newObject,resource);
		// also set a default name
		EStructuralFeature feature = newObject.eClass().getEStructuralFeature("name");
		if (feature!=null) {
			newObject.eSet(feature, ""+ModelUtil.toDisplayName(newObject.eClass().getName()));
		}
		return newObject;
	}
}
