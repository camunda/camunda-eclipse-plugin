package org.camunda.bpm.modeler.plugin.palette;

import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.IToolEntry;

/**
 * An interface that specifies the integration of a
 * custom task feature into the modelers palette.
 * 
 * @author nico.rehwaldt
 */
public interface IPaletteIntegration {
	
	/**
	 * Returns the {@link IPaletteCompartmentEntry} into which the {@link IToolEntry} will be added.
	 * 
	 * @param compartments a helper to retrieve compartments
	 * 
	 * @return
	 */
	public IPaletteCompartmentEntry getCompartmentEntry(IPaletteCompartments compartments);
}
