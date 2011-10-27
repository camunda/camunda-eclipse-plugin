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

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.activity.task.ICustomTaskFeature;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Internal;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.SimpleFeatureMapEntry;

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
		public String ref;
		
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
					else if (propValue instanceof Property) {
						String s = ((Property)propValue).getFirstStringValue();
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
	protected List<Property> properties = new ArrayList<Property>();
	protected EObject customTask;
	protected Resource containingResource;
	
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
	
	public List<Property> getProperties() {
		return properties;
	}
	
	public EObject createObject(EObject container) {
		containingResource = container.eResource();
		customTask = createObject(getType());
		populateObject(customTask,getProperties());
		return customTask;
	}
	
	private EObject createObject(String className) {
		// look in the extension model package for the class name first
		EPackage pkg = getRuntime().getModelDescriptor().getEPackage();
		EClass eClass = (EClass) pkg.getEClassifier(className);
		if (eClass==null) {
			// if not found, look in BPMN2 package
			eClass = (EClass) Bpmn2Package.eINSTANCE.getEClassifier(className);
		}
		if (eClass!=null)
			return createObject(eClass);
		return null;
	}

	private EObject createObject(EClass eClass) {
		EObject eObject = eClass.getEPackage().getEFactoryInstance().create(eClass);
		
		// if the object has an "id", assign it now.
		String id = ModelUtil.setID(eObject,containingResource);
		// also set a default name
		EStructuralFeature feature = eObject.eClass().getEStructuralFeature("name");
		if (feature!=null) {
			if (id!=null)
				eObject.eSet(feature, ModelUtil.toDisplayName(id));
			else
				eObject.eSet(feature, "New "+ModelUtil.toDisplayName(eObject.eClass().getName()));
		}

		return eObject;
	}
	
	private EStructuralFeature getFeature(Class type, String name) {
		EStructuralFeature feature = getFeature(
				getRuntime().getModelDescriptor().getEPackage(),
				type, name);
		if (feature==null) {
			// try the bpmn2 package
			feature = getFeature(Bpmn2Package.eINSTANCE, type, name);
		}
		return feature;
	}
	
	private EStructuralFeature getFeature(EPackage pkg, Class type, String name) {
		TreeIterator<EObject> it = pkg.eAllContents();
		while (it.hasNext()) {
			EObject o = it.next();
			if (type.isInstance(o)) {
				EStructuralFeature fName = o.eClass().getEStructuralFeature("name");
				if (fName!=null && o.eGet(fName)!=null && o.eGet(fName).equals(name)) {
					return (EStructuralFeature)o;
				}
			}
		}
		return null;
	}
	
	public void populateObject(EObject eObj, List<Property> props) {
		
		for (Property prop : props) {
			populateObject(eObj,prop);
		}
	}
	
	public void populateObject(EObject eObj, Property prop) {
			
		EStructuralFeature feature = eObj.eClass().getEStructuralFeature(prop.name);
		if (feature==null) {
			Class type = EAttribute.class;
			if (prop.ref!=null || prop.getValues().get(0) instanceof Property)
				type = EReference.class;
			feature = getFeature(type,prop.name);
		}

		if (feature instanceof EAttribute) {
			// TODO: wip
//			if ( feature.getEType().getInstanceClass().isInstance(FeatureMap.Entry.class) ) {
//				for (Object o : prop.getValues()) {
//					if (o instanceof Property) {
//						Property prop2 = (Property)o;
//						EObject eObj2 = createObject(prop2.name);
//						FeatureMap.Entry entry = new SimpleFeatureMapEntry((Internal) feature,eObj2);
//						eObj.eSet(feature, eObj2);
//						populateObject(eObj2,prop2);
//					}
//				}
//			}
//			else
				eObj.eSet(feature, prop.getFirstStringValue());
		}
		else if (feature instanceof EReference) {
			EReference ref = (EReference)feature;
			EFactory factory = ref.getEReferenceType().getEPackage().getEFactoryInstance();
			EObject eObj2 = null;
			if (prop.ref!=null) {
				// navigate down the newly created custom task to find the object reference
				eObj2 = customTask;
				String[] segments = prop.ref.split("/");
				for (String s : segments) {
					// is the feature an Elist?
					int index = s.indexOf('#');
					if (index>0) {
						index = Integer.parseInt(s.substring(index+1));
						s = s.split("#")[0];
					}
					EStructuralFeature f = eObj2.eClass().getEStructuralFeature(s);
					if (index<0) {
						eObj2 = (EObject)eObj2.eGet(f);
					}
					else
					{
						eObj2 = (EObject)((EList)eObj2.eGet(f)).get(index);
					}
				}
				if (feature.isMany()) {
					((EList)eObj.eGet(feature)).add(eObj2);
				}
				else {
					eObj.eSet(feature, eObj2);
				}
			}
			else
			{
				eObj2 = createObject(ref.getEReferenceType());
				if (feature.isMany()) {
					((EList)eObj.eGet(feature)).add(eObj2);
				}
				else {
					eObj.eSet(feature, eObj2);
				}
				
				for (Object o : prop.getValues()) {
					if (o instanceof Property) {
						Property prop2 = (Property)o;
						populateObject(eObj2,prop2);
					}
				}
			}
		}
	}
	
	public Object getProperty(String name) {

		for (Property prop : getProperties()) {
			if (prop.name.equals(name)) {
				if (!prop.getValues().isEmpty()) {
					return prop.getValues().get(0);
				}
			}
		}
		return null;
	}
}