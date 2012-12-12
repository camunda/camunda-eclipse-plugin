package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class ProcessPropertiesFactory extends AbstractPropertiesFactory {

	public ProcessPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		PropertyUtil.createText(section, parent, "Is Executable",
				Bpmn2Package.eINSTANCE.getProcess_IsExecutable(), bo);
	}
}
