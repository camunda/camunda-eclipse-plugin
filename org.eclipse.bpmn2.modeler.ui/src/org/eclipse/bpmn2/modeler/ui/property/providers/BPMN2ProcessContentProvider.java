/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.property.providers;

import java.util.List;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;

/**
 * Content provider for Process elements.
 * 
 * Expects a Role or a Definition or a context as input.
 */
public class BPMN2ProcessContentProvider extends AbstractContentProvider  {

	@Override
	public void collectElements(Object input, List list)  {
		
		if (input instanceof Definitions) {
			Definitions definitions = (Definitions) input;
			for (RootElement re : definitions.getRootElements()) {
				if (re instanceof Process)
					list.add(re);
			}
		} else if (input instanceof Process) {
			Process process = (Process) input;
			list.add(process);
			
		}
	}
}
