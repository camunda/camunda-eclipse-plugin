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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.emf.transaction.util.TransactionUtil;

/**
 * This adapter will insert an EObject into its container feature when the EObject's
 * content changes. This allows the UI to construct new objects without inserting
 * them into their container unless the user changes some feature in the new object.
 * Thus, an empty EObject is available for use by the UI for rendering only, without
 * creating an EMF transaction, and hence, a useless entry on the undo stack.
 */
public class InsertionAdapter extends EContentAdapter {
	
	protected Resource resource;
	protected EObject object;
	protected EStructuralFeature feature;
	protected EObject value;
	
	private InsertionAdapter(EObject object, EStructuralFeature feature, EObject value) {
		this(null,object,feature,value);
	}

	private InsertionAdapter(Resource resource, EObject object, EStructuralFeature feature, EObject value) {
		// in order for this to work, the object must businessObject contained in a Resource,
		// the value must NOT YET businessObject contained in a Resource,
		// and the value must businessObject an instance of the feature EType.
//		assert(object.eResource()!=null);
//		assert(value.eResource()==null);
//		assert(feature.getEType().isInstance(value));
		if (resource==null)
			this.resource = object.eResource();
		else
			this.resource = resource;
		this.object = object;
		this.feature = feature;
		this.value = value;
	}
	
	private InsertionAdapter(EObject object, String featureName, EObject value) {
		this(object, object.eClass().getEStructuralFeature(featureName), value);
	}
	
	public static EObject add(Resource resource, EObject object, EStructuralFeature feature, EObject value) {
		value.eAdapters().add(
				new InsertionAdapter(resource, object, feature, value));
		return value;
	}

	public static EObject add(EObject object, EStructuralFeature feature, EObject value) {
		value.eAdapters().add(
				new InsertionAdapter(object, feature, value));
		return value;
	}
	
	public static EObject add(EObject object, String featureName, EObject value) {
		value.eAdapters().add(
				new InsertionAdapter(object, featureName, value));
		return value;
	}

	public void notifyChanged(Notification notification) {
		if (notification.getNotifier() == value && !(notification.getOldValue() instanceof InsertionAdapter)) {
			// execute if an attribute in the new value has changed
			execute();
		}
		else if (notification.getNotifier()==object && notification.getNewValue()==value) {
			// if the new value has been added to the object, we can remove this adapter
			object.eAdapters().remove(this);
		}
	}

	private void executeChildren(List list) {
		for (Object o : list) {
			if (o instanceof List) {
				executeChildren((List)o);
			}
			else if (o instanceof EObject) {
				executeChildren((EObject)o);
			}
		}
	}
	
	private void executeChildren(EObject value) {
		// allow other adapters to execute first
		for (EStructuralFeature f : value.eClass().getEAllStructuralFeatures()) {
			try {
				Object v = value.eGet(f);
				if (v instanceof List) {
					executeChildren((List)v);
				}
				else if (v instanceof EObject) {
					executeIfNeeded((EObject)v);
				}
			}
			catch (Exception e) {
				// some getters may throw exceptions - ignore those
			}
		}
		executeIfNeeded(value);
	}
	
	@SuppressWarnings("unchecked")
	public void execute() {
		// if the object into which this value is being added has other adapters execute those first
		executeIfNeeded(object);
		
		// remove this adapter from the value - this adapter is a one-shot deal!
		value.eAdapters().remove(this);
		try {
			Object o = object.eGet(feature);
		}
		catch (Exception e1) {
			try {
				Object o = value.eGet(feature);
				// this is the inverse add of object into value
				o = value;
				value = object;
				object = (EObject)o;
			}
			catch (Exception e2) {
			}
		}
		// if there are any EObjects contained or referenced by this value, execute those adapters first
		executeChildren(value);
		
		// set the value in the object
		boolean valueChanged = false;
		final EList<EObject> list = feature.isMany() ? (EList<EObject>)object.eGet(feature) : null;
		if (list==null)
			valueChanged = object.eGet(feature)!=value;
		else
			valueChanged = !list.contains(value);
		
		if (valueChanged) {
			TransactionalEditingDomain domain = getEditingDomain();
			if (domain==null) {
				if (list==null)
					object.eSet(feature, value);
				else
					list.add(value);
				// assign the value's ID if it has one:
				// because of changes made by cascading InsertionAdapters,
				// the object could now businessObject contained in a resource and hence
				// the setID() will need to businessObject executed on the command stack.
				domain = getEditingDomain();
				if (domain==null) {
					ModelUtil.setID(value);
				}
				else {
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							ModelUtil.setID(value);
						}
					});
				}
			}
			else {
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
						if (adapter!=null) {
							adapter.getFeatureDescriptor(feature).setValue(value);
						}
						else {
							if (list==null)
								object.eSet(feature, value);
							else
								list.add(value);
						}
						// assign the value's ID if it has one
						ModelUtil.setID(value);
					}
				});
			}
		}
	}
	
	private TransactionalEditingDomain getEditingDomain() {
		// If a transaction is already active, we want to run this
		// inside that transaction instead of creating a new one.
		if (resource==null) {
			resource = getResource(object);
			if (resource==null)
				return null;
		}
		TransactionalEditingDomainImpl domain = (TransactionalEditingDomainImpl)
				TransactionUtil.getEditingDomain(resource);
		if (domain.getActiveTransaction().isActive())
			return null;
		return domain;
	}
	
	public static void executeIfNeeded(EObject value) {
		List<InsertionAdapter> allAdapters = new ArrayList<InsertionAdapter>();
		
		for (Adapter adapter : value.eAdapters()) {
			if (adapter instanceof InsertionAdapter) {
				allAdapters.add((InsertionAdapter)adapter);
			}
		}
		value.eAdapters().removeAll(allAdapters);
		for (InsertionAdapter adapter : allAdapters)
			adapter.execute();
	}
	
	public Resource getResource() {
		if (resource==null) {
			Resource res = object.eResource();
			if (res!=null)
				return res;
			InsertionAdapter insertionAdapter = AdapterUtil.adapt(object, InsertionAdapter.class);
			if (insertionAdapter!=null)
				return insertionAdapter.getResource();
		}
		return resource;
	}
	
	public static Resource getResource(EObject object) {
		InsertionAdapter adapter = AdapterUtil.adapt(object, InsertionAdapter.class);
		if (adapter!=null) {
			return adapter.getResource();
		}
		if (object!=null)
			return object.eResource();
		return null;
	}
}
