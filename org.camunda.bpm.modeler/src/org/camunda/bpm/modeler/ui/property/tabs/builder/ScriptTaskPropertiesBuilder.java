package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ModelRadioBinding;
import org.camunda.bpm.modeler.ui.property.tabs.radio.Radio.RadioGroup;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ScriptTaskPropertiesBuilder extends AbstractPropertiesBuilder<ScriptTask> {
	
	protected static final String SCRIPT_LANGUAGE_LABEL = "Script Language";
	protected static final String SCRIPT_RESOURCE_LABEL = "Script Resource";
	protected static final String SCRIPT_LABEL = "Script";
	
	protected static final String EMPTY_STRING = "";
	
	protected static final EStructuralFeature SCRIPT_FORMAT_FEATURE = Bpmn2Package.eINSTANCE.getScriptTask_ScriptFormat();
	protected static final EStructuralFeature SCRIPT_RESOURCE_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Resource1();
	protected static final EStructuralFeature SCRIPT_FEATURE = Bpmn2Package.eINSTANCE.getScriptTask_Script();
	
	private static final EStructuralFeature[] TYPE_FEATURES = new EStructuralFeature[] { 
		SCRIPT_RESOURCE_FEATURE, 
		SCRIPT_FEATURE
	};
	
	protected RadioGroup<EStructuralFeature> radioGroup;

	public ScriptTaskPropertiesBuilder(Composite parent, GFPropertySection section, ScriptTask bo) {
		super(parent, section, bo);
		radioGroup = new RadioGroup<EStructuralFeature>();
	}

	@Override
	public void create() {
		PropertyUtil.createText(section, parent, SCRIPT_LANGUAGE_LABEL, SCRIPT_FORMAT_FEATURE, bo);
		
		createScriptResourceText();
		createScriptText();
	}

	protected void createScriptResourceText() {
		PropertyUtil.createRadioText(section, parent, SCRIPT_RESOURCE_LABEL, SCRIPT_RESOURCE_FEATURE, radioGroup, bo);
		
		Button radioControl = radioGroup.getRadioControl(SCRIPT_RESOURCE_FEATURE);
		ModelRadioBinding modelRadioBinding = new ModelRadioBinding(bo, SCRIPT_RESOURCE_FEATURE, TYPE_FEATURES, radioControl) { 
			protected void activateFeature(EStructuralFeature feature) {
				bo.eSet(feature, EMPTY_STRING);
			};
		};
		
		modelRadioBinding.establish();
	}
	
	protected void createScriptText() {
		PropertyUtil.createRadioMultiText(section, parent, SCRIPT_LABEL, SCRIPT_FEATURE, radioGroup, bo);
		
		Button radioControl = radioGroup.getRadioControl(SCRIPT_FEATURE);
		ModelRadioBinding modelRadioBinding = new ModelRadioBinding(bo, SCRIPT_FEATURE, TYPE_FEATURES, radioControl) { 
			protected void activateFeature(EStructuralFeature feature) {
				bo.eSet(feature, EMPTY_STRING);
			};
		};
		
		modelRadioBinding.establish();
	}
	
}
