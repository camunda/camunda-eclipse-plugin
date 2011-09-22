/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;

/**
 * @author Bob Brodt
 *
 */
public abstract class AbstractBpmn2CreateConnectionFeature extends
		AbstractCreateConnectionFeature {

	/**
	 * @param fp
	 * @param name
	 * @param description
	 */
	public AbstractBpmn2CreateConnectionFeature(IFeatureProvider fp,
			String name, String description) {
		super(fp, name, description);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.ICreateConnection#canCreate(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.ICreateConnection#create(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public Connection create(ICreateConnectionContext context) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.ICreateConnection#canStartConnection(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		return false;
	}
	
	/**
	 * @return
	 */
	public abstract Class getBusinessObjectClass();
}
