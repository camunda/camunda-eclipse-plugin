/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.camunda.bpm.modeler.core.importer;

import org.eclipse.emf.ecore.EObject;

/**
 * 
 * @author Nico Rehwaldt
 */
public class InvalidContentException extends ImportException {

	private static final long serialVersionUID = 1L;
	
	public InvalidContentException(String message) {
		super(message);
	}
	
	public InvalidContentException(EObject element) {
		super("Element has invalid content", element);
	}
	
	public InvalidContentException(String message, EObject element) {
		super(message, element);
	}
}
