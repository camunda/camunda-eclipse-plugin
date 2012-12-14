package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.ModelButtonBinding;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;

class ModelRadioBinding extends ModelButtonBinding<Boolean> {

	public ModelRadioBinding(EObject model, EStructuralFeature feature, Button control) {
		super(model, feature, control);
	}

	@Override
	public void setViewValue(Boolean value) {
		control.setSelection(value);
		
		if (value) {
			control.notifyListeners(SWT.Selection, new Event());
		}
	}

	@Override
	public Boolean getViewValue() {
		return control.getSelection();
	}

	@Override
	public Boolean getModelValue() {
		return model.eIsSet(feature);
	}

	@Override
	public void setModelValue(Boolean value) {
		// do nothing
	}
}