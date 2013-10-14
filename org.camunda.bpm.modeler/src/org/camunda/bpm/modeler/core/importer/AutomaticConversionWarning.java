package org.camunda.bpm.modeler.core.importer;

import org.eclipse.emf.ecore.EObject;

public class AutomaticConversionWarning extends ImportException {

	public AutomaticConversionWarning(String message, EObject object, EObject newObject) {
		super(message, object);
	}
}
