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

import java.util.List;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.BaseElementConnectionFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractAddFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractReconnectFlowFeature;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.internal.datatypes.impl.LocationImpl;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class AssociationFeatureContainer extends BaseElementConnectionFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof Association;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractAddFlowFeature(fp) {

			@Override
			protected void decorateConnectionLine(Polyline connectionLine) {
				connectionLine.setLineWidth(2);
				connectionLine.setLineStyle(LineStyle.DOT);
			}

			@Override
			protected void hook(IAddContext context, Connection connection,
					BaseElement element) {
				super.hook(context, connection, element);
				AddConnectionContext ac = (AddConnectionContext)context;
				Anchor sourceAnchor = ac.getSourceAnchor();
				Anchor targetAnchor = ac.getTargetAnchor();
				AnchorContainer sourceAnchorContainer = sourceAnchor.getParent();
				AnchorContainer targetAnchorContainer = targetAnchor.getParent();
				if (sourceAnchorContainer instanceof FreeFormConnection) {
					AnchorUtil.createConnectionPoint(getFeatureProvider(),
							(FreeFormConnection)sourceAnchorContainer,
							Graphiti.getPeLayoutService().getConnectionMidpoint((FreeFormConnection)sourceAnchorContainer, 0.5));
				}
				if (targetAnchorContainer instanceof FreeFormConnection) {
					AnchorUtil.createConnectionPoint(getFeatureProvider(),
							(FreeFormConnection)targetAnchorContainer,
							Graphiti.getPeLayoutService().getConnectionMidpoint((FreeFormConnection)targetAnchorContainer, 0.5));
				}
			}

			@Override
			protected Class<? extends BaseElement> getBoClass() {
				return Association.class;
			}
		};
	}

	@Override
	public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
		return new CreateAssociationFeature(fp);
	}

	@Override
	public IReconnectionFeature getReconnectionFeature(IFeatureProvider fp) {
		// TODO Auto-generated method stub
		return new ReconnectAssociationFeature(fp);
	}

	public static class CreateAssociationFeature extends AbstractCreateFlowFeature<BaseElement, BaseElement> {

		public CreateAssociationFeature(IFeatureProvider fp) {
			super(fp, "Association", "Associate information with artifacts and flow objects");
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_ASSOCIATION;
		}

		@Override
		protected Class<BaseElement> getSourceClass() {
			return BaseElement.class;
		}

		@Override
		protected Class<BaseElement> getTargetClass() {
			return BaseElement.class;
		}

		@Override
		protected BaseElement createFlow(ModelHandler mh, BaseElement source, BaseElement target) {
			return mh.createAssociation(source, target);
		}
	}
	
	
	public static class ReconnectAssociationFeature extends AbstractReconnectFlowFeature {

		public ReconnectAssociationFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canReconnect(IReconnectionContext context) {
			BaseElement targetElement = BusinessObjectUtil.getFirstElementOfType(context.getTargetPictogramElement(), BaseElement.class);
			if (targetElement instanceof Association)
				return false;
			PictogramElement targetPictogramElement = context.getTargetPictogramElement();
			if (targetPictogramElement instanceof FreeFormConnection) {
				return true;
			}
			return super.canReconnect(context);
		}

		@Override
		protected Class<? extends EObject> getTargetClass() {
			return BaseElement.class;
		}

		@Override
		protected Class<? extends EObject> getSourceClass() {
			return BaseElement.class;
		}

		@Override
		public void preReconnect(IReconnectionContext context) {
			PictogramElement targetPictogramElement = context.getTargetPictogramElement();
			if (targetPictogramElement instanceof FreeFormConnection) {
				Shape connectionPointShape = AnchorUtil.createConnectionPoint(
						getFeatureProvider(),
						(FreeFormConnection)targetPictogramElement,
						context.getTargetLocation());
				
				if (context instanceof ReconnectionContext) {
					ReconnectionContext rc = (ReconnectionContext) context;
					rc.setNewAnchor(AnchorUtil.getConnectionPointAnchor(connectionPointShape));
					rc.setTargetPictogramElement(connectionPointShape);
				}
			}
			super.preReconnect(context);
		}

		@Override
		public void postReconnect(IReconnectionContext context) {
			Anchor oldAnchor = context.getOldAnchor();
			AnchorContainer oldAnchorContainer = oldAnchor.getParent();
			if (AnchorUtil.isConnectionPoint(oldAnchorContainer) && oldAnchorContainer.getLink()!=null) {
				List<Connection> allConnections = Graphiti.getPeService().getAllConnections(oldAnchor);
				if (allConnections.size()==0) {
					// remove the bendpoint from target connection if there are no other connections going to it
					FreeFormConnection oldTargetConnection = (FreeFormConnection) oldAnchorContainer.getLink().getBusinessObjects().get(0);
					
					Point bp = null;
					for (Point p : oldTargetConnection.getBendpoints()) {
						if (AnchorUtil.isConnectionPointNear(oldAnchorContainer, p, 0)) {
							bp = p;
							break;
						}
					}
					if (bp!=null)
						oldTargetConnection.getBendpoints().remove(bp);
					
					RemoveContext ctx = new RemoveContext(oldAnchorContainer);
					getFeatureProvider().getRemoveFeature(ctx).remove(ctx);
				}
			}
			super.postReconnect(context);
		}
	} 
	
}