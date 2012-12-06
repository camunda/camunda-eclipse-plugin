package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Text;

public class IntegerTextBinding extends ModelAttributeTextBinding<Integer> {

	public IntegerTextBinding(EObject model, EStructuralFeature feature, Text control) {
		super(model, feature, control);
	}

	@Override
	protected String toString(Integer value) {
		if (value == null) {
			return "";
		} else {
			return String.valueOf(value);
		}
	}

	/**
	 * Returns an integer from the argument
	 * 
	 * @param string the string to parse
	 * @return the parsed integer
	 * 
	 * @throws NumberFormatException if the string cannot be parsed as an integer
	 */
	@Override
	protected Integer fromString(String string) {
		if (string == null || string.trim().isEmpty()) {
			return null;
		} else {
			return Integer.parseInt(string, 10);
		}
	}
}
