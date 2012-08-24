package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.validation;

import java.util.Iterator;

import org.eclipse.bpmn2.BusinessRuleTask;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

public class BusinessRuleTaskConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject eObj = ctx.getTarget();
		if (eObj instanceof BusinessRuleTask) {
			BusinessRuleTask bt = (BusinessRuleTask)eObj;
			
			Iterator<FeatureMap.Entry> biter = bt.getAnyAttribute().iterator();
			boolean foundRuleflowGroup = false;
			while (biter.hasNext()) {
				FeatureMap.Entry entry = biter.next();
				if (entry.getEStructuralFeature().getName().equals("ruleFlowGroup")) {
					foundRuleflowGroup = true;
					String ruleflowGroup = (String) entry.getValue();
					if (ruleflowGroup==null || ruleflowGroup.isEmpty()) {
						return ctx.createFailureStatus("Business Rule Task has no ruleflow group");
					}
				}
			}
			if (!foundRuleflowGroup) {
				return ctx.createFailureStatus("Business Rule Task has no ruleflow group");
			}
		}	
		return ctx.createSuccessStatus();
	}

}
