package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class StartEventPropertiesFactory extends PropertiesFactory {

	public StartEventPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	@Override
	protected void createControls() {
		if (!ModelUtil.hasEventDefinition((Event) bo, TimerEventDefinition.class)) {
			PropertyUtil.createText(section, parent, "Form Key", ModelPackage.eINSTANCE.getDocumentRoot_FormKey(), bo);
		}
	}

}
