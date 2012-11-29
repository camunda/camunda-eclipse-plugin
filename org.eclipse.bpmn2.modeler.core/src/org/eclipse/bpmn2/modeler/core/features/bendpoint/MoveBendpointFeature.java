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
package org.eclipse.bpmn2.modeler.core.features.bendpoint;

import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveBendpointContext;
import org.eclipse.graphiti.features.impl.DefaultMoveBendpointFeature;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class MoveBendpointFeature extends DefaultMoveBendpointFeature {

	public MoveBendpointFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean moveBendpoint(IMoveBendpointContext context) {
		boolean moved = super.moveBendpoint(context);
		
		FreeFormConnection connection = context.getConnection();
		
		DIUtils.updateDIEdge(connection);
		
		// also need to move the connection point if there is one at this bendpoint
		Shape connectionPointShape = AnchorUtil.getConnectionPointAt(connection, context.getBendpoint());
		if (connectionPointShape != null)  {
			AnchorUtil.setConnectionPointLocation(connectionPointShape, context.getX(), context.getY());
		}
		return moved;
	}
}