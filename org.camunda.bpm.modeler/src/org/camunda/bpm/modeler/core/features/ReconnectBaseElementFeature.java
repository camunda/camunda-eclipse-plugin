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
package org.camunda.bpm.modeler.core.features;

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.features.impl.DefaultReconnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class ReconnectBaseElementFeature extends DefaultReconnectionFeature {

	public ReconnectBaseElementFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void postReconnect(IReconnectionContext context) {
		super.postReconnect(context);

		String reconnectType = context.getReconnectType();
		
		Connection connection = context.getConnection();
		PictogramElement targetPictogramElement = context.getTargetPictogramElement();
		
		BPMNEdge bpmnEdge = BusinessObjectUtil.getFirstElementOfType(connection, BPMNEdge.class);
		
		DiagramElement diagramElement = BusinessObjectUtil.getFirstElementOfType(targetPictogramElement, DiagramElement.class);
		if (reconnectType.equals(ReconnectionContext.RECONNECT_TARGET)) {
			bpmnEdge.setTargetElement(diagramElement);
		} else {
			bpmnEdge.setSourceElement(diagramElement);
		}

		BaseElement flow = BusinessObjectUtil.getFirstElementOfType(connection, BaseElement.class);
		BaseElement be = BusinessObjectUtil.getFirstElementOfType(targetPictogramElement, BaseElement.class);
		if (reconnectType.equals(ReconnectionContext.RECONNECT_TARGET)) {
			EStructuralFeature feature = flow.eClass().getEStructuralFeature("targetRef");
			if (feature != null)
				flow.eSet(feature, be);
		} else {
			EStructuralFeature feature = flow.eClass().getEStructuralFeature("sourceRef");
			if (feature != null)
				flow.eSet(feature, be);
		}
		
		// layout if possible
		layoutPictogramElement(connection);
	}
}