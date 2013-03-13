package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class UserTaskPropertiesBuilder extends AbstractPropertiesBuilder<UserTask> {

	public UserTaskPropertiesBuilder(Composite parent, GFPropertySection section, UserTask bo) {
		super(parent, section, bo);
	}

	public void create() {
		
		PropertyUtil.createText(section, parent, "Assignee", ModelPackage.eINSTANCE.getDocumentRoot_Assignee(), bo);
		PropertyUtil.createText(section, parent, "Candidate Users", ModelPackage.eINSTANCE.getDocumentRoot_CandidateUsers(), bo);
		PropertyUtil.createText(section, parent, "Candidate Groups", ModelPackage.eINSTANCE.getDocumentRoot_CandidateGroups(), bo);
		
		PropertyUtil.createText(section, parent, "Form Key", ModelPackage.eINSTANCE.getDocumentRoot_FormKey(), bo);
		
		Text dueDateField = PropertyUtil.createText(section, parent, "Due Date", ModelPackage.eINSTANCE.getDocumentRoot_DueDate(), bo);
		PropertyUtil.createToolTipFor(dueDateField, "The due date as an EL expression (e.g. ${someDate}) or a ISO date (e.g. 2012-03-01T15:30:23)");
		
		PropertyUtil.createText(section, parent, "Priority", ModelPackage.eINSTANCE.getDocumentRoot_Priority(), bo);
	}
}
