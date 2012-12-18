package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class ServiceTaskPropertiesBuilder extends AbstractPropertiesBuilder<ServiceTask> {

	/**
	 * Creates a new factory from the given arguments
	 * 
	 * @param parent
	 * @param section
	 * @param bo
	 */
	public ServiceTaskPropertiesBuilder(Composite parent, GFPropertySection section, ServiceTask bo) {
		super(parent, section, bo);
	}

	/**
	 * Creates the service task specific controls
	 */
	@Override
	public void create() {
		PropertyUtil.createText(section, parent, "Result Variable", ModelPackage.eINSTANCE.getDocumentRoot_ResultVariableName(), bo);	
	}
}
