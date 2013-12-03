package org.camunda.bpm.modeler.ui.diagram.palette;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.modeler.ui.FeatureMap;
import org.eclipse.emf.ecore.EClass;

/**
 * Contains all default categories for the palette
 * 
 * @author nico.rehwaldt
 */
public enum Category {
	
	CONNECTORS("Connectors", true),
	EVENT_DEFINITIONS("Event Definitions", false),
	EVENTS("Events", true),
	GATEWAYS("Gateways", true),
	TASKS("Tasks", true),
	DATA("Data Items", false),
	OTHER("Other", false);
	
	//// static helpers /////
	
	private static final Map<Category, List<EClass>> CATEGORY_MAP;
	
	static {
		CATEGORY_MAP = new EnumMap<Category, List<EClass>>(Category.class);
		CATEGORY_MAP.put(Category.CONNECTORS, FeatureMap.CONNECTORS);
		CATEGORY_MAP.put(Category.EVENTS, FeatureMap.EVENTS);
		CATEGORY_MAP.put(Category.EVENT_DEFINITIONS, FeatureMap.EVENT_DEFINITIONS);
		CATEGORY_MAP.put(Category.GATEWAYS, FeatureMap.GATEWAYS);
		CATEGORY_MAP.put(Category.TASKS, FeatureMap.TASKS);
		CATEGORY_MAP.put(Category.DATA, FeatureMap.DATA);
		CATEGORY_MAP.put(Category.OTHER, FeatureMap.OTHER);
	}
	
	public static List<EClass> getDefaultTypes(Category t) {
		return CATEGORY_MAP.get(t);
	}

	//// instance //////
	
	private String label;
	private boolean initiallyOpen;

	private Category(String label, boolean initiallyOpen) {
		this.label = label;
		this.initiallyOpen = initiallyOpen;
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean isInitiallyOpen() {
		return initiallyOpen;
	}
}
