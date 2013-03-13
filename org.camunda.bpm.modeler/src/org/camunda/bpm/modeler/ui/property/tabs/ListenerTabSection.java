package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.core.property.AbstractTabSection;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

/**
 * Event tabs for ... multi instance
 * 
 * @author nico.rehwaldt
 */
public class ListenerTabSection extends AbstractTabSection {

	@Override
	protected Composite createCompositeForObject(Composite parent, EObject businessObject) {
		new ListenerTabCompositeFactory(this, parent).createCompositeForBusinessObject((BaseElement) businessObject);
		return parent;
	}
}
