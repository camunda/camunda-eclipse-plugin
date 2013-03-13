package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Process;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for the id property
 * 
 * @author nico.rehwaldt
 */
public class ProcessIdPropertyBuilder extends IdPropertyBuilder {

	public ProcessIdPropertyBuilder(Composite parent, GFPropertySection section, Process bo) {
		super(parent, section, bo, "Process Id");
	}

	@Override
	public void create() {
		super.create();
	}
	
}
