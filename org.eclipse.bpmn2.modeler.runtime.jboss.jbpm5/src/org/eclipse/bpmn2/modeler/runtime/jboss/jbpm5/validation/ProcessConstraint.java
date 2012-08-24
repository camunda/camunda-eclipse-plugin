package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.validation;

import java.util.Iterator;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

public class ProcessConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject eObj = ctx.getTarget();
		if (eObj instanceof Process) {
			Process process = (Process)eObj;
			
			String name = null;
			Iterator<FeatureMap.Entry> iter = process.getAnyAttribute().iterator();
			while (iter.hasNext()) {
				FeatureMap.Entry entry = iter.next();
				if (entry.getEStructuralFeature().getName().equals("packageName")) {
					name = (String) entry.getValue();
					if (name==null || name.isEmpty()) {
						ctx.addResult(entry.getEStructuralFeature());
						return ctx.createFailureStatus("Process has no package name.");
					}
				}
			}
	
			name = process.getName();
			if (name==null || name.isEmpty()) {
				ctx.addResult(process.eClass().getEStructuralFeature("name"));
				return ctx.createFailureStatus("Process has no name.");
			}
		}	
		return ctx.createSuccessStatus();
	}

}
