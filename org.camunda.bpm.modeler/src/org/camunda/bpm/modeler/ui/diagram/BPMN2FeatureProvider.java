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
package org.camunda.bpm.modeler.ui.diagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.features.AbstractBpmn2CreateFeature;
import org.camunda.bpm.modeler.core.features.DefaultDeleteBPMNShapeFeature;
import org.camunda.bpm.modeler.core.features.DefaultRemoveBPMNShapeFeature;
import org.camunda.bpm.modeler.core.features.activity.task.ICustomTaskFeature;
import org.camunda.bpm.modeler.core.features.bendpoint.AddBendpointFeature;
import org.camunda.bpm.modeler.core.features.bendpoint.MoveAnchorFeature;
import org.camunda.bpm.modeler.core.features.bendpoint.MoveBendpointFeature;
import org.camunda.bpm.modeler.core.features.bendpoint.RemoveBendpointFeature;
import org.camunda.bpm.modeler.core.features.container.ConnectionFeatureContainer;
import org.camunda.bpm.modeler.core.features.container.FeatureContainer;
import org.camunda.bpm.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.ui.features.activity.subprocess.AdHocSubProcessFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.subprocess.CallActivityFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.subprocess.TransactionFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.BusinessRuleTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.CustomTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.ManualTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.ReceiveTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.ScriptTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.SendTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.ServiceTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.UserTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.CustomTaskFeatureContainer.CreateCustomTaskFeature;
import org.camunda.bpm.modeler.ui.features.artifact.GroupFeatureContainer;
import org.camunda.bpm.modeler.ui.features.artifact.TextAnnotationFeatureContainer;
import org.camunda.bpm.modeler.ui.features.choreography.CallChoreographyFeatureContainer;
import org.camunda.bpm.modeler.ui.features.choreography.ChoreographyMessageLinkFeatureContainer;
import org.camunda.bpm.modeler.ui.features.choreography.ChoreographyTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.choreography.SubChoreographyFeatureContainer;
import org.camunda.bpm.modeler.ui.features.conversation.ConversationFeatureContainer;
import org.camunda.bpm.modeler.ui.features.conversation.ConversationLinkFeatureContainer;
import org.camunda.bpm.modeler.ui.features.data.DataInputFeatureContainer;
import org.camunda.bpm.modeler.ui.features.data.DataObjectFeatureContainer;
import org.camunda.bpm.modeler.ui.features.data.DataObjectReferenceFeatureContainer;
import org.camunda.bpm.modeler.ui.features.data.DataOutputFeatureContainer;
import org.camunda.bpm.modeler.ui.features.data.DataStoreReferenceFeatureContainer;
import org.camunda.bpm.modeler.ui.features.data.MessageFeatureContainer;
import org.camunda.bpm.modeler.ui.features.event.BoundaryEventFeatureContainer;
import org.camunda.bpm.modeler.ui.features.event.EndEventFeatureContainer;
import org.camunda.bpm.modeler.ui.features.event.IntermediateCatchEventFeatureContainer;
import org.camunda.bpm.modeler.ui.features.event.IntermediateThrowEventFeatureContainer;
import org.camunda.bpm.modeler.ui.features.event.StartEventFeatureContainer;
import org.camunda.bpm.modeler.ui.features.event.definitions.CancelEventDefinitionContainer;
import org.camunda.bpm.modeler.ui.features.event.definitions.CompensateEventDefinitionContainer;
import org.camunda.bpm.modeler.ui.features.event.definitions.ConditionalEventDefinitionContainer;
import org.camunda.bpm.modeler.ui.features.event.definitions.ErrorEventDefinitionContainer;
import org.camunda.bpm.modeler.ui.features.event.definitions.EscalationEventDefinitionContainer;
import org.camunda.bpm.modeler.ui.features.event.definitions.LinkEventDefinitionContainer;
import org.camunda.bpm.modeler.ui.features.event.definitions.MessageEventDefinitionContainer;
import org.camunda.bpm.modeler.ui.features.event.definitions.SignalEventDefinitionContainer;
import org.camunda.bpm.modeler.ui.features.event.definitions.TerminateEventDefinitionFeatureContainer;
import org.camunda.bpm.modeler.ui.features.event.definitions.TimerEventDefinitionContainer;
import org.camunda.bpm.modeler.ui.features.flow.AssociationFeatureContainer;
import org.camunda.bpm.modeler.ui.features.flow.DataAssociationFeatureContainer;
import org.camunda.bpm.modeler.ui.features.flow.MessageFlowFeatureContainer;
import org.camunda.bpm.modeler.ui.features.flow.SequenceFlowFeatureContainer;
import org.camunda.bpm.modeler.ui.features.gateway.ComplexGatewayFeatureContainer;
import org.camunda.bpm.modeler.ui.features.gateway.EventBasedGatewayFeatureContainer;
import org.camunda.bpm.modeler.ui.features.gateway.ExclusiveGatewayFeatureContainer;
import org.camunda.bpm.modeler.ui.features.gateway.InclusiveGatewayFeatureContainer;
import org.camunda.bpm.modeler.ui.features.gateway.ParallelGatewayFeatureContainer;
import org.camunda.bpm.modeler.ui.features.label.ConnectionLabelFeatureContainer;
import org.camunda.bpm.modeler.ui.features.label.LabelFeatureContainer;
import org.camunda.bpm.modeler.ui.features.lane.LaneFeatureContainer;
import org.camunda.bpm.modeler.ui.features.participant.ParticipantFeatureContainer;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddBendpointFeature;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveAnchorFeature;
import org.eclipse.graphiti.features.IMoveBendpointFeature;
import org.eclipse.graphiti.features.IMoveConnectionDecoratorFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.IRemoveBendpointFeature;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddBendpointContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveAnchorContext;
import org.eclipse.graphiti.features.context.IMoveBendpointContext;
import org.eclipse.graphiti.features.context.IMoveConnectionDecoratorContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.IRemoveBendpointContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.features.impl.DefaultMoveConnectionDecoratorFeature;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;

