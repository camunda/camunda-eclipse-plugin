package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * <p>Builds for a task and a throwing message event the controls to set a result
 * variable.</p>
 * 
 */
public class ServiceTaskPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	/**
	 * Creates a new factory from the given arguments
	 * 
	 * @param parent
	 * @param section
	 * @param bo
	 */
	public ServiceTaskPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
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
