package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for the id property
 * 
 * @author nico.rehwaldt
 */
public class ParticipantPropertiesBuilder extends AbstractPropertiesBuilder<Participant> {
	
	public ParticipantPropertiesBuilder(Composite parent, GFPropertySection section, Participant bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		Process process = bo.getProcessRef();
		
		if (process != null) { // empty participant must not have a process
			new ProcessIdPropertyBuilder(parent, section, process).create();
			new NamePropertyBuilder(parent, section, process, "Process Name").create();
			
			new ProcessPropertiesBuilder(parent, section, process).create();
		}
	}
}
