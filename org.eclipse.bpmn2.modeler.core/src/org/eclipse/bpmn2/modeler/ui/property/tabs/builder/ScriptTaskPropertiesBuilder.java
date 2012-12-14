package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class ScriptTaskPropertiesBuilder extends AbstractPropertiesBuilder<ScriptTask> {

	public ScriptTaskPropertiesBuilder(Composite parent, GFPropertySection section, ScriptTask bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		PropertyUtil.createText(section, parent, "Script Language", Bpmn2Package.eINSTANCE.getScriptTask_ScriptFormat(), bo);
		
		PropertyUtil.createMultiText(section, parent, "Script", Bpmn2Package.eINSTANCE.getScriptTask_Script(), bo);
	}

}
