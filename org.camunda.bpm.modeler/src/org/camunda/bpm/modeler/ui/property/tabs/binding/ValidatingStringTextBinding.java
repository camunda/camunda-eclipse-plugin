package org.camunda.bpm.modeler.ui.property.tabs.binding;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Text;

public class ValidatingStringTextBinding extends ValidatingTextBinding<String> {
	
	public ValidatingStringTextBinding(EObject model,
			EStructuralFeature feature, Text control) {
		super(model, feature, control);
	}

	@Override
	protected String toString(String value) {
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}

	@Override
	protected String fromString(String str) {
		if (str == null || str.trim().isEmpty()) {
			return null;
		} else {
			return str;
		}
	}
	
	@Override
	protected boolean isEmptyValue(String value) {
		return value == null || value.trim().isEmpty(); 
	}

}
