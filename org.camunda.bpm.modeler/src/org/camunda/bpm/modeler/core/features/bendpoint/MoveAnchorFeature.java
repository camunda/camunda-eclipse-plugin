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
import org.eclipse.graphiti.features.context.IMoveAnchorContext;
import org.eclipse.graphiti.features.impl.DefaultMoveAnchorFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;

public class MoveAnchorFeature extends DefaultMoveAnchorFeature {

	public MoveAnchorFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public void postMoveAnchor(IMoveAnchorContext context) {
		super.postMoveAnchor(context);
		for (Connection connection : context.getAnchor().getOutgoingConnections()) {
			updateConnectionDi(context, connection, false);
		}
		
		for (Connection connection : context.getAnchor().getIncomingConnections()) {
			updateConnectionDi(context, connection, true);
		}
	}

	private void updateConnectionDi(IMoveAnchorContext context, Connection connection, final boolean last) {
		DIUtils.updateDIEdge(connection);
	}
}