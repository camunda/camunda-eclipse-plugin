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
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.features.flow;

import java.io.IOException;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.BaseElementConnectionFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.ReconnectBaseElementFeature;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractAddFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractReconnectFlowFeature;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.features.choreography.ChoreographyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;

public class MessageFlowFeatureContainer extends BaseElementConnectionFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof MessageFlow;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractAddFlowFeature(fp) {

			@Override
			protected void decorateConnectionLine(Polyline connectionLine) {
				connectionLine.setLineStyle(LineStyle.DASH);
			}

			@Override
			protected void createConnectionDecorators(Connection connection) {
				IPeService peService = Graphiti.getPeService();
				IGaService gaService = Graphiti.getGaService();

				ConnectionDecorator endDecorator = peService.createConnectionDecorator(connection, false, 1.0, true);
				ConnectionDecorator startDecorator = peService.createConnectionDecorator(connection, false, 0, true);

				int w = 5;
				int l = 15;
				
				Polyline polyline = gaService.createPolygon(endDecorator, new int[] { -l, w, 0, 0, -l, -w, -l, w });
				polyline.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				polyline.setBackground(manageColor(IColorConstant.WHITE));
				polyline.setFilled(true);
				polyline.setLineWidth(1);

				Ellipse ellipse = gaService.createEllipse(startDecorator);
				ellipse.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				ellipse.setBackground(manageColor(IColorConstant.WHITE));
				ellipse.setFilled(true);
				ellipse.setLineWidth(1);
				gaService.setSize(ellipse, 10, 10);
			}

			@Override
			protected Class<? extends BaseElement> getBoClass() {
				return MessageFlow.class;
			}
		};
	}

	@Override
	public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
		return new CreateMessageFlowFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(new UpdateBaseElementNameFeature(fp));
		// TODO: any other updates needed?
		return multiUpdate;
	}

	@Override
	public IReconnectionFeature getReconnectionFeature(IFeatureProvider fp) {
		return new ReconnectMessageFlowFeature(fp);
	}
	
	public static class CreateMessageFlowFeature extends AbstractCreateFlowFeature<InteractionNode, InteractionNode> {

		public CreateMessageFlowFeature(IFeatureProvider fp) {
			super(fp, "Message Flow", "Represents message between two participants");
		}

		@Override
		public boolean canStartConnection(ICreateConnectionContext context) {
			if (ChoreographyUtil.isChoreographyParticipantBand(context.getSourcePictogramElement()))
				return false;
			return true;
		}

		@Override
		public boolean canCreate(ICreateConnectionContext context) {
			if (ChoreographyUtil.isChoreographyParticipantBand(context.getSourcePictogramElement()))
				return false;
			if (context.getTargetPictogramElement()!=null) {
				if (ChoreographyUtil.isChoreographyParticipantBand(context.getTargetPictogramElement()))
					return false;
			}
			InteractionNode source = getSourceBo(context);
			InteractionNode target = getTargetBo(context);
			return super.canCreate(context) && isDifferentParticipants(source, target);
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_MESSAGE_FLOW;
		}

		@Override
		protected BaseElement createFlow(ModelHandler mh, InteractionNode source, InteractionNode target) {
			MessageFlow flow = mh.createMessageFlow(source, target);
			flow.setName("");
			return flow;
		}

		@Override
		protected Class<InteractionNode> getSourceClass() {
			return InteractionNode.class;
		}

		@Override
		protected Class<InteractionNode> getTargetClass() {
			return InteractionNode.class;
		}

		private boolean isDifferentParticipants(InteractionNode source, InteractionNode target) {
			if (source == null || target == null) {
				return true;
			}
			boolean different = false;
			try {
				ModelHandler handler = ModelHandler.getInstance(getDiagram());
				Participant sourceParticipant = handler.getParticipant(source);
				Participant targetParticipant = handler.getParticipant(target);
				if (sourceParticipant==null) {
					if (targetParticipant==null)
						return true;
					return false;
				}
				different = !sourceParticipant.equals(targetParticipant);
			} catch (IOException e) {
				Activator.logError(e);
			}
			return different;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateConnectionFeature#getBusinessObjectClass()
		 */
		@Override
		public Class getBusinessObjectClass() {
			return MessageFlow.class;
		}
	}
	
	public static class ReconnectMessageFlowFeature extends AbstractReconnectFlowFeature {

		public ReconnectMessageFlowFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected Class<? extends EObject> getTargetClass() {
			return InteractionNode.class;
		}

		@Override
		protected Class<? extends EObject> getSourceClass() {
			return InteractionNode.class;
		}
	} 
}