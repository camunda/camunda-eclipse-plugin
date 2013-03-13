/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/
package org.camunda.bpm.modeler.core.validation;

import org.camunda.bpm.modeler.core.runtime.ModelDescriptor;
import org.camunda.bpm.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.di.BpmnDiPackage;
import org.eclipse.dd.dc.DcPackage;
import org.eclipse.dd.di.DiPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.validation.model.IClientSelector;

public class ValidationDelegateClientSelector implements IClientSelector {
	
	public boolean selects(Object object) {
		if (object instanceof EObject) {
			EPackage pkg = ((EObject)object).eClass().getEPackage();
			if (pkg == Bpmn2Package.eINSTANCE)
				return true;
			if (pkg == BpmnDiPackage.eINSTANCE)
				return true;
			if (pkg == DiPackage.eINSTANCE)
				return true;
			if (pkg == DcPackage.eINSTANCE)
				return true;
			ModelDescriptor md = TargetRuntime.getCurrentRuntime().getModelDescriptor();
			if (md!=null && pkg == md.getEPackage())
				return true;
		}
		return false;
	}
}
