package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.core.property.AbstractTabSection;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

/**
 * Field injections tab to configure a field for a 'service task like' element
 * 
 * @author kristin.polenz
 */
public class FieldInjectionsTabSection extends AbstractTabSection {

	@Override
	protected Composite createCompositeForObject(Composite parent, EObject businessObject) {
		if (businessObject instanceof BaseElement) {
			new FieldInjectionsTabCompositeFactory(this, parent).createCompositeForBusinessObject((BaseElement) businessObject);
		}

		return parent;
	}
}
