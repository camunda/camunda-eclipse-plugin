/*******************************************************************************
 * Copyright (c) 2006 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.camunda.bpm.modeler.core.adapters;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Sep 18, 2006
 *
 */
public class AbstractStatefulAdapter extends AbstractAdapter implements
		IStatefullAdapter {
	
	@Override
	public void setTarget (Object newTarget) {		
		super.setTarget(newTarget);
	}

}
