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
package org.camunda.bpm.modeler.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

public class ModelUtil {

	// TODO: need to determine whether IDs need to be unique within a Resource or
	// ResourceSet - see getKey()

	// Map of EMF resource sets to ID mapping tables. The ID mapping tables map a
	// BPMN2 element ID string to the EObject.
	// The EObject is not used anywhere (yet!) just a placeholder to allow use of
	// a HashMap for fast lookups of the ID string.
	// The ID strings are composed from the BPMN2 element description name and a
	// sequence number (starting at 1).
	// When a new ID is requested, generateID() simply increments the sequence
	// number until an ID is found that isn't
	// already in the table.
	public static HashMap<Object, Hashtable<String, EObject>> ids = new HashMap<Object, Hashtable<String, EObject>>();
	
	// Map of ID strings and sequential counters for each BPMN2 element
	// description.
	public static HashMap<String, Integer> defaultIds = new HashMap<String, Integer>();

	/**
	 * Clear the IDs hashmap for the given EMF Resource. This should be called
	 * when the editor is disposed to avoid unnecessary growth of the IDs table.
	 * 
	 * @param res
	 *          - the EMF Resource that was used to generate the ID strings.
	 */
	public static void clearIDs(Resource res, boolean all) {
		ids.remove(getKey(res));
		if (all) {
			defaultIds.clear();
		}
	}

	/**
	 * Construct the first part of the ID string using the BPMN2 element
	 * description name. If the object is a DI element, concatenate the BPMN2
	 * element description name.
	 * 
	 * @param obj
	 *          - the BPMN2 object
	 * @return name string
	 */
	public static String getObjectName(EObject obj) {
		String name;
		EStructuralFeature feature = ((EObject) obj).eClass()
				.getEStructuralFeature("bpmnElement");
		if (feature != null && obj.eGet(feature) != null) {
			EObject bpmnElement = (EObject) obj.eGet(feature);
			name = obj.eClass().getName() + "_" + bpmnElement.eClass().getName();
		} else {
			name = obj.eClass().getName();
		}
		return name;
	}

	private static Object getKey(EObject obj) {
		Resource resource = getResource(obj);
		if (resource == null) {
			return null;
		}
		assert (obj != null);
		return getKey(resource);
	}

	private static Object getKey(Resource res) {
		assert (res != null);
		return res.getResourceSet();
	}

	/**
	 * If an EObject has not yet been added to a Resource (e.g. during
	 * construction) generate an ID string using a different strategy (basically
	 * same ID prefixed with an underscore). The "defaultIds" table is used to
	 * track the next sequential ID value for a given element description.
	 * 
	 * @param obj
	 *          - the BPMN2 object
	 * @return the ID string
	 */
	private static String generateDefaultID(EObject obj, String name) {
		if (name == null)
			name = getObjectName(obj);
		Integer value = defaultIds.get(name);
		if (value == null)
			value = Integer.valueOf(1);
		value = Integer.valueOf(value.intValue() + 1);
		defaultIds.put(name, Integer.valueOf(value));

		return "_" + name + "_" + value;
	}

	/**
	 * Generate an ID string for a given BPMN2 object that will (eventually!) be
	 * added to the given Resource.
	 * 
	 * CAUTION: IDs for objects that have already been deleted WILL be reused.
	 * 
	 * @param obj
	 *          - the BPMN2 object
	 * @param res
	 *          - the Resource to which the object will be added
	 * @return the ID string
	 */
	private static String generateID(EObject obj, Resource res) {
		return generateID(obj, res, null);
	}

	public static String generateID(EObject obj, Resource res, String name) {
		Object key = (res == null ? getKey(obj) : getKey(res));
		if (key != null) {
			Hashtable<String, EObject> tab = ids.get(key);
			if (tab == null) {
				tab = new Hashtable<String, EObject>();
				ids.put(key, tab);
			}

			String id = name;
			if (name == null) {
				name = getObjectName(obj);
				id = name + "_" + 1;
			}

			for (int i = 1;; ++i) {
				if (tab.get(id) == null) {
					tab.put(id, obj);
					return id;
				}
				id = name + "_" + i;
			}
		}
		return generateDefaultID(obj, name);
	}

