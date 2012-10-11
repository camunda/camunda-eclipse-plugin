/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.importer.util;

import org.eclipse.bpmn2.modeler.core.Activator;

/**
 * Handles error logging in a eclipse agnostic way
 * 
 * @author Nico Rehwaldt
 */
public class ErrorLogger {

	public static void log(Exception e) {
		Activator.logError(e);
	}
	
	public static <T extends Exception> void logAndThrow(T e) throws T {
		log(e);
		throw e;
	}
}
