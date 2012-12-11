package org.eclipse.bpmn2.modeler.runtime.activiti.util;

import java.util.List;

import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public class AttributeUtil {

	public static boolean attributeExits(EObject object, EAttribute attribute) {
		return object.eIsSet(attribute);
	}
	
	public static Object resolveAttributeValue(EObject object, EAttribute attribute) {
		Object attributeValue = null;
		if (attributeExits(object, attribute)) {
			attributeValue = object.eGet(attribute);
		}
		
		return attributeValue;
	}
	
	/**
	 * Clears the extension elements for a given business object 
	 * if no extensions are specified
	 * 
	 * @param object
	 */
	public static void clearEmptyExtensionElements(final EObject object) {
		List<ExtensionAttributeValue> extensionElements = ModelUtil.getExtensionAttributeValues(object);
		if (extensionElements != null && !extensionElements.isEmpty()) {
			if (extensionElements.get(0).getValue().isEmpty()) {
				extensionElements.clear();
			}
		}
	}
}