package org.camunda.bpm.modeler.ui.change.filter;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;

/**
 * Abstract object and feature based filter
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractFeatureChangeFilter extends NotificationFilter.Custom {

	protected EObject object;
	protected EStructuralFeature feature;

	public AbstractFeatureChangeFilter(EObject object, EStructuralFeature feature) {

		Assert.isNotNull(object);
		Assert.isNotNull(feature);

		this.object = object;
		this.feature = feature;
	}

	protected boolean matchesFeature(Object featureObj) {
		if (featureObj instanceof EStructuralFeature) {
			EStructuralFeature f = (EStructuralFeature) featureObj;
			return feature.getName().equals(f.getName());
		}
		
		return false;
	}

	protected boolean matchesObject(Object notifierObj) {
		return object.equals(notifierObj);
	}

	protected boolean isAdd(Notification notification) {
		return notification.getEventType() == Notification.ADD;
	}

	protected boolean isRemove(Notification notification) {
		int eventType = notification.getEventType();
		return eventType == notification.UNSET || eventType == notification.REMOVE;
	}
}