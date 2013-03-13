package org.camunda.bpm.modeler.ui.property.tabs.binding;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;

/**
 * Binding which looks up the model value from an emf attribute and
 * binds it against the selected state of a button.
 * 
 * @author nico.rehwaldt
 */
public class BooleanButtonBinding extends ModelAttributeButtonBinding<Boolean> {
	
	public BooleanButtonBinding(EObject model, EStructuralFeature feature, Button control) {
		super(model, feature, control);
	}

	@Override
	public void setViewValue(Boolean value) {
		control.setSelection(value != null && value.booleanValue());
		control.notifyListeners(SWT.Selection, new Event());
	}

	@Override
	public Boolean getViewValue() throws IllegalArgumentException {
		return control.getSelection();
	}
}
