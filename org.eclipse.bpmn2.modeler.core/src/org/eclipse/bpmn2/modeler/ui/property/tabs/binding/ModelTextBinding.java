package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Establishes a simple model to text binding
 * 
 * @author nico.rehwaldt
 */
public abstract class ModelTextBinding<V> extends ModelViewBinding<Text, V> {

	public ModelTextBinding(EObject model, EStructuralFeature feature, Text control) {
		super(model, feature, control);
	}

	@Override
	public void setViewValue(V value) {
		String str = toString(value);
		
		control.setText(str);
		
		if (control.isFocusControl()) {
			control.setSelection(str.length());
		}
	}

	/**
	 * Returns the current view value or throws an exception if 
	 * it cannot be parsed
	 * 
	 * @return the view value
	 * 
	 * @throws IllegalArgumentException if the view value cannot be obtained
	 */
	@Override
	public V getViewValue() throws IllegalArgumentException {
		return fromString(control.getText());
	}

	/**
	 * Converts a model value to its string representation
	 * 
	 * @param value
	 * @return
	 */
	protected abstract String toString(V value);
	
	/**
	 * Converts a string representation of a model value to 
	 * its actual value
	 * 
	 * @param string
	 * @return
	 */
	protected abstract V fromString(String string);

	@Override
	protected void establishModelViewBinding() {
		ensureChangeSupportAdded();

		control.addListener(Events.MODEL_CHANGED, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				V modelValue = getModelValue();
				V viewValue = null;
				
				try {
					viewValue = getViewValue();
				} catch (IllegalArgumentException e) {
					; // expected
				}
				
				if (isChangeWithNullChecks(modelValue, viewValue)) {
					setViewValue(modelValue);
				}
			}
		});
	}

	protected void ensureChangeSupportAdded() {
		EAttributeChangeSupport.ensureAdded(model, feature, control);
	}
	
	@Override
	protected void establishViewModelBinding() {
		IObservableValue textValue = SWTObservables.observeText(control, SWT.Modify);
		textValue.addValueChangeListener(new IValueChangeListener() {
			
			@Override
			public void handleValueChange(final ValueChangeEvent event) {
				try {
					V viewValue = fromString((String) event.diff.getNewValue());
					V modelValue = getModelValue();
					
					if (isChangeWithNullChecks(modelValue, viewValue)) {
						setModelValue(viewValue);
					}
				} catch (IllegalArgumentException e) {
					; // expected
				}
			}
		});
	}
}
