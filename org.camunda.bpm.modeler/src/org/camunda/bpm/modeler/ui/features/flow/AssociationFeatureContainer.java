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

import java.util.List;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.features.container.BaseElementConnectionFeatureContainer;
import org.camunda.bpm.modeler.core.features.flow.AbstractAddFlowFeature;
import org.camunda.bpm.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.camunda.bpm.modeler.core.features.flow.AbstractReconnectFlowFeature;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations.CreateConnectionOperation;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations.ReconnectConnectionOperation;
import org.camunda.bpm.modeler.core.features.rules.ConnectionOperations.StartFormCreateConnectionOperation;
import org.camunda.bpm.modeler.core.utils.AnchorUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.runtime.engine.model.BoundaryEvent;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class AssociationFeatureContainer extends BaseElementConnectionFeatureContainer {

	private static final EClass ASSOCIATION = Bpmn2Package.eINSTANCE.getAssociation();
	
	protected CreateConnectionContext createContext;

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof Association;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractAddFlowFeature<Association>(fp) {

			@Override
			public PictogramElement add(IAddContext context) {
				AddConnectionContext ac = (AddConnectionContext)context;
				Anchor sourceAnchor = ac.getSourceAnchor();
				Anchor targetAnchor = ac.getTargetAnchor();
				PictogramElement source = sourceAnchor==null ? null : sourceAnchor.getParent();
				PictogramElement target = targetAnchor==null ? null : targetAnchor.getParent();
				boolean anchorChanged = false;
				
				if (createContext!=null) {
					if (source==null) {
						source = createContext.getSourcePictogramElement();
						sourceAnchor = createContext.getSourceAnchor();
					}
					if (target==null) {
						target = createContext.getTargetPictogramElement();
						targetAnchor = createContext.getTargetAnchor();
					}
				}
				
				if (sourceAnchor==null && source instanceof FreeFormConnection) {
					Shape connectionPointShape = AnchorUtil.createConnectionPoint(getFeatureProvider(),
							(FreeFormConnection)source,
							Graphiti.getPeLayoutService().getConnectionMidpoint((FreeFormConnection)source, 0.5));
					sourceAnchor = AnchorUtil.getConnectionPointAnchor(connectionPointShape);
					anchorChanged = true;
				}
				if (targetAnchor==null && target instanceof FreeFormConnection) {
					Shape connectionPointShape = AnchorUtil.createConnectionPoint(getFeatureProvider(),
							(FreeFormConnection)target,
							Graphiti.getPeLayoutService().getConnectionMidpoint((FreeFormConnection)target, 0.5));
					targetAnchor = AnchorUtil.getConnectionPointAnchor(connectionPointShape);
					anchorChanged = true;
				}
				
				// this is silly! why are there no setters for sourceAnchor and targetAnchor in AddConnectionContext???
				if (anchorChanged) {
					AddConnectionContext newContext = new AddConnectionContext(sourceAnchor, targetAnchor);
					newContext.setSize(ac.getHeight(), ac.getWidth());
					newContext.setLocation(ac.getX(), ac.getY());
					newContext.setNewObject(getBusinessObject(ac));
					newContext.setTargetConnection(ac.getTargetConnection());
					newContext.setTargetConnectionDecorator(ac.getTargetConnectionDecorator());
					newContext.setTargetContainer(ac.getTargetContainer());
					
					context = newContext;
				}
				// we're done with this
				createContext = null;
				
				return super.add(context);
			}
			
			@Override
			protected Polyline createConnectionLine(Connection connection) {
				Polyline connectionLine = super.createConnectionLine(connection);
				connectionLine.setLineWidth(2);
				connectionLine.setLineStyle(LineStyle.DOT);
				return connectionLine;
			}
			
			@Override
			protected EClass getBusinessObjectClass() {
				return ASSOCIATION;
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

	public class CreateAssociationFeature extends AbstractCreateFlowFeature<Association, BaseElement, BaseElement> {

		public CreateAssociationFeature(IFeatureProvider fp) {
			super(fp, "Association", "Associate information with artifacts and flow objects");
		}

		@Override
		public boolean isAvailable(IContext context) {
			if (context instanceof ICreateConnectionContext) {
				ICreateConnectionContext ccc = (ICreateConnectionContext)context;
				
				if (ccc.getSourcePictogramElement()!=null) {
					BaseElement o = BusinessObjectUtil.getFirstElementOfType(ccc.getSourcePictogramElement(), BaseElement.class);
					
					if (o instanceof BoundaryEvent) {
						BoundaryEvent boundaryEvent = (BoundaryEvent) o;
						List<EventDefinition> eventDefinitions = boundaryEvent.getEventDefinitions();
						
						if (!eventDefinitions.isEmpty() && eventDefinitions.size() == 1) {
							return eventDefinitions.get(0) instanceof CompensateEventDefinition;
						}
					}
					
					return o instanceof Artifact;
				}
			}
			
			return super.isAvailable(context);
		}
		
		@Override
		public boolean canStartConnection(ICreateConnectionContext context) {
			if (context.getSourcePictogramElement() instanceof Diagram) {
				return false;
			}
			
			context.putProperty(ConnectionOperations.CONNECTION_TYPE, ASSOCIATION);
			StartFormCreateConnectionOperation operation = ConnectionOperations.getStartFromConnectionCreateOperation(context);
			return operation.canExecute(context);
		}
		
		@Override
		public boolean canCreate(ICreateConnectionContext context) {
			if (context.getTargetPictogramElement() instanceof Diagram) {
				return false;
			}
			
			context.putProperty(ConnectionOperations.CONNECTION_TYPE, ASSOCIATION);
			CreateConnectionOperation operation = ConnectionOperations.getConnectionCreateOperation(context);
			return operation.canExecute(context);
		}

		@Override
		public Connection create(ICreateConnectionContext context) {
			// save the CreateContext because we'll need it in AddFeature
			createContext = (CreateConnectionContext)context;
			Anchor sourceAnchor = createContext.getSourceAnchor();
			Anchor targetAnchor = createContext.getTargetAnchor();
			PictogramElement source = createContext.getSourcePictogramElement();
			PictogramElement target = createContext.getTargetPictogramElement();
			
			if (sourceAnchor==null && source instanceof FreeFormConnection) {
				Shape connectionPointShape = AnchorUtil.createConnectionPoint(getFeatureProvider(),
						(FreeFormConnection)source,
						Graphiti.getPeLayoutService().getConnectionMidpoint((FreeFormConnection)source, 0.5));
				sourceAnchor = AnchorUtil.getConnectionPointAnchor(connectionPointShape);
				createContext.setSourceAnchor(sourceAnchor);
			}
			if (targetAnchor==null && target instanceof FreeFormConnection) {
				Shape connectionPointShape = AnchorUtil.createConnectionPoint(getFeatureProvider(),
						(FreeFormConnection)target,
						Graphiti.getPeLayoutService().getConnectionMidpoint((FreeFormConnection)target, 0.5));
				targetAnchor = AnchorUtil.getConnectionPointAnchor(connectionPointShape);
				createContext.setTargetAnchor(targetAnchor);
			}

			return super.create(context);
		}

		@Override
		protected String getStencilImageId() {
			return Images.IMG_16_ASSOCIATION;
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
		protected BaseElement getSourceBo(ICreateConnectionContext context) {
			Anchor anchor = context.getSourceAnchor();
			if (anchor != null && anchor.getParent() instanceof Shape) {
				Shape shape = (Shape) anchor.getParent();
				Connection conn = AnchorUtil.getConnectionPointOwner(shape);
				if (conn!=null) {
					return BusinessObjectUtil.getFirstElementOfType(conn, getTargetClass());
				}
				return BusinessObjectUtil.getFirstElementOfType(shape, getTargetClass());
			}
			return null;
		}

		@Override
		protected BaseElement getTargetBo(ICreateConnectionContext context) {
			Anchor anchor = context.getTargetAnchor();
			if (anchor != null && anchor.getParent() instanceof Shape) {
				Shape shape = (Shape) anchor.getParent();
				Connection conn = AnchorUtil.getConnectionPointOwner(shape);
				if (conn!=null) {
					return BusinessObjectUtil.getFirstElementOfType(conn, getTargetClass());
				}
				return BusinessObjectUtil.getFirstElementOfType(shape, getTargetClass());
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.AbstractBpmn2CreateConnectionFeature#getBusinessObjectClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return ASSOCIATION;
		}

		@Override
		public Association createBusinessObject(ICreateConnectionContext context) {
			ModelHandler mh = ModelHandler.getInstance(getDiagram());
			
			BaseElement source = getSourceBo(context);
			BaseElement target = getTargetBo(context);
			Association bo = mh.createAssociation(source, target);
			putBusinessObject(context, bo);
			
			return bo;
		}
	}
	
	
	public static class ReconnectAssociationFeature extends AbstractReconnectFlowFeature {

		public ReconnectAssociationFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canReconnect(IReconnectionContext context) {
			context.putProperty(ConnectionOperations.CONNECTION_TYPE, ASSOCIATION);
			ReconnectConnectionOperation operation = ConnectionOperations.getConnectionReconnectOperation(context);
			return operation.canExecute(context);
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
			AnchorUtil.deleteConnectionPointIfPossible(getFeatureProvider(), (Shape) oldAnchorContainer);
			super.postReconnect(context);
		}
	} 
	
}