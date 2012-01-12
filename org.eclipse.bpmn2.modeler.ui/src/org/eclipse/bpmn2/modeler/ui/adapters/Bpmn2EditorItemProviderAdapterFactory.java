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
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.ItemKind;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.Messages;
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
		supportedTypes.add(Bpmn2ExtendedPropertiesAdapter.class);
	}

	@Override
	public Adapter adaptNew(Notifier object, Object type) {
		if (type == Bpmn2ExtendedPropertiesAdapter.class) {
			return modelSwitch.doSwitch((EObject) object);
		}
		return super.adaptNew(object, type);
	}
	
    protected Bpmn2Switch<Bpmn2ExtendedPropertiesAdapter> modelSwitch = new Bpmn2Switch<Bpmn2ExtendedPropertiesAdapter>() {

        @Override
		public Bpmn2ExtendedPropertiesAdapter defaultCase(EObject object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(Bpmn2EditorItemProviderAdapterFactory.this,object);
        	adapter.setObjectDescriptor(new Bpmn2ObjectDescriptor(object) {
				@Override
				public String getLabel(Object context) {
					EObject object = this.object;
					if (context instanceof EObject)
						object = (EObject)context;
					if (ModelUtil.isStructureRefValue(object)) {
						return "Item Type";
					}
					return super.getLabel(context);
				}

				@Override
				public String getText(Object context) {
					EObject object = this.object;
					if (context instanceof EObject)
						object = (EObject)context;
					if (ModelUtil.isStructureRefValue(object)) {
						return ModelUtil.getStructureRefValue(object);
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
        public Bpmn2ExtendedPropertiesAdapter caseScriptTask(ScriptTask object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = caseTask(object);
        	adapter.setProperty(Bpmn2ExtendedPropertiesAdapter.LONG_DESCRIPTION, Messages.UI_ScriptTask_long_description); //$NON-NLS-1$
        	return adapter;
        }

        @Override
        public Bpmn2ExtendedPropertiesAdapter caseCallActivity(CallActivity object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(Bpmn2EditorItemProviderAdapterFactory.this,object);
        	adapter.setProperty(Bpmn2ExtendedPropertiesAdapter.LONG_DESCRIPTION, Messages.UI_CallActivity_long_description); //$NON-NLS-1$
        	adapter.setProperty(Bpmn2Package.CALL_ACTIVITY__CALLED_ELEMENT_REF, Bpmn2ExtendedPropertiesAdapter.UI_CAN_CREATE_NEW, Boolean.FALSE);
        	return adapter;
        }

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseTask(Task object) {
			Bpmn2ExtendedPropertiesAdapter adapter = caseActivity(object);
			addInteractionNodeProperties(adapter);
			return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseActivity(Activity object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(Bpmn2EditorItemProviderAdapterFactory.this,object);
        	addActivityProperties(adapter);
        	return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseSequenceFlow(SequenceFlow object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(Bpmn2EditorItemProviderAdapterFactory.this,object);
        	return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseFormalExpression(FormalExpression object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(Bpmn2EditorItemProviderAdapterFactory.this,object);
        	EStructuralFeature body = Bpmn2Package.eINSTANCE.getFormalExpression_Body();
        	adapter.setFeatureDescriptor(body,
    			new Bpmn2FeatureDescriptor(object,body) {
    				@Override
    				public String getLabel(Object context) {
						EObject object = this.object;
    					if (context instanceof EObject)
    						object = (EObject)context;
    					if (object.eContainer() instanceof SequenceFlow)
    						return "Constraint";
    					return super.getLabel(context);
    				}

					@Override
					public boolean isMultiLine(Object context) {
						// formal expression body is always a multiline text field
						return true;
					}
    			}
        	);
        	return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseItemDefinition(ItemDefinition object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(Bpmn2EditorItemProviderAdapterFactory.this,object);
        	EStructuralFeature ref = Bpmn2Package.eINSTANCE.getItemDefinition_StructureRef();
        	adapter.setFeatureDescriptor(ref,
    			new Bpmn2FeatureDescriptor(object,ref) {
    				@Override
    				public String getLabel(Object context) {
						EObject object = this.object;
    					if (context instanceof EObject)
    						object = (EObject)context;
    					if (object instanceof ItemDefinition) {
        					ItemDefinition itemDefinition = (ItemDefinition) object;
        					if (itemDefinition.getItemKind().equals(ItemKind.PHYSICAL))
        						return "Physical Type";
        					else if (itemDefinition.getItemKind().equals(ItemKind.INFORMATION))
        						return "Informational Type";
    					}
    					return super.getLabel(context);
    				}

					@Override
					public String getText(Object context) {
						EObject object = this.object;
    					if (context instanceof EObject)
    						object = (EObject)context;
    					if (object instanceof ItemDefinition) {
    						return ModelUtil.getStructureRefValue( ((ItemDefinition)object).getStructureRef() );
    					}
    					return super.getText(context);
					}
    			}
        	);
        	return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseItemAwareElement(ItemAwareElement object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(Bpmn2EditorItemProviderAdapterFactory.this,object);
        	EStructuralFeature ref = Bpmn2Package.eINSTANCE.getItemAwareElement_ItemSubjectRef();
        	adapter.setFeatureDescriptor(ref,
    			new Bpmn2FeatureDescriptor(object,ref) {
    				@Override
    				public String getLabel(Object context) {
						EObject object = this.object;
    					if (context instanceof EObject)
    						object = (EObject)context;
    					ItemDefinition itemDefinition = null;
    					if (object instanceof ItemDefinition)
    						itemDefinition = (ItemDefinition) object;
    					else if (object instanceof ItemAwareElement)
    						itemDefinition = (ItemDefinition) object.eGet(feature);
    					if (itemDefinition!=null) {
    						Bpmn2ExtendedPropertiesAdapter adapter = (Bpmn2ExtendedPropertiesAdapter) AdapterUtil.adapt(itemDefinition, Bpmn2ExtendedPropertiesAdapter.class);
    						return adapter.getFeatureDescriptor(Bpmn2Package.eINSTANCE.getItemDefinition_StructureRef()).getLabel(itemDefinition);
    					}
    					return ModelUtil.getLabel(object) + " Type";
    				}

					@Override
					public String getText(Object context) {
						EObject object = this.object;
    					if (context instanceof EObject)
    						object = (EObject)context;
    					ItemDefinition itemDefinition = null;
    					if (object instanceof ItemDefinition)
    						itemDefinition = (ItemDefinition) object;
    					else if (object instanceof ItemAwareElement)
    						itemDefinition = (ItemDefinition) object.eGet(feature);
    					if (itemDefinition!=null) {
    						Bpmn2ExtendedPropertiesAdapter adapter = (Bpmn2ExtendedPropertiesAdapter) AdapterUtil.adapt(itemDefinition, Bpmn2ExtendedPropertiesAdapter.class);
    						return adapter.getFeatureDescriptor(Bpmn2Package.eINSTANCE.getItemDefinition_StructureRef()).getText(itemDefinition);
    					}
						return super.getText(context);
					}
    			}
        	);
        	return adapter;
		}

		private void addActivityProperties(Bpmn2ExtendedPropertiesAdapter adapter) {
        	adapter.setProperty(Bpmn2Package.ACTIVITY__LOOP_CHARACTERISTICS, Bpmn2ExtendedPropertiesAdapter.UI_CAN_CREATE_NEW, Boolean.FALSE);
        	adapter.setProperty(Bpmn2Package.ACTIVITY__LOOP_CHARACTERISTICS, Bpmn2ExtendedPropertiesAdapter.UI_CAN_EDIT, Boolean.FALSE);
		}

		private void addInteractionNodeProperties(Bpmn2ExtendedPropertiesAdapter adapter) {
        	adapter.setProperty(Bpmn2Package.INTERACTION_NODE__INCOMING_CONVERSATION_LINKS, Bpmn2ExtendedPropertiesAdapter.UI_CAN_CREATE_NEW, Boolean.FALSE);
        	adapter.setProperty(Bpmn2Package.INTERACTION_NODE__OUTGOING_CONVERSATION_LINKS, Bpmn2ExtendedPropertiesAdapter.UI_CAN_CREATE_NEW, Boolean.FALSE);
		}
    };
	
}
