package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ModelAttributeButtonBinding;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.LoopCharacteristics;
import org.eclipse.bpmn2.MultiInstanceLoopCharacteristics;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

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
		// multi instance
		final Button multiInstanceCheckbox = PropertyUtil.createUnboundCheckbox(section, parent, "Is Multi Instance");

		// standard loop
		final Button standardLoopCheckbox = PropertyUtil.createUnboundCheckbox(section, parent, "Is Loop");
		standardLoopCheckbox.setText(HelpText.STANDARD_LOOP_CHARACTERISTICS_NOTE);

		final EClass multiInstanceECls = Bpmn2Package.eINSTANCE.getMultiInstanceLoopCharacteristics();
		new LoopCharacteristicsButtonBinding(bo, LOOP_CHARACTERISTICS_FEATURE, multiInstanceCheckbox, multiInstanceECls).establish();

		multiInstanceCheckbox.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		    		standardLoopCheckbox.setSelection(false);
		            rebuildProperties();
		    }
		});

		final EClass standardLoopECls = Bpmn2Package.eINSTANCE.getStandardLoopCharacteristics();
		new LoopCharacteristicsButtonBinding(bo, LOOP_CHARACTERISTICS_FEATURE, standardLoopCheckbox, standardLoopECls).establish();

		standardLoopCheckbox.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		    	multiInstanceCheckbox.setSelection(false);
		        rebuildProperties();
		    }
		});

		rebuildProperties();
	}

	private void rebuildProperties() {
		EObject loopCharacteristics = (EObject) bo.eGet(LOOP_CHARACTERISTICS_FEATURE);

		if (multiInstancePropertiesComposite != null && !multiInstancePropertiesComposite.isDisposed()) {
			multiInstancePropertiesComposite.dispose();
		}

		multiInstancePropertiesComposite = PropertyUtil.createGridLayoutedComposite(section, parent);

		if (loopCharacteristics != null && loopCharacteristics instanceof MultiInstanceLoopCharacteristics) {
			PropertyUtil.createText(section, multiInstancePropertiesComposite, "Loop Cardinality", LOOP_CARDINALITY_FEATURE, loopCharacteristics);
			PropertyUtil.createCheckbox(section, multiInstancePropertiesComposite, "Is Sequential", IS_SEQUENCIAL_FEATURE, loopCharacteristics);

			PropertyUtil.createText(section, multiInstancePropertiesComposite, "Collection", COLLECTION_FEATURE, loopCharacteristics);
			PropertyUtil.createText(section, multiInstancePropertiesComposite, "Element Variable", ELEMENT_VARIABLE_FEATURE, loopCharacteristics);

			PropertyUtil.createText(section, multiInstancePropertiesComposite, "Completion Condition", COMPLETION_CONDITION_FEATURE, loopCharacteristics);
		}

		relayout();
	}

	private class LoopCharacteristicsButtonBinding extends ModelAttributeButtonBinding<LoopCharacteristics> {

		private EClass typeECls;

		public LoopCharacteristicsButtonBinding(EObject model, EStructuralFeature feature, Button control, EClass typeECls) {
			super(model, feature, control);

			this.typeECls = typeECls;
		}

		@Override
		public void setViewValue(LoopCharacteristics value) {
			if (value != null && value.eClass().getName().equals(typeECls.getName())) {
				control.setSelection(value != null);
			}
		}

		@Override
		public LoopCharacteristics getViewValue() throws IllegalArgumentException {
			if (control.getSelection()) {
				final EObject eObject = Bpmn2Factory.eINSTANCE.create(typeECls);
				return (LoopCharacteristics) eObject;
			} else {
				return null;
			}
		}
	}
}
