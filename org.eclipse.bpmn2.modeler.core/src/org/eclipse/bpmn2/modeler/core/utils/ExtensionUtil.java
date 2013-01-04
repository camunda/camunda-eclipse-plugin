package org.eclipse.bpmn2.modeler.core.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.model.Bpmn2ModelerFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;

/**
 * Handles extensions of {@link BaseElement}s.
 * 
 * @author nico.rehwaldt
 */
public class ExtensionUtil {

	/**
	 * Given an object, the feature and the feature attribute name, return the
	 * feature attributes value if the the attribute exists.
	 * 
	 * @param object
	 * @param feature
	 * @param attributeName
	 * 
	 * @return
	 */
	public static Object getExtension(EObject object, EStructuralFeature feature, String attributeName) {
		
		String featureName = feature.getName();
		
		ExtensionAttributeValue extensionAttributeValue = getExtensionAttributeValue(object);
		if (extensionAttributeValue != null) {
			FeatureMap featureMap = extensionAttributeValue.getValue();
			Object featureObject = featureMap.get(feature, true);
			
			if (featureObject instanceof FeatureMapUtil.FeatureEList<?>) {
				FeatureMapUtil.FeatureEList<?> featureList = (FeatureMapUtil.FeatureEList<?>) featureObject;
				if (featureList.size() == 1) {
					EObject eFeatureObject = (EObject) featureList.get(0);
					EList<EStructuralFeature> features = eFeatureObject.eClass().getEAllStructuralFeatures();
					for (EStructuralFeature f: features) {
						if (attributeName.equals(f.getName())) {
							return eFeatureObject.eGet(f);
						}
					}
				}
			}
			
			Iterator<FeatureMap.Entry> iterator = featureMap.iterator();
			while (iterator.hasNext()) {
				FeatureMap.Entry entry = iterator.next();
				if (featureName.equals(entry.getEStructuralFeature().getName())) {
					Object value = entry.getValue();
					if (value instanceof AnyType) {
						AnyType anyType = (AnyType) value;
						FeatureMap.Entry anyTypeEntry = anyType.getMixed().get(0);
						if (attributeName.equals(anyTypeEntry.getEStructuralFeature().getName())) {
							return anyTypeEntry.getValue();
						}
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the extensions of a given type from 
	 * the specified object
	 * 
	 * @param object
	 * @param cls
	 * 
	 * @return
	 */
	public static <T> List<T> getExtensions(EObject object, Class<T> cls) {
		List<T> results = new ArrayList<T>();
		
		EStructuralFeature extensionValuesFeature = object.eClass().getEStructuralFeature("extensionValues");
		EList<ExtensionAttributeValue> list = (EList<ExtensionAttributeValue>) object.eGet(extensionValuesFeature);
		
		for (ExtensionAttributeValue value : list) {
			FeatureMap featureMap = value.getValue();
			for (Entry e : featureMap) {
				Object valueObject = e.getValue();
				if (cls.isInstance(valueObject)) {
					results.add((T) valueObject);
				}
			}
		}
		
		return results;
	}
	
	/**
	 * Adds the extension value at the given feature to the object.
	 * Permits multiple extensions of the same type.
	 * 
	 * @param object
	 * @param feature
	 * @param value
	 */
	public static void addExtension(EObject object, EStructuralFeature feature, EObject value) {
		EStructuralFeature extensionValuesFeature = object.eClass().getEStructuralFeature("extensionValues");
		EList<EObject> list = (EList<EObject>) object.eGet(extensionValuesFeature);
		
		ExtensionAttributeValue extensionAttributeValue = null;
		
		if (list.isEmpty()) {
			extensionAttributeValue = createExtensionAttributeValue();
			list.add(extensionAttributeValue);
			
			ModelUtil.setID(extensionAttributeValue);
		} else {
			extensionAttributeValue = (ExtensionAttributeValue) list.get(0);
		}
		
		FeatureMap map = extensionAttributeValue.getValue();
		map.add(feature, value);
	}
	
	/**
	 * Removes the extension with the given feature from the object
	 * 
	 * @param object
	 * @param feature
	 */
	public static void removeExtensionByFeature(EObject object, EStructuralFeature feature) {
		
		String featureName = feature.getName();
		
		ExtensionAttributeValue extensions = getExtensionAttributeValue(object);
		if (extensions != null) {
			Iterator<Entry> iterator = extensions.getValue().iterator();
			while (iterator.hasNext()) {
				EStructuralFeature f = iterator.next().getEStructuralFeature();
				if (f.getName().equals(featureName)) {
					iterator.remove();
					break;
				}
			}
		}
	}
	
	/**
	 * Removes the extension with the given value from the object
	 * 
	 * @param object
	 * @param extensionValue
	 */
	public static void removeExtensionByValue(EObject object, EObject extensionValue) {

		EStructuralFeature extensionValuesFeature = object.eClass().getEStructuralFeature("extensionValues");
		EList<ExtensionAttributeValue> extensionAttributesList = (EList<ExtensionAttributeValue>) object.eGet(extensionValuesFeature);
		
		for (ExtensionAttributeValue extensionAttributes : extensionAttributesList) {
			FeatureMap featureMap = extensionAttributes.getValue();
			ListIterator<Entry> iterator = featureMap.listIterator();
			while (iterator.hasNext()) {
				Entry e = iterator.next();
				if (extensionValue.equals(e.getValue())) {
					iterator.remove();
				}
			}
		}
	}
	
	/**
	 * This method is evil as it may open a transaction (!!!)
	 * @param object
	 * @return
	 */
	public static List<ExtensionAttributeValue> getExtensionAttributeValues(EObject object) {
		
		if (object instanceof BPMNDiagram) {
			BPMNDiagram diagram = (BPMNDiagram) object;
			BaseElement bpmnElement = diagram.getPlane().getBpmnElement();
			
			if (bpmnElement instanceof org.eclipse.bpmn2.Process) {
				return bpmnElement.getExtensionValues();
			}
		} else 
		
		if (object instanceof BaseElement) {
			return ((BaseElement) object).getExtensionValues();
		} else
		
		if (object instanceof Participant) {
			final Participant participant = (Participant) object;
			if (participant.getProcessRef() == null) {
				if (participant.eContainer() instanceof Collaboration) {
					Collaboration collab = (Collaboration) participant.eContainer();
					if (collab.eContainer() instanceof Definitions) {
						final Definitions definitions = ModelUtil.getDefinitions(collab);
						
						TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(object);
						
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

		return new ArrayList<ExtensionAttributeValue>();
	}

	/**
	 * Return the extension attribute value for a given object 
	 * in which to store extensions.
	 * 
	 * @param object
	 * @return
	 */
	public static ExtensionAttributeValue getExtensionAttributeValue(EObject object) {

		EStructuralFeature extensionValuesFeature = object.eClass().getEStructuralFeature("extensionValues");
		EList<ExtensionAttributeValue> extensionAttributeValues = (EList<ExtensionAttributeValue>) object.eGet(extensionValuesFeature);
		
		if (extensionAttributeValues != null && extensionAttributeValues.size() == 1) {
			return extensionAttributeValues.get(0);
		}

		return null;
	}

	public static void updateExtension(EObject object, EStructuralFeature feature, EObject value) {

		List<ExtensionAttributeValue> extensionElements = getExtensionAttributeValues(object);
		
		if (extensionElements != null) {
			if (extensionElements.isEmpty()) {
				ExtensionAttributeValue extensionAttributeValue = createExtensionAttributeValue();
				extensionElements.add(extensionAttributeValue);

				ModelUtil.setID(extensionAttributeValue);
			} else {
				removeExtensionByFeature(object, feature);
			}
			
			extensionElements.get(0).getValue().add(feature, value);
		}
	}
	
	protected static ExtensionAttributeValue createExtensionAttributeValue() {
		return Bpmn2Factory.eINSTANCE.createExtensionAttributeValue();
	}
}
