package org.camunda.bpm.modeler.ui.dialog.importer;

import org.camunda.bpm.modeler.core.importer.ImportException;

/**
 * Model for warning / problems dialog
 * 
 * @author nico.rehwaldt
 */
public class Problem {

	private boolean warning;
	private ImportException exception;

	public Problem(ImportException exception, boolean warning) {
		this.exception = exception;
		this.warning = warning;
	}
	
	public boolean isWarning() {
		
		return warning;
	}

	public String getSummary() {
		return exception.getMessage();
	}

	public ImportException getException() {
		return exception;
	}
}
