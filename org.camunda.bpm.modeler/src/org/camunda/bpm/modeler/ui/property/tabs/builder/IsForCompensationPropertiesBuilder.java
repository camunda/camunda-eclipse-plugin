package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author kristin.polenz
 *
 */
public class IsForCompensationPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	private static final EStructuralFeature IS_FOR_COMPENSATION_FEATURE = Bpmn2Package.eINSTANCE.getActivity_IsForCompensation();
	
	public IsForCompensationPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		
		PropertyUtil.createCheckbox(section, parent, "For Compensation", IS_FOR_COMPENSATION_FEATURE, bo);
		
	}

}
