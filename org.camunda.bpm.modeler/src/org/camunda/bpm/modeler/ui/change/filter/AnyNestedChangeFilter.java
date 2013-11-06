package org.camunda.bpm.modeler.ui.change.filter;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Matches all changes to an objects feature value (reference or attribute) or nested changes in the value.
 * 
 * Assumes feature not to be isMany.
 * 
 * Does not match add or removal of the feature value. To accomplish this, use the {@link FeatureChangeFilter}.
 * 
 * @author nico.rehwaldt
 * 
 * @see FeatureChangeFilter
 */
public class AnyNestedChangeFilter extends AbstractFeatureChangeFilter {

	private EClassifier rootAttributeCls;
	
	public AnyNestedChangeFilter(EObject object, EStructuralFeature feature) {
		super(object, feature);
		
		if (feature.isMany()) {
			throw new IllegalArgumentException("Expect feature not to be many");
		}
		
		rootAttributeCls = feature.getEType();
	}

	@Override
	public boolean matches(Notification notification) {
		Object notifier = notification.getNotifier();
		
		if (notifier instanceof EObject) {
			return matchesNestedChange((EObject) notifier, notification);
		}
		
		return false;
	}

	private boolean matchesNestedChange(EObject notifier, Notification notification) {
		do {
			// traverse changed elements up to an element
			// that has the rootAttributeCls type
			if (rootAttributeCls.isInstance(notifier)) {
				
				// check if it is actually us
				// (change was performed in the context of our object)
				EObject extensionValue = getValue(object, feature);
				
				boolean attributeSame = notifier.equals(extensionValue);
				if (attributeSame) {
					return true;
				} else {
					return false;
				}
			}
			
			notifier = notifier.eContainer();
		} while (notifier != null);
		
		return false;
	}

	private EObject getValue(EObject object, EStructuralFeature feature) {
		
		if (object.eClass().getEAllStructuralFeatures().contains(feature)) {
			return (EObject) object.eGet(feature);
		} else {
			// TODO: fix when correct meta-model is in place
			List<?> allExtensions = ExtensionUtil.getExtensions(object, feature.getEType().getInstanceClass());
			return (EObject) (allExtensions.isEmpty() ? null : allExtensions.get(0));
		}
	}
}
