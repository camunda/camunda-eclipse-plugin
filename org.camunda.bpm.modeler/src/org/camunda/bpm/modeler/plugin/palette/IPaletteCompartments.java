package org.camunda.bpm.modeler.plugin.palette;

import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;

/**
 * A helper to retrieve a {@link IPaletteCompartmentEntry} by name.
 * 
 * @author nico.rehwaldt
 *
 */
public interface IPaletteCompartments {

	/**
	 * Gets (or creates) a compartment with the given name
	 * 
	 * @param name
	 * @return
	 */
	public IPaletteCompartmentEntry getOrCreateByName(String name);
}
