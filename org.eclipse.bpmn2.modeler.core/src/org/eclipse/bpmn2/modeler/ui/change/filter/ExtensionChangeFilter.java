package org.eclipse.bpmn2.modeler.ui.change.filter;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Feature change filter which detects changes on element extensions
 * 
 * @author nico.rehwaldt
 */
public class ExtensionChangeFilter extends FeatureChangeFilter {

	private static final EStructuralFeature EXTENSION_FEATURE = Bpmn2Package.eINSTANCE.getBaseElement_ExtensionValues();

	private static final EClass EXTENSION_CLS = Bpmn2Package.eINSTANCE.getExtensionAttributeValue();

	public ExtensionChangeFilter(EObject object, EStructuralFeature feature) {
		super(object, feature);
	}

	@Override
	public boolean matches(Notification notification) {
		Object featureObj = notification.getFeature();
		Object notifierObj = notification.getNotifier();

		EObject notifierEObj;

		if (notifierObj instanceof EObject) {
			notifierEObj = (EObject) notifierObj;
		} else {
			return false;
		}

		// tracking removal of extension element
		if (isRemove(notification)) {
			if (matchesObject(notifierEObj) && isExtensionType(notification.getOldValue())) {
				return true;
			}
		}
		
		// we have a extension attribute value
		// object <- extension
		if (isExtensionValue(notifierEObj)) {
			if (matchesFeature(featureObj)) {
				return true;
			}

			if (isRemove(notification)) {
				Object oldValue = notification.getOldValue();
				if (matchesRemovedAnyAttributeFeature(oldValue)) {
					return true;
				}
			}
		} else {
			notifierEObj = notifierEObj.eContainer();
			if (notifierEObj == null) {
				return false;
			}

			// we have a nested object connected through the objects extension
			// attribute value
			// object <- extension <- notifierObj
			if (isExtensionValue(notifierEObj)) {
				return true;
			}
		}

		return false;
	}

	private boolean isExtensionType(Object o) {
		if (!(o instanceof EObject)) {
			return false;
		}
	
		EObject eObject = (EObject) o;
		return EXTENSION_CLS.equals(eObject.eClass());
	}

	protected boolean isExtensionFeature(Object featureObj) {
		return EXTENSION_FEATURE.equals(featureObj);
	}

	protected boolean isExtensionValue(Object notifierObj) {
		if (!(notifierObj instanceof EObject)) {
			return false;
		}

		EObject o = (EObject) notifierObj;
		if (isExtensionType(o)) {
			if (object.equals(o.eContainer())) {
				return true;
			}
		}

		return false;
	}
}