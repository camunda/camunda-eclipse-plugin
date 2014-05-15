package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.ui.property.tabs.builder.FieldInjectionsPropertiesBuilder;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Field injections tab composite factory
 * 
 * @author kristin.polenz
 */
public class FieldInjectionsTabCompositeFactory extends AbstractTabCompositeFactory<BaseElement> {
	
	public FieldInjectionsTabCompositeFactory(GFPropertySection section, Composite parent) {
		super(section, parent);
	}
	
	@Override
	public Composite createCompositeForBusinessObject(BaseElement businessObject) {
		new FieldInjectionsPropertiesBuilder(parent, section, businessObject).create();

		return parent;
	}
}
