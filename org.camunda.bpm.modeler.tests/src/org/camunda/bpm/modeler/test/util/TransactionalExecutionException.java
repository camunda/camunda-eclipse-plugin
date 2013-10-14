package org.camunda.bpm.modeler.test.util;

/**
 * A {@link RuntimeException} signaling the failure of a transactional execution.
 * 
 * @see TestHelper#transactionalExecute(org.eclipse.emf.transaction.TransactionalEditingDomain, Runnable)
 * 
 * @author nico.rehwaldt
 */
public class TransactionalExecutionException extends RuntimeException {

	public TransactionalExecutionException(Throwable cause) {
		super(cause);
	}
}
