package org.camunda.bpm.modeler.core.validation;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;

public class FormalExpressionConstraint extends
		AbstractModelConstraint {

	public FormalExpressionConstraint() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IStatus validate(IValidationContext ctx) {

		EObject eObj = ctx.getTarget();
		EMFEventType eType = ctx.getEventType();
		// In the case of batch mode.
		if (eType == EMFEventType.NULL) {						
			if(eObj instanceof FormalExpression){
				FormalExpression formalExpression = (FormalExpression) eObj;
				boolean error = false;
				if(formalExpression.getLanguage() == null || formalExpression.getLanguage().length() == 0){
					ctx.addResult(Bpmn2Package.eINSTANCE.getFormalExpression_Language());
					error = true;
				}
				if(formalExpression.getBody() == null || formalExpression.getBody().length() == 0){
					ctx.addResult(Bpmn2Package.eINSTANCE.getFormalExpression_Body());
					error = true;
				}
				if (error)
					return ctx.createFailureStatus(new Object[] {eObj.eClass().getName()});		
			}
		} 
		return ctx.createSuccessStatus();
	}

}
