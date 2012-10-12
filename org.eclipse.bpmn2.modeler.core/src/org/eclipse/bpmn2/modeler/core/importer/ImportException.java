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

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class ImportException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private EObject element;

	public ImportException() {
		super();
	}

	public ImportException(String message, EObject element) {
		super(message);
		
		this.element = element;
	}

	public ImportException(String message) {
		super(message);
	}

	public EObject getContextElement() {
		return element;
	}
	
	@Override
	public String getMessage() {
		if (element != null) {
			return contextToString() + ": " + super.getMessage();
		} else {
			return super.getMessage();
		}
	}
	
	protected String contextToString() {
		return element.getClass().getSimpleName().replaceAll("Impl", "") + "#" + ModelUtil.getFeature(element, "id");
	}
}
