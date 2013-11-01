package org.camunda.bpm.modeler.plugin.core;

import java.util.List;

import org.camunda.bpm.modeler.plugin.ICustomTaskProvider;

/**
 * A simple registry that provides modeler extensions
 * 
 * @author nico.rehwaldt
 */
public interface Extensions {

	/**
	 * Returns a list of custom task providers
	 * 
	 * @return
	 */
	public List<ICustomTaskProvider> getCustomTaskProviders();
}
