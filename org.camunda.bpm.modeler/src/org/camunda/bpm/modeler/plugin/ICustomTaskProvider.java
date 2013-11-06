package org.camunda.bpm.modeler.plugin;

import org.camunda.bpm.modeler.core.features.api.container.IFeatureContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;

/**
 * Implementations of this interface provide custom tasks to the
 * modeler.
 * 
 * @author nico.rehwaldt
 */
public interface ICustomTaskProvider {

	/**
	 * Returns the id of the custom task provider
	 * @return
	 */
	public String getId();
	
	/**
	 * <p>Returns true if the given provider applies to the 
	 * specified {@link EObject}.</p>
	 * 
	 * If a {@link IFeatureContainer} is provided by this implementation via {@link #getFeatureContainer()}
	 * this method should behave in the same way as {@link IFeatureContainer#canApplyTo(Object)}. 
	 * 
	 * @param eObject
	 * 
	 * @return
	 */
	public boolean appliesTo(EObject eObject);
	
	/**
	 * Returns a {@link IFeatureContainer} that provides custom task features
	 * 
	 * @return
	 */
	public IFeatureContainer getFeatureContainer();
	
	public ITabDescriptor getTabDescriptor();
}
