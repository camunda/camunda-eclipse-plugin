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
 * @author Ivar Meikas
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterRegistry;
import org.eclipse.bpmn2.modeler.core.adapters.INamespaceMap;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceSetImpl;
import org.eclipse.bpmn2.modeler.core.runtime.ModelDescriptor;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;

public class ModelUtil {

	// TODO: need to determine whether IDs need to be unique within a Resource or ResourceSet - see getKey()
	
	// Map of EMF resource sets to ID mapping tables. The ID mapping tables map a BPMN2 element ID string to the EObject.
	// The EObject is not used anywhere (yet!) just a placeholder to allow use of a HashMap for fast lookups of the ID string.
	// The ID strings are composed from the BPMN2 element description name and a sequence number (starting at 1).
	// When a new ID is requested, generateID() simply increments the sequence number until an ID is found that isn't
	// already in the table.
	public static HashMap<Object, Hashtable<String, EObject>> ids = new  HashMap<Object, Hashtable<String, EObject>>();
	// Map of ID strings and sequential counters for each BPMN2 element description.
	public static HashMap<String, Integer> defaultIds = new HashMap<String, Integer>();

	/**
	 * Clear the IDs hashmap for the given EMF Resource. This should be called
	 * when the editor is disposed to avoid unnecessary growth of the IDs table.
	 * 
	 * @param res - the EMF Resource that was used to generate the ID strings.
	 */
	public static void clearIDs(Resource res, boolean all) {
		ids.remove( getKey(res) );
		if (all) {
			defaultIds.clear();
		}
	}

	/**
	 * Construct the first part of the ID string using the BPMN2 element description name.
	 * If the object is a DI element, concatenate the BPMN2 element description name.
	 * 
	 * @param obj - the BPMN2 object
	 * @return name string
	 */
	public static String getObjectName(EObject obj) {
		String name;
		EStructuralFeature feature = ((EObject)obj).eClass().getEStructuralFeature("bpmnElement");
		if (feature!=null && obj.eGet(feature)!=null) {
			EObject bpmnElement = (EObject) obj.eGet(feature);
			name = obj.eClass().getName() + "_" + bpmnElement.eClass().getName();
		}
		else {
			name = obj.eClass().getName();
		}
		return name;
	}
	
	private static Object getKey(EObject obj) {
		if (obj.eResource()==null) {
			System.out.println("The object type "+obj.getClass().getName()+" is not contained in a Resource");
			return null;
		}
		assert(obj!=null);
		return getKey(obj.eResource());
	}
	
	private static Object getKey(Resource res) {
		assert(res!=null);
		return res.getResourceSet();
	}
	
	/**
	 * If an EObject has not yet been added to a Resource (e.g. during construction)
	 * generate an ID string using a different strategy (basically same ID prefixed with an underscore).
	 * The "defaultIds" table is used to track the next sequential ID value for a given element description.
	 * 
	 * @param obj - the BPMN2 object
	 * @return the ID string
	 */
	private static String generateDefaultID(EObject obj) {
		String name = getObjectName(obj);
		Integer value = defaultIds.get(name);
		if (value==null)
			value = Integer.valueOf(1);
		value = Integer.valueOf( value.intValue() + 1 );
		defaultIds.put(name, Integer.valueOf(value));
		
		return "_" + name + "_" + value;
	}

	/**
	 * Generate an ID string for a given BPMN2 object.
	 * 
	 * @param obj - the BPMN2 object
	 * @return the ID string
	 */
	public static String generateID(EObject obj) {
		return generateID(obj,obj.eResource());
	}

	/**
	 * Generate an ID string for a given BPMN2 object that will (eventually!) be added to the given Resource.
	 * 
	 * CAUTION: IDs for objects that have already been deleted WILL be reused.
	 * 
	 * @param obj - the BPMN2 object
	 * @param res - the Resource to which the object will be added
	 * @return the ID string
	 */
	public static String generateID(EObject obj, Resource res) {
		Object key = (res==null ? getKey(obj) : getKey(res));
		if (key!=null) {
			Hashtable<String, EObject> tab = ids.get(key);
			if (tab==null) {
				tab = new Hashtable<String, EObject>();
				ids.put(key, tab);
			}
			
			String name = getObjectName(obj);
			for (int i=1;; ++i) {
				String id = name + "_" + i;
				if (tab.get(id)==null) {
					tab.put(id, obj);
					return id;
				}
			}
		}
		return generateDefaultID(obj);
	}

