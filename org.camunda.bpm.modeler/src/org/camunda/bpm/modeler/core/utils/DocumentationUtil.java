package org.camunda.bpm.modeler.core.utils;

import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Documentation;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Handles {@link Documentation} of {@link BaseElement}s
 * 
 * @author roman.smirnov
 *
 */
public class DocumentationUtil {

	/**
	 * Given an object (the {@link BaseElement}), return the {@link Documentation}, if a
	 * {@link Documentation} exists.
	 * 
	 * @param object
	 * 
	 * @return
	 */
	public static Documentation getDocumentation(EObject object) {
		EStructuralFeature documentationFeature = object.eClass().getEStructuralFeature("documentation");
		EList<Documentation> documentationValues = (EList<Documentation>) object.eGet(documentationFeature);
		
		if (documentationValues != null && documentationValues.size() == 1) {
			return documentationValues.get(0);
		}

		return null;
	}

	/**
	 * Given an object (the {@link BaseElement}), return the text of the
	 * {@link Documentation}, if a {@link Documentation} exists.
	 * 
	 * @param object
	 * 
	 * @return
	 */
	public static String getDocumentationText(EObject object) {
		Documentation documentation = getDocumentation(object);
		
		if (documentation != null) {
			return documentation.getText();
		}
		
		return null;
	}
	
	private static List<Documentation> getDocumentations(EObject object) {
		EStructuralFeature documentationFeature = object.eClass().getEStructuralFeature("documentation");
		EList<Documentation> documentationValues = (EList<Documentation>) object.eGet(documentationFeature);
		
		return documentationValues;
	}
	
	/**
	 * Updates the object with the passed value.
	 * 
	 * @param object
	 * @param value
	 */
	public static void updateDocumentation(EObject object, Documentation value) {
		List<Documentation> documentations = getDocumentations(object);
		
		if (documentations != null) {
			documentations.clear();
			documentations.add(value);
		}
	}
	
	/**
	 * Removes the documentation from the given object.
	 * 
	 * @param object
	 */
	public static void removeDocumentation(EObject object) {
		List<Documentation> documentations = getDocumentations(object);
		
		if (documentations != null) {
			documentations.clear();
		}
	}
	
	
}
