package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.ModelRadioBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.radio.Radio.RadioGroup;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.HelpText;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Builder for the name property
 * 
 * @author nico.rehwaldt
 */
public class TimerEventDefinitionPropertiesBuilder extends AbstractPropertiesBuilder<TimerEventDefinition> {

	private static final String[] TYPE_NAMES = new String[] { 
		"Date", "Duration", "Cycle" 
	};

	private static final EStructuralFeature[] TYPE_FEATURES = new EStructuralFeature[] { 
		 Bpmn2Package.eINSTANCE.getTimerEventDefinition_TimeDate(), 
		 Bpmn2Package.eINSTANCE.getTimerEventDefinition_TimeDuration(), 
		 Bpmn2Package.eINSTANCE.getTimerEventDefinition_TimeCycle()
	};
	
	private static final String[] TYPE_HELP_TEXTS = new String[] { 
		 HelpText.TIME_DATE, 
		 HelpText.TIME_DURATION, 
		 HelpText.TIME_CYCLE, 
	};

	private RadioGroup<EStructuralFeature> radioGroup;
	private Map<EStructuralFeature, Text> featureToInputMap;
	
	public TimerEventDefinitionPropertiesBuilder(Composite parent, GFPropertySection section, TimerEventDefinition eventDefinition) {
		super(parent, section, eventDefinition);

		this.radioGroup = new RadioGroup<EStructuralFeature>();
		this.featureToInputMap = new HashMap<EStructuralFeature, Text>(); 
	}
	
	@Override
	public void create() {
		createTimerFireControls();
	}

	private void createTimerFireControls() {
		
		for (int i = 0; i < TYPE_NAMES.length; i++) {
			String name = TYPE_NAMES[i];
			final EStructuralFeature feature = TYPE_FEATURES[i];
			final Text text = PropertyUtil.createRadioText(section, parent, name, feature, radioGroup, bo);
			
			PropertyUtil.attachNote(text, TYPE_HELP_TEXTS[i]);
			
			featureToInputMap.put(feature, text);
			
			Button radioControl = radioGroup.getRadioControl(feature);
			
			ModelRadioBinding modelRadioBinding = new ModelRadioBinding(bo, feature, TYPE_FEATURES, radioControl) {
				@Override
				protected void activateFeature(EStructuralFeature feature) {
					bo.eSet(feature, Bpmn2Factory.eINSTANCE.createFormalExpression());
				}
			};
			
			modelRadioBinding.establish();
		}
	}
}
