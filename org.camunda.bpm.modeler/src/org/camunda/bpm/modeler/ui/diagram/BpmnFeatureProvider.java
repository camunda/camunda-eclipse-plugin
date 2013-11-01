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
import org.camunda.bpm.modeler.core.features.DefaultBpmn2DecorateFeature;
import org.camunda.bpm.modeler.core.features.DefaultBpmn2DeleteShapeFeature;
import org.camunda.bpm.modeler.core.features.DefaultBpmn2RemoveShapeFeature;
import org.camunda.bpm.modeler.core.features.api.IDecorateContext;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.features.api.container.IFeatureContainer;
import org.camunda.bpm.modeler.core.features.bendpoint.AddBendpointFeature;
import org.camunda.bpm.modeler.core.features.bendpoint.MoveAnchorFeature;
import org.camunda.bpm.modeler.core.features.bendpoint.MoveBendpointFeature;
import org.camunda.bpm.modeler.core.features.bendpoint.RemoveBendpointFeature;
import org.camunda.bpm.modeler.core.features.container.ConnectionFeatureContainer;
import org.camunda.bpm.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.features.activity.subprocess.AdHocSubProcessFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.subprocess.CallActivityFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.subprocess.TransactionFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.BusinessRuleTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.ManualTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.ReceiveTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.ScriptTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.SendTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.ServiceTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.activity.task.UserTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.artifact.GroupFeatureContainer;
import org.camunda.bpm.modeler.ui.features.artifact.TextAnnotationFeatureContainer;
import org.camunda.bpm.modeler.ui.features.choreography.CallChoreographyFeatureContainer;
import org.camunda.bpm.modeler.ui.features.choreography.ChoreographyMessageLinkFeatureContainer;
import org.camunda.bpm.modeler.ui.features.choreography.ChoreographyTaskFeatureContainer;
import org.camunda.bpm.modeler.ui.features.choreography.SubChoreographyFeatureContainer;
import org.camunda.bpm.modeler.ui.features.conversation.ConversationFeatureContainer;
import org.camunda.bpm.modeler.ui.features.conversation.ConversationLinkFeatureContainer;
import org.camunda.bpm.modeler.ui.features.data.DataInputFeatureContainer;
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
import org.camunda.bpm.modeler.ui.features.validation.ValidateDiagramFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
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
import org.eclipse.graphiti.internal.features.context.impl.base.PictogramElementContext;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;

/**
 * 
 */
public class BpmnFeatureProvider extends DefaultFeatureProvider {

	private List<IFeatureContainer> featureContainers;
	private List<IFeatureContainer> customFeatureContainers;

	private ICreateFeature[] createFeatures;
	private ICreateConnectionFeature[] createConnectionFeatures;

	private HashMap<EClass, IFeature> classToCreateFeatureMap = new HashMap<EClass, IFeature>();

	public BpmnFeatureProvider(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);

		Assert.isLegal(diagramTypeProvider instanceof Bpmn2DiagramTypeProvider, "Expected diagramTypeProvider to be an instance of Bpmn2DiagramTypeProvider");
		
		featureContainers = createDefaultContainers();
		customFeatureContainers = new ArrayList<IFeatureContainer>();
		