	/**
	 * Add an ID string to the ID mapping table(s). This must be used during model
	 * import to add existing BPMN2 element IDs to the table so we don't generate
	 * duplicates.
	 * 
	 * @param obj the BPMN2 object
	 */
	public static void addID(EObject obj) {
		EStructuralFeature feature = ((EObject) obj).eClass().getEStructuralFeature("id");
		if (feature != null) {
			Object value = obj.eGet(feature);
			if (value != null) {
				addID(obj, (String) value);
			}
			// what to do here if the BPMN2 element has an "id" attribute which is not set?
			// should we generate one and set it?
			// No, because we dont want to add IDs on elements like FormalExpression
			// or other elements which have a ID feature, but do not have ids by default
		}

	}

	/**
	 * Add an ID string to the ID mapping table(s). This must be used during model
	 * import to add existing BPMN2 element IDs to the table so we don't generate
	 * duplicates.
	 * 
	 * @param obj
	 *          - the BPMN2 object
	 * @param id
	 *          - the object's ID string
	 */
	public static void addID(EObject obj, String id) {
		Object key = getKey(obj);
		String name = getObjectName(obj);
		if (key == null || id.startsWith("_" + name + "_")) {
			int newValue = 0;
			try {
				int i = id.lastIndexOf('_') + 1;
				if (i < id.length())
					newValue = Integer.parseInt(id.substring(i));
			} catch (Exception e) {
			}
			Integer oldValue = defaultIds.get(name);
			if (oldValue == null || newValue > oldValue.intValue())
				defaultIds.put(name, Integer.valueOf(newValue));
		} else {
			Hashtable<String, EObject> tab = ids.get(key);
			if (tab == null) {
				tab = new Hashtable<String, EObject>();
				ids.put(key, tab);
			}
			tab.put(id, obj);
		}
	}

	/**
	 * Generate a unique ID for the given BPMN2 element and set it. This should
	 * only be used during object construction AFTER an object has already been
	 * added to a Resource.
	 * 
	 * @param obj
	 *          - the BPMN2 object
	 */
	public static String setID(EObject obj) {
		return setID(obj, getResource(obj));
	}

	/**
	 * Generate a unique ID for the given BPMN2 element and set it. This should be
	 * used during object construction if the object has NOT YET been added to a
	 * Resource.
	 * 
	 * @param obj
	 *          - the BPMN2 object
	 * @param res
	 *          - the Resource to which the object will be added
	 */
	public static String setID(EObject obj, Resource res) {
		String id = null;
		EStructuralFeature feature = ((EObject) obj).eClass()
				.getEStructuralFeature("id");
		if (feature != null) {
			if (obj.eGet(feature) == null) {
				id = generateID(obj, res);
				obj.eSet(feature, id);
			} else {
				id = (String) obj.eGet(feature);
			}
		}
		return id;
	}

	/**
	 * Sets the feature with the name "id" to the given value
	 * 
	 * @param obj
	 *          the object with the id
	 * @param value
	 *          the new id value
	 * @return the id value, if the id feature was set
	 * 
	 * @throws IllegalArgumentException
	 *           if the object does not have a id feature
	 */
	public static String setID(EObject obj, String value) {
		EStructuralFeature feature = ((EObject) obj).eClass()
				.getEStructuralFeature("id");
		if (feature != null) {
			obj.eSet(feature, value);
			addID(obj, value);
			return value;
		} else {
			throw new IllegalArgumentException("Object does not have a id feature : "
					+ obj);
		}
	}

	public static String getFeature(EObject obj, String attribute) {
		EStructuralFeature feature = ((EObject) obj).eClass()
				.getEStructuralFeature(attribute);
		if (feature != null) {
			return (String) obj.eGet(feature);
		} else {
			return null;
		}
	}

	public static int getIDNumber(String id) {
		try {
			int i = id.lastIndexOf("_");
			return Integer.parseInt(id.substring(i + 1));
		} catch (Exception e) {
			return -1;
		}
	}

	public static String getName(BaseElement element) {
		if (element != null) {
			EStructuralFeature feature = element.eClass().getEStructuralFeature(
					"name");
			if (feature != null && element.eGet(feature) instanceof String)
				return (String) element.eGet(feature);
		}
		return null;
	}

	public static boolean hasName(EObject obj) {
		EStructuralFeature feature = obj.eClass().getEStructuralFeature("name");
		return feature != null;
	}

