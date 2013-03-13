package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.ui.property.tabs.builder.MultiInstancePropertiesBuilder;
import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Event tab composite factory
 * 
 * @author nico.rehwaldt
 */
public class MultiInstanceTabCompositeFactory extends AbstractTabCompositeFactory<Activity> {
	
	public MultiInstanceTabCompositeFactory(GFPropertySection section, Composite parent) {
		super(section, parent);
	}
	
	@Override
	public Composite createCompositeForBusinessObject(Activity activity) {
		createMultiInstanceComposite(activity);
		
		return parent;
	}

	private void createMultiInstanceComposite(Activity activity) {
		new MultiInstancePropertiesBuilder(parent, section, activity).create();
	}
}