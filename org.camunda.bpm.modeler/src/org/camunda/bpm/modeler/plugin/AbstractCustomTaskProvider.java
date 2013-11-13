package org.camunda.bpm.modeler.plugin;

import org.camunda.bpm.modeler.core.features.api.container.IFeatureContainer;
import org.camunda.bpm.modeler.plugin.palette.IPaletteIntegration;
import org.eclipse.ui.views.properties.tabbed.ISection;

/**
 * A base implementation for {@link ICustomTaskProvider}s.
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractCustomTaskProvider implements ICustomTaskProvider {

	@Override
	public IFeatureContainer getFeatureContainer() {
		return null;
	}

	@Override
	public ISection getTabSection() {
		return null;
	}

	@Override
	public IPaletteIntegration getPaletteIntegration() {
		return null;
	}
}
