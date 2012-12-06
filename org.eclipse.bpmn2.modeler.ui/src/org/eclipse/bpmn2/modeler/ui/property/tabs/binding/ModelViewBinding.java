package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * 
 * @author nico.rehwaldt
 *
 * @param <T> the control type to bind
 * @param <V> the model value type
 */
public abstract class ModelViewBinding<T, V> extends AbstractModelViewBinding<T, V> {
	
	/**
	 * The model feature
	 */
	protected EStructuralFeature feature;
	
	public ModelViewBinding(EObject model, EStructuralFeature feature, T control) {
		super(model, control);
		
		this.feature = feature;
	}
}
