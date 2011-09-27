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
package org.eclipse.bpmn2.modeler.core.runtime;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.features.activity.task.ICustomTaskFeature;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

public class CustomTaskDescriptor extends BaseRuntimeDescriptor {


	// Container class for property values
	public static class Value {
		
		static int ID = 0;
		String id;
		public List<Object>values;
		
		public Value() {
			setDefaultId();
		}
		
		public Value(String id) {
			if (id==null || id.isEmpty())
				setDefaultId();
			else
				this.id = id;
		}
		
		public List<Object> getValues() {
			if (values==null) {
				values = new ArrayList<Object>();
			}
			return values;
		}
		
		private void setDefaultId() {
			id = "V-" + ID++;
		}
	}
	
	// name/value pairs constructed from Custom Task extension point
	public static class Property {
		public String name;
		public String description;
		public List<Object>values;
		
		public Property() {
			this.name = "unknown";
		}
		
		public Property(String name, String description) {
			this.name = name;
			this.description = description;
		}
		
		public List<Object> getValues() {
			if (values==null) {
				values = new ArrayList<Object>();
			}
			return values;
		}

		public String getFirstStringValue() {

			if (!this.getValues().isEmpty()) {
				// simple attribute - find a String value for it
				for (Object propValue : this.getValues()) {
					if (propValue instanceof String) {
						return (String)propValue;
					}
					else if (propValue instanceof CustomTaskDescriptor.Property) {
						String s = ((CustomTaskDescriptor.Property)propValue).getFirstStringValue();
						if (s!=null)
							return s;
					}
				}
			}
			return null;
		}
	}
	
	protected String id;
	protected String name;
	protected String type;
	protected String description;
	protected ICustomTaskFeature createFeature;
	protected List<CustomTaskDescriptor.Property> properties = new ArrayList<CustomTaskDescriptor.Property>();
	
	public CustomTaskDescriptor(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getDescription() {
		return description;
	}

	public ICustomTaskFeature getCreateFeature() {
		return createFeature;
	}
	
	public List<CustomTaskDescriptor.Property> getProperties() {
		return properties;
	}
	
	public EObject createObject() {
		TargetRuntime rt = this.getRuntime();
		ModelDescriptor md = rt.getModelDescriptor(); 
		EFactory factory = md.getEFactory();
		EPackage pkg = md.getEPackage();
		EClass eClass = (EClass) pkg.getEClassifier(this.getType());
		EObject eObj = factory.create(eClass);

		populateObject(factory,eObj,getProperties());
		
		return eObj;
	}
	
	public void populateObject(EFactory factory, EObject eObj, List<CustomTaskDescriptor.Property> props) {
		
		for (CustomTaskDescriptor.Property prop : props) {
			EStructuralFeature feature = eObj.eClass().getEStructuralFeature(prop.name);
			if (feature instanceof EAttribute) {
				eObj.eSet(feature, prop.getFirstStringValue());
			}
			else if (feature instanceof EReference) {
				EReference ref = (EReference)feature;
				for (Object o : prop.getValues()) {
					if (o instanceof CustomTaskDescriptor.Value) {
						List<CustomTaskDescriptor.Property> props2 = new ArrayList<CustomTaskDescriptor.Property>();
						CustomTaskDescriptor.Value val = (CustomTaskDescriptor.Value)o;
						for (Object o2 : val.getValues()) {
							props2.add((CustomTaskDescriptor.Property)o2);
						}
						EObject eObj2 = factory.create(ref.getEReferenceType());
						populateObject(factory,eObj2,props2);
						if (feature.isMany()) {
							((EList)eObj.eGet(feature)).add(eObj2);
						}
						else {
							eObj.eSet(feature, eObj2);
							break;
						}
					}
				}
			}
		}
	}
	
	public Object getProperty(String name) {

		for (CustomTaskDescriptor.Property prop : getProperties()) {
			if (prop.name.equals(name)) {
				if (!prop.getValues().isEmpty()) {
					return prop.getValues().get(0);
				}
			}
		}
		return null;
	}
}