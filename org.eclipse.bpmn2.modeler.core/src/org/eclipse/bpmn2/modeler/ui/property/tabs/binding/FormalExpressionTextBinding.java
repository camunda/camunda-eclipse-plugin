package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Text;

/**
 * Mapping the condition expression to a string
 * 
 * @author nico.rehwaldt
 */
public class FormalExpressionTextBinding extends ModelAttributeTextBinding<FormalExpression> {

	public FormalExpressionTextBinding(EObject model, EStructuralFeature feature, Text control) {
		super(model, feature, control);
	}

	@Override
	protected String toString(FormalExpression value) {
		if (value == null || value.getBody() == null) {
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