package org.eclipse.bpmn2.modeler.ui.property.tabs.binding.util;

import static org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.MODEL_CHANGED;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.swt.widgets.Control;

public class EAttributeChangeSupport extends EObjectChangeSupport {
	
	private EStructuralFeature feature;
	
	public EAttributeChangeSupport(EObject model, EStructuralFeature feature, Control control) {
		
		super(model, control);
		
		this.feature = feature;
	}
	
	// ResourceSetListener API implementation /////////////////
	
	@Override
	public NotificationFilter getFilter() {
		return new InstanceSpecificFilter();
	}
	
	@Override
	protected void fireModelChanged(ResourceSetChangeEvent event) {
		if (!control.isDisposed()) {
			control.notifyListeners(MODEL_CHANGED, new ModelChangedEvent(model, feature, control, event.getSource()));
		}
	}
	
	// utility classes ///////////////////////////////////
	
	/**
	 * Filters only changes for the managed business object and feature
	 * 
	 * @author nico.rehwaldt
	 */
	private class InstanceSpecificFilter extends NotificationFilter.Custom {

		@Override
		public boolean matches(Notification notification) {

			Object notificationFeature = notification.getFeature();
			Object notifier = notification.getNotifier();
			
			if (model.equals(notifier) || feature.equals(notificationFeature)) {
				return true;
			}
			
			if (notification.getEventType() == Notification.REMOVE) {
				Object removedValue = notification.getOldValue();
				
				if (removedValue instanceof FeatureMap.Entry) {
					FeatureMap.Entry removed = (FeatureMap.Entry) removedValue;
					EStructuralFeature removedFeature = removed.getEStructuralFeature();
					
					if (feature.equals(removedFeature)) {
						return true;
					}
				}
			}

			return false;
		}
	};
	
	// static utility methods /////////////////////////
	
	/**
	 * Adds change support to the given model and makes sure it is only added once.
	 * 
	 * @param model
	 * @param feature
	 * @param control
	 */
	public static void ensureAdded(EObject model, EStructuralFeature feature, Control control) {
		String CHANGE_SUPPORT_KEY = EAttributeChangeSupport.class.getName() + "_data";
		
		if (control.getData(CHANGE_SUPPORT_KEY) != null) {
			return;
		}
		
		EAttributeChangeSupport modelViewChangeSupport = new EAttributeChangeSupport(model, feature, control);
		modelViewChangeSupport.register();
		
		control.setData(CHANGE_SUPPORT_KEY, modelViewChangeSupport);
	}
}
