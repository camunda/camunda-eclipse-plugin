package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
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
			new NamePropertyBuilder(parent, section, process, "Process Name").create();
			new IdPropertyBuilder(parent, section, process, "Process Id").create();
			
			PropertyUtil.createCheckbox(section, parent, "Is Executable",
					Bpmn2Package.eINSTANCE.getProcess_IsExecutable(), process);
		}
	}
}
