package org.eclipse.bpmn2.modeler.core.test.assertions;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.fest.assertions.core.Condition;

public class LinkedObjectCondition extends Condition<EObject> {

	protected Class<?> cls;
	protected String id;
	
	public LinkedObjectCondition identifiedBy(String id) {
		this.id = id;
		
		return this;
	}
	
	public LinkedObjectCondition ofType(Class<?> cls) {
		this.cls = cls;
		
		return this;
	}

	@Override
	public boolean matches(EObject value) {
		if (id != null) {
			if (value instanceof BaseElement) {
				BaseElement element = (BaseElement) value;
				if (!id.equals(element.getId())) {
					return false;
				}
			} else {
				return false;
			}
		}
		
		if (cls != null) {
			return cls.isInstance(value);
		} else {
			return true;
		}
	}
	
	public static LinkedObjectCondition elementOfType(Class<?> cls) {
		return new LinkedObjectCondition().ofType(cls);
	}
	
	public static LinkedObjectCondition elementIdentifiedBy(String id) {
		return new LinkedObjectCondition().identifiedBy(id);
	}
}
