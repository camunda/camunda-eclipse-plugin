package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.core.property.AbstractTabSection;
import org.eclipse.bpmn2.Event;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

/**
 * Event tabs for ... events
 * 
 * @author nico.rehwaldt
 */
public class EventTabSection extends AbstractTabSection {

	@Override
	protected Composite createCompositeForObject(Composite parent, EObject businessObject) {
		if (businessObject instanceof Event) {
			new EventTabCompositeFactory(this, parent).createCompositeForBusinessObject((Event) businessObject);
		}
		
		return parent;
	}
}
