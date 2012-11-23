package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class CallActivityPropertiesFactory extends PropertiesFactory {

	public CallActivityPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	@Override
	protected void createControls() {
		PropertyUtil.createText(section, parent, "Called Element", ModelPackage.eINSTANCE.getCallActivity_CalledElement(), bo);
	}

}
