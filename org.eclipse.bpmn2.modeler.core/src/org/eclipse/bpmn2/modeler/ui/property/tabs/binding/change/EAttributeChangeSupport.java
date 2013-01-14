package org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change;

import static org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.MODEL_CHANGED;

import org.eclipse.bpmn2.modeler.ui.change.filter.FeatureChangeFilter;
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
	
	@Override
	protected String getHash() {
		return 
			super.getHash() + "_" + 
			feature.getFeatureID() + "_" + 
			feature.hashCode();
	}
	
	// ResourceSetListener API implementation /////////////////

	@Override
	protected void fireModelChanged(final ResourceSetChangeEvent event) {
		if (!control.isDisposed()) {
			control.notifyListeners(MODEL_CHANGED, new ModelChangedEvent(object, feature, control, event.getSource()));
		}
	}
	
	// static utility methods /////////////////////////
	
	/**
	 * Adds change support to the given model and makes sure it is only added once.
	 * 
	 * @param object
	 * @param feature
	 * @param control
	 */
	public static void ensureAdded(EObject object, EStructuralFeature feature, Control control) {
		EAttributeChangeSupport changeSupport = new EAttributeChangeSupport(object, feature, control);
		
		ensureAdded(changeSupport, control);
	}
	
	/**
	 * Ensure the change support is added to the given model
	 * 
	 * @param changeSupport
	 * @param control
	 */
	public static void ensureAdded(EAttributeChangeSupport changeSupport, Control control) {
		String CHANGE_SUPPORT_KEY = changeSupport.getHash();
		
		if (control.getData(CHANGE_SUPPORT_KEY) != null) {
			System.err.println("[EAttributeChangeSupport#ensureAdded] Skip addition of change support to " + control + " (already registered)");
			return;
		}
		
		changeSupport.register();
		
		control.setData(CHANGE_SUPPORT_KEY, changeSupport);
	}
}
