package org.camunda.bpm.modeler.ui.property.tabs.binding;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.custom.CCombo;

/**
 * Maps the id of a base element to a combo box.
 * 
 * @author nico.rehwaldt
 */
public abstract class BaseElementIdComboBinding<T extends BaseElement> extends ModelAttributeComboBinding<T> {

	public BaseElementIdComboBinding(EObject model, EStructuralFeature feature, CCombo control) {
		super(model, feature, control);
	}

	@Override
	protected String toString(T value) {
		if (value == null) {
			return "";
		} else {
			return value.getId();
		}
	}

	@Override
	protected T fromString(String id) {
		if (id == null || id.trim().isEmpty()) {
			return null;
		}
		
		return getModelById(id);
	}

	/**
	 * Returns the element by id
	 * 
	 * @param id
	 * @return
	 */
	protected abstract T getModelById(String id);

	/**
	 * Retrieves the model value from an attribute
	 * 
	 * @return the view value
	 */
	@Override
	public T getModelValue() {
		return (T) model.eGet(feature);
	}

	/**
	 * Sets the model value to the specified argument
	 * 
	 * @param value the value to update the model with
	 */
	public void setModelValue(T value)  {
		TransactionalEditingDomain domain = getTransactionalEditingDomain();
		ModelUtil.setValue(domain, model, feature, value);
	}
}