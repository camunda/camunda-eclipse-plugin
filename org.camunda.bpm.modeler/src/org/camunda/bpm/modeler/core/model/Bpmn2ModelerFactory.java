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

package org.camunda.bpm.modeler.core.model;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.impl.Bpmn2FactoryImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

/**
 * This Factory will invoke the super factory to create a "bare bones" model
 * object, and then "decorate" it with model extensions defined by the Target
 * Runtime plugin extension.
 * 
 * @author Bob Brodt
 * 
 */
public class Bpmn2ModelerFactory extends Bpmn2FactoryImpl {

	public static Bpmn2ModelerFactory getInstance() {
		return (Bpmn2ModelerFactory) Bpmn2ModelerFactory.eINSTANCE;
	}

	@SuppressWarnings("unchecked")
	public static <T extends EObject> T create(Class<T> clazz) {
		EObject newObject = null;
		EClassifier eClassifier = Bpmn2Package.eINSTANCE.getEClassifier(clazz.getSimpleName());
		if (eClassifier instanceof EClass) {
			newObject = Bpmn2ModelerFactory.eINSTANCE.create((EClass) eClassifier);
		}
		return (T) newObject;
	}

	public DocumentRoot createDocumentRoot() {
		DocumentRoot documentRoot = super.createDocumentRoot();
		documentRoot.eSetDeliver(false);
		return documentRoot;
	}
}
