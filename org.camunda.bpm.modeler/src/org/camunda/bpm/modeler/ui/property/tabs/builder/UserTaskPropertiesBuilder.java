package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
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
		PropertyUtil.attachNoteWithLink(section, dueDateField, HelpText.DUE_DATE);
		
		Text followUpDateField = PropertyUtil.createText(section, parent, "Follow Up Date", ModelPackage.eINSTANCE.getDocumentRoot_FollowUpDate(), bo);
		PropertyUtil.attachNoteWithLink(section, followUpDateField, HelpText.FOLLOW_UP_DATE);

		PropertyUtil.createText(section, parent, "Priority", ModelPackage.eINSTANCE.getDocumentRoot_Priority(), bo);
	}
}
