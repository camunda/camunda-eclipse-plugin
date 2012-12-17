package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import java.util.List;

import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class StartEventPropertiesBuilder extends AbstractPropertiesBuilder<StartEvent> {

	public StartEventPropertiesBuilder(Composite parent, GFPropertySection section, StartEvent bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(bo);
		
		if (!hasDefinitions(eventDefinitions)) {
			PropertyUtil.createText(section, parent, "Form Key", ModelPackage.eINSTANCE.getDocumentRoot_FormKey(), bo);
		}
	}

	private boolean hasDefinitions(List<EventDefinition> eventDefinitions) {
		return !eventDefinitions.isEmpty();
	}
}
