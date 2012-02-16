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
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.ResourceAssignmentExpression;
import org.eclipse.bpmn2.ResourceParameterBinding;
import org.eclipse.bpmn2.ResourceRole;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ActivityPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.Bpmn2EditorPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.CallActivityPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.DataAssociationPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ErrorPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.FormalExpressionPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.InterfacePropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ItemAwareElementPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ItemDefinitionPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.MessageFlowPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.MessagePropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ResourceAssignmentExpressionPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ResourceParameterBindingPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ResourceRolePropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ScriptTaskPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.SequenceFlowPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.TaskPropertiesAdapter;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.bpmn2.util.Bpmn2Switch;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class adds a name-value map to the Bpmn2ItemProviderAdapterFactory.
 * This allows for additional customization of the UI.
 */
public class Bpmn2EditorItemProviderAdapterFactory extends Bpmn2ItemProviderAdapterFactory {

	public Bpmn2EditorItemProviderAdapterFactory() {
		super();
		supportedTypes.add(Bpmn2EditorPropertiesAdapter.class);
	}

	@Override
	public Adapter adaptNew(Notifier object, Object type) {
		if (type == Bpmn2EditorPropertiesAdapter.class) {
			return modelSwitch.doSwitch((EObject) object);
		}
		return super.adaptNew(object, type);
	}
	
    protected Bpmn2Switch<Bpmn2EditorPropertiesAdapter> modelSwitch = new Bpmn2ExtendedPropertiesSwitch(this);
    
    public class Bpmn2ExtendedPropertiesSwitch extends Bpmn2Switch<Bpmn2EditorPropertiesAdapter> {

    	private Bpmn2EditorItemProviderAdapterFactory adapterFactory;
    	
    	public Bpmn2ExtendedPropertiesSwitch(Bpmn2EditorItemProviderAdapterFactory adapterFactory) {
    		super();
    		this.adapterFactory = adapterFactory;
    	}
    	
        @Override
		public Bpmn2EditorPropertiesAdapter defaultCase(EObject object) {
        	Bpmn2EditorPropertiesAdapter adapter = new Bpmn2EditorPropertiesAdapter(adapterFactory,object);
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
					if (object instanceof EObject) {
						EObject eo = (EObject)object;
						EStructuralFeature feature = eo.eClass().getEStructuralFeature("name");
						String name = eo.eClass().getName();
						if (feature!=null && eo.eIsSet(feature)) {
							return name+" "+eo.eGet(feature);
						}
						else
							return name;
					}
					return super.getText(context);
				}
        	});
        	return adapter;
		}

		@Override
        public Bpmn2EditorPropertiesAdapter caseScriptTask(ScriptTask object) {
			return new ScriptTaskPropertiesAdapter(adapterFactory,object);
        }

        @Override
        public Bpmn2EditorPropertiesAdapter caseCallActivity(CallActivity object) {
        	return new CallActivityPropertiesAdapter(adapterFactory,object);
        }

		@Override
		public Bpmn2EditorPropertiesAdapter caseTask(Task object) {
			return new TaskPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseActivity(Activity object) {
        	return new ActivityPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseSequenceFlow(SequenceFlow object) {
        	return new SequenceFlowPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseFormalExpression(FormalExpression object) {
	    	return new FormalExpressionPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseItemDefinition(ItemDefinition object) {
        	return new ItemDefinitionPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseItemAwareElement(ItemAwareElement object) {
        	return new ItemAwareElementPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseResourceAssignmentExpression(ResourceAssignmentExpression object) {
        	return new ResourceAssignmentExpressionPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseResourceRole(ResourceRole object) {
        	return new ResourceRolePropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseDataAssociation(DataAssociation object) {
        	return new DataAssociationPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseError(Error object) {
        	return new ErrorPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseResourceParameterBinding(ResourceParameterBinding object) {
        	return new ResourceParameterBindingPropertiesAdapter(adapterFactory,object);
		}
		
		@Override
		public Bpmn2EditorPropertiesAdapter caseMessageFlow(MessageFlow object) {
        	return new MessageFlowPropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseMessage(Message object) {
        	return new MessagePropertiesAdapter(adapterFactory,object);
		}

		@Override
		public Bpmn2EditorPropertiesAdapter caseInterface(Interface object) {
			return new InterfacePropertiesAdapter(adapterFactory,object);
		}

		
    };
	
}
