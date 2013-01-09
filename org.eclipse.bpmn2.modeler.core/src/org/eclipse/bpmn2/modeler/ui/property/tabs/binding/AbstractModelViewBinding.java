package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.validation.internal.modeled.model.validation.Feature;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Feature agnostic model view binding
 * 
 * @author nico.rehwaldt
 *
 * @param <T> the control type to bind
 * @param <V> the model value type
 */
public abstract class AbstractModelViewBinding<T extends Control, V> {

	/**
	 * Set to true for verbose debug output
	 */
	protected static boolean LOGGING = false;
	
	/**
	 * The model
	 */
	protected EObject model;
	
	/**
	 * The view
	 */
	protected T control;
	
	public AbstractModelViewBinding(EObject model, T control) {
		this.model = model;
		this.control = control;
	}

	/**
	 * Updates the view value with the given data
	 * 
	 * @param value
	 */
	public abstract void setViewValue(V value);
	
	/**
	 * Returns the current view value
	 * 
	 * @return
	 */
	public abstract V getViewValue();

	/**
	 * Returns the model value
	 * @return
	 */
	public abstract V getModelValue();
	
	/**
	 * Sets the model value
	 * 
	 * @param value
	 */
	public abstract void setModelValue(V value);
	
	/**
	 * Called regardless of whether an actual view update took place, 
	 * the control state can be updated. 
	 * 
	 * Subclasses may override this method to perform something useful.
	 * 
	 * @param modelValue
	 */
	protected void updateViewState(V modelValue) {
		
	}
	
	/**
	 * Returns the editing domain for the managed model
	 * 
	 * @param matchModel
	 * @return
	 */
	protected TransactionalEditingDomain getTransactionalEditingDomain() {
		return TransactionUtil.getEditingDomain(model);
	}
	
	/**
	 * Establishes a bi-directional binding between model and view
	 * 
	 */
	public void establish() {
		// update view value with latest value from model
		V modelValue = getModelValue();
		setViewValue(modelValue);
		
		updateViewState(modelValue);
		
		log("establish");
		
		establishModelViewBinding();
		establishViewModelBinding();
	}

	protected void log(String msg) {
		if (LOGGING) {
			System.out.println(String.format("#binding %s <-> %s %s", control.getClass().getSimpleName(), model.getClass().getSimpleName(), msg));
		}
	}

	/**
	 * Establish the model view binding.
	 * Can be overridden by subclasses. 
	 */
	protected void establishModelViewBinding() {
		
	}
	
	/**
	 * Establish the view model binding.
	 * Can be overridden by subclasses. 
	 */
	protected void establishViewModelBinding() {
		
	}
	
	/**
	 * Performs a similarity check on the values.
	 * Must be null safe.
	 * 
	 * @param oldValue
	 * @param newValue
	 * 
	 * @return true if the transition between old and new value is a change
	 */
	protected boolean isChangeWithNullChecks(V oldValue, V newValue) {
		if (oldValue != null && newValue != null) {
			return isChange(oldValue, newValue);
		}
		
		return oldValue != newValue;
	}
	
	/**
	 * Provides the comparison between two values, the old and the new
	 * value. Can be overridden in subclasses to customize that behavior.
	 * 
	 * The argumens oldValue and newValue will never be null.
	 * 
	 * @param oldValue
	 * @param newValue
	 * 
	 * @return true if the transition between old and new value is a change
	 */
	protected boolean isChange(V oldValue, V newValue) {
		return !newValue.equals(oldValue);
	}
}
