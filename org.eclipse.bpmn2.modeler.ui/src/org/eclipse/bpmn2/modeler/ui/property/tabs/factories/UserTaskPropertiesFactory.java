package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class UserTaskPropertiesFactory extends PropertiesFactory {

	public UserTaskPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	protected void createControls() {
		
		PropertyUtil.createText(section, parent, "Assignee", ModelPackage.eINSTANCE.getDocumentRoot_Assignee(), bo);
		PropertyUtil.createText(section, parent, "Candidate Users", ModelPackage.eINSTANCE.getDocumentRoot_CandidateUsers(), bo);
		PropertyUtil.createText(section, parent, "Candidate Groups", ModelPackage.eINSTANCE.getDocumentRoot_CandidateGroups(), bo);
		
		PropertyUtil.createText(section, parent, "Form Key", ModelPackage.eINSTANCE.getDocumentRoot_FormKey(), bo);
		
		Text dueDateField = PropertyUtil.createTextWithDatePicker(section, parent, "Due Date", ModelPackage.eINSTANCE.getDocumentRoot_DueDate(), bo);
		PropertyUtil.createToolTipFor(dueDateField, "The due date as an EL expression (e.g. ${someDate}) or a ISO date (e.g. 2012-03-01T15:30:23)");
		
		PropertyUtil.createText(section, parent, "Priority", ModelPackage.eINSTANCE.getDocumentRoot_Priority(), bo);
	}

}
