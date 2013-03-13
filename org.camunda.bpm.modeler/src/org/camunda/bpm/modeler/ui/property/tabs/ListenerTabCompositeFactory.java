package org.camunda.bpm.modeler.ui.property.tabs;

import org.camunda.bpm.modeler.ui.property.tabs.builder.ExecutionListenerPropertiesBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.builder.TaskListenerPropertiesBuilder;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Execution listener tab composite factory
 * 
 * @author nico.rehwaldt
 */
public class ListenerTabCompositeFactory extends AbstractTabCompositeFactory<BaseElement> {
	
	public ListenerTabCompositeFactory(GFPropertySection section, Composite parent) {
		super(section, parent);
	}
	
	@Override
	public Composite createCompositeForBusinessObject(BaseElement baseElement) {
		if (baseElement instanceof Participant) {
			baseElement = ((Participant) baseElement).getProcessRef();
		}
		
		createExecutionListenerComposite(baseElement);
		
		if (baseElement instanceof UserTask) {
			createTaskListenerComposite((UserTask) baseElement);
		}
		return parent;
	}

	private void createTaskListenerComposite(UserTask userTask) {
		new TaskListenerPropertiesBuilder(parent, section, userTask).create();
	}

	private void createExecutionListenerComposite(BaseElement baseElement) {
		new ExecutionListenerPropertiesBuilder(parent, section, baseElement).create();
	}
}