package org.camunda.bpm.modeler.plugin.palette;

import org.camunda.bpm.modeler.ui.diagram.palette.Category;
import org.eclipse.core.runtime.Assert;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;

/**
 * Helper to be used to create {@link IPaletteIntegration}s.
 * 
 * @author nico.rehwaldt
 */
public class PaletteIntegration {

	/**
	 * Integrate into compartment with given name.
	 * 
	 * @param name
	 * @return
	 */
	public static IPaletteIntegration intoCompartmentNamed(String name) {
		return new IntoNamedCompartment(name);
	}
	
	/**
	 * Integrate into compartment for given category.
	 * 
	 * @param category
	 * @return
	 */
	public static IPaletteIntegration intoCompartmentForCategory(Category category) {
		return new IntoCategoryCompartment(category);
	}
	
	/**
	 * A palette integration that allows to specify the category to integrate to.
	 * 
	 * @author nico.rehwaldt
	 */
	protected static class IntoCategoryCompartment extends IntoNamedCompartment {
		
		public IntoCategoryCompartment(Category category) {
			super(category.getLabel());
		}
	}

	/**
	 * Into named comparment.
	 * 
	 * @author nico.rehwaldt
	 */
	protected static class IntoNamedCompartment implements IPaletteIntegration {
			
		private String name;
		
		public IntoNamedCompartment(String name) {
			Assert.isNotNull(name);
			
			this.name = name;
		}
		
		@Override
		public IPaletteCompartmentEntry getCompartmentEntry(IPaletteCompartments compartments) {
			return compartments.getOrCreateByName(name);
		}
	}
}
