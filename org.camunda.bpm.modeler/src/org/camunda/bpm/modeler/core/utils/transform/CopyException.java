package org.camunda.bpm.modeler.core.utils.transform;


public class CopyException extends RuntimeException {

	private CopyProblem warning;

	public CopyException(CopyProblem warning) {
		super(warning.toString());
		
		this.warning = warning;
	}
	
	public CopyProblem getWarning() {
		return warning;
	}
}
