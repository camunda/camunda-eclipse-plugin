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
import org.camunda.bpm.modeler.core.utils.AnchorUtil;
import org.camunda.bpm.modeler.core.utils.ConnectionLabelUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IRemoveBendpointContext;
import org.eclipse.graphiti.features.impl.DefaultRemoveBendpointFeature;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class RemoveBendpointFeature extends DefaultRemoveBendpointFeature {

	public RemoveBendpointFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public boolean canRemoveBendpoint(IRemoveBendpointContext context) {
		Shape connectionPointShape = AnchorUtil.getConnectionPointAt(context.getConnection(), context.getBendpoint());
		if (connectionPointShape!=null)
			return false;
		
		return super.canRemoveBendpoint(context);
	}

	@Override
	public void removeBendpoint(IRemoveBendpointContext context) {
	    super.removeBendpoint(context);
	    
		FreeFormConnection connection = context.getConnection();

		repositionLabel(connection);

		updateDi(connection);
	}

	protected void repositionLabel(FreeFormConnection connection) {
		LabelUtil.repositionConnectionLabel(connection, getFeatureProvider());
	}

	protected void updateDi(FreeFormConnection connection) {
		DIUtils.updateDI(connection);
	}
}