package org.camunda.bpm.modeler.core.validation;

import org.eclipse.bpmn2.Task;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;

public class TaskNameNotEmpty extends AbstractModelConstraint {

	@Override
	public IStatus validate(IValidationContext ctx) {

		EObject eObj = ctx.getTarget();
		EMFEventType eType = ctx.getEventType();
		// In the case of batch mode.
		if (eType == EMFEventType.NULL) {
			if (eObj instanceof Task) {
				Task t = (Task) eObj;
				if (t.getName() == null || t.getName().length() == 0) {
					ctx.addResult(t.eClass().getEStructuralFeature("name"));
					return ctx.createFailureStatus(new Object[] { eObj.eClass().getName() });
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
