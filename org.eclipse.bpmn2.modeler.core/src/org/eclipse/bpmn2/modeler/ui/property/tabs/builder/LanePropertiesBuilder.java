package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for the id property
 * 
 * @author nico.rehwaldt
 */
public class LanePropertiesBuilder extends AbstractPropertiesBuilder<Lane> {
	
	public LanePropertiesBuilder(Composite parent, GFPropertySection section, Lane bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		
	}
}
