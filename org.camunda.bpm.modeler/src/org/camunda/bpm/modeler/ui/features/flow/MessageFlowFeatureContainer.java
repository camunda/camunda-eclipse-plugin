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
package org.camunda.bpm.modeler.ui.features.flow;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.features.DirectEditNamedConnectionFeature;
import org.camunda.bpm.modeler.core.features.UpdateBaseElementNameFeature;
import org.camunda.bpm.modeler.core.features.container.BaseElementConnectionFeatureContainer;
import org.camunda.bpm.modeler.core.features.flow.AbstractAddFlowFeature;
import org.camunda.bpm.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.camunda.bpm.modeler.core.features.flow.AbstractReconnectFlowFeature;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations.CreateConnectionOperation;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations.ReconnectConnectionOperation;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations.StartFormCreateConnectionOperation;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IContext;
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

	private static final EClass MESSAGE_FLOW = Bpmn2Package.eINSTANCE.getMessageFlow();

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof MessageFlow;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddMessageFlowFeature(fp);
	}

	@Override
	public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
		return new CreateMessageFlowFeature(fp);
	}
	
	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return new DirectEditNamedConnectionFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new UpdateBaseElementNameFeature(fp);
	}

	@Override
	public IReconnectionFeature getReconnectionFeature(IFeatureProvider fp) {
		return new ReconnectMessageFlowFeature(fp);
	}
	
	public static class AddMessageFlowFeature extends AbstractAddFlowFeature<MessageFlow> {
		
		public AddMessageFlowFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected Polyline createConnectionLine(Connection connection) {
			IPeService peService = Graphiti.getPeService();
			IGaService gaService = Graphiti.getGaService();
			BaseElement be = BusinessObjectUtil.getFirstBaseElement(connection);

			Polyline connectionLine = super.createConnectionLine(connection);
			connectionLine.setLineStyle(LineStyle.DASH);
			connectionLine.setLineWidth(2);

			ConnectionDecorator endDecorator = peService.createConnectionDecorator(connection, false, 1.0, true);
			ConnectionDecorator startDecorator = peService.createConnectionDecorator(connection, false, 0, true);

			int w = 5;
			int l = 10;
			
			Polyline arrowhead = gaService.createPolygon(endDecorator, new int[] { -l, w, 0, 0, -l, -w, -l, w });
			StyleUtil.applyStyle(arrowhead, be);
			arrowhead.setBackground(manageColor(IColorConstant.WHITE));

			Ellipse circle = gaService.createEllipse(startDecorator);
			gaService.setSize(circle, 10, 10);
			StyleUtil.applyStyle(circle, be);
			circle.setBackground(manageColor(IColorConstant.WHITE));
			
			return connectionLine;
		}

		@Override
		protected EClass getBusinessObjectClass() {
			return MESSAGE_FLOW;
		}
		
		@Override
		protected boolean isCreateExternalLabel() {
			return true;
		}
	}

	public static class CreateMessageFlowFeature extends AbstractCreateFlowFeature<MessageFlow, InteractionNode, InteractionNode> {
		
		public CreateMessageFlowFeature(IFeatureProvider fp) {
			super(fp, "Message Flow", "Represents message between two participants");
		}

		@Override
		public boolean canStartConnection(ICreateConnectionContext context) {
			context.putProperty(ConnectionOperations.CONNECTION_TYPE, MESSAGE_FLOW);
			StartFormCreateConnectionOperation operation = ConnectionOperations.getStartFromConnectionCreateOperation(context);
			return operation.canExecute(context);
		}

		@Override
		public boolean canCreate(ICreateConnectionContext context) {
			context.putProperty(ConnectionOperations.CONNECTION_TYPE, MESSAGE_FLOW);
			CreateConnectionOperation operation = ConnectionOperations.getConnectionCreateOperation(context);
			return operation.canExecute(context);
		}
		
		@Override
		public boolean isAvailable(IContext context) {
			if (context instanceof ICreateConnectionContext) {
				ICreateConnectionContext ccc = (ICreateConnectionContext) context;
				BaseElement o = BusinessObjectUtil.getFirstBaseElement(ccc.getSourcePictogramElement());
				
				if (o instanceof EndEvent) {
					return true;
				}
			}
			
			return super.isAvailable(context);
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_MESSAGE_FLOW;
		}

		@Override
		public MessageFlow createBusinessObject(ICreateConnectionContext context) {
			ModelHandler mh = ModelHandler.getInstance(getDiagram());
			InteractionNode source = getSourceBo(context);
			InteractionNode target = getTargetBo(context);
			MessageFlow bo = mh.createMessageFlow(source, target);
			bo.setName("");
			putBusinessObject(context, bo);
			
			return bo;
		}

		@Override
		protected Class<InteractionNode> getSourceClass() {
			return InteractionNode.class;
		}

		@Override
		protected Class<InteractionNode> getTargetClass() {
			return InteractionNode.class;
		}

		@Override
		public EClass getBusinessObjectClass() {
			return MESSAGE_FLOW;
		}
	}
	
	public static class ReconnectMessageFlowFeature extends AbstractReconnectFlowFeature {

		public ReconnectMessageFlowFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canReconnect(IReconnectionContext context) {
			context.putProperty(ConnectionOperations.CONNECTION_TYPE, MESSAGE_FLOW);
			ReconnectConnectionOperation operation = ConnectionOperations.getConnectionReconnectOperation(context);
			return operation.canExecute(context);
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