package org.eclipse.bpmn2.modeler.ui.property.tabs;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.modeler.core.property.AbstractTabSection;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
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
