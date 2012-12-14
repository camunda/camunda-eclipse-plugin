package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.FormalExpressionTextBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SequenceFlowPropertiesBuilder extends AbstractPropertiesBuilder<SequenceFlow> {

	private static final EStructuralFeature CONDITION_EXPRESSION_FEATURE = Bpmn2Package.eINSTANCE.getSequenceFlow_ConditionExpression();
	
	public SequenceFlowPropertiesBuilder(Composite parent, GFPropertySection section, SequenceFlow bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		Text text = PropertyUtil.createUnboundText(section, parent, "Condition");
		
		FormalExpressionTextBinding binding = new FormalExpressionTextBinding(bo, CONDITION_EXPRESSION_FEATURE, text);
		binding.establish();
	}
}