	/**
	 * Add an ID string to the ID mapping table(s). This must be used during model import
	 * to add existing BPMN2 element IDs to the table so we don't generate duplicates.
	 * 
	 * @param obj - the BPMN2 object
	 */
	public static void addID(EObject obj) {
		EStructuralFeature feature = ((EObject)obj).eClass().getEStructuralFeature("id");
		if (feature!=null) {
			Object value = obj.eGet(feature);
			if (value!=null) {
				addID(obj,(String)value);
			}
			else {
				// TODO: what to do here if the BPMN2 element has an "id" attribute which is not set?
				// should we generate one and set it?
				// yup
				setID(obj);
			}
		}
		
	}
	
	/**
	 * Add an ID string to the ID mapping table(s). This must be used during model import
	 * to add existing BPMN2 element IDs to the table so we don't generate duplicates.
	 * 
	 * @param obj - the BPMN2 object
	 * @param id - the object's ID string
	 */
	public static void addID(EObject obj, String id) {
		Object key = getKey(obj);
		String name = getObjectName(obj);
		if (key==null || id.startsWith("_" + name + "_")) {
			int newValue = 0;
			try {
				int i = id.lastIndexOf('_') + 1;
				if (i<id.length())
					newValue = Integer.parseInt(id.substring(i));
			} catch (Exception e) {
			}
			Integer oldValue = defaultIds.get(name);
			if (oldValue==null || newValue > oldValue.intValue())
				defaultIds.put(name, Integer.valueOf(newValue));
		}
		else {	
			Hashtable<String, EObject> tab = ids.get(key);
			if (tab==null) {
				tab = new Hashtable<String, EObject>();
				ids.put(key, tab);
			}
			tab.put(id, obj);
		}
	}

	/**
	 * Generate a unique ID for the given BPMN2 element and set it.
	 * This should only be used during object construction AFTER an object has
	 * already been added to a Resource.
	 * 
	 * @param obj - the BPMN2 object
	 */
	public static String setID(EObject obj) {
		return setID(obj,obj.eResource());
	}

	/**
	 * Generate a unique ID for the given BPMN2 element and set it.
	 * This should be used during object construction if the object has NOT YET
	 * been added to a Resource.
	 * 
	 * @param obj - the BPMN2 object
	 * @param res - the Resource to which the object will be added
	 */
	public static String setID(EObject obj, Resource res) {
		String id = null;
		EStructuralFeature feature = ((EObject)obj).eClass().getEStructuralFeature("id");
		if (feature!=null) {
			if (obj.eGet(feature)==null) {
				id = generateID(obj,res);
				obj.eSet(feature, id);
			}
		}
		return id;
	}


	public static String getName(BaseElement element) {
		EStructuralFeature feature = element.eClass().getEStructuralFeature("name");
		if (feature!=null && element.eGet(feature) instanceof String)
			return (String) element.eGet(feature);
		return null;
	}

	public static boolean hasName(EObject obj) {
		EStructuralFeature feature = obj.eClass().getEStructuralFeature("name");
		return feature!=null;
	}

	public static String getObjectDisplayName(EObject obj) {
		String objName = null;
		if (obj instanceof BPMNDiagram) {
			Bpmn2DiagramType type = getDiagramType((BPMNDiagram)obj); 
			if (type == Bpmn2DiagramType.CHOREOGRAPHY) {
				objName = "Choreography Diagram";
			}
			else if (type == Bpmn2DiagramType.COLLABORATION) {
				objName = "Collaboration Diagram";
			}
			else if (type == Bpmn2DiagramType.PROCESS) {
				objName = "Process Diagram";
			}
		}
		if (objName==null){
			objName = toDisplayName( obj.eClass().getName() );
		}
		return objName;
	}

	public static String getDisplayName(EObject obj) {
		String objName = getObjectDisplayName(obj);
		EStructuralFeature feature = obj.eClass().getEStructuralFeature("name");
		if (feature!=null) {
			String name = (String)obj.eGet(feature);
			if (name==null || name.isEmpty())
				name = "Unnamed " + objName;
			else
				name = objName + " \"" + name + "\"";
			return name;
		}
		return objName;
	}
	
	public static String getDisplayName(EObject obj, EAttribute attr) {
		if (attr!=null) {
			ItemProviderAdapter itemProviderAdapter = (ItemProviderAdapter) new Bpmn2ItemProviderAdapterFactory()
					.adapt(obj, ItemProviderAdapter.class);
			
			IItemPropertyDescriptor propertyDescriptor = itemProviderAdapter.getPropertyDescriptor(obj,attr);
			if (propertyDescriptor!=null)
				return propertyDescriptor.getDisplayName(attr);
			
			// There are no property descriptors available for this EObject -
			// this is probably because the "edit" plugin was not generated for
			// the EMF model, or is not available.
			// Use the class name to synthesize a display name
			obj = attr;
			itemProviderAdapter.dispose();
		}
		
		String className = obj.eClass().getName();
		className = className.replaceAll("Impl$", "");
		return toDisplayName(className);
	}
	
