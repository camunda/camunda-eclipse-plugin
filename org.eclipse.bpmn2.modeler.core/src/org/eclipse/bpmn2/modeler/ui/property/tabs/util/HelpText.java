package org.eclipse.bpmn2.modeler.ui.property.tabs.util;


/**
 * Help texts used by the application
 * 
 * @author nico.rehwaldt
 */
public class HelpText {

	public static final String TABLE_HELP = "Add or remove table entry by right-click";
	
	public static final String TIME_DATE = "Date in ISO 8601 format (e.g. \"2011-03-11T12:13:14\")";
	public static final String TIME_DURATION = "Duration in ISO 8601 format (e.g. \"P4DT12H30M5S\" as a duration of \"four days, 12 hours, 30 minutes, and five seconds\")";
	public static final String TIME_CYCLE = "Retry interval in ISO 8601 format (e.g. \"R3/PT10M\" for \"3 cycles, every 10 minutes\")";

	public static final String ELEMENT_DEF_TABLE = "%ss can be defined below";

	private static final String ASYNC_LINK_TO_USER_GUIDE = 
			"https://app.camunda.com/confluence/display/foxUserGuide/Thread+and+Transaction+Management#ThreadandTransactionManagement-AsynchronousContinuation";
	
	
	public static final String ASYNC_FLAG = String.format(
			"More infomation on asynchronous continuation can be found in the <a href=\"%s\">user guide</a>.", ASYNC_LINK_TO_USER_GUIDE);
	
}
