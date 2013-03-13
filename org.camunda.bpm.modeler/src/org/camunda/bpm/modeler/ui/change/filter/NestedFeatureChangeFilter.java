package org.camunda.bpm.modeler.ui.change.filter;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Filter which detects changes on nested resources (in a containment list).
 * 
 * @author nico.rehwaldt
 */
public class NestedFeatureChangeFilter extends AbstractFeatureChangeFilter {

	public NestedFeatureChangeFilter(EObject object, EStructuralFeature feature) {
		super(object, feature);

		if (!feature.isMany()) {
			throw new IllegalArgumentException("Feature is not a many");
		}
	}

	@Override
	public boolean matches(Notification notification) {
		Object notifierObj = notification.getNotifier();
		
		List<EObject> results = (List<EObject>) object.eGet(feature);
		
		if (results.contains(notifierObj)) {
			return true;
		}

		return false;
	}
}