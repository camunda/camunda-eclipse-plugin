/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.wid;

/**
 * Custom exception class so we know specific errors coming out of the 
 * WIDHandler.
 * @author bfitzpat
 *
 */
public class WIDException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WIDException (String message) {
		super(message);
	}
	
	public WIDException (String message, Throwable exception) {
		super(message, exception);
	}
}
