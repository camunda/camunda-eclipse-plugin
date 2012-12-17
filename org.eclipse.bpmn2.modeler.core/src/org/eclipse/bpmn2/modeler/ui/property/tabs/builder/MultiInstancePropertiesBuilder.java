package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.LoopCharacteristics;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.ModelAttributeButtonBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.draw2d.GridData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Builder for the multi instance properties
 * 
 * @author nico.rehwaldt
 */
public class MultiInstancePropertiesBuilder extends AbstractPropertiesBuilder<Activity> {

	protected static final EStructuralFeature LOOP_CHARACTERISTICS_FEATURE = Bpmn2Package.eINSTANCE.getActivity_LoopCharacteristics();

	protected static final EStructuralFeature LOOP_CARDINALITY_FEATURE = Bpmn2Package.eINSTANCE.getMultiInstanceLoopCharacteristics_LoopCardinality();
	protected static final EStructuralFeature IS_SEQUENCIAL_FEATURE = Bpmn2Package.eINSTANCE.getMultiInstanceLoopCharacteristics_IsSequential();
	protected static final EStructuralFeature COMPLETION_CONDITION_FEATURE = Bpmn2Package.eINSTANCE.getMultiInstanceLoopCharacteristics_CompletionCondition();
	
	protected static final EStructuralFeature COLLECTION_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Collection();
	protected static final EStructuralFeature ELEMENT_VARIABLE_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_ElementVariable();

	private Composite multiInstancePropertiesComposite = null;
	
	public MultiInstancePropertiesBuilder(Composite parent, GFPropertySection section, Activity bo) {
		super(parent, section, bo);
	}
	
	@Override
	public void create() {
		final Button multiInstanceCheckbox = PropertyUtil.createUnboundCheckbox(section, parent, "Is Multi Instance");

		MultiInstanceButtonBinding binding = new MultiInstanceButtonBinding(bo, LOOP_CHARACTERISTICS_FEATURE, multiInstanceCheckbox);
		binding.establish();
		
		multiInstanceCheckbox.addListener(Events.MODEL_CHANGED, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				rebuildMultiInstanceProperties();
			}
		});
		
		rebuildMultiInstanceProperties();
	}
	
	private void rebuildMultiInstanceProperties() {
		EObject loopCharacteristics = (EObject) bo.eGet(LOOP_CHARACTERISTICS_FEATURE);
				
		if (multiInstancePropertiesComposite != null && !multiInstancePropertiesComposite.isDisposed()) {
			multiInstancePropertiesComposite.dispose();
		}

		multiInstancePropertiesComposite = PropertyUtil.createGridLayoutedComposite(section, parent);

		if (loopCharacteristics != null) {
			PropertyUtil.createText(section, multiInstancePropertiesComposite, "Loop Cardinality", LOOP_CARDINALITY_FEATURE, loopCharacteristics);
			PropertyUtil.createCheckbox(section, multiInstancePropertiesComposite, "Is Sequential ", IS_SEQUENCIAL_FEATURE, loopCharacteristics);
			
			PropertyUtil.createText(section, multiInstancePropertiesComposite, "Collection", COLLECTION_FEATURE, loopCharacteristics);
			PropertyUtil.createText(section, multiInstancePropertiesComposite, "Element Variable", ELEMENT_VARIABLE_FEATURE, loopCharacteristics);
			
			PropertyUtil.createText(section, multiInstancePropertiesComposite, "Completion Condition", COMPLETION_CONDITION_FEATURE, loopCharacteristics);
		}

		relayout();
	}
	
	private class MultiInstanceButtonBinding extends ModelAttributeButtonBinding<LoopCharacteristics> {

		public MultiInstanceButtonBinding(EObject model, EStructuralFeature feature, Button control) {
			super(model, feature, control);
		}
		
		@Override
		public void setViewValue(LoopCharacteristics value) {
			control.setSelection(value != null);
		}
		
		@Override
		public LoopCharacteristics getViewValue() throws IllegalArgumentException {
			if (control.getSelection()) {
				return Bpmn2Factory.eINSTANCE.createMultiInstanceLoopCharacteristics();
			} else {
				return null;
			}
		}
	}
}
