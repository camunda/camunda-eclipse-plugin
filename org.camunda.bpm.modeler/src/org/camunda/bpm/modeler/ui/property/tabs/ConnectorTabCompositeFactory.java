package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.ui.property.tabs.builder.ConnectorPropertiesBuilder;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Roman Smirnov
 */
public class ConnectorTabCompositeFactory extends AbstractTabCompositeFactory<BaseElement> {

	public ConnectorTabCompositeFactory(GFPropertySection section, Composite parent) {
		super(section, parent);
	}
	
	@Override
	public Composite createCompositeForBusinessObject(BaseElement baseElement) {
		new ConnectorPropertiesBuilder(parent, section, baseElement).create();
		return parent;
	}
	
}
