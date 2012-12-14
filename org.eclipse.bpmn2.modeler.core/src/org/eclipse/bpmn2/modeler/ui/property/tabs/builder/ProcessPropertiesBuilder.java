package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.bpmn2.Process;

public class ProcessPropertiesBuilder extends AbstractPropertiesBuilder<Process> {

	public ProcessPropertiesBuilder(Composite parent, GFPropertySection section, Process bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		PropertyUtil.createCheckbox(section, parent, "Is Executable",
				Bpmn2Package.eINSTANCE.getProcess_IsExecutable(), bo);
	}
}
