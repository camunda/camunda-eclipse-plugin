package org.camunda.bpm.modeler.ui.property.tabs.builder;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.emf.ecore.EObject;
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
		
		if (isContainedInEventSubProcess(bo)) {
			PropertyUtil.createCheckbox(section, parent, "Interrupting", Bpmn2Package.eINSTANCE.getStartEvent_IsInterrupting(), bo);
		}
	}

	private boolean isContainedInEventSubProcess(StartEvent bo) {
		EObject container = bo.eContainer();
		if (container instanceof SubProcess) {
			SubProcess subProcess = (SubProcess) container;
			return subProcess.isTriggeredByEvent();
		}
		
		return false;
	}

	private boolean hasDefinitions(List<EventDefinition> eventDefinitions) {
		return !eventDefinitions.isEmpty();
	}
}
