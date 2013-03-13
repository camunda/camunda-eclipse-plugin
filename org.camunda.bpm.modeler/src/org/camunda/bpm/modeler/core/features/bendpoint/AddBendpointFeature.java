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
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core.features.bendpoint;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddBendpointContext;
import org.eclipse.graphiti.features.impl.DefaultAddBendpointFeature;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

public class AddBendpointFeature extends DefaultAddBendpointFeature {

	public AddBendpointFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void addBendpoint(IAddBendpointContext context) {
		super.addBendpoint(context);
	
		FreeFormConnection connection = context.getConnection();
		DIUtils.updateDIEdge(connection);
	}
}