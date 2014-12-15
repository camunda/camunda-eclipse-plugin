package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.ScriptType;
import org.camunda.bpm.modeler.ui.change.filter.AbstractFeatureChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ModelRadioBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.StringTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.camunda.bpm.modeler.ui.property.tabs.radio.Radio.RadioGroup;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author Roman Smirnov
 */
public class CamundaScriptPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {
	
	protected static final String SCRIPT_FORMAT_LABEL = "Script Format";
	protected static final String SCRIPT_RESOURCE_LABEL = "Script Resource";
	protected static final String SCRIPT_LABEL = "Script";
	
	protected static final String EMPTY_STRING = "";
	
	protected static final EStructuralFeature SCRIPT_FORMAT_FEATURE = ModelPackage.eINSTANCE.getScriptType_ScriptFormat();
	protected static final EStructuralFeature SCRIPT_RESOURCE_FEATURE = ModelPackage.eINSTANCE.getScriptType_Resource();
	protected static final EStructuralFeature SCRIPT_TEXT_FEATURE = ModelPackage.eINSTANCE.getScriptType_Text();
	protected static final EStructuralFeature SCRIPT_MIXED_FEATURE = ModelPackage.eINSTANCE.getScriptType_Mixed();

	private static final EStructuralFeature[] TYPE_FEATURES = new EStructuralFeature[] { 
		SCRIPT_RESOURCE_FEATURE,
		SCRIPT_TEXT_FEATURE
	};
	
	protected EObject container;
	protected EStructuralFeature scriptFeature;
	protected boolean canDeleteScript;
	
	protected RadioGroup<EStructuralFeature> radioGroup;
	
	public CamundaScriptPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo, EObject container, EStructuralFeature scriptFeature) {
		this(parent, section, bo, container, scriptFeature, false);
	}

	public CamundaScriptPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo, EObject container, EStructuralFeature scriptFeature, boolean canDeleteScript) {
		super(parent, section, bo);
		
		this.container = container;
		this.scriptFeature = scriptFeature;
		this.canDeleteScript = canDeleteScript;
		
		this.radioGroup = new RadioGroup<EStructuralFeature>();
	}
	
	@Override
	public void create() {
		createScriptFormatText();
		createRadioGroup();
	}

	protected void createScriptFormatText() {
		Text scriptFormatText = PropertyUtil.createUnboundText(section, parent, SCRIPT_FORMAT_LABEL);
		
		StringTextBinding scriptFormatTextbinding = new StringTextBinding(bo, SCRIPT_FORMAT_FEATURE, scriptFormatText) {
			
			@Override
			public String getModelValue() {
				ScriptType script = getScript();
				if (script != null) {
					return script.getScriptFormat();
				}
				return null;
			}
			
			@Override
			public void setModelValue(final String value) {
				if (getScript() != null || value != null && !value.isEmpty()) {
					
					TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						
						@Override
						protected void doExecute() {
							ScriptType script = getScript();

							if (script == null) {
								script = createScript();
							}
							
							if (value != null && !value.isEmpty()) {
								script.setScriptFormat(value);
								
							} else {
								script.eUnset(feature);

								if (canDeleteScript(script)) {
									deleteScript();
								}
							}
						}
					});
				}
			}
		};
		
		scriptFormatTextbinding.establish();
	}
	
	protected void createRadioGroup() {
		createScriptResourceText();
		createScriptText();
	}
	
	protected void createScriptResourceText() {
		Text scriptResourceText = PropertyUtil.createUnboundRadioText(section, parent, SCRIPT_RESOURCE_LABEL, SCRIPT_RESOURCE_FEATURE, radioGroup);
		
		// text binding
		
		StringTextBinding scriptResourceBinding = new StringTextBinding(bo, SCRIPT_RESOURCE_FEATURE, scriptResourceText) {
			
			@Override
			protected String fromString(String str) {
				return CamundaScriptPropertiesBuilder.this.fromString(str);
			}
			
			@Override
			public String getModelValue() {
				ScriptType script = getScript();
				if (script != null) {
					return script.getResource();
				}
				return null;
			}
			
			@Override
			public void setModelValue(final String value) {
				if (getScript() != null || value != null && !value.isEmpty()) {
					
					TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						
						@Override
						protected void doExecute() {
							ScriptType script = getScript();
							
							if (script == null) {
								script = createScript();
							}
							
							if (value != null && !value.isEmpty()) {
								script.setResource(value);
							} else {
								script.eUnset(feature);

								if (canDeleteScript(script)) {
									deleteScript();
								}
							}
						}
					});
				}
			}
		};
		
		scriptResourceBinding.setDisableOnNull(true);
		scriptResourceBinding.establish();
		
		// register change support for text
		
		EAttributeChangeSupport scriptResourceTextChangeSupport = new EAttributeChangeSupport(bo, SCRIPT_RESOURCE_FEATURE, scriptResourceText);
		scriptResourceTextChangeSupport.setFilter(new ScriptChangeFilter(bo, SCRIPT_RESOURCE_FEATURE));
		
		scriptResourceTextChangeSupport.register();
		
		// radio binding
		
		Button scriptResourceRadio = radioGroup.getRadioControl(SCRIPT_RESOURCE_FEATURE);
		ModelRadioBinding resourceModelRadioBinding = new ModelRadioBinding(bo, SCRIPT_RESOURCE_FEATURE, TYPE_FEATURES, scriptResourceRadio) {
			
			@Override
			public Boolean getModelValue() {
				ScriptType script = getScript();
				if (script != null) {
					return script.eIsSet(feature);
				}
				return false;
			}
			
			@Override
			protected void transactionalHandleFeatureActivation(EStructuralFeature feature) {
				TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(model);
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					
					@Override
					protected void doExecute() {
						ScriptType script = getScript();
						
						if (script == null) {
							script = createScript();
						}
						
						
						FeatureMap mixed = script.getMixed();
						if (mixed != null && !mixed.isEmpty()) {
							mixed.clear();
						}
						
						activateFeature(SCRIPT_RESOURCE_FEATURE);
					}
				});
			}
			
			protected void activateFeature(EStructuralFeature feature) {
				getScript().setResource(EMPTY_STRING);
			};
			
		};
		
		resourceModelRadioBinding.establish();
		
		// register change support for radio
		
		EAttributeChangeSupport scriptResourceRadioChangeSupport = new EAttributeChangeSupport(bo, SCRIPT_RESOURCE_FEATURE, scriptResourceRadio);
		scriptResourceRadioChangeSupport.setFilter(new ScriptChangeFilter(bo, SCRIPT_RESOURCE_FEATURE));
		
		scriptResourceRadioChangeSupport.register();
	}
	
	protected void createScriptText() {
		Text scriptText = PropertyUtil.createUnboundRadioMultiText(section, parent, SCRIPT_LABEL, SCRIPT_TEXT_FEATURE, radioGroup);
		Object data = scriptText.getLayoutData();
		if (data instanceof FormData) {
			FormData layout = (FormData) data;
			layout.height = 150;
		}
		// text binding
		
		StringTextBinding scriptTextBinding = new StringTextBinding(bo, SCRIPT_TEXT_FEATURE, scriptText) {
			
			@Override
			protected String fromString(String str) {
				return CamundaScriptPropertiesBuilder.this.fromString(str);
			}
			
			@Override
			public String getModelValue() {
				ScriptType script = getScript();
				if (script != null) {
					FeatureMap mixed = script.getMixed();
					if (mixed != null && !mixed.isEmpty()) {
						return (String) script.getText();
					}
				}
				return null;
			}
			
			@Override
			public void setModelValue(final String value) {
				if (getScript() != null || value != null && !value.isEmpty()) {
					
					
					TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						
						@Override
						protected void doExecute() {
							ScriptType script = getScript();
							
							if (script == null) {
								script = createScript();
							}
							
							if (value != null && !value.isEmpty()) {
								script.eSet(feature, value);
								
							} else {
								
								FeatureMap mixed = script.getMixed();
								if (mixed != null && !mixed.isEmpty()) {
									mixed.clear();
								}

								if (canDeleteScript(script)) {
									deleteScript();
								}
							}
						}
					});
				}
			}
		};
		
		scriptTextBinding.setDisableOnNull(true);
		scriptTextBinding.establish();
		
		// register change support for text
		
		EAttributeChangeSupport scriptTextChangeSupport = new EAttributeChangeSupport(bo, SCRIPT_TEXT_FEATURE, scriptText);
		scriptTextChangeSupport.setFilter(new ScriptChangeFilter(bo, SCRIPT_TEXT_FEATURE));
		
		scriptTextChangeSupport.register();
		
		// radio binding
		
		Button scriptRadio = radioGroup.getRadioControl(SCRIPT_TEXT_FEATURE);
		ModelRadioBinding textModelRadioBinding = new ModelRadioBinding(bo, SCRIPT_TEXT_FEATURE, TYPE_FEATURES, scriptRadio) {
			@Override
			public Boolean getModelValue() {
				ScriptType script = getScript();
				if (script != null) {
					FeatureMap mixed = script.getMixed();
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
						ScriptType script = getScript();
						
						if (script == null) {
							script = createScript();
						}
						
						script.eUnset(SCRIPT_RESOURCE_FEATURE);
						
						activateFeature(SCRIPT_TEXT_FEATURE);
					}
				});
			}
			
			protected void activateFeature(EStructuralFeature feature) {
				getScript().setText(EMPTY_STRING);
			};
		};
		
		textModelRadioBinding.establish();
		
		// register changes support for radio
		
		EAttributeChangeSupport scriptRadioChangeSupport = new EAttributeChangeSupport(bo, SCRIPT_TEXT_FEATURE, scriptRadio);
		scriptRadioChangeSupport.setFilter(new ScriptChangeFilter(bo, SCRIPT_TEXT_FEATURE));
		
		scriptRadioChangeSupport.register();
	}
	
	protected ScriptType getScript() {
		Object element = container.eGet(scriptFeature);
		
		if (element instanceof ScriptType) {
			return (ScriptType) element;
		}
		
		return null;
	}
	
	protected ScriptType createScript () {
		ScriptType script = ModelFactory.eINSTANCE.createScriptType();
		container.eSet(scriptFeature, script);
		return script;
	}
	
	protected boolean canDeleteScript(ScriptType script) {
		if (canDeleteScript) {
			String scriptFormat = script.getScriptFormat();
			String resource = script.getResource();
			FeatureMap mixed = script.getMixed();
			
			return scriptFormat == null && resource == null && (mixed == null || mixed.isEmpty());
		}
		
		return false;
	}
	
	protected void deleteScript() {
		container.eUnset(scriptFeature);
	}
	
	protected String fromString(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		} else {
			return str;
		}
	}
	
	protected class ScriptChangeFilter extends AbstractFeatureChangeFilter {

		public ScriptChangeFilter(EObject object, EStructuralFeature feature) {
			super(object, feature);
		}

		@Override
		public boolean matches(Notification notification) {
			EStructuralFeature feature = (EStructuralFeature) notification.getFeature();

			if (feature != null) {
				return SCRIPT_RESOURCE_FEATURE.equals(feature)
						|| SCRIPT_TEXT_FEATURE.equals(feature)
						|| SCRIPT_MIXED_FEATURE.equals(feature);
			}
			
			return false;
		}
	}
}