		updateFeatureLists();
	}
	
	protected List<IFeatureContainer> createDefaultContainers() {
		ArrayList<IFeatureContainer> containers = new ArrayList<IFeatureContainer>();
		
		containers.add(new LabelFeatureContainer());
		containers.add(new ConnectionLabelFeatureContainer());
		containers.add(new GroupFeatureContainer());
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
		
		return containers;
	}

	public void addCustomFeatureContainer(IFeatureContainer customFeatureContainer) {
		featureContainers.add(0, customFeatureContainer);
		customFeatureContainers.add(customFeatureContainer);
		
		updateFeatureLists();
	}
	
	@SuppressWarnings("rawtypes")
	private void updateFeatureLists() {
		
		createFeatures = updateCreateFeatures();
		createConnectionFeatures = updateCreateConnectionFeatures();

		classToCreateFeatureMap.clear();
		
		for (IFeature cf : createFeatures) {

			if (cf instanceof AbstractBpmn2CreateFeature) {

				AbstractBpmn2CreateFeature acf = (AbstractBpmn2CreateFeature)cf;

				EClass cls = acf.getBusinessObjectClass();
				cls = getActualEClass(cls);
				
				classToCreateFeatureMap.put(cls, cf);
			}
		}

		for (IFeature cf : createConnectionFeatures) {
			if (cf instanceof AbstractCreateFlowFeature) {
				AbstractCreateFlowFeature acf = (AbstractCreateFlowFeature)cf;

				EClass cls = acf.getBusinessObjectClass();
				cls = getActualEClass(cls);

				classToCreateFeatureMap.put(cls, cf);
			}
		}
	}
	
	private ICreateConnectionFeature[] updateCreateConnectionFeatures() {
		List<ICreateConnectionFeature> features = new ArrayList<ICreateConnectionFeature>();

		for (IFeatureContainer container : featureContainers) {
			if (container instanceof ConnectionFeatureContainer) {
				ConnectionFeatureContainer connectionFeatureContainer = (ConnectionFeatureContainer) container;
				ICreateConnectionFeature createConnectionFeature = connectionFeatureContainer.getCreateConnectionFeature(this);
				
				if (createConnectionFeature != null) {
					features.add(createConnectionFeature);
				}
			}
		}

		return features.toArray(new ICreateConnectionFeature[0]);
	}

	private ICreateFeature[] updateCreateFeatures() {

		List<ICreateFeature> features = new ArrayList<ICreateFeature>();

		for (IFeatureContainer container : featureContainers) {
			ICreateFeature createFeature = container.getCreateFeature(this);
			if (createFeature != null) {
				features.add(createFeature);
			}
		}

		return features.toArray(new ICreateFeature[0]);
	}

	public IFeatureContainer getFeatureContainer(IContext context) {
		for (IFeatureContainer container : featureContainers) {
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
		IFeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			IAddFeature feature = container.getAddFeature(this);
			if (feature != null) {
				return feature;
			}
		}
		return super.getAddFeature(context);
	}

	public IDecorateFeature getDecorateFeature(IDecorateContext context) {
		IFeatureContainer container = getFeatureContainer(context);
		if (container != null) {
			IDecorateFeature feature = container.getDecorateFeature(this);
			if (feature != null) {
				return feature;
			}
		}
		
		return null;
	}
	
	@Override
	public ICreateFeature[] getCreateFeatures() {
		return createFeatures;
	}

	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		IFeatureContainer container = getFeatureContainer(context);
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
		IFeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			IDirectEditingFeature feature = container.getDirectEditingFeature(this);
			if (feature != null)
				return feature;
		}
		return super.getDirectEditingFeature(context);
	}

	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		IFeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			ILayoutFeature feature = container.getLayoutFeature(this);
			if (feature != null)
				return feature;
		}
		return super.getLayoutFeature(context);
	}

	@Override
	public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
		IFeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			IMoveShapeFeature feature = container.getMoveFeature(this);
			if (feature != null)
				return feature;
		}
		return super.getMoveShapeFeature(context);
	}

	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
		IFeatureContainer container = getFeatureContainer(context);
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
		for (IFeatureContainer container : featureContainers) {
			
			if (container instanceof ConnectionFeatureContainer) {
				
				ConnectionFeatureContainer connectionFeatureContainer = (ConnectionFeatureContainer) container;
				if (canApplyFeatureContainer(connectionFeatureContainer, context)) {
					
					return connectionFeatureContainer.getReconnectionFeature(this);
				}
			}
		}
		
		return super.getReconnectionFeature(context);
	}

	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		IFeatureContainer container = getFeatureContainer(context);
		if (container!=null) {
			IDeleteFeature feature = container.getDeleteFeature(this);
			if (feature != null) {
				return feature;
			}
		}
		
		return new DefaultBpmn2DeleteShapeFeature(this);
	}

	@Override
	public IRemoveFeature getRemoveFeature(IRemoveContext context) {
		IFeatureContainer container = getFeatureContainer(context);
		if (container != null) {
			IRemoveFeature feature = container.getRemoveFeature(this);
			if (feature != null) {
				return feature;
			}
		}
		
		return new DefaultBpmn2RemoveShapeFeature(this);
	}
	
	@Override
	public ICustomFeature[] getCustomFeatures(ICustomContext context) {
		List<ICustomFeature> list = new ArrayList<ICustomFeature>();

		for (IFeatureContainer fc : featureContainers) {
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

		if (isDiagramSelected(context)) {
			list.add(new ValidateDiagramFeature(this));
		}
		
		return list.toArray(new ICustomFeature[list.size()]);
	}
	
	private boolean isDiagramSelected(ICustomContext context) {
		PictogramElement[] pictogramElements = context.getPictogramElements();
		return pictogramElements != null && pictogramElements.length == 1 && pictogramElements[0] instanceof Diagram;
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
			}
		};
	}
	
	/**
	 * Return a list of custom create features
	 * @return
	 */
	public List<IFeature> getCustomCreateFeatures() {
		
		List<IFeature> createFeatures = new ArrayList<IFeature>();
		for (IFeatureContainer container : customFeatureContainers) {
			
			ICreateFeature createFeature = container.getCreateFeature(this);
			createFeatures.add(createFeature);
		}
		
		return createFeatures;
	}
	
	// TODO: move this to the adapter registry
	public IFeature getCreateFeatureForPictogramElement(PictogramElement pictogramElement) {
		
		// try to find create feature based on matching 
		// custom feature container
		for (IFeatureContainer featureContainer : featureContainers) {
			
			if (canApplyFeatureContainer(featureContainer, pictogramElement)) {
				return featureContainer.getCreateFeature(this);
			}
		}

		return null;
	}

	@SuppressWarnings("restriction")
	private boolean canApplyFeatureContainer(IFeatureContainer featureContainer, PictogramElement pictogramElement) {
		return canApplyFeatureContainer(featureContainer, new PictogramElementContext(pictogramElement));
	}
	
	private boolean canApplyFeatureContainer(IFeatureContainer featureContainer, IContext context) {
		Object object = featureContainer.getApplyObject(context);
		return object != null && featureContainer.canApplyTo(object);
	}
	
	@Override
	public Bpmn2DiagramTypeProvider getDiagramTypeProvider() {
		return (Bpmn2DiagramTypeProvider) super.getDiagramTypeProvider();
	}

	private EClass getActualEClass(EClass cls) {
		if (cls.getEPackage().equals(Bpmn2Package.eINSTANCE)) {
			EClassifier modelClassifier = ModelPackage.eINSTANCE.getEClassifier(cls.getName());
			if (modelClassifier instanceof EClass) {
				EClass modelCls = (EClass) modelClassifier;
				if (cls.isSuperTypeOf(modelCls)) {
					return modelCls;
				}
			}
		}
		
		return cls;
	}
	
	public IFeature getCreateFeatureForBusinessObject(EClass cls) {

		cls = getActualEClass(cls);

		if (classToCreateFeatureMap.containsKey(cls)) {
			return classToCreateFeatureMap.get(cls);
		}

		return null;
	}
}