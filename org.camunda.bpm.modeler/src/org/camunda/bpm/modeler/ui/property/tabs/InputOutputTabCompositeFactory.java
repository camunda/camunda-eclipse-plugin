package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.ui.property.tabs.builder.InputOutputPropertiesBuilder;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Roman Smirnov
 */
public class InputOutputTabCompositeFactory extends AbstractTabCompositeFactory<BaseElement> {

	public InputOutputTabCompositeFactory(GFPropertySection section, Composite parent) {
		super(section, parent);
	}
	
	@Override
	public Composite createCompositeForBusinessObject(BaseElement businessObject) {
		new InputOutputPropertiesBuilder(parent, section, businessObject).create();
		return parent;
	}
	
}
