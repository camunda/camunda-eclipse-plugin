package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Text;

public class StringTextBinding extends ModelAttributeTextBinding<String> {

	public StringTextBinding(EObject model, EStructuralFeature feature, Text control) {
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
	protected String fromString(String string) {
		if (string == null || string.trim().isEmpty()) {
			return null;
		} else {
			return string;
		}
	}
}
