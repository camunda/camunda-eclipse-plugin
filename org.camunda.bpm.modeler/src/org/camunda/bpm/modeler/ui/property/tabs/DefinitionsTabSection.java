package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.core.property.AbstractTabSection;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

/**
 * Event tabs for ... definitions
 * 
 * @author nico.rehwaldt
 */
public class DefinitionsTabSection extends AbstractTabSection {

	@Override
	protected Composite createCompositeForObject(Composite parent, EObject businessObject) {
		Definitions definitions = ModelUtil.getDefinitions(businessObject);
		new DefinitionsTabCompositeFactory(this, parent).createCompositeForBusinessObject(definitions);
		
		return parent;
	}
}
