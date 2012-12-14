package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Establishes a simple model to button binding
 * 
 * @author nico.rehwaldt
 */
public abstract class ModelButtonBinding<V> extends ModelViewBinding<Button, V> {

	public ModelButtonBinding(EObject model, EStructuralFeature feature, Button control) {
		super(model, feature, control);
	}
	
	@Override
	protected void establishModelViewBinding() {
		EAttributeChangeSupport.ensureAdded(model, feature, control);

		control.addListener(Events.MODEL_CHANGED, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				V modelValue = getModelValue();
				V viewValue = getViewValue();
				
				if (isChangeWithNullChecks(viewValue, modelValue)) {
					setViewValue(modelValue);
				}
			}
		});
	}
	
	@Override
	protected void establishViewModelBinding() {
			
		final IObservableValue checkboxSelection = SWTObservables.observeSelection(control);
		checkboxSelection.addChangeListener(new IChangeListener() {
			
			@Override
			public void handleChange(ChangeEvent event) {
				V viewValue = getViewValue();
				V modelValue = getModelValue();

				if (isChangeWithNullChecks(modelValue, viewValue)) {
					setModelValue(viewValue);
				}
			}
		});
	}
}
