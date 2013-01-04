package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
		ensureChangeSupportAdded();

		control.addListener(Events.MODEL_CHANGED, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				V modelValue = getModelValue();
				V viewValue = getViewValue();
				
				if (isChangeWithNullChecks(viewValue, modelValue)) {
//					System.out.println(String.format("%s\t%s\t%s\t%s -> %s", "radio", feature.getName(), "M->V", viewValue, modelValue));
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
		
		control.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				V viewValue = getViewValue();
				V modelValue = getModelValue();
	
				if (isChangeWithNullChecks(modelValue, viewValue)) {
//					System.out.println(String.format("%s\t%s\t%s\t%s -> %s", "radio", feature.getName(), "V->M", modelValue, viewValue));
					setModelValue(viewValue);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
}
