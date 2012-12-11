package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.ModelAttributeTextBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SequenceFlowPropertiesFactory extends AbstractPropertiesFactory {

	private static final EStructuralFeature CONDITION_EXPRESSION_FEATURE = Bpmn2Package.eINSTANCE.getSequenceFlow_ConditionExpression();
	
	public SequenceFlowPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		Text text = PropertyUtil.createUnboundText(section, parent, "Condition");
		
		ConditionExpressionTextBinding binding = new ConditionExpressionTextBinding(bo, CONDITION_EXPRESSION_FEATURE, text);
		binding.establish();
	}
	
	/**
	 * Mapping the condition expression to a string
	 * 
	 * @author nico.rehwaldt
	 */
	private static class ConditionExpressionTextBinding extends ModelAttributeTextBinding<FormalExpression> {

		public ConditionExpressionTextBinding(EObject model, EStructuralFeature feature, Text control) {
			super(model, feature, control);
		}

		@Override
		protected String toString(FormalExpression value) {
			if (value == null) {
				return "";
			} else {
				return value.getBody();
			}
		}

		@Override
		protected FormalExpression fromString(String expr) {
			if (expr == null || expr.isEmpty()) {
				return null;
			}
			
			FormalExpression expression = Bpmn2Factory.eINSTANCE.createFormalExpression();
			expression.setBody(expr);
			
			return expression;
		}
		
		@Override
		protected boolean isChange(FormalExpression oldValue, FormalExpression newValue) {
			String oldExpr = oldValue.getBody();
			String newExpr = newValue.getBody();
			
			if (oldExpr != null && newExpr != null) {
				return !newExpr.equals(oldExpr);
			} else {
				return oldExpr != newExpr;
			}
		}
	}
}