/**
 * 
 */
public class BPMN2FeatureProvider extends DefaultFeatureProvider {

	private List<FeatureContainer> containers;

	private ICreateFeature[] createFeatures;

	private ICreateConnectionFeature[] createConnectionFeatures;

	public BPMN2FeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);

		containers = new ArrayList<FeatureContainer>();
		containers.add(new LabelFeatureContainer());
		containers.add(new ConnectionLabelFeatureContainer());
		containers.add(new GroupFeatureContainer());
		containers.add(new DataObjectFeatureContainer());
		containers.add(new DataObjectReferenceFeatureContainer());
		containers.add(new DataStoreReferenceFeatureContainer());
		containers.add(new DataInputFeatureContainer());
		containers.add(new DataOutputFeatureContainer());
		containers.add(new MessageFeatureContainer());
		containers.add(new StartEventFeatureContainer());
		containers.add(new EndEventFeatureContainer());
		containers.add(new IntermediateCatchEventFeatureContainer());
		containers.add(new IntermediateThrowEventFeatureContainer());
		containers.add(new BoundaryEventFeatureContainer());
		containers.add(new TaskFeatureContainer());
		containers.add(new ScriptTaskFeatureContainer());
		containers.add(new ServiceTaskFeatureContainer());
		containers.add(new UserTaskFeatureContainer());
		containers.add(new ManualTaskFeatureContainer());
		containers.add(new BusinessRuleTaskFeatureContainer());
		containers.add(new SendTaskFeatureContainer());
		containers.add(new ReceiveTaskFeatureContainer());
		containers.add(new ChoreographyTaskFeatureContainer());
		containers.add(new ExclusiveGatewayFeatureContainer());
		containers.add(new InclusiveGatewayFeatureContainer());
		containers.add(new ParallelGatewayFeatureContainer());
		containers.add(new EventBasedGatewayFeatureContainer());
		containers.add(new ComplexGatewayFeatureContainer());
		containers.add(new AdHocSubProcessFeatureContainer());
		containers.add(new CallActivityFeatureContainer());
		containers.add(new TransactionFeatureContainer());
		containers.add(new SubProcessFeatureContainer());
		containers.add(new ConditionalEventDefinitionContainer());
		containers.add(new MessageEventDefinitionContainer());
		containers.add(new TimerEventDefinitionContainer());
		containers.add(new SignalEventDefinitionContainer());
		containers.add(new EscalationEventDefinitionContainer());
		containers.add(new CompensateEventDefinitionContainer());
		containers.add(new LinkEventDefinitionContainer());
		containers.add(new ErrorEventDefinitionContainer());
		containers.add(new CancelEventDefinitionContainer());
		containers.add(new TerminateEventDefinitionFeatureContainer());
		containers.add(new SequenceFlowFeatureContainer());
		containers.add(new MessageFlowFeatureContainer());
		containers.add(new AssociationFeatureContainer());
		containers.add(new ConversationFeatureContainer());
		containers.add(new ConversationLinkFeatureContainer());
		containers.add(new DataAssociationFeatureContainer());
		containers.add(new SubChoreographyFeatureContainer());
		containers.add(new CallChoreographyFeatureContainer());
		containers.add(new ParticipantFeatureContainer());
		containers.add(new LaneFeatureContainer());
		containers.add(new TextAnnotationFeatureContainer());
		containers.add(new ChoreographyMessageLinkFeatureContainer());
		containers.add(new LabelFeatureContainer());

		updateFeatureLists();
	}
	
	HashMap<Class,IFeature> mapBusinessObjectClassToCreateFeature = new HashMap<Class,IFeature>();

	public void addFeatureContainer(FeatureContainer fc) throws Exception {
		boolean canAdd = true;
		
		for (FeatureContainer container : containers) {
				if (container.getClass().isInstance(fc)) {
					canAdd = false;
					break;
				}
		}
		if (canAdd) {
			containers.add(fc);
			updateFeatureLists();
		}
		else
			throw new Exception("Attempt to add a Custom Feature with a duplicate ID "+fc.getClass().getName());
	}
	
	private void updateFeatureLists() {
		
		List<ICreateFeature> createFeaturesList = new ArrayList<ICreateFeature>();

		for (FeatureContainer container : containers) {
			ICreateFeature createFeature = container.getCreateFeature(this);
			if (createFeature != null) {
				createFeaturesList.add(createFeature);
			}
		}

		createFeatures = createFeaturesList.toArray(new ICreateFeature[createFeaturesList.size()]);

		List<ICreateConnectionFeature> createConnectionFeatureList = new ArrayList<ICreateConnectionFeature>();

		for (FeatureContainer c : containers) {
			if (c instanceof ConnectionFeatureContainer) {
				ConnectionFeatureContainer connectionFeatureContainer = (ConnectionFeatureContainer) c;
				ICreateConnectionFeature createConnectionFeature = connectionFeatureContainer
						.getCreateConnectionFeature(this);
				if (createConnectionFeature == null) {
					continue;
				}
				createConnectionFeatureList.add(createConnectionFeature);
			}
		}

		createConnectionFeatures = createConnectionFeatureList
				.toArray(new ICreateConnectionFeature[createConnectionFeatureList.size()]);
		
		mapBusinessObjectClassToCreateFeature.clear();
		for (IFeature cf : createFeatures) {
			if (cf instanceof AbstractBpmn2CreateFeature) {
				if (cf instanceof CreateCustomTaskFeature) {
					continue;
				}
				AbstractBpmn2CreateFeature acf = (AbstractBpmn2CreateFeature)cf;
				mapBusinessObjectClassToCreateFeature.put(acf.getBusinessObjectClass().getInstanceClass(), cf);
			}
		}
		for (IFeature cf : createConnectionFeatures) {
			if (cf instanceof AbstractCreateFlowFeature) {
				AbstractCreateFlowFeature acf = (AbstractCreateFlowFeature)cf;
				mapBusinessObjectClassToCreateFeature.put(acf.getBusinessObjectClass().getInstanceClass(), cf);
			}
		}
	}
	
	public FeatureContainer getFeatureContainer(IContext context) {
		Object id = CustomTaskFeatureContainer.getId(context); 
		for (FeatureContainer container : containers) {
			if (id!=null && !(container instanceof CustomTaskFeatureContainer))
				continue;
			Object o = container.getApplyObject(context);
			if (o != null && container.canApplyTo(o)) {
				return container;
			}
		}
		return null;
	}

	//  Copy and paste is not functional yet
	
