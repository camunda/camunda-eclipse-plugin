package org.camunda.bpm.modeler.plugin.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.plugin.ICustomTaskProvider;
import org.camunda.bpm.modeler.plugin.core.Extensions;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * An implementation of {@link Extensions} that keeps track of 
 * all registered modeler extensions.
 * 
 * @author nico.rehwaldt
 */
public class ExtensionImpl implements Extensions {

	private static final String CUSTOM_TASK_EXTENSION_ID = "org.camunda.bpm.modeler.plugin.customtask";
	
	private List<ICustomTaskProvider> customTaskProviders = new ArrayList<ICustomTaskProvider>();
	
	public void addCustomTaskProvider(ICustomTaskProvider provider) {
		this.customTaskProviders.add(provider);
	}
	
	public List<ICustomTaskProvider> getCustomTaskProviders() {
		return Collections.unmodifiableList(customTaskProviders);
	}
	
	public void load() {
		loadCustomTaskProviders();
	}

	/// custom tasks
	
	private void loadCustomTaskProviders() {
		
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(CUSTOM_TASK_EXTENSION_ID);
		
		for (IConfigurationElement e : config) {
			try {
				final Object o = e.createExecutableExtension("class");
				if (o instanceof ICustomTaskProvider) {
					registerCustomTask((ICustomTaskProvider) o);
				}
			} catch (CoreException ex) {
				Activator.logError(ex);
			}
		}
	}

	private void registerCustomTask(ICustomTaskProvider provider) {
		customTaskProviders.add(provider);

		Activator.logStatus(new Status(IStatus.INFO, Activator.PLUGIN_ID, "Loaded custom task provider " + provider.getClass().getName()));
	}
}
