/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.camunda.bpm.modeler.core.importer.util;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.model.Bpmn2ModelerFactory;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.Expression;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.di.BpmnDiPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Slim version of {@link ModelHandler}. 
 * 
 * May not be instantiated / nor does it static contain state of any 
 * kind to disallow any kind of bad design *gg*.
 * 
 * @author Nico Rehwaldt
 */
public class ModelHelper {
	
	private ModelHelper() {}
	
	///////////////////
	// static methods for EObject creation
	///////////////////
	
	public static EObject create(Resource resource, EClass eClass) {
		EObject newObject = null;
		EPackage pkg = eClass.getEPackage();
		EFactory factory = pkg.getEFactoryInstance();
		
		// make sure we don't try to construct abstract objects here!
		if (eClass == Bpmn2Package.eINSTANCE.getExpression()) {
			eClass = Bpmn2Package.eINSTANCE.getFormalExpression();
		}
		
		newObject = factory.create(eClass);
		
		initialize(resource, newObject);
		
		return newObject;
	}

	public static <T extends EObject> T create(Resource resource, Class<T> clazz) {
		EObject newObject = null;
		EClassifier eClassifier = Bpmn2Package.eINSTANCE.getEClassifier(clazz.getSimpleName());
		if (eClassifier instanceof EClass) {
			EClass eClass = (EClass)eClassifier;
			newObject = Bpmn2ModelerFactory.getInstance().create(eClass);
		} else {
			// maybe it's a DI object type?
			eClassifier = BpmnDiPackage.eINSTANCE.getEClassifier(clazz.getSimpleName());
			if (eClassifier instanceof EClass) {
				EClass eClass = (EClass)eClassifier;
				newObject = BpmnDiFactory.eINSTANCE.create(eClass);
			}
		}
		
		if (newObject != null) {
			initialize(resource, newObject);
		}

		return (T) newObject;
	}

	public static void initialize(Resource resource, EObject newObject) {
		
		if (resource == null) {
			throw new IllegalArgumentException("resource is null");
		}
		
		if (newObject == null) {
			throw new IllegalArgumentException("newObject is null");
		}
		
		if (newObject.eClass().getEPackage() == Bpmn2Package.eINSTANCE) {
			// Set appropriate default values for the object features here
			switch (newObject.eClass().getClassifierID()) {
			case Bpmn2Package.CONDITIONAL_EVENT_DEFINITION:
				{
					Expression expr = Bpmn2ModelerFactory.getInstance().createFormalExpression();
					((ConditionalEventDefinition)newObject).setCondition(expr);
				}
				break;
			}
		}
		
		// if the object has an "id", assign it now.
		ModelUtil.setID(newObject,resource);
	}
}
