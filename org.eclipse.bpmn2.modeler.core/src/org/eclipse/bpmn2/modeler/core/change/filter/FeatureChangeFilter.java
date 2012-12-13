package org.eclipse.bpmn2.modeler.core.change.filter;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * Simple feature change filter
 * 
 * @author nico.rehwaldt
 */
public class FeatureChangeFilter extends AbstractFeatureChangeFilter {

	private static final EStructuralFeature ANY_ATTRIBUTE = Bpmn2Package.eINSTANCE.getBaseElement_AnyAttribute();

	public FeatureChangeFilter(EObject object, EStructuralFeature feature) {
		super(object, feature);
	}

	@Override
	public boolean matches(Notification notification) {
		Object featureObj = notification.getFeature();
		Object notifierObj = notification.getNotifier();

		if (!matchesObject(notifierObj)) {
			return false;
		}

		if (matchesFeature(featureObj)) {
			return true;
		}

		if (isAnyAttribute(featureObj)) {
			if (isRemove(notification)) {
				Object oldValue = notification.getOldValue();
				if (matchesRemovedAnyAttributeFeature(oldValue)) {
					return true;
				}
			}
		}

		return false;
	}

	protected boolean isAnyAttribute(Object featureObj) {
		return ANY_ATTRIBUTE.equals(featureObj);
	}

	protected boolean matchesRemovedAnyAttributeFeature(Object value) {

		if (value instanceof FeatureMap.Entry) {
			FeatureMap.Entry removed = (FeatureMap.Entry) value;
			EStructuralFeature removedFeature = removed.getEStructuralFeature();

			if (matchesFeature(removedFeature)) {
				return true;
			}
		}

		return false;
	}
}