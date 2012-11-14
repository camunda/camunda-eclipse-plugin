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

import org.eclipse.emf.ecore.resource.Resource.Diagnostic;


/**
 * 
 * @author Nico Rehwaldt
 */
public class ResourceImportException extends ImportException {

	private static final long serialVersionUID = 1L;
	
	private Diagnostic diagnostic;
	
	public ResourceImportException(String message) {
		super(message);
	}
	
	public ResourceImportException(String message, Diagnostic diagnostic) {
		super(message);
		
		this.diagnostic = diagnostic;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + " : " + diagnostic.toString();
	}
}