//	@Override
//	public ICopyFeature getCopyFeature(ICopyContext context) {
//		return new CopyFeature(this);
//	}
//	
//	@Override
//	public IPasteFeature getPasteFeature(IPasteContext context) {
//		return new PasteFeature(this);
//	}
	
	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		FeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			IAddFeature feature = container.getAddFeature(this);
			if (feature != null)
				return feature;
		}
		return super.getAddFeature(context);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		return createFeatures;
	}

	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		FeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			IUpdateFeature feature = container.getUpdateFeature(this);
			if (feature != null)
				return feature;
		}
		return super.getUpdateFeature(context);
	}

	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return createConnectionFeatures;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IDirectEditingContext context) {
		FeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			IDirectEditingFeature feature = container.getDirectEditingFeature(this);
			if (feature != null)
				return feature;
		}
		return super.getDirectEditingFeature(context);
	}

	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		FeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			ILayoutFeature feature = container.getLayoutFeature(this);
			if (feature != null)
				return feature;
		}
		return super.getLayoutFeature(context);
	}

	@Override
	public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
		FeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			IMoveShapeFeature feature = container.getMoveFeature(this);
			if (feature != null)
				return feature;
		}
		return super.getMoveShapeFeature(context);
	}

	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
		FeatureContainer container = getFeatureContainer(context);
		if (container != null) {
			IResizeShapeFeature feature = container.getResizeFeature(this);
			if (feature != null)
				return feature;
		}
		
		return null;
	}

	@Override
	public IAddBendpointFeature getAddBendpointFeature(IAddBendpointContext context) {
		return new AddBendpointFeature(this);
	}

	@Override
	public IMoveBendpointFeature getMoveBendpointFeature(IMoveBendpointContext context) {
		return new MoveBendpointFeature(this);
	}
	
	@Override
	public IMoveAnchorFeature getMoveAnchorFeature(IMoveAnchorContext context) {
		return new MoveAnchorFeature(this);
	}
	
	@Override
	public IRemoveBendpointFeature getRemoveBendpointFeature(IRemoveBendpointContext context) {
		return new RemoveBendpointFeature(this);
	}

	@Override
	public IReconnectionFeature getReconnectionFeature(IReconnectionContext context) {
		for (FeatureContainer container : containers) {
			Object o = container.getApplyObject(context);
			if (o != null && container.canApplyTo(o) && container instanceof ConnectionFeatureContainer) {
				IReconnectionFeature feature = ((ConnectionFeatureContainer)container).getReconnectionFeature(this);
				if (feature == null) {
					break;
				}
				return feature;
			}
		}
		return super.getReconnectionFeature(context);
	}

	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		FeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			IDeleteFeature feature = container.getDeleteFeature(this);
			if (feature != null)
				return feature;
		}
		return new DefaultDeleteBPMNShapeFeature(this);
	}

	@Override
	public IRemoveFeature getRemoveFeature(IRemoveContext context) {
		FeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			IRemoveFeature feature = container.getRemoveFeature(this);
			if (feature != null)
				return feature;
		}
		return new DefaultRemoveBPMNShapeFeature(this);
	}
	
	@Override
	public ICustomFeature[] getCustomFeatures(ICustomContext context) {
		List<ICustomFeature> list = new ArrayList<ICustomFeature>();

		for (FeatureContainer fc : containers) {
			Object o = fc.getApplyObject(context);
			if (o!=null && fc.canApplyTo(o)) {
				ICustomFeature[] cfa = fc.getCustomFeatures(this);
				if (cfa!=null) {
					for (ICustomFeature cf : cfa) {
						boolean found = false;
						for (ICustomFeature cfl : list) {
							if (cfl != null && cf != null && cfl.getClass() == cf.getClass()) {
								found = true;
								break;
							}
						}
						if (!found)
							list.add(cf);
					}
				}
			}
		}

		return list.toArray(new ICustomFeature[list.size()]);
	}
	
	@Override
	public IMoveConnectionDecoratorFeature getMoveConnectionDecoratorFeature(IMoveConnectionDecoratorContext context) {
		return new DefaultMoveConnectionDecoratorFeature(this) {
			@Override
			public void moveConnectionDecorator(IMoveConnectionDecoratorContext context) {
				super.moveConnectionDecorator(context);
				
				ConnectionDecorator labelShape = context.getConnectionDecorator();
				
				BPMNEdge bpmnEdge = BusinessObjectUtil.getFirstElementOfType(labelShape, BPMNEdge.class);
				
				DIUtils.updateDILabel(labelShape, bpmnEdge);
				
				System.out.println(LayoutUtil.getAbsoluteBounds(labelShape));
			}
		};
	}

	// TODO: move this to the adapter registry
	public IFeature getCreateFeatureForPictogramElement(PictogramElement pe) {
		if (pe!=null) {
			String id = Graphiti.getPeService().getPropertyValue(pe,ICustomTaskFeature.CUSTOM_TASK_ID);
			if (id!=null) {
				for (FeatureContainer container : containers) {
					if (container instanceof CustomTaskFeatureContainer) {
						CustomTaskFeatureContainer ctf = (CustomTaskFeatureContainer)container;
						if (id.equals(ctf.getId())) {
							return ctf.getCreateFeature(this);
						}
					}
				}
			}

			EObject be = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			return getCreateFeatureForBusinessObject(be);
		}
		return null;
	}
	
	public IFeature getCreateFeatureForBusinessObject(Object be) {
		IFeature feature = null;
		if (be!=null) {
			Class[] ifs = be.getClass().getInterfaces();
			for (int i=0; i < ifs.length && feature==null; ++i) {
				feature = mapBusinessObjectClassToCreateFeature.get(ifs[i]);
			}
		}
		return feature;
	}
	
	public IFeature getCreateFeatureForBusinessObject(Class clazz) {
		return mapBusinessObjectClassToCreateFeature.get(clazz);
	}
}