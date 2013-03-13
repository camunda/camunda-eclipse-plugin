package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

public class SequenceFlowPropertiesBuilder extends AbstractPropertiesBuilder<SequenceFlow> {

	private static final EStructuralFeature CONDITION_EXPRESSION_FEATURE = Bpmn2Package.eINSTANCE.getSequenceFlow_ConditionExpression();
	
	public SequenceFlowPropertiesBuilder(Composite parent, GFPropertySection section, SequenceFlow bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		PropertyUtil.createText(section, parent, "Condition", CONDITION_EXPRESSION_FEATURE, bo);
	}
}
