package org.camunda.bpm.modeler.ui.property.tabs.binding;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.widgets.Button;

/**
 * Binding which looks up the model value as an emf attribute.
 * 
 * @author nico.rehwaldt
 *
 * @param <V>
 */
public abstract class ModelAttributeButtonBinding<V> extends ModelButtonBinding<V> {

	public ModelAttributeButtonBinding(EObject model, EStructuralFeature feature, Button control) {
		super(model, feature, control);
	}

	/**
	 * Retrieves the model value from an attribute
	 * 
	 * @return the view value
	 */
	@Override
	public V getModelValue() {
		return (V) model.eGet(feature);
	}

	/**
	 * Sets the model value to the specified argument
	 * 
	 * @param value the value to update the model with
	 */
	public void setModelValue(V value)  {
		TransactionalEditingDomain domain = getTransactionalEditingDomain();
		ModelUtil.setValue(domain, model, feature, value);
	}
}
