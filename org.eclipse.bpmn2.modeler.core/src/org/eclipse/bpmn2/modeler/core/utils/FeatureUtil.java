package org.eclipse.bpmn2.modeler.core.utils;

import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;

/**
 * Feature utility.
 * 
 * @author nico.rehwaldt
 */
public class FeatureUtil {

	/**
	 * Returns the first executable feature matching the given context
	 * which is provided in the passed feature provider.
	 * 
	 * @param featureProvider
	 * @param context
	 * 
	 * @return the feature or null if no feature is provided matching the context
	 */
	public static IFeature getFeature(IFeatureProvider featureProvider, IContext context) {
		
		if (context instanceof ICustomContext) {
			ICustomContext customContext = (ICustomContext) context;
			ICustomFeature[] customFeatures = featureProvider.getCustomFeatures(customContext);
			
			for (ICustomFeature customFeature : customFeatures) {
				if (customFeature.canExecute(customContext)) {
					return customFeature;
				}
			}
			
			return null;
		} else {
			// not supported yet
			throw new UnsupportedOperationException("Non custom contexts not yet supported");
		}
	}
}
