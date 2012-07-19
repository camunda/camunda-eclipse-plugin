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
 * @author Bob Brodt
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.adapters;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CallChoreography;
import org.eclipse.bpmn2.CallConversation;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.CorrelationKey;
import org.eclipse.bpmn2.CorrelationPropertyBinding;
import org.eclipse.bpmn2.CorrelationPropertyRetrievalExpression;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.Escalation;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.MultiInstanceLoopCharacteristics;
import org.eclipse.bpmn2.Operation;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.ReceiveTask;
import org.eclipse.bpmn2.ResourceAssignmentExpression;
import org.eclipse.bpmn2.ResourceParameterBinding;
import org.eclipse.bpmn2.ResourceRole;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SendTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.Signal;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.PropertyExtensionDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ActivityPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.CallActivityPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.CallChoreographyPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.CallConversationPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.CompensateEventDefinitionPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.CorrelationKeyPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.CorrelationPropertyBindingPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.CorrelationPropertyRetrievalExpressionPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.DataAssociationPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.DataInputPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.DataObjectReferencePropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.DataOutputPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ErrorPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.EscalationPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.FlowElementPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.FormalExpressionPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.InterfacePropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ItemAwareElementPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ItemDefinitionPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.MessageFlowPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.MessagePropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.MultiInstanceLoopCharacteristicsPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.OperationPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ParticipantPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.PropertyPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ReceiveTaskPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ResourceAssignmentExpressionPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ResourceParameterBindingPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ResourceRolePropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.RootElementPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ScriptTaskPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.SendTaskPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.SequenceFlowPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ServiceTaskPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.SignalPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.TaskPropertiesAdapter;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.bpmn2.util.Bpmn2Switch;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;

/**
 * This class adds a name-value map to the Bpmn2ItemProviderAdapterFactory.
 * This allows for additional customization of the UI.
 */
public class Bpmn2EditorItemProviderAdapterFactory extends Bpmn2ItemProviderAdapterFactory {

	public Bpmn2EditorItemProviderAdapterFactory() {
		super();
		supportedTypes.add(ExtendedPropertiesAdapter.class);
	}

	@Override
	public Adapter adaptNew(Notifier object, Object type) {
		if (type == ExtendedPropertiesAdapter.class && object instanceof EObject) {
			return bpmn2ModelSwitch.doSwitch((EObject) object);
		}
		return super.adaptNew(object, type);
	}

	@Override
	protected void associate(Adapter adapter, Notifier target) {
		if (adapter != null) {
			if (!target.eAdapters().contains(adapter))
				target.eAdapters().add(adapter);
		}
	}

    protected Bpmn2Switch<ExtendedPropertiesAdapter> bpmn2ModelSwitch = new Bpmn2ExtendedPropertiesSwitch(this);
    
    public class Bpmn2ExtendedPropertiesSwitch extends Bpmn2Switch<ExtendedPropertiesAdapter> {

    	private AdapterFactory adapterFactory;
    	
    	public Bpmn2ExtendedPropertiesSwitch(AdapterFactory adapterFactory) {
    		super();
    		this.adapterFactory = adapterFactory;
    	}
    	
        @Override
		public ExtendedPropertiesAdapter defaultCase(EObject object) {
        	ExtendedPropertiesAdapter adapter = new ExtendedPropertiesAdapter(adapterFactory,object);
        	adapter.setObjectDescriptor(new ObjectDescriptor(adapterFactory, object) {
				@Override
				public String getLabel(Object context) {
					EObject object = this.object;
					if (context instanceof EObject)
						object = (EObject)context;
					if (ModelUtil.isStringWrapper(object)) {
						return "Item Type";
					}
					return super.getLabel(context);
				}

				@Override
				public String getText(Object context) {
					EObject object = this.object;
					if (context instanceof EObject)
						object = (EObject)context;
					if (ModelUtil.isStringWrapper(object)) {
						return ModelUtil.getStringWrapperValue(object);
					}
					return super.getText(context);
				}
        	});
        	return adapter;
		}

