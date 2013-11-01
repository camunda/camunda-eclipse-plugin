package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.core.property.AbstractTabSection;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

/**
 * Form fields tab for form data properties
 * 
 * @author kristin.polenz
 */
public class FormFieldsTabSection extends AbstractTabSection {

	@Override
	protected Composite createCompositeForObject(Composite parent, EObject businessObject) {
		if (businessObject instanceof BaseElement) {
			new FormFieldsTabCompositeFactory(this, parent).createCompositeForBusinessObject((BaseElement) businessObject);
		}

		return parent;
	}
}
