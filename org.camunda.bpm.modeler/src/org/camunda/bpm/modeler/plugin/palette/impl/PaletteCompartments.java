package org.camunda.bpm.modeler.plugin.palette.impl;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.plugin.palette.IPaletteCompartments;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;

/**
 * A simple {@link IPaletteCompartments implementation}.
 * 
 * @author nico.rehwaldt
 */
public class PaletteCompartments implements IPaletteCompartments {

	private List<PaletteCompartmentEntry> compartmentEntries = new ArrayList<PaletteCompartmentEntry>();
	
	public IPaletteCompartmentEntry[] getCompartments() {
		return compartmentEntries.toArray(new IPaletteCompartmentEntry[0]);
	}

	@Override
	public PaletteCompartmentEntry getOrCreateByName(String name) {
		
		// get
		for (PaletteCompartmentEntry entry : compartmentEntries) {
			if (name.equals(entry.getLabel())) {
				return entry;
			}
		}
		
		// or create
		PaletteCompartmentEntry entry = new PaletteCompartmentEntry(name, null);
		compartmentEntries.add(entry);
		
		return entry;
	}
}