        private ExtendedPropertiesAdapter getTargetRuntimeAdapter(EObject object) {
			PropertyExtensionDescriptor ped = TargetRuntime.getCurrentRuntime().getPropertyExtension(object.getClass());
			if (ped!=null)
				return ped.getAdapter(adapterFactory,object);
			return null;
        }
        
		@Override
        public ExtendedPropertiesAdapter caseScriptTask(ScriptTask object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new ScriptTaskPropertiesAdapter(adapterFactory,object);
        }

        @Override
        public ExtendedPropertiesAdapter caseCallActivity(CallActivity object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new CallActivityPropertiesAdapter(adapterFactory,object);
        }

		@Override
		public ExtendedPropertiesAdapter caseTask(Task object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new TaskPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseActivity(Activity object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new ActivityPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseSequenceFlow(SequenceFlow object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new SequenceFlowPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseFormalExpression(FormalExpression object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
	    	return new FormalExpressionPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseItemDefinition(ItemDefinition object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new ItemDefinitionPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseItemAwareElement(ItemAwareElement object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new ItemAwareElementPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseResourceAssignmentExpression(ResourceAssignmentExpression object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new ResourceAssignmentExpressionPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseResourceRole(ResourceRole object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new ResourceRolePropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseDataAssociation(DataAssociation object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new DataAssociationPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseError(Error object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new ErrorPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseResourceParameterBinding(ResourceParameterBinding object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new ResourceParameterBindingPropertiesAdapter(adapterFactory,object);
		}
		
		@Override
		public ExtendedPropertiesAdapter caseMessageFlow(MessageFlow object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new MessageFlowPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseMessage(Message object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new MessagePropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseInterface(Interface object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new InterfacePropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseOperation(Operation object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new OperationPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseReceiveTask(ReceiveTask object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new ReceiveTaskPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseSendTask(SendTask object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new SendTaskPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseCorrelationPropertyRetrievalExpression(CorrelationPropertyRetrievalExpression object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new CorrelationPropertyRetrievalExpressionPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseProperty(Property object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new PropertyPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseMultiInstanceLoopCharacteristics(MultiInstanceLoopCharacteristics object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new MultiInstanceLoopCharacteristicsPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseFlowElement(FlowElement object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new FlowElementPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseDataInput(DataInput object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new DataInputPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseDataOutput(DataOutput object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new DataOutputPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseServiceTask(ServiceTask object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new ServiceTaskPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseCompensateEventDefinition(CompensateEventDefinition object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new CompensateEventDefinitionPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseRootElement(RootElement object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new RootElementPropertiesAdapter(adapterFactory,object);
		}


		// TODO: add remaining BPMN2 elements

		@Override
		public ExtendedPropertiesAdapter caseDataObjectReference(DataObjectReference object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new DataObjectReferencePropertiesAdapter(adapterFactory,object);
		}

		@Override
		public ExtendedPropertiesAdapter caseEscalation(Escalation object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new EscalationPropertiesAdapter(adapterFactory,object);
		}

        @Override
        public ExtendedPropertiesAdapter caseCallChoreography(CallChoreography object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new CallChoreographyPropertiesAdapter(adapterFactory,object);
        }

        @Override
        public ExtendedPropertiesAdapter caseCallConversation(CallConversation object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new CallConversationPropertiesAdapter(adapterFactory,object);
        }

        @Override
        public ExtendedPropertiesAdapter caseCorrelationKey(CorrelationKey object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new CorrelationKeyPropertiesAdapter(adapterFactory,object);
        }

        @Override
        public ExtendedPropertiesAdapter caseCorrelationPropertyBinding(CorrelationPropertyBinding object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new CorrelationPropertyBindingPropertiesAdapter(adapterFactory,object);
        }

        @Override
        public ExtendedPropertiesAdapter caseParticipant(Participant object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
        	return new ParticipantPropertiesAdapter(adapterFactory,object);
        }

		@Override
		public ExtendedPropertiesAdapter caseSignal(Signal object) {
			ExtendedPropertiesAdapter adapter = getTargetRuntimeAdapter(object);
			if (adapter!=null)
				return adapter;
			return new SignalPropertiesAdapter(adapterFactory,object);
		}
    };
}
