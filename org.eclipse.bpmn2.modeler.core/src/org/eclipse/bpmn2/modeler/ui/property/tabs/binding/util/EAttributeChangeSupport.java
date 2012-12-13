package org.eclipse.bpmn2.modeler.ui.property.tabs.binding.util;

import static org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.MODEL_CHANGED;

import org.eclipse.bpmn2.modeler.core.change.filter.FeatureChangeFilter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.swt.widgets.Control;

public class EAttributeChangeSupport extends EObjectChangeSupport {
	
	private EStructuralFeature feature;
	
	public EAttributeChangeSupport(EObject object, EStructuralFeature feature, Control control) {
		super(object, control);
		
		this.feature = feature;
		this.filter = new FeatureChangeFilter(object, feature);
	}
	
	// ResourceSetListener API implementation /////////////////

	@Override
	protected void fireModelChanged(ResourceSetChangeEvent event) {
		if (!control.isDisposed()) {
			control.notifyListeners(MODEL_CHANGED, new ModelChangedEvent(object, feature, control, event.getSource()));
		}
	}
	
	// static utility methods /////////////////////////
	
	/**
	 * Adds change support to the given model and makes sure it is only added once.
	 * 
	 * @param model
	 * @param feature
	 * @param control
	 */
	public static void ensureAdded(EObject model, EStructuralFeature feature, Control control) {
		String CHANGE_SUPPORT_KEY = EAttributeChangeSupport.class.getName() + "_" + model.eClass().getClassifierID() + "_" + feature.getFeatureID() + "_" + model.hashCode() + "_" + feature.hashCode();
		
		if (control.getData(CHANGE_SUPPORT_KEY) != null) {
			return;
		}
		
		EAttributeChangeSupport modelViewChangeSupport = new EAttributeChangeSupport(model, feature, control);
		modelViewChangeSupport.register();
		
		control.setData(CHANGE_SUPPORT_KEY, modelViewChangeSupport);
	}
}
