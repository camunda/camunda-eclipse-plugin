package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Process;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ProcessPropertiesBuilder extends AbstractPropertiesBuilder<Process> {

	private static final EStructuralFeature IS_EXECUTABLE = Bpmn2Package.eINSTANCE.getProcess_IsExecutable();
	
	public ProcessPropertiesBuilder(Composite parent, GFPropertySection section, Process bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		Button isExecutableCheckbox = PropertyUtil.createCheckbox(section, parent, "Is Executable", IS_EXECUTABLE, bo);
	}
}
