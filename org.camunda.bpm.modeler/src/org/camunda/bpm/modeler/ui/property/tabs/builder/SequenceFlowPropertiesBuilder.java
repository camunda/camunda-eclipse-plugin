package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.change.filter.AbstractFeatureChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ModelRadioBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.StringTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.camunda.bpm.modeler.ui.property.tabs.radio.Radio.RadioGroup;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SequenceFlowPropertiesBuilder extends AbstractPropertiesBuilder<SequenceFlow> {

	protected static final String CONDITION_LANGUAGE_LABEL = "Condition Language";
	protected static final String CONDITION_RESOURCE_LABEL = "Condition Resource";
	protected static final String CONDITION_LABEL = "Condition";
	
	protected static final String EMPTY_STRING = "";
	
	protected static final EStructuralFeature CONDITION_EXPRESSION_FEATURE = Bpmn2Package.eINSTANCE.getSequenceFlow_ConditionExpression();
	
	protected static final EStructuralFeature CONDITION_LANGUAGE_FEATURE = Bpmn2Package.eINSTANCE.getFormalExpression_Language();
	protected static final EStructuralFeature CONDITION_RESOURCE_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Resource1();
	protected static final EStructuralFeature CONDITION_BODY_FEATURE = Bpmn2Package.eINSTANCE.getFormalExpression_Body();
	protected static final EStructuralFeature CONDITION_ANY_ATTRIBUTE_FEATURE = Bpmn2Package.eINSTANCE.getBaseElement_AnyAttribute();
	protected static final EStructuralFeature CONDITION_MIXED_FEATURE = Bpmn2Package.eINSTANCE.getFormalExpression_Mixed();
	
	protected static final EStructuralFeature[] TYPE_FEATURES = new EStructuralFeature[] { 
		CONDITION_BODY_FEATURE,
		CONDITION_RESOURCE_FEATURE
	};
	
	protected RadioGroup<EStructuralFeature> radioGroup;
	
	public SequenceFlowPropertiesBuilder(Composite parent, GFPropertySection section, SequenceFlow bo) {
		super(parent, section, bo);
		radioGroup = new RadioGroup<EStructuralFeature>();
	}

	@Override
	public void create() {
		createConditionLanguage();
		createConditionResource();
		createCondition();
	}
	
	protected void createConditionLanguage() {
		Text conditionLanguageText = PropertyUtil.createUnboundText(section, parent, CONDITION_LANGUAGE_LABEL);
		
		StringTextBinding languageTextBinding = new StringTextBinding(bo, CONDITION_LANGUAGE_FEATURE, conditionLanguageText) {
			
			@Override
			public String getModelValue() {
				FormalExpression expression = getFormalExpression();
				if (expression != null) {
					return expression.getLanguage();
				}
				return null;
			}
			
			@Override
			public void setModelValue(final String value) {
				if (getFormalExpression() != null || value != null && !value.isEmpty()) {
					
					TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						
						@Override
						protected void doExecute() {
							FormalExpression expression = getFormalExpression();

							if (expression == null) {
								expression = createFormalExpression();
							}
							
							if (value != null && !value.isEmpty()) {
								expression.eSet(feature, value);
								
							} else {
								expression.eUnset(feature);

								if (canDeleteFormalExpression(expression)) {
									deleteFormalExpression();
								}
							}
						}
					});
				}
			}
		};
		
		languageTextBinding.establish();
	}
	
	protected void createConditionResource() {
		Text conditionResourceText = PropertyUtil.createUnboundRadioText(section, parent, CONDITION_RESOURCE_LABEL, CONDITION_RESOURCE_FEATURE, radioGroup);
		
		// text binding
		
		StringTextBinding conditionResourceBinding = new StringTextBinding(bo, CONDITION_RESOURCE_FEATURE, conditionResourceText) {
			
			@Override
			public String getModelValue() {
				FormalExpression expression = getFormalExpression();
				if (expression != null) {
					Object value = expression.eGet(feature);
					
					if (value != null && value instanceof String) {
						return (String) value;
					}
				}
				return null;
			}
			
			@Override
			public void setModelValue(final String value) {
				if (getFormalExpression() != null || value != null && !value.isEmpty()) {
					
					TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						
						@Override
						protected void doExecute() {
							FormalExpression expression = getFormalExpression();
							
							if (expression == null) {
								expression = createFormalExpression();
							}
							
							if (value != null && !value.isEmpty()) {
								expression.eSet(feature, value);
							} else {
								expression.eUnset(feature);

								if (canDeleteFormalExpression(expression)) {
									deleteFormalExpression();
								}
							}
						}
					});
				}
			}
		};
		
		conditionResourceBinding.setDisableOnNull(true);
		conditionResourceBinding.establish();
		
		// register change support for text
		
		EAttributeChangeSupport conditionResourceTextChangeSupport = new EAttributeChangeSupport(bo, CONDITION_RESOURCE_FEATURE, conditionResourceText);
		conditionResourceTextChangeSupport.setFilter(new ConditionChangeFilter(bo, CONDITION_RESOURCE_FEATURE));
		
		conditionResourceTextChangeSupport.register();
		
		// radio binding
		
		Button conditionResourceRadio = radioGroup.getRadioControl(CONDITION_RESOURCE_FEATURE);
		ModelRadioBinding resourceModelRadioBinding = new ModelRadioBinding(bo, CONDITION_RESOURCE_FEATURE, TYPE_FEATURES, conditionResourceRadio) {
			
			@Override
			public Boolean getModelValue() {
				FormalExpression expression = getFormalExpression();
				if (expression != null) {
					return expression.eIsSet(feature);
				}
				return false;
			}
			
			@Override
			protected void transactionalHandleFeatureActivation(EStructuralFeature feature) {
				TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(model);
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					
					@Override
					protected void doExecute() {
						FormalExpression expression = getFormalExpression();
						
						if (expression == null) {
							expression = createFormalExpression();
						}
						
						expression.getMixed().clear();
						
						activateFeature(CONDITION_RESOURCE_FEATURE);
					}
				});
			}
			
			protected void activateFeature(EStructuralFeature feature) {
				getFormalExpression().eSet(feature, EMPTY_STRING);
			};
			
		};
		
		resourceModelRadioBinding.establish();
		
		// register change support for radio
		
		EAttributeChangeSupport conditionResourceRadioChangeSupport = new EAttributeChangeSupport(bo, CONDITION_RESOURCE_FEATURE, conditionResourceRadio);
		conditionResourceRadioChangeSupport.setFilter(new ConditionChangeFilter(bo, CONDITION_RESOURCE_FEATURE));
		
		conditionResourceRadioChangeSupport.register();
	}
	
	protected void createCondition() {
		Text conditionText = PropertyUtil.createUnboundRadioMultiText(section, parent, CONDITION_LABEL, CONDITION_BODY_FEATURE, radioGroup);

		// text binding
		
		StringTextBinding conditionTextBinding = new StringTextBinding(bo, CONDITION_BODY_FEATURE, conditionText) {
			
			@Override
			protected String fromString(String str) {
				return SequenceFlowPropertiesBuilder.this.fromString(str);
			}
			
			@Override
			public String getModelValue() {
				FormalExpression expression = getFormalExpression();
				if (expression != null) {
					FeatureMap mixed = expression.getMixed();
					if (mixed != null && !mixed.isEmpty()) {
						return (String) expression.getBody();
					}
				}
				return null;
			}
			
			@Override
			public void setModelValue(final String value) {
				if (getFormalExpression() != null || value != null && !value.isEmpty()) {
					
					
					TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						
						@Override
						protected void doExecute() {
							FormalExpression expression = getFormalExpression();
							
							if (expression == null) {
								expression = createFormalExpression();
							}
							
							if (value != null && !value.isEmpty()) {
								expression.eSet(feature, value);
								
							} else {
								
								FeatureMap mixed = expression.getMixed();
								if (mixed != null && !mixed.isEmpty()) {
									mixed.clear();
								}

								if (canDeleteFormalExpression(expression)) {
									deleteFormalExpression();
								}
							}
						}
					});
				}
			}
		};
		
		conditionTextBinding.setDisableOnNull(true);
		conditionTextBinding.establish();
		
		// register change support for text
		
		EAttributeChangeSupport conditionTextChangeSupport = new EAttributeChangeSupport(bo, CONDITION_BODY_FEATURE, conditionText);
		conditionTextChangeSupport.setFilter(new ConditionChangeFilter(bo, CONDITION_BODY_FEATURE));
		
		conditionTextChangeSupport.register();
		
		// radio binding
		
		Button conditionRadio = radioGroup.getRadioControl(CONDITION_BODY_FEATURE);
		ModelRadioBinding conditionModelRadioBinding = new ModelRadioBinding(bo, CONDITION_BODY_FEATURE, TYPE_FEATURES, conditionRadio) {
			@Override
			public Boolean getModelValue() {
				FormalExpression expression = getFormalExpression();
				if (expression != null) {
					FeatureMap mixed = expression.getMixed();
					return mixed != null && !mixed.isEmpty();
				}
				return false;
			}
			
			@Override
			protected void transactionalHandleFeatureActivation(EStructuralFeature feature) {
				TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(model);
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					
					@Override
					protected void doExecute() {
						FormalExpression expression = getFormalExpression();
						
						if (expression == null) {
							expression = createFormalExpression();
						}
						
						expression.eUnset(CONDITION_RESOURCE_FEATURE);
						
						activateFeature(CONDITION_BODY_FEATURE);
					}
				});
			}
			
			protected void activateFeature(EStructuralFeature feature) {
				getFormalExpression().setBody(EMPTY_STRING);
			};
		};
		
		conditionModelRadioBinding.establish();
		
		// register changes support for radio
		
		EAttributeChangeSupport conditionRadioChangeSupport = new EAttributeChangeSupport(bo, CONDITION_BODY_FEATURE, conditionRadio);
		conditionRadioChangeSupport.setFilter(new ConditionChangeFilter(bo, CONDITION_BODY_FEATURE));
		
		conditionRadioChangeSupport.register();
	}
	
	protected FormalExpression getFormalExpression() {
		Object element = bo.eGet(CONDITION_EXPRESSION_FEATURE);
		
		if (element instanceof FormalExpression) {
			return (FormalExpression) element;
		}
		
		return null;
	}

	protected FormalExpression createFormalExpression() {
		FormalExpression newExpression = Bpmn2Factory.eINSTANCE.createFormalExpression();
		bo.eSet(CONDITION_EXPRESSION_FEATURE, newExpression);
		return newExpression;
	}
	
	protected boolean canDeleteFormalExpression(FormalExpression expression) {
		boolean body = expression.eIsSet(CONDITION_BODY_FEATURE);
		boolean language = expression.eIsSet(CONDITION_LANGUAGE_FEATURE);
		boolean resource = expression.eIsSet(CONDITION_RESOURCE_FEATURE);
		
		return !(body || language || resource);
	}

	protected void deleteFormalExpression() {
		bo.eUnset(CONDITION_EXPRESSION_FEATURE);
	}
	
	protected String fromString(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		} else {
			return str;
		}
	}
	
	protected class ConditionChangeFilter extends AbstractFeatureChangeFilter {

		public ConditionChangeFilter(EObject object, EStructuralFeature feature) {
			super(object, feature);
		}

		@Override
		public boolean matches(Notification notification) {
			EStructuralFeature feature = (EStructuralFeature) notification.getFeature();

			if (feature != null) {
				return CONDITION_RESOURCE_FEATURE.equals(feature)
						|| CONDITION_BODY_FEATURE.equals(feature)
						|| CONDITION_ANY_ATTRIBUTE_FEATURE.equals(feature)
						|| CONDITION_MIXED_FEATURE.equals(feature);
			}
			
			return false;
		}
	}
}
