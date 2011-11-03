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

package org.eclipse.bpmn2.modeler.core.model;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.impl.Bpmn2FactoryImpl;
import org.eclipse.bpmn2.modeler.core.runtime.ModelExtensionDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Bob Brodt
 *
 */
public class Bpmn2ModelerFactory extends Bpmn2FactoryImpl {
	
    @Override
    public EObject create(EClass eClass) {
    	EObject object = super.create(eClass);
    	TargetRuntime rt = TargetRuntime.getCurrentRuntime();
    	if (rt!=null) {
	    	for (ModelExtensionDescriptor med : rt.getModelExtensions()) {
	    		if (med.getType().equals(eClass.getName())) {
	    			med.populateObject(object);
	    			break;
	    		}
	    	}
    	}
    	return object;
    }
    
	
	@SuppressWarnings("unchecked")
	public static <T extends EObject> T create(Class<T> clazz) {
		EObject newObject = null;
		EClassifier eClassifier = Bpmn2Package.eINSTANCE.getEClassifier(clazz.getSimpleName());
		if (eClassifier instanceof EClass) {
			newObject = Bpmn2ModelerFactory.eINSTANCE.create((EClass)eClassifier);
		}
		return (T)newObject;
	}
}
