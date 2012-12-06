package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class ScriptTaskPropertiesFactory extends AbstractPropertiesFactory {

	public ScriptTaskPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		PropertyUtil.createText(section, parent, "Script Language", Bpmn2Package.eINSTANCE.getScriptTask_ScriptFormat(), bo);
		
		PropertyUtil.createMultiText(section, parent, "Script", Bpmn2Package.eINSTANCE.getScriptTask_Script(), bo);
	}

}
