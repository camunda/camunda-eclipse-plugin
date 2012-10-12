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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterRegistry;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.INamespaceMap;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerResourceSetImpl;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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

	protected static Hashtable<EClass,EObject> dummyObjects = new Hashtable<EClass,EObject>();

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
	private static String getObjectName(EObject obj) {
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
		Resource resource = getResource(obj);
		if (resource==null) {
//			System.out.println("The object type "+obj.getClass().getName()+" is not contained in a Resource");
			return null;
		}
		assert(obj!=null);
		return getKey(resource);
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
	private static String generateDefaultID(EObject obj, String name) {
		if (name==null)
			name = getObjectName(obj);
		Integer value = defaultIds.get(name);
		if (value==null)
			value = Integer.valueOf(1);
		value = Integer.valueOf( value.intValue() + 1 );
		defaultIds.put(name, Integer.valueOf(value));
		
		return "_" + name + "_" + value;
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
	private static String generateID(EObject obj, Resource res) {
		return generateID(obj, res, null);
	}

	public static String generateID(EObject obj, Resource res, String name) {
		Object key = (res==null ? getKey(obj) : getKey(res));
		if (key!=null) {
			Hashtable<String, EObject> tab = ids.get(key);
			if (tab==null) {
				tab = new Hashtable<String, EObject>();
				ids.put(key, tab);
			}
			
			String id = name;
			if (name==null) {
				name = getObjectName(obj);
				id = name + "_" + 1;
			}
			
			for (int i=1;; ++i) {
				if (tab.get(id)==null) {
					tab.put(id, obj);
					return id;
				}
				id = name + "_" + i;
			}
		}
		return generateDefaultID(obj, name);
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
		return setID(obj,getResource(obj));
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

	public static String getFeature(EObject obj, String attribute) {
		EStructuralFeature feature = ((EObject) obj).eClass().getEStructuralFeature(attribute);
		if (feature != null) {
			return (String) obj.eGet(feature);
		} else {
			return null;
		}
	}
	
	public static int getIDNumber(String id) {
		try {
			int i = id.lastIndexOf("_");
			return Integer.parseInt(id.substring(i+1));
		}
		catch (Exception e) {
			return -1;
		}
	}

	public static String getName(BaseElement element) {
		if (element != null) {
			EStructuralFeature feature = element.eClass().getEStructuralFeature("name");
			if (feature!=null && element.eGet(feature) instanceof String)
				return (String) element.eGet(feature);
		}
		return null;
	}

	public static boolean hasName(EObject obj) {
		EStructuralFeature feature = obj.eClass().getEStructuralFeature("name");
		return feature!=null;
	}
/*	
	public static String getLabel(EObject object) {
		if (object==null)
			return "";
		return toDisplayName(object.eClass().getName());
	}
*/	
	public static String toDisplayName(String anyName) {
		// get rid of the "Impl" java suffix
		anyName = anyName.replaceAll("Impl$", "");
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
		return displayName.trim();
	}

	@SuppressWarnings("unchecked")
	public static List<EventDefinition> getEventDefinitions(Event event) {
		if (event!=null) {
			EStructuralFeature feature = event.eClass().getEStructuralFeature("eventDefinitions");
			if (feature!=null) {
				return (List<EventDefinition>) event.eGet(feature);
			}
		}
		return new ArrayList<EventDefinition>();
	}
	
	/**
	 * Checks if an event has a specific event definition type defined
	 * 
	 * @param event the event to be checked
	 * @param clazz the class of the event definition to 
	 * @return true if the event definition is defined for this event instance, false otherwise
	 */
	public static boolean hasEventDefinition (Event event, Class<?> clazz) {
		for (EventDefinition def : getEventDefinitions(event)) {
			if (clazz.isInstance(def)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the first event definition for an given event and given type
	 * 
	 * @param event the event 
	 * @param clazz the event definition class
	 * @return the first event definition definied for this event instance
	 */
	public static EventDefinition getEventDefinition (Event event, Class<?> clazz) {
		for (EventDefinition def : getEventDefinitions(event)) {
			if (clazz.isInstance(def)) {
				return def;
			}
		}
		return null;
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

	public static Bpmn2DiagramType getDiagramType(String name) {
		for (Bpmn2DiagramType t : Bpmn2DiagramType.values()) {
			if (t.toString().equalsIgnoreCase(name))
				return t;
		}
		return Bpmn2DiagramType.NONE;
	}
	
	public static Bpmn2DiagramType getDiagramType(EObject object) {
		if (object!=null && getResource(object)!=null) {
			Definitions defs = getDefinitions(object);
			if (defs.getDiagrams().size()>=1) {
				BPMNDiagram diagram = defs.getDiagrams().get(0);
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
			}
		}
		return Bpmn2DiagramType.NONE;
	}
	
	public static String getDiagramTypeName(BPMNDiagram object) {
		Bpmn2DiagramType type = getDiagramType((BPMNDiagram)object); 
		if (type == Bpmn2DiagramType.CHOREOGRAPHY) {
			return "Choreography Diagram";
		}
		else if (type == Bpmn2DiagramType.COLLABORATION) {
			return "Collaboration Diagram";
		}
		else if (type == Bpmn2DiagramType.PROCESS) {
			return "Process Diagram";
		}
		return "Unknown Diagram Type";
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
	
	public static EStructuralFeature getAnyAttribute(EObject object, String name) {
		EStructuralFeature anyAttribute = ((EObject)object).eClass().getEStructuralFeature("anyAttribute");
		if (anyAttribute!=null && object.eGet(anyAttribute) instanceof BasicFeatureMap) {
			BasicFeatureMap map = (BasicFeatureMap)object.eGet(anyAttribute);
			for (Entry entry : map) {
				EStructuralFeature feature = entry.getEStructuralFeature();
				if (feature.getName().equals(name))
					return feature;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static EStructuralFeature addAnyAttribute(EObject childObject, String name, Object value) {
		return addAnyAttribute(childObject, childObject.eClass().getEPackage().getNsURI(), name, value);
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

	public static EObject createStringWrapper(String value) {
		DynamicEObjectImpl de = new DynamicEObjectImpl();
		de.eSetClass(EcorePackage.eINSTANCE.getEObject());
		de.eSetProxyURI(URI.createURI(value));
		return de;
	}
	
	public static String getStringWrapperValue(Object wrapper) {
		if (wrapper instanceof DynamicEObjectImpl) {
			DynamicEObjectImpl de = (DynamicEObjectImpl)wrapper;
			URI uri = de.eProxyURI();
			return uri.toString();
		}
		else if (wrapper instanceof EObject) {
			return EcoreUtil.getURI((EObject)wrapper).toString();
		}
		return null;
	}
	
	public static boolean setStringWrapperValue(Object wrapper, String value) {
		if (isStringWrapper(wrapper)) {
			DynamicEObjectImpl de = (DynamicEObjectImpl)wrapper;
			de.eSetProxyURI(URI.createURI(value));
			return true;
		}
		return false;
	}
	
	public static boolean isStringWrapper(Object wrapper) {
		return wrapper instanceof DynamicEObjectImpl;
	}
	
	public static boolean isElementSelected(PictogramElement[] elements, PictogramElement element) {
		for (PictogramElement search : elements) {
			if (search.equals(element)) {
				return true;
			}
		}
		return false;
	}
	
	public static Resource getResource(EObject object) {
		Resource resource = object.eResource();
		if (resource==null) {
			InsertionAdapter insertionAdapter = AdapterUtil.adapt(object, InsertionAdapter.class);
			if (insertionAdapter!=null)
				resource = insertionAdapter.getResource();
			// TODO: can we use any of the referenced objects to find a Resource?
//			if (resource==null) {
//				EClass eclass = object.eClass();
//				for (EReference ref : eclass.getEAllReferences()) {
//					Object value = object.eGet(ref);
//					if (value instanceof EObject) {
//						resource = getResource((EObject) value);
//						if (resource!=null)
//							return resource;
//					}
//				}
//			}
		}
		return resource;
	}
	public static Definitions getDefinitions(EObject object) {
		Resource resource = getResource(object);
		if (resource!=null) {
			Object defs = resource.getContents().get(0).eContents().get(0);
			if (defs instanceof Definitions)
				return (Definitions)defs;
		}
		return null;
	}
	
	public static DocumentRoot getDocumentRoot(EObject object) {
		Resource resource = getResource(object);
		if (resource!=null) {
			EList<EObject> contents = resource.getContents();
			if (!contents.isEmpty() && contents.get(0) instanceof DocumentRoot)
				return (DocumentRoot)contents.get(0);
		}
		return null;
	}
	
	public static List<EObject> getAllReachableObjects(EObject object, EStructuralFeature feature) {
		ArrayList<EObject> list = null;
		if (object!=null && feature.getEType() instanceof EClass) {
			Resource resource = getResource(object);
			if (resource!=null) {
				EClass eClass = (EClass)feature.getEType();
				list = new ArrayList<EObject>();
				TreeIterator<EObject> contents = resource.getAllContents();
				while (contents.hasNext()) {
					Object item = contents.next();
					if (eClass.isInstance(item)) {
						list.add((EObject)item);
					}
				}
			}
		}
		return list;
	}
	
	public static List<EObject> getAllReachableObjects(EObject object, EClass eClass) {
		ArrayList<EObject> list = null;
		Resource resource = getResource(object);
		if (resource!=null) {
			list = new ArrayList<EObject>();
			TreeIterator<EObject> contents = resource.getAllContents();
			while (contents.hasNext()) {
				Object item = contents.next();
				if (eClass.isInstance(item)) {
					list.add((EObject)item);
				}
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllRootElements(Definitions definitions, final Class<T> class1) {
		ArrayList<T> list = new ArrayList<T>();
		for (RootElement re : definitions.getRootElements()) {
			if (class1.isInstance(re)) {
				list.add((T) re);
			}
		}
		return list;
	}
	
	public static boolean compare(Object v1, Object v2) {
		if (v1==null) {
			if (v2!=null)
				return false;
		}
		else if (v2==null) {
			if (v1!=null)
				return false;
		}
		return v1.equals(v2);
	}

	public static EObject findNearestAncestor(EObject object, Class[] types) {
		EObject ancestor = null;
		if (object!=null) {
			ancestor = object.eContainer();
			while (ancestor!=null) {
				Class type = ancestor.getClass();
				for (Class t : types) {
					if (t.isAssignableFrom(type))
						return ancestor;
				}
				ancestor = ancestor.eContainer();
			}
		}
		return ancestor;
	}
	
	public static List<EObject> collectAncestorObjects(EObject object, String featureName, Class[] ancestorTypes) {
		return collectAncestorObjects(object, featureName, ancestorTypes, null);
	}
	
	public static List<EObject> collectAncestorObjects(EObject object, String featureName, Class[] ancestorTypes, Class[] objectTypes) {
		List<EObject> values = new ArrayList<EObject>();
		EObject ancestor = ModelUtil.findNearestAncestor(object, ancestorTypes);
		while (ancestor!=null) {
			EStructuralFeature feature = ancestor.eClass().getEStructuralFeature(featureName);
			if (feature!=null && ancestor.eGet(feature) instanceof List) {
				List<EObject> objects = (List<EObject>) ancestor.eGet(feature);
				if (objectTypes==null) {
					values.addAll(objects);
				}
				else {
					for (EObject item : objects) {
						for (Class t : objectTypes) {
							if (t.isAssignableFrom(item.getClass()))
								values.add(item);
						}
					}
				}
			}
			ancestor = ModelUtil.findNearestAncestor(ancestor, ancestorTypes);
		}
		return values;
	}
	
	public static <T> List<T> getAllExtensionAttributeValues(EObject object, Class<T> clazz) {
		List<T> results = new ArrayList<T>();
		
		EStructuralFeature evf = object.eClass().getEStructuralFeature("extensionValues");
		EList<ExtensionAttributeValue> list = (EList<ExtensionAttributeValue>)object.eGet(evf);
		for (ExtensionAttributeValue eav : list) {
			FeatureMap fm = eav.getValue();
			for (Entry e : fm) {
				EStructuralFeature sf = e.getEStructuralFeature();
				if (clazz.isInstance(e.getValue())) {
					results.add((T)e.getValue());
				}
			}
		}
		return results;
	}
	
	public static List<ExtensionAttributeValue> getExtensionAttributeValues(EObject be) {
		if (be instanceof Participant) {
			final Participant participant = (Participant) be;
			if (participant.getProcessRef() == null) {
				if (participant.eContainer() instanceof Collaboration) {
					Collaboration collab = (Collaboration) participant.eContainer();
					if (collab.eContainer() instanceof Definitions) {
						final Definitions definitions = getDefinitions(collab);
						
						TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(definitions.eResource());
						
						domain.getCommandStack().execute(new RecordingCommand(domain) {
							@Override
							protected void doExecute() {
								Process process = Bpmn2ModelerFactory.create(Process.class);
								participant.setProcessRef(process);
								definitions.getRootElements().add(process);
								ModelUtil.setID(process);
							}
							
						});
						
					}
				}
			}
			return participant.getProcessRef().getExtensionValues();
		}
		if (be instanceof BPMNDiagram) {
			BPMNDiagram diagram = (BPMNDiagram) be;
			BaseElement bpmnElement = diagram.getPlane().getBpmnElement();
			if (bpmnElement instanceof org.eclipse.bpmn2.Process) {
				return bpmnElement.getExtensionValues();
			}
		}
		if (be instanceof BaseElement) {
			return ((BaseElement) be).getExtensionValues();
		}

		return new ArrayList<ExtensionAttributeValue>();
	}
	
	public static void addExtensionAttributeValue(EObject object, EStructuralFeature feature, EObject value) {
		EStructuralFeature evf = object.eClass().getEStructuralFeature("extensionValues");
		EList<EObject> list = (EList<EObject>)object.eGet(evf);
		
		if (list.size()==0) {
			ExtensionAttributeValue newItem = Bpmn2ModelerFactory.create(ExtensionAttributeValue.class);
			FeatureMap map = newItem.getValue();
			map.add(feature, value);
			list.add(newItem);
			ModelUtil.setID(newItem);
		}
		else {
			ExtensionAttributeValue oldItem = (ExtensionAttributeValue) list.get(0);
			FeatureMap map = oldItem.getValue();
			map.add(feature, value);
		}
	}

	/**
	 * Dummy objects are constructed when needed for an ExtendedPropertiesAdapter. The adapter factory
	 * (@see org.eclipse.bpmn2.modeler.ui.adapters.Bpmn2EditorItemProviderAdapterFactory) knows how to
	 * construct an ExtendedPropertiesAdapter from an EClass, however the adapter itself needs an EObject.
	 * This method constructs and caches these dummy objects as they are needed.
	 * 
	 * @param eclass
	 * @return
	 */
	public static EObject getDummyObject(EClass eclass) {
		EObject object = dummyObjects.get(eclass);
		if (object==null && eclass.eContainer() instanceof EPackage) {
	    	EPackage pkg = (EPackage)eclass.eContainer();
			object = pkg.getEFactoryInstance().create(eclass);
			dummyObjects.put(eclass, object);
		}
		return object;
	}

	/*
	 * Various model object and feature UI property methods
	 */
	public static String getLabel(Object object) {
		String label = "";
		if (object instanceof EObject) {
			EObject eObject = (EObject)object;
			ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(eObject, ExtendedPropertiesAdapter.class);
			if (adapter!=null)
				label = adapter.getObjectDescriptor().getLabel(eObject);
			else
				label = toDisplayName( eObject.eClass().getName() );
		}
		else
			label = object.toString();
		label = label.replaceAll(" Ref$", "");
		return label;
	}

	public static void setLabel(EObject object, EStructuralFeature feature, String label) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
		if (adapter!=null)
			adapter.getFeatureDescriptor(feature).setLabel(label);
	}

	public static String getLabel(EObject object, EStructuralFeature feature) {
		String label = "";
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
		if (adapter!=null)
			label = adapter.getFeatureDescriptor(feature).getLabel(object);
		else
			label = toDisplayName( feature.getName() );
		label = label.replaceAll(" Ref$", "");
		return label;
	}

	public static String getDisplayName(Object object) {
		if (object instanceof EObject) {
			EObject eObject = (EObject)object;
			ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(eObject, ExtendedPropertiesAdapter.class);
			if (adapter!=null)
				return adapter.getObjectDescriptor().getDisplayName(eObject);
			return getLongDisplayName(eObject);
		}
		return object==null ? null : object.toString();
	}

	public static String getDisplayName(EObject object, EStructuralFeature feature) {
		if (feature==null)
			return getDisplayName(object);
		
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
		if (adapter!=null)
			return adapter.getFeatureDescriptor(feature).getDisplayName(object);
		return getLongDisplayName(object, feature);
	}

	public static boolean getIsMultiLine(EObject object, EStructuralFeature feature) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
		if (adapter!=null)
			return adapter.getFeatureDescriptor(feature).isMultiLine(object);
		return false;
	}

	public static Hashtable<String, Object> getChoiceOfValues(EObject object, EStructuralFeature feature) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
		if (adapter!=null)
			return adapter.getFeatureDescriptor(feature).getChoiceOfValues(object);
		return null;
	}

	public static boolean setValue(TransactionalEditingDomain domain, final EObject object, final EStructuralFeature feature, final Object value) {
		ExtendedPropertiesAdapter adapter = AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
		Object oldValue = adapter==null ? object.eGet(feature) : adapter.getFeatureDescriptor(feature).getValue();
		boolean valueChanged = (value != oldValue);
		if (value!=null && oldValue!=null)
			valueChanged = !value.equals(oldValue);
		
		if (valueChanged) {
			try {
				if (value instanceof EObject) {
					// make sure the new object is added to its control first
					// so that it inherits the control's Resource and EditingDomain
					// before we try to change its value.
					InsertionAdapter.executeIfNeeded((EObject)value);
				}
				
				if (value==null){ // DO NOT use isEmpty() because this erases an object's anyAttribute feature!
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							object.eUnset(feature);
						}
					});
				}
				else if (adapter!=null) { 			// use the Extended Properties adapter if there is one
					adapter.getFeatureDescriptor(feature).setValue(value);
				}
				else {
					// fallback is to set the new value here using good ol' EObject.eSet()
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							if (object.eGet(feature) instanceof List) {
								((List)object.eGet(feature)).add(value);
							}
							else
								object.eSet(feature, value);
						}
					});
				}
			} catch (Exception e) {
				ErrorUtils.showErrorMessage(e.getMessage());
				return false;
			}
		}
		return true;
	}

	public static EObject createObject(Object object) {
		if (object instanceof EObject)
			return createObject(((EObject)object).eResource(),object);
		return createObject(null,object);
	}

	public static EObject createObject(Resource resource, Object object) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
		if (adapter!=null)
			return adapter.getObjectDescriptor().createObject(resource, object);
		return null;
	}

	public static EObject createFeature(EObject object, EStructuralFeature feature) {
		return createFeature(object, feature, null);
	}

	public static EObject createFeature(EObject object, EStructuralFeature feature, EClass eclass) {
		return createFeature(object.eResource(), object, feature, eclass);
	}

	public static EObject createFeature(Resource resource, EObject object, EStructuralFeature feature, EClass eclass) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
		if (adapter!=null)
			return adapter.getFeatureDescriptor(feature).createFeature(resource, object, eclass);
		return null;
	}

	public static boolean canEdit(EObject object, EStructuralFeature feature) {
		if (feature.getEType() instanceof EClass) {
			ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
			if (adapter!=null) {
				Object result = adapter.getProperty(feature, ExtendedPropertiesAdapter.UI_CAN_EDIT);
				if (result instanceof Boolean)
					return ((Boolean)result);
			}
			if (feature instanceof EReference) {
				if (((EReference)feature).isContainment())
					return true;
				if (Bpmn2Package.eINSTANCE.getRootElement().isSuperTypeOf((EClass)feature.getEType()))
					return true;
				return false;
			}
			return true;
		}
		return false;
	}

	public static boolean canCreateNew(EObject object, EStructuralFeature feature) {
		if (feature.getEType() instanceof EClass) {
			ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
			if (adapter!=null) {
				Object result = adapter.getProperty(feature, ExtendedPropertiesAdapter.UI_CAN_CREATE_NEW);
				if (result instanceof Boolean)
					return ((Boolean)result);
			}
			if (feature instanceof EReference) {
				if (((EReference)feature).isContainment())
					return true;
				if (Bpmn2Package.eINSTANCE.getRootElement().isSuperTypeOf((EClass)feature.getEType()))
					return true;
				return false;
			}
			return true;
		}
		return false;
	}

	public static boolean canEditInline(EObject object, EStructuralFeature feature) {
		if (feature.getEType() instanceof EClass) {
			ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
			if (adapter!=null) {
				Object result = adapter.getProperty(feature, ExtendedPropertiesAdapter.UI_CAN_EDIT_INLINE);
				if (result instanceof Boolean)
					return ((Boolean)result);
			}
		}
		return false;
	}

	public static boolean canSetNull(EObject object, EStructuralFeature feature) {
		if (feature.getEType() instanceof EClass) {
			ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
			if (adapter!=null) {
				Object result = adapter.getProperty(feature, ExtendedPropertiesAdapter.UI_CAN_SET_NULL);
				if (result instanceof Boolean)
					return ((Boolean)result);
			}
			return true;
		}
		return false;
	}

	public static boolean isMultiChoice(EObject object, EStructuralFeature feature) {
		if (feature.getEType() instanceof EClass)
		{
			ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
			if (adapter!=null) {
				Object result = adapter.getProperty(feature, ExtendedPropertiesAdapter.UI_IS_MULTI_CHOICE);
				if (result instanceof Boolean)
					return ((Boolean)result);
			}
		}
		return getChoiceOfValues(object,feature) != null;
	}

	/*
	 * Fallbacks in case a property provider does not exist
	 */
	public static String getLongDisplayName(EObject object) {
		String objName = null;
		if (object instanceof BPMNDiagram) {
			Bpmn2DiagramType type = getDiagramType((BPMNDiagram)object); 
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
			objName = toDisplayName( object.eClass().getName() );
		}
		EStructuralFeature feature = object.eClass().getEStructuralFeature("name");
		if (feature!=null) {
			String name = (String)object.eGet(feature);
			if (name==null || name.isEmpty())
				name = "Unnamed " + objName;
			else
				name = objName + " \"" + name + "\"";
			return name;
		}
		feature = object.eClass().getEStructuralFeature("id");
		if (feature!=null) {
			if (object.eGet(feature)!=null)
				objName = (String)object.eGet(feature);
		}
		feature = object.eClass().getEStructuralFeature("qName");
		if (feature!=null) {
			Object qName = object.eGet(feature);
			if (qName!=null) {
				return qName.toString();
			}
		}
		return objName;
	}

	public static String getLongDisplayName(EObject object, EStructuralFeature feature) {
		Object value = object.eGet(feature);
		if (value==null)
			return "";
		return value.toString();
	}

	public static boolean isEmpty(Object result) {
		if (result == null)
			return true;
		if (result instanceof String)
			return ((String) result).isEmpty();
		return false;
	}

	public static void disposeChildWidgets(Composite parent) {
		Control[] kids = parent.getChildren();
		for (Control k : kids) {
			if (k instanceof Composite) {
				disposeChildWidgets((Composite)k);
			}
			k.dispose();
		}
	}

	/**
	 * Ugly hack to force layout of the entire widget tree of the property sheet page.
	 * @param parent
	 */
	public static void recursivelayout(Composite parent) {
		Control[] kids = parent.getChildren();
		for (Control k : kids) {
			if (k.isDisposed())
				Activator.logError(new SWTException("Widget is disposed."));
			if (k instanceof Composite) {
				recursivelayout((Composite)k);
				((Composite)k).layout(true);
			}
		}
		parent.layout(true);
	}

	public static DiagramEditor getEditor(EObject object) {
		Resource resource = InsertionAdapter.getResource(object);
		if(resource!=null)
			return getEditor(resource.getResourceSet());
		return null;
	}
	
	public static DiagramEditor getEditor(ResourceSet resourceSet) {
	    Iterator<Adapter> it = resourceSet.eAdapters().iterator();
	    while (it.hasNext()) {
	        Object next = it.next();
	        if (next instanceof DiagramEditorAdapter) {
	            return ((DiagramEditorAdapter)next).getDiagramEditor();
	        }
	    }
	    return null;
	}
}