	public static String toDisplayName(String anyName) {
		String displayName = "";
		boolean first = true;
		char[] chars = anyName.toCharArray();
		for (int i=0; i<chars.length; ++i) {
			char c = chars[i];
			if (Character.isUpperCase(c)) {
				if (displayName.length()>0 && i+1<chars.length && !Character.isUpperCase(chars[i+1]))
					displayName += " ";
			}
			if (first) {
				c = Character.toUpperCase(c);
				first = false;
			}
			if (c=='_')
				c = ' ';
			displayName += c;
		}
		return displayName;
	}

	

	/**
	 * This is a slightly hacked resource set that we will be using for to solve
	 * the problem of loading the right resources from URLs that betray no
	 * information on the type of the resource.
	 * 
	 * @param resourceSet
	 * 
	 * @return the BPELResourceSetImpl that walks around the problem indicated.
	 * 
	 */

	public static Bpmn2ModelerResourceSetImpl slightlyHackedResourceSet(
			ResourceSet resourceSet) {

		if (resourceSet instanceof Bpmn2ModelerResourceSetImpl) {
			return (Bpmn2ModelerResourceSetImpl) resourceSet;
		}

		Map<Object, Object> map = resourceSet.getLoadOptions();
		Bpmn2ModelerResourceSetImpl result = (Bpmn2ModelerResourceSetImpl) map
				.get(Bpmn2ModelerResourceSetImpl.SLIGHTLY_HACKED_KEY);
		if (result == null) {
			result = new Bpmn2ModelerResourceSetImpl();
			map.put(Bpmn2ModelerResourceSetImpl.SLIGHTLY_HACKED_KEY, result);
		}
		return result;
	}

	/**
	 * Return the resource set that we should be using to load "specific" type
	 * of resources. The "slightlyHacked" resource set is kept in the load
	 * options map.
	 * 
	 * @param eObj
	 * @return the slightly hacked resource set.
	 * 
	 */
	public static Bpmn2ModelerResourceSetImpl slightlyHackedResourceSet(EObject eObj) {
		return slightlyHackedResourceSet(eObj.eResource().getResourceSet());
	}
	
	public static Object resolveXSDObject(Object xsdObject) {
		if (xsdObject instanceof XSDElementDeclaration) {
			XSDElementDeclaration resolvedElement = ((XSDElementDeclaration)xsdObject).getResolvedElementDeclaration();
			if (resolvedElement != null) xsdObject = resolvedElement;
		} else if (xsdObject instanceof XSDAttributeDeclaration) {
			XSDAttributeDeclaration resolvedAttribute = ((XSDAttributeDeclaration)xsdObject).getResolvedAttributeDeclaration();
			if (resolvedAttribute != null) xsdObject = resolvedAttribute;
		}
		return xsdObject;
	}

	/**
	 * @param eObject
	 * @return the namespace map for the given object.
	 */

	@SuppressWarnings("unchecked")
	static public INamespaceMap<String, String> getNamespaceMap(EObject eObject) {

		if (eObject == null) {
			throw new NullPointerException(
					"eObject cannot be null in getNamespaceMap()");
		}

		INamespaceMap<String, String> nsMap = null;
    	// Bug 120110 - this eObject may not have a namespace map, but its
		// ancestors might, so keep searching until we find one or until
		// we run out of ancestors.
		while (nsMap==null && eObject!=null) {
			nsMap = AdapterRegistry.INSTANCE.adapt(
				eObject, INamespaceMap.class);
			if (nsMap==null)
				eObject = eObject.eContainer();
		}
		
		if (nsMap == null) {
			throw new IllegalStateException(
					"INamespaceMap cannot be attached to an eObject");
		}

		return nsMap;
	}

	public static String getNamespacePrefix(EObject eObject, String namespace) {

		for (EObject context = eObject; context != null; context = context
				.eContainer()) {
			List<String> pfxList = getNamespaceMap(context).getReverse(
					namespace);
			if (pfxList.size() > 0) {
				return pfxList.get(0);
			}
		}
		return null;
	}
	
	public enum Bpmn2DiagramType {
		NONE, PROCESS, CHOREOGRAPHY, COLLABORATION;
	}
	
