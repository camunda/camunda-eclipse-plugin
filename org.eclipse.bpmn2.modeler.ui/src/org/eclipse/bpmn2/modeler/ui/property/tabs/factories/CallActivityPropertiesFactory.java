package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class CallActivityPropertiesFactory extends AbstractPropertiesFactory {

	private static final String CALLED_ELEMENT = "Called Element";
	
	
	public CallActivityPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		PropertyUtil.createText(section, parent, CALLED_ELEMENT, ModelPackage.eINSTANCE.getCallActivity_CalledElement(), bo);
		
		createAttributesTable();
	}

	private void createAttributesTable() {
		
	}
}
