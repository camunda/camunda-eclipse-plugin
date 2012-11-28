package org.eclipse.bpmn2.modeler.runtime.activiti.util;

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
	
}
