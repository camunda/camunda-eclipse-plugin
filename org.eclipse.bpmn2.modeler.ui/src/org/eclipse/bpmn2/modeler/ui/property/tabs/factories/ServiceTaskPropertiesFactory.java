package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.swt.Radio;
import org.eclipse.bpmn2.modeler.ui.property.tabs.swt.Radio.RadioGroup;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ServiceTaskPropertiesFactory extends PropertiesFactory {

	private RadioGroup<EStructuralFeature> radioGroup;
	private Map<EStructuralFeature, Text> featureToInputMap; 
	
	public ServiceTaskPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
		
		this.radioGroup = new RadioGroup<EStructuralFeature>();
		this.featureToInputMap = new HashMap<EStructuralFeature, Text>(); 
	}

	@Override
	public void create() {
		PropertyUtil.createText(section, parent, "Result Variable", ModelPackage.eINSTANCE.getDocumentRoot_ResultVariableName(), bo);
		
		createServiceTaskTypeControls();
	}

	
	private void createServiceTaskTypeControls() {
		
		String[] names = new String[] { "Class", "Expression", "Expression Delegate" };
		EStructuralFeature[] features = new EStructuralFeature[] { 
			 ModelPackage.eINSTANCE.getDocumentRoot_Class(), 
			 ModelPackage.eINSTANCE.getDocumentRoot_ActExpression(), 
			 ModelPackage.eINSTANCE.getDocumentRoot_DelegateExpression()
		};
		
		EStructuralFeature selected = null;
		
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			EStructuralFeature feature = features[i];
			
			Text text = PropertyUtil.createRadioText(section, parent, name, feature, radioGroup, bo);
			featureToInputMap.put(feature, text);
			
			if (bo.eIsSet(feature)) {
				selected = feature;
			}
		}
		
		radioGroup.addListener(Radio.SELECTION_CHANGED, new Radio.RadioSelectionAdapter<EStructuralFeature>() {
			
			@Override
			public void radioSelectionChanged(Radio.SelectionChangedEvent<EStructuralFeature> event) {
				EStructuralFeature oldType = event.getOldSelection();
				
				Text oldText = featureToInputMap.get(oldType);
				oldText.setText("");
				
				transactionalHandleTypeChange(oldType, event.getNewSelection());
			}
		});
		
		radioGroup.select(selected);
	}

	protected void transactionalHandleTypeChange(EStructuralFeature oldType, EStructuralFeature newType) {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(bo);
		domain.getCommandStack().execute(new ToggleFeaturesCommand(domain, bo, oldType, newType));
	}

	// transactional behavior /////////////////////////////
	
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
			object.eUnset(oldFeature);
			object.eSet(newFeature, "");
		}
	}
}
