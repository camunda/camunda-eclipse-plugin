package org.camunda.bpm.modeler.test.util.assertions;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.fest.assertions.core.Condition;
import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;

public class LinkedObjectCondition extends Condition<EObject> {

	protected Class<?> cls;
	protected String id;
	private EObject identity;
	
	public LinkedObjectCondition identifiedBy(String id) {
		this.id = id;
		
		return this;
	}
	
	public LinkedObjectCondition ofType(Class<?> cls) {
		this.cls = cls;
		
		return this;
	}

	public LinkedObjectCondition withIdentity(EObject element) {
		this.identity = element;
		return this;
	}
	
	protected Description createDescription() {
		String description = "";
		
		if (identity != null) {
			description += "with identity " + identity + " ";
		}
		
		if (cls != null) {
			description += "of type " + cls.getName() + " ";
		}
		
		if (id != null) {
			description += "with id " + id;
		}
		
		return new TextDescription(description);
	}
	
	@Override
	public boolean matches(EObject value) {
		describedAs(createDescription());
		
		if (identity != null) {
			if (!identity.equals(value)) {
				return false;
			}
		}
		
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

	public static LinkedObjectCondition element(EObject element) {
		return new LinkedObjectCondition().withIdentity(element);
	}
}
