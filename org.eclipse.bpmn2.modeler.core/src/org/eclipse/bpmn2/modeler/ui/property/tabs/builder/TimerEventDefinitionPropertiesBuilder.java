package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import static org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.RADIO_SELECTION_CHANGED;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.FormalExpressionTextBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.ModelRadioBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.radio.Radio.RadioGroup;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.RadioSelectionChanged;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
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

		EStructuralFeature selected = null;
		
		for (int i = 0; i < TYPE_NAMES.length; i++) {
			String name = TYPE_NAMES[i];
			final EStructuralFeature feature = TYPE_FEATURES[i];
			
			final Text text = PropertyUtil.createRadioText(section, parent, name, feature, radioGroup, bo);
			text.setEnabled(false);
			
			featureToInputMap.put(feature, text);
			
			if (bo.eIsSet(feature)) {
				selected = feature;
			}
			
			Button radioControl = radioGroup.getRadioControl(feature);
			
			new ModelRadioBinding(bo, feature, radioControl).establish();
		}
		
//		radioGroup.select(selected, true);
		
		radioGroup.addListener(RADIO_SELECTION_CHANGED, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				RadioSelectionChanged<EStructuralFeature> event = (RadioSelectionChanged<EStructuralFeature>) e;
				
				transactionalHandleTypeChange(event.getOldSelection(), event.getNewSelection());
			}
		});
	}

	protected void transactionalHandleTypeChange(EStructuralFeature oldType, EStructuralFeature newType) {
		TransactionalEditingDomain domain = getTransactionalEditingDomain();
		domain.getCommandStack().execute(new ToggleFeaturesCommand(domain, bo, oldType, newType));
	}

	private TransactionalEditingDomain getTransactionalEditingDomain() {
		return TransactionUtil.getEditingDomain(bo);
	}

	// model radio binding ////////////////////////////////

	private class ToggleFeaturesCommand extends RecordingCommand {
		
		private EObject object;
		
		private EStructuralFeature oldFeature;
		private EStructuralFeature newFeature;
		
		public ToggleFeaturesCommand(TransactionalEditingDomain domain, EObject object, EStructuralFeature oldFeature, EStructuralFeature newFeature) {
			super(domain);
			
			this.object = object;
			
			this.oldFeature = oldFeature;
			this.newFeature = newFeature;
		}
		
		@Override
		protected void doExecute() {
			if (oldFeature != null) {
				object.eUnset(oldFeature);
			}
			object.eSet(newFeature, Bpmn2Factory.eINSTANCE.createFormalExpression());
		}
	}
}
