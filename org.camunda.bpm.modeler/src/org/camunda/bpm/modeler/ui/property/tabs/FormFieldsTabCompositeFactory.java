package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.ui.property.tabs.builder.FormFieldsPropertiesBuilder;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Form fields tab composite factory
 * 
 * @author kristin.polenz
 */
public class FormFieldsTabCompositeFactory extends AbstractTabCompositeFactory<BaseElement> {
	
	public FormFieldsTabCompositeFactory(GFPropertySection section, Composite parent) {
		super(section, parent);
	}
	
	@Override
	public Composite createCompositeForBusinessObject(BaseElement businessObject) {
		new FormFieldsPropertiesBuilder(parent, section, businessObject).create();

		return parent;
	}
}
