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
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.DataState;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.ItemKind;
import org.eclipse.bpmn2.ResourceAssignmentExpression;
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
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

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
	
    protected Bpmn2Switch<Bpmn2ExtendedPropertiesAdapter> modelSwitch = new Bpmn2ExtendedPropertiesSwitch(this);
    
    public class Bpmn2ExtendedPropertiesSwitch extends Bpmn2Switch<Bpmn2ExtendedPropertiesAdapter> {

    	private Bpmn2EditorItemProviderAdapterFactory adapterFactory;
    	
    	public Bpmn2ExtendedPropertiesSwitch(Bpmn2EditorItemProviderAdapterFactory adapterFactory) {
    		super();
    		this.adapterFactory = adapterFactory;
    	}
    	
        @Override
		public Bpmn2ExtendedPropertiesAdapter defaultCase(EObject object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(adapterFactory,object);
        	adapter.setObjectDescriptor(new Bpmn2ObjectDescriptor(adapterFactory, object) {
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
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(adapterFactory,object);
        	adapter.setProperty(Bpmn2ExtendedPropertiesAdapter.LONG_DESCRIPTION, Messages.UI_CallActivity_long_description); //$NON-NLS-1$
        	adapter.setProperty(Bpmn2Package.CALL_ACTIVITY__CALLED_ELEMENT_REF, Bpmn2ExtendedPropertiesAdapter.UI_CAN_CREATE_NEW, Boolean.TRUE);
        	EStructuralFeature ce = Bpmn2Package.eINSTANCE.getCallActivity_CalledElementRef();
        	adapter.setFeatureDescriptor(ce,
    			new Bpmn2FeatureDescriptor(adapterFactory,object,ce) {
    				@Override
    				public String getLabel(Object context) {
   						return "Called Activity";
    				}
    				
    				@Override
    				public String getText(Object context) {
    					CallActivity object = (CallActivity)this.object;
    					if (context instanceof CallActivity)
    						object = (CallActivity)context;
    					CallableElement ce = object.getCalledElementRef();
    					if (ce.eIsProxy())
    						return ((InternalEObject)ce).eProxyURI().lastSegment();
    					return super.getText(context);
    				}
    			}
        	);
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
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(adapterFactory,object);
        	addActivityProperties(adapter);
        	return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseSequenceFlow(SequenceFlow object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(adapterFactory,object);
        	return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseFormalExpression(FormalExpression object) {
        	final Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(adapterFactory,object);
        	final EStructuralFeature body = Bpmn2Package.eINSTANCE.getFormalExpression_Body();
        	adapter.setFeatureDescriptor(body,
    			new Bpmn2FeatureDescriptor(adapterFactory,object,body) {
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
			adapter.setObjectDescriptor(new Bpmn2ObjectDescriptor(adapterFactory, object) {
				@Override
				public String getText(Object context) {
					return adapter.getFeatureDescriptor(body).getText(context);
				}
			});

			return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseItemDefinition(ItemDefinition object) {
        	final Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(adapterFactory,object);
        	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getItemDefinition_StructureRef();
        	adapter.setFeatureDescriptor(ref,
    			new Bpmn2FeatureDescriptor(adapterFactory,object,ref) {
    				@Override
    				public String getLabel(Object context) {
						EObject object = this.object;
    					if (context instanceof EObject)
    						object = (EObject)context;
    					if (object instanceof ItemDefinition) {
    						return "Data Type";
    					}
    					return super.getLabel(context);
    				}

					@Override
					public String getText(Object context) {
						EObject object = this.object;
    					if (context instanceof EObject)
    						object = (EObject)context;
    					if (object instanceof ItemDefinition) {
        					ItemDefinition itemDefinition = (ItemDefinition) object;
        					if (itemDefinition.getStructureRef()!=null) {
        						String type = " (Undefined";
	        					if (itemDefinition.getItemKind().equals(ItemKind.PHYSICAL))
	        						type = " (Physical";
	        					else if (itemDefinition.getItemKind().equals(ItemKind.INFORMATION))
	        						type = " (Informational";
	        					if (itemDefinition.isIsCollection())
	        						type += " Collection)";
	        					else
	        						type += ")";
	
	    						return ModelUtil.getStructureRefValue( itemDefinition.getStructureRef() ) + type;
        					}
        					else
        						return "";
    					}
    					return super.getText(context);
					}
    			}
        	);
			adapter.setObjectDescriptor(new Bpmn2ObjectDescriptor(adapterFactory, object) {
				@Override
				public String getText(Object context) {
					return adapter.getFeatureDescriptor(ref).getText(context);
				}
			});
			return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseItemAwareElement(ItemAwareElement object) {
        	Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(adapterFactory,object);
        	EStructuralFeature ref = Bpmn2Package.eINSTANCE.getItemAwareElement_ItemSubjectRef();
        	adapter.setFeatureDescriptor(ref,
    			new Bpmn2FeatureDescriptor(adapterFactory,object,ref) {
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
        	
        	ref = Bpmn2Package.eINSTANCE.getItemAwareElement_DataState();
        	adapter.setFeatureDescriptor(ref,
    			new Bpmn2FeatureDescriptor(adapterFactory,object,ref) {
					@Override
					public void setValue(EObject context, Object value) {
						final EObject object = context==null ? this.object : context;
						if (value instanceof String) {
							// construct a DataState from the given name string
							DataState ds = Bpmn2Factory.eINSTANCE.createDataState();
							ds.setName((String)value);
							value = ds;
						}
						if (value instanceof DataState) {
							final DataState oldValue = (DataState) object.eGet(feature);
							if (value != oldValue) {
								// if this DataState belongs to some other ItemAwareElement, make a copy
								final DataState newValue = (DataState)(((DataState)value).eContainer()!=null ?
									clone((EObject) value) : value);
								TransactionalEditingDomain editingDomain = getEditingDomain(object);
								if (editingDomain == null) {
									object.eSet(feature, value);
								} else {
									editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
										@Override
										protected void doExecute() {
											object.eSet(feature, newValue);
											ModelUtil.setID(newValue);
										}
									});
								}
							}
						}
					}
    			}
        	);
        	adapter.setProperty(Bpmn2Package.ITEM_AWARE_ELEMENT__DATA_STATE, Bpmn2ExtendedPropertiesAdapter.UI_IS_MULTI_CHOICE, Boolean.FALSE);

        	return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseCallableElement(CallableElement object) {
			return super.caseCallableElement(object);
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseResourceAssignmentExpression(ResourceAssignmentExpression object) {
        	final Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(adapterFactory,object);
        	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getResourceAssignmentExpression_Expression();
        	adapter.setFeatureDescriptor(ref,
    			new Bpmn2FeatureDescriptor(adapterFactory,object,ref) {

					@Override
					public String getText(Object context) {
						EObject object = this.object;
    					if (context instanceof ResourceAssignmentExpression)
    						object = (EObject)context;
    					ResourceAssignmentExpression rae = null;
    					if (object instanceof ResourceAssignmentExpression)
    						rae = (ResourceAssignmentExpression) object;
    					if (rae!=null && rae.getExpression() instanceof FormalExpression) {
    						return ((FormalExpression)rae.getExpression()).getBody();
    					}
						return "";
					}

					@Override
					public void setValue(EObject context, Object value) {
						ResourceAssignmentExpression rae = (ResourceAssignmentExpression)this.object;
						if (!(rae.getExpression() instanceof FormalExpression)) {
							final FormalExpression e = Bpmn2Factory.eINSTANCE.createFormalExpression();
							e.setBody((String) value);
							TransactionalEditingDomain editingDomain = getEditingDomain(object);
							if (editingDomain == null) {
								object.eSet(feature, value);
							} else {
								editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
									@Override
									protected void doExecute() {
										object.eSet(feature, e);
										ModelUtil.setID(e);
									}
								});
							}
						}
					}
        		}
        	);
        	adapter.setObjectDescriptor(new Bpmn2ObjectDescriptor(adapterFactory, object) {
				@Override
				public String getText(Object context) {
					return adapter.getFeatureDescriptor(ref).getText(context);
				}
        	});

        	return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseDataAssociation(DataAssociation object) {
        	final Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(adapterFactory,object);
        	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getDataAssociation_SourceRef();
        	adapter.setFeatureDescriptor(ref,
    			new Bpmn2FeatureDescriptor(adapterFactory,object,ref) {

					@Override
					public void setValue(EObject context, final Object value) {
						final DataAssociation association = context instanceof DataAssociation ?
								(DataAssociation)context :
								(DataAssociation)this.object;
    					
						TransactionalEditingDomain editingDomain = getEditingDomain(object);
						if (association.getSourceRef().size()==0) {
							if (editingDomain == null) {
								association.getSourceRef().add((ItemAwareElement)value);
							} else {
								editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
									@Override
									protected void doExecute() {
										association.getSourceRef().add((ItemAwareElement)value);
									}
								});
							}
						}
						else {
							if (editingDomain == null) {
								association.getSourceRef().set(0,(ItemAwareElement)value);
							} else {
								editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
									@Override
									protected void doExecute() {
										association.getSourceRef().set(0,(ItemAwareElement)value);
									}
								});
							}
						}
					}
        		}
        	);

        	return adapter;
		}

		@Override
		public Bpmn2ExtendedPropertiesAdapter caseError(Error object) {
        	final Bpmn2ExtendedPropertiesAdapter adapter = new Bpmn2ExtendedPropertiesAdapter(adapterFactory,object);
        	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getResourceAssignmentExpression_Expression();
        	adapter.setObjectDescriptor(new Bpmn2ObjectDescriptor(adapterFactory, object) {
				@Override
				public String getText(Object context) {
					final Error error = context instanceof Error ?
							(Error)context :
							(Error)this.object;
					String text = "";
					if (error.getName()!=null) {
						text += error.getName();
					}
					else if (error.getErrorCode()!=null) {
						text += "Error Code " + error.getErrorCode();
					}
					return text;
				}
        	});

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
