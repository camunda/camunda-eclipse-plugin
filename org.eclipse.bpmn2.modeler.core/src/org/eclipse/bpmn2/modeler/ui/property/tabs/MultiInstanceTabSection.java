package org.eclipse.bpmn2.modeler.ui.property.tabs;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.modeler.core.property.AbstractTabSection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

/**
 * Event tabs for ... multi instance
 * 
 * @author nico.rehwaldt
 */
public class MultiInstanceTabSection extends AbstractTabSection {

	@Override
	protected Composite createCompositeForObject(Composite parent, EObject businessObject) {
		new MultiInstanceTabCompositeFactory(this, parent).createCompositeForBusinessObject((Activity) businessObject);
		
		return parent;
	}
}
