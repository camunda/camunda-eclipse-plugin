package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.ui.property.tabs.builder.DocumentPropertiesBuilder;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Document tab composite factory
 * 
 * @author kristin.polenz
 */
public class DocumentTabCompositeFactory extends AbstractTabCompositeFactory<Definitions> {

	public DocumentTabCompositeFactory(GFPropertySection section, Composite parent) {
		super(section, parent);
	}

	@Override
	public Composite createCompositeForBusinessObject(Definitions definitions) {
		new DocumentPropertiesBuilder(parent, section, definitions).create();

		return parent;
	}
}