	public static String toDisplayName(String anyName) {
		// get rid of the "Impl" java suffix
		anyName = anyName.replaceAll("Impl$", "");
		String displayName = "";
		boolean first = true;
		char[] chars = anyName.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			char c = chars[i];
			if (Character.isUpperCase(c)) {
				if (displayName.length() > 0 && i + 1 < chars.length
						&& !Character.isUpperCase(chars[i + 1]))
					displayName += " ";
			}
			if (first) {
				c = Character.toUpperCase(c);
				first = false;
			}
			if (c == '_')
				c = ' ';
			displayName += c;
		}
		return displayName.trim();
	}

	@SuppressWarnings("unchecked")
	public static List<EventDefinition> getEventDefinitions(Event event) {
		if (event != null) {
			EStructuralFeature feature = event.eClass().getEStructuralFeature(
					"eventDefinitions");
			if (feature != null) {
				return (List<EventDefinition>) event.eGet(feature);
			}
		}
		return new ArrayList<EventDefinition>();
	}

	/**
	 * Get the first event definition for an given event and given type
	 * 
	 * @param event
	 *          the event
	 * @param clazz
	 *          the event definition class
	 * @return the first event definition definied for this event instance
	 */
	public static EventDefinition getEventDefinition(Event event, Class<?> clazz) {
		for (EventDefinition def : getEventDefinitions(event)) {
			if (clazz.isInstance(def)) {
				return def;
			}
		}
		return null;
	}

	public enum Bpmn2DiagramType {
		NONE, PROCESS, CHOREOGRAPHY, COLLABORATION;

		/**
		 * return enum label with first letter uppercase
		 * 
		 * @param type
		 * @return
		 */
		public String getLabel() {
			String label = this.name().toLowerCase();
			return label.substring(0, 1).toUpperCase() + label.substring(1);
		}
	}

	public static Bpmn2DiagramType getDiagramType(String name) {
		for (Bpmn2DiagramType t : Bpmn2DiagramType.values()) {
			if (t.toString().equalsIgnoreCase(name))
				return t;
		}
		return Bpmn2DiagramType.NONE;
	}

	public static Bpmn2DiagramType getDiagramType(EObject object) {
		if (object != null && getResource(object) != null) {
			Definitions defs = getDefinitions(object);
			if (defs.getDiagrams().size() >= 1) {
				BPMNDiagram diagram = defs.getDiagrams().get(0);
				BPMNPlane plane = diagram.getPlane();
				if (plane != null) {
					BaseElement be = plane.getBpmnElement();
					if (be instanceof Process) {
						for (RootElement re : defs.getRootElements()) {
							if (re instanceof Choreography) {
								for (Participant p : ((Choreography) re).getParticipants()) {
									if (p.getProcessRef() == be)
										return Bpmn2DiagramType.CHOREOGRAPHY;
								}
							} else if (re instanceof Collaboration) {
								for (Participant p : ((Collaboration) re).getParticipants()) {
									if (p.getProcessRef() == be)
										return Bpmn2DiagramType.COLLABORATION;
								}
							}
						}
						return Bpmn2DiagramType.PROCESS;
					} else if (be instanceof Choreography)
						return Bpmn2DiagramType.CHOREOGRAPHY;
					else if (be instanceof Collaboration)
						return Bpmn2DiagramType.COLLABORATION;
				}
			}
		}
		return Bpmn2DiagramType.NONE;
	}

	public static String getDiagramTypeName(BPMNDiagram object) {
		Bpmn2DiagramType type = getDiagramType((BPMNDiagram) object);
		if (type == Bpmn2DiagramType.CHOREOGRAPHY) {
			return "Choreography Diagram";
		} else if (type == Bpmn2DiagramType.COLLABORATION) {
			return "Collaboration Diagram";
		} else if (type == Bpmn2DiagramType.PROCESS) {
			return "Process Diagram";
		}
		return "Unknown Diagram Type";
	}

	public static EAttribute createDynamicAttribute(EPackage pkg, EObject object,
			String name, String type) {
		EClass docRoot = ExtendedMetaData.INSTANCE.getDocumentRoot(pkg);
		for (EStructuralFeature f : docRoot.getEStructuralFeatures()) {
			if (f.getName().equals(name)) {
				if (f instanceof EAttribute)
					return (EAttribute) f;
				return null;
			}
		}
		if (type == null)
			type = "EString";

		EDataType eDataType = (EDataType) EcorePackage.eINSTANCE
				.getEClassifier(type);
		EAttribute attr = EcorePackage.eINSTANCE.getEcoreFactory()
				.createEAttribute();
		attr.setName(name);
		attr.setEType(eDataType);
		ExtendedMetaData.INSTANCE.setFeatureKind(attr,
				ExtendedMetaData.ATTRIBUTE_FEATURE);

		docRoot.getEStructuralFeatures().add(attr);
		ExtendedMetaData.INSTANCE.setNamespace(attr, pkg.getNsURI());

		return attr;
	}

	public static EObject createStringWrapper(String value) {
		DynamicEObjectImpl de = new DynamicEObjectImpl();
		de.eSetClass(EcorePackage.eINSTANCE.getEObject());
		de.eSetProxyURI(URI.createURI(value));
		return de;
	}

	public static String getStringWrapperValue(Object wrapper) {
		if (wrapper instanceof DynamicEObjectImpl) {
			DynamicEObjectImpl de = (DynamicEObjectImpl) wrapper;
			URI uri = de.eProxyURI();
			return uri.toString();
		} else if (wrapper instanceof EObject) {
			return EcoreUtil.getURI((EObject) wrapper).toString();
		}
		return null;
	}

	public static Resource getResource(EObject object) {
		return object.eResource();
	}

	public static Definitions getDefinitions(EObject object) {
		Resource resource = getResource(object);
		if (resource != null) {
			Object defs = resource.getContents().get(0).eContents().get(0);
			if (defs instanceof Definitions)
				return (Definitions) defs;
		}
		return null;
	}

	public static List<EObject> getAllReachableObjects(EObject object,
			EStructuralFeature feature) {
		ArrayList<EObject> list = null;
		if (object != null && feature.getEType() instanceof EClass) {
			Resource resource = getResource(object);
			if (resource != null) {
				EClass eClass = (EClass) feature.getEType();
				list = new ArrayList<EObject>();
				TreeIterator<EObject> contents = resource.getAllContents();
				while (contents.hasNext()) {
					Object item = contents.next();
					if (eClass.isInstance(item)) {
						list.add((EObject) item);
					}
				}
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllReachableObjects(EObject be, Class<T> cls) {
		List<T> result = new ArrayList<T>();

		if (cls.isInstance(be)) {
			result.add((T) be);
		}

		if (!(be instanceof FlowElementsContainer)) {
			return result;
		}

		FlowElementsContainer container = (FlowElementsContainer) be;
		for (FlowElement elem : container.getFlowElements()) {

			List<T> reachableElements = getAllReachableObjects(elem, cls);
			if (reachableElements != null && !reachableElements.isEmpty()) {
				result.addAll(reachableElements);
			}

		}

		return result;
	}

	/**
	 * Collect all {@link EObject}, where each element of them is a instance of
	 * the passed cls, from the assigned {@link EObject} be, and his immediate
	 * parent and transitive parents.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllReachableObjectsIncludingParents(EObject be, Class<T> cls) {
		List<T> result = new ArrayList<T>();

		if (be instanceof Participant) {
			Participant participant = (Participant) be;
			be = participant.getProcessRef();
		}

		if (be == null) {
			return result;
		}

		EObject parent = be.eContainer();
		if (parent != null) {
			List<T> elements = getAllReachableObjectsIncludingParents(parent, cls);
			if (elements != null && !elements.isEmpty()) {
				result.addAll(elements);
			}
		}

		if (!(be instanceof FlowElementsContainer)) {
			return result;
		}

		FlowElementsContainer container = (FlowElementsContainer) be;

		for (FlowElement element : container.getFlowElements()) {
			if (cls.isInstance(element)) {
				result.add((T) element);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllRootElements(Definitions definitions, final Class<T> cls) {
		ArrayList<T> list = new ArrayList<T>();
		for (RootElement re : definitions.getRootElements()) {
			if (cls.isInstance(re)) {
				list.add((T) re);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllFlowElements(Process process, final Class<T> cls) {
		ArrayList<T> list = new ArrayList<T>();
		for (FlowElement fe : process.getFlowElements()) {
			if (cls.isInstance(fe)) {
				list.add((T) fe);
			}
		}
		return list;
	}

	/**
	 * returns the {@link FlowElementsContainer} of the given baseElement
	 * 
	 * @param baseElement
	 * @return
	 */
	public static FlowElementsContainer getFlowElementsContainer(EObject baseElement) {

		while ((baseElement = baseElement.eContainer()) != null) {
			if (baseElement instanceof FlowElementsContainer) {
				return (FlowElementsContainer) baseElement;
			}
		}

		return null;
	}

	public static boolean compare(Object v1, Object v2) {
		if (v1 == null) {
			if (v2 != null)
				return false;
		} else if (v2 == null) {
			if (v1 != null)
				return false;
		}
		return v1.equals(v2);
	}

	/*
	 * Various model object and feature UI property methods
	 */
	public static String getLabel(Object object) {
		String label = "";
		
		if (object instanceof EObject) {
			EObject eObject = (EObject) object;
			label = toDisplayName(eObject.eClass().getName());
		} else {
			label = object.toString();
		}
		label = label.replaceAll(" Ref$", "");
		return label;
	}

	public static String getDisplayName(Object object) {
		if (object instanceof EObject) {
			EObject eObject = (EObject) object;
			return getLongDisplayName(eObject);
		}
		
		return object == null ? null : object.toString();
	}

	public static String getDisplayName(EObject object, EStructuralFeature feature) {
		if (feature == null) {
			return getDisplayName(object);
		}

		return getLongDisplayName(object, feature);
	}

	public static void setValue(TransactionalEditingDomain domain, EObject object, EStructuralFeature feature, Object value) {
		domain.getCommandStack().execute(getUpdateCommand(domain, object, feature, value));
	}

	public static RecordingCommand getUpdateCommand(
			final TransactionalEditingDomain domain, final EObject object,
			final EStructuralFeature feature, final Object value) {
		
		return new RecordingCommand(domain) {
			
			@Override
			protected void doExecute() {
				Object oldValue = object.eGet(feature);
				boolean changed = (value != null && value.equals(oldValue)) || value != oldValue;
				
				if (changed) {
					if (value == null) {
						object.eUnset(feature);
					} else {
						if (object.eGet(feature) instanceof List) {
							((List) object.eGet(feature)).add(value);
						} else {
							object.eSet(feature, value);
						}
					}
				}
			}
		};
	}

	public static EObject createObject(Resource resource, Object object) {
		return null;
	}

	/*
	 * Fallbacks in case a property provider does not exist
	 */
	public static String getLongDisplayName(EObject object) {
		String objName = null;
		if (object instanceof BPMNDiagram) {
			Bpmn2DiagramType type = getDiagramType((BPMNDiagram) object);
			if (type == Bpmn2DiagramType.CHOREOGRAPHY) {
				objName = "Choreography Diagram";
			} else if (type == Bpmn2DiagramType.COLLABORATION) {
				objName = "Collaboration Diagram";
			} else if (type == Bpmn2DiagramType.PROCESS) {
				objName = "Process Diagram";
			}
		}
		if (objName == null) {
			objName = toDisplayName(object.eClass().getName());
		}
		EStructuralFeature feature = object.eClass().getEStructuralFeature("name");
		if (feature != null) {
			String name = (String) object.eGet(feature);
			if (name == null || name.isEmpty())
				name = "Unnamed " + objName;
			else
				name = objName + " \"" + name + "\"";
			return name;
		}
		feature = object.eClass().getEStructuralFeature("id");
		if (feature != null) {
			if (object.eGet(feature) != null)
				objName = (String) object.eGet(feature);
		}
		feature = object.eClass().getEStructuralFeature("qName");
		if (feature != null) {
			Object qName = object.eGet(feature);
			if (qName != null) {
				return qName.toString();
			}
		}
		return objName;
	}

	public static String getLongDisplayName(EObject object,
			EStructuralFeature feature) {
		Object value = object.eGet(feature);
		if (value == null)
			return "";
		return value.toString();
	}

	public static DiagramEditor getEditor(EObject object) {
		Resource resource = getResource(object);
		if (resource != null) {
			return getEditor(resource.getResourceSet());
		}
		
		return null;
	}

	public static DiagramEditor getEditor(ResourceSet resourceSet) {
		Iterator<Adapter> it = resourceSet.eAdapters().iterator();
		while (it.hasNext()) {
			Object next = it.next();
			if (next instanceof DiagramEditorAdapter) {
				return ((DiagramEditorAdapter) next).getDiagramEditor();
			}
		}
		return null;
	}

	/**
	 * Makes cosmetic changes to names to make them easier visualizable in the
	 * user interface
	 * 
	 * @param displayName
	 * @return
	 */
	public static String beautifyName(String displayName) {
		// strip \n from display names
		return displayName.replaceAll("\n", " ").replaceAll("[\\s]+", " ");
	}
}
