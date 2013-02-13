package org.eclipse.bpmn2.modeler.core.features;

/**
 * Common constants for contexts and pictogram elements.
 * 
 * @author nico.rehwaldt
 */
public interface PropertyNames {
	
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String BUSINESS_OBJECT = "businessObject";

	public static final String CONNECTION_BENDPOINTS = "PropertyNames.BENDPOINTS";
	public static final String REPAIR_IF_POSSIBLE = "PropertyNames.CONNECTION_RETAIN_BENDPOINTS";
	
	public static final String LABEL_CONTEXT = "labelContext";
	public static final String LABEL_CUSTOM_POSITION = "PropertyNames.LABEL_POSITION";
	
	public static final String LABELED_PICTOGRAM_ELEMENT = "PropertyNames.LABELED_PICTOGRAM_ELEMENT";
	
	public static final String CONNECTION_LABEL_REF_LENGTH = "PropertyNames.CONNECTION_LABEL_REF_LENGTH";
	public static final String OFFSET_X = "PropertyNames.OFFSET_X";
	public static final String OFFSET_Y = "PropertyNames.OFFSET_Y";
	
	public static final String LAYOUT_ADJUST_LABEL = "PropertyNames.LAYOUT_ADJUST_LABEL";
	public static final String LAYOUT_REPAIR_CONNECTIONS = "PropertyNames.LAYOUT_REPAIR_CONNECTIONS";

	public static final String LAYOUT_CONNECTION_FORCE = "PropertyNames.LAYOUT_CONNECTION_FORCE";
	public static final String LAYOUT_CONNECTION_ON_REPAIR_FAIL = "PropertyNames.LAYOUT_CONNECTION_ON_REPAIR_FAIL";
	
	public static final String[] LAYOUT_PROPERTIES = new String[] {
		LAYOUT_ADJUST_LABEL, LAYOUT_REPAIR_CONNECTIONS
	};
}
