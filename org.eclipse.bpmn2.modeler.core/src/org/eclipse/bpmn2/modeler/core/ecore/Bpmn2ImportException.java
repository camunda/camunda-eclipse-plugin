/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.ecore;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 * 
 */
public class Bpmn2ImportException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Bpmn2ImportException() {
		super();
	}

	public Bpmn2ImportException(String message, Throwable cause) {
		super(message, cause);
	}

	public Bpmn2ImportException(String message) {
		super(message);
	}

	public Bpmn2ImportException(Throwable cause) {
		super(cause);
	}

}
