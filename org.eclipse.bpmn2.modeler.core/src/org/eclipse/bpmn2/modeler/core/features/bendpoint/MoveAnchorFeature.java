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

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.dd.dc.Point;
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

	private void updateConnectionDi(IMoveAnchorContext context,
			Connection connection, final boolean last) {
		BaseElement element = (BaseElement) BusinessObjectUtil.getFirstElementOfType(connection, BaseElement.class);
		ModelHandler modelHandler;
		try {
			modelHandler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			BPMNEdge edge = (BPMNEdge) modelHandler.findDIElement(element);
			
			int waypointIndex = last ? edge.getWaypoint().size()-1 : 0; 
			
			Point p = edge.getWaypoint().get(waypointIndex);
			p.setX(context.getX());
			p.setY(context.getY());
		} catch (Exception e) {
			Activator.logError(e);
		}
	}

}