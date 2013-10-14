package org.camunda.bpm.modeler.test.core.utils;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * A {@link recording command that captures thrown exceptions for later
 * reporting.
 * 
 * @author nico.rehwaldt
 */
public class ExceptionCapturingCommand extends RecordingCommand {

	private Runnable runnable;
	private RuntimeException capturedException;
	
	public ExceptionCapturingCommand(TransactionalEditingDomain editingDomain, Runnable runnable) {
		super(editingDomain);
		
		this.runnable = runnable;
	}

	@Override
	protected void doExecute() {
		capturedException = null;
		
		try {
			runnable.run();
		} catch (RuntimeException e) {
			capturedException = e;
			throw e;
		}
	}
	
	public boolean failedWithException() {
		return capturedException != null;
	}
	
	public RuntimeException getCapturedException() {
		return capturedException;
	}
}
