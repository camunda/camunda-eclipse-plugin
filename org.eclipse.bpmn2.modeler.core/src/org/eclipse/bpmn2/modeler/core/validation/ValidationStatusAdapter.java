package org.eclipse.bpmn2.modeler.core.validation;

import org.eclipse.emf.common.notify.impl.AdapterImpl;

/**
 * The plan is to attach this adapter to the offending EObject, then find the corresponding
 * PictogramElement to which it is linked and decorate it with an error marker.
 * 
 * This adapter is constructed and added to the EObject during live validation.
 * 
 * @author bbrodt
 *
 */
public class ValidationStatusAdapter extends AdapterImpl {

	public ValidationStatusAdapter() {
	}

}
