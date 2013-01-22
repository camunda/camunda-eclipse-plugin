package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class ServiceTaskPropertiesBuilder extends AbstractPropertiesBuilder<Task> {

	/**
	 * Creates a new factory from the given arguments
	 * 
	 * @param parent
	 * @param section
	 * @param bo
	 */
	public ServiceTaskPropertiesBuilder(Composite parent, GFPropertySection section, Task bo) {
		super(parent, section, bo);
	}

	/**
	 * Creates the service task specific controls
	 */
	@Override
	public void create() {
		EStructuralFeature propertyFeature = bo.eGet(ModelPackage.eINSTANCE.getDocumentRoot_ResultVariableName()) != null ? ModelPackage.eINSTANCE.getDocumentRoot_ResultVariableName() : ModelPackage.eINSTANCE.getDocumentRoot_ResultVariable();
		PropertyUtil.createText(section, parent, "Result Variable", propertyFeature, bo);	
	}
}
