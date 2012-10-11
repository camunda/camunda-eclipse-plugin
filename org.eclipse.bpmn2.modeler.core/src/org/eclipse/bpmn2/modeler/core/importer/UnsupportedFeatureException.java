/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.importer;

import org.eclipse.emf.ecore.EObject;

/**
 * 
 * @author Nico Rehwaldt
 */
public class UnsupportedFeatureException extends ImportException {

	private static final long serialVersionUID = 1L;

	public UnsupportedFeatureException(String message, EObject element) {
		super(message, element);
	}

	public UnsupportedFeatureException(String message) {
		super(message);
	}
}
