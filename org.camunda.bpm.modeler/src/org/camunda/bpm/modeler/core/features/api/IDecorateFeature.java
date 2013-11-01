package org.camunda.bpm.modeler.core.features.api;

import org.camunda.bpm.modeler.core.features.api.container.IFeatureContainer;
import org.eclipse.graphiti.features.IFeature;

/**
 * A decoration feature that allows a {@link IFeatureContainer} to decorate 
 * a shape during redrawal.
 * 
 * @author nico.rehwaldt
 */
public interface IDecorateFeature extends IFeature {

	/**
	 * Decorate an element according to the type after its
	 * {@link IFeatureContainer} has changed.
	 * 
	 * @param context
	 */
	public void decorate(IDecorateContext context);
}