	public static Bpmn2DiagramType getDiagramType(BPMNDiagram diagram) {
		Definitions defs = (Definitions)diagram.eContainer();
		BPMNPlane plane = diagram.getPlane();
		if (plane!=null) {
			BaseElement be = plane.getBpmnElement();
			if (be instanceof Process) {
				for (RootElement re : defs.getRootElements()) {
					if (re instanceof Choreography) {
						for (Participant p : ((Choreography)re).getParticipants()) {
							if (p.getProcessRef() == be)
								return Bpmn2DiagramType.CHOREOGRAPHY;
						}
					}
					else if (re instanceof Collaboration) {
						for (Participant p : ((Collaboration)re).getParticipants()) {
							if (p.getProcessRef() == be)
								return Bpmn2DiagramType.COLLABORATION;
						}
					}
				}
				return Bpmn2DiagramType.PROCESS;
			}
			else if (be instanceof Choreography)
				return Bpmn2DiagramType.CHOREOGRAPHY;
			else if (be instanceof Collaboration)
				return Bpmn2DiagramType.COLLABORATION;
		}
		return Bpmn2DiagramType.NONE;
	}
	
	public static List<EStructuralFeature> getAnyAttributes(EObject object) {
		List<EStructuralFeature> list = new ArrayList<EStructuralFeature>();
		EStructuralFeature anyAttribute = ((EObject)object).eClass().getEStructuralFeature("anyAttribute");
		if (anyAttribute!=null && object.eGet(anyAttribute) instanceof BasicFeatureMap) {
			BasicFeatureMap map = (BasicFeatureMap)object.eGet(anyAttribute);
			for (Entry entry : map) {
				EStructuralFeature feature = entry.getEStructuralFeature();
				list.add(feature);
			}
		}
		return list;
	}
	
	public static EAttribute createDynamicAttribute(EPackage pkg, EObject object, String name, String type) {
		EClass docRoot = ExtendedMetaData.INSTANCE.getDocumentRoot(pkg);
		for (EStructuralFeature f : docRoot.getEStructuralFeatures()) {
			if (f.getName().equals(name)) {
				if (f instanceof EAttribute)
					return (EAttribute)f;
				return null;
			}
		}
		if (type==null)
			type = "EString";
		
		EDataType eDataType = (EDataType)EcorePackage.eINSTANCE.getEClassifier(type);
		EAttribute attr = EcorePackage.eINSTANCE.getEcoreFactory().createEAttribute();
		attr.setName(name);
		attr.setEType(eDataType);
		ExtendedMetaData.INSTANCE.setFeatureKind(attr,ExtendedMetaData.ATTRIBUTE_FEATURE);
		
		docRoot.getEStructuralFeatures().add(attr);
		ExtendedMetaData.INSTANCE.setNamespace(attr, pkg.getNsURI());

		return attr;
	}
	
	public static EReference createDynamicReference(EPackage pkg, EObject object, String name, EObject value) {
		EClass docRoot = ExtendedMetaData.INSTANCE.getDocumentRoot(pkg);
		for (EStructuralFeature f : docRoot.getEStructuralFeatures()) {
			if (f.getName().equals(name)) {
				if (f instanceof EReference)
					return (EReference)f;
				return null;
			}
		}
		EReference ref = EcorePackage.eINSTANCE.getEcoreFactory().createEReference();
		ref.setName(name);
		EClass eClass = value.eClass();
		ref.setEType(eClass);
		ExtendedMetaData.INSTANCE.setFeatureKind(ref,ExtendedMetaData.ELEMENT_FEATURE);
		
		docRoot.getEStructuralFeatures().add(ref);
		ExtendedMetaData.INSTANCE.setNamespace(ref, pkg.getNsURI());

		return ref;
	}
		
	@SuppressWarnings("unchecked")
	public static EStructuralFeature addAnyAttribute(EObject childObject, String namespace, String name, Object value) {
		EStructuralFeature anyAttribute = childObject.eClass().getEStructuralFeature(Bpmn2Package.BASE_ELEMENT__ANY_ATTRIBUTE);
		List<BasicFeatureMap.Entry> anyMap = (List<BasicFeatureMap.Entry>)childObject.eGet(anyAttribute);
		for (BasicFeatureMap.Entry fe : anyMap) {
			if (fe.getEStructuralFeature() instanceof EAttributeImpl) {
				EAttributeImpl a = (EAttributeImpl) fe.getEStructuralFeature();
				if (namespace.equals(a.getExtendedMetaData().getNamespace()) && name.equals(a.getName())) {
					return a;
				}
			}
		}
		
		// this featuremap can only hold attributes, not elements
		EStructuralFeature attr = ExtendedMetaData.INSTANCE.demandFeature(namespace, name, false);
		anyMap.add( FeatureMapUtil.createEntry(attr, value) );
		return attr;
	}

}
