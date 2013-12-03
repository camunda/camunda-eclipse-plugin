package org.camunda.bpm.modeler.ui.diagram.palette;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.features.api.container.IFeatureContainer;
import org.camunda.bpm.modeler.plugin.ICustomTaskProvider;
import org.camunda.bpm.modeler.plugin.palette.IPaletteIntegration;
import org.camunda.bpm.modeler.plugin.palette.impl.PaletteCompartments;
import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.IToolEntry;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;

/**
 * A simple builder for the diagram palette
 * 
 * @author nico.rehwaldt
 */
public class PaletteBuilder {
	
	private Bpmn2FeatureProvider featureProvider;

	/**
	 * Creates the palette builder with the given feature provider.
	 * 
	 * @param featureProvider
	 */
	public PaletteBuilder(Bpmn2FeatureProvider featureProvider) {
		this.featureProvider = featureProvider;
	}

	/**
	 * Builds the palette
	 * @return
	 */
	public IPaletteCompartmentEntry[] build() {

		PaletteCompartments compartments = new PaletteCompartments();
			
		// add compartments from super class
		create(Category.CONNECTORS, compartments);
		create(Category.TASKS, compartments);
		create(Category.GATEWAYS, compartments);
		create(Category.EVENTS, compartments);
		create(Category.EVENT_DEFINITIONS, compartments);
		create(Category.DATA, compartments);
		create(Category.OTHER, compartments);
	
		addCustomTasks(compartments);
		
		return compartments.getCompartments();
	}
	
	protected void addCustomTasks(PaletteCompartments compartments) {
		List<ICustomTaskProvider> providers = Activator.getExtensions().getCustomTaskProviders();
		
		for (ICustomTaskProvider provider : providers) {
			IPaletteIntegration paletteIntegration = provider.getPaletteIntegration();
			IFeatureContainer featureContainer = provider.getFeatureContainer();
			
			ICreateFeature createFeature = featureContainer.getCreateFeature(featureProvider);
			
			if (paletteIntegration == null || createFeature == null) {
				continue;
			}
			
			PaletteCompartmentEntry compartmentEntry = (PaletteCompartmentEntry) paletteIntegration.getCompartmentEntry(compartments);
			if (compartmentEntry != null) {
				compartmentEntry.addToolEntry(createToolEntry(createFeature));
			}
		}
	}

	protected void create(Category category, PaletteCompartments compartments) {
		List<EClass> types = getTypesForCategory(category);
		
		List<IToolEntry> entries = createEntriesForBusinessObjectClasses(types);
		
		if (!entries.isEmpty()) {
			PaletteCompartmentEntry compartmentEntry = compartments.getOrCreateByName(category.getLabel());
			
			compartmentEntry.setInitiallyOpen(category.isInitiallyOpen());
			
			for (IToolEntry entry : entries) {
				compartmentEntry.addToolEntry(entry);
			}
		}
	}

	protected List<EClass> getTypesForCategory(Category category) {
		return filterTypes(category, Category.getDefaultTypes(category));
	}

	/**
	 * Filters the categories
	 * @param category
	 * @param types
	 * @return
	 */
	protected List<EClass> filterTypes(Category category, List<EClass> types) {
		return types;
	}
	
	private List<IToolEntry> createEntriesForBusinessObjectClasses(List<EClass> classes) {
		
		List<IToolEntry> entries = new ArrayList<IToolEntry>();
		
		for (EClass eClass: classes) {
			IFeature feature = featureProvider.getCreateFeatureForBusinessObject(eClass);
			IToolEntry entry = createToolEntry(feature);
			
			if (entry != null) {
				entries.add(entry);
			}
		}
		
		return entries;
	}

	/**
	 * Creates a tool entry for a given feature
	 * 
	 * @param feature
	 * @return
	 */
	protected IToolEntry createToolEntry(IFeature feature) {
		
		if (feature instanceof ICreateFeature) {
			ICreateFeature cf = (ICreateFeature) feature;
			
			ObjectCreationToolEntry objectCreationToolEntry = new ObjectCreationToolEntry(
					cf.getCreateName(), cf.getCreateDescription(),
					cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
			
			return objectCreationToolEntry;
		} else if (feature instanceof ICreateConnectionFeature) {
			ICreateConnectionFeature cf = (ICreateConnectionFeature) feature;
			
			ConnectionCreationToolEntry connectionCreationToolEntry = new ConnectionCreationToolEntry(
					cf.getCreateName(), cf.getCreateDescription(),
					cf.getCreateImageId(), cf.getCreateLargeImageId());
			
			connectionCreationToolEntry.addCreateConnectionFeature(cf);
			return connectionCreationToolEntry;
		}
		
		return null;
	}
}
