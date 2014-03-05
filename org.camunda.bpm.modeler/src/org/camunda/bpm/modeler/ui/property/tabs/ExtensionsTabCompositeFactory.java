package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.ui.property.tabs.builder.ExtensionsPropertiesBuilder;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Extensions tab composite factory
 * 
 * @author kristin.polenz
 */
public class ExtensionsTabCompositeFactory extends AbstractTabCompositeFactory<BaseElement> {

	public ExtensionsTabCompositeFactory(GFPropertySection section, Composite parent) {
		super(section, parent);
	}

	@Override
	public Composite createCompositeForBusinessObject(BaseElement businessObject) {
		new ExtensionsPropertiesBuilder(parent, section, businessObject).create();

		return parent;
	}
}
