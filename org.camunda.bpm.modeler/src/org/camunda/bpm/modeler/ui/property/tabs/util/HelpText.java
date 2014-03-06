package org.camunda.bpm.modeler.ui.property.tabs.util;


/**
 * Help texts used by the application
 * 
 * @author nico.rehwaldt
 */
public class HelpText {

	public static final String SUPPORTED_VERSION_NOTE = "This feature is supported for camunda BPM engine version 7.1 and higher.";

	public static final String TABLE_HELP = "Add or remove table entry by right-click.";
	
	public static final String TIME_DATE = "Date in ISO 8601 format (e.g. \"2011-03-11T12:13:14\")";
	public static final String TIME_DURATION = "Duration in ISO 8601 format (e.g. \"P4DT12H30M5S\" as a duration of \"four days, 12 hours, 30 minutes, and five seconds\")";
	public static final String TIME_CYCLE = "Retry interval in ISO 8601 format (e.g. \"R3/PT10M\" for \"3 cycles, every 10 minutes\")";

	public static final String ELEMENT_DEF_TABLE = "%ss can be defined below";

	private static final String ASYNC_LINK_TO_USER_GUIDE = 
			"http://docs.camunda.org/latest/guides/user-guide/#process-engine-transactions-in-processes-asynchronous-continuations";
	
	private static final String CALL_ACTIVITY_VARIABLES_GUIDE = "http://docs.camunda.org/latest/api-references/bpmn20/#subprocesses-call-activity-passing-variables";
	private static final String CALL_ACTIVITY_BUSINESS_KEY_GUIDE = "http://docs.camunda.org/latest/api-references/bpmn20/#subprocesses-call-activity-passing-business-key";

	private static final String LINK_EVENT_DEFINITION_USER_GUIDE = "http://docs.camunda.org/latest/api-references/bpmn20/#events-link-events";

	private static final String COMPENSATION_THROWING_EVENT_USER_GUIDE = "http://docs.camunda.org/latest/api-references/bpmn20/#events-cancel-and-compensation-events-compensation-intermediate-throwing-event";

	private static final String MULTI_INSTANCE_USER_GUIDE = "http://docs.camunda.org/latest/api-references/bpmn20/#tasks-task-markers-multiple-instance";

	private static final String IS_LOOP_USER_GUIDE = "http://docs.camunda.org/latest/api-references/bpmn20/#tasks-task-markers-loops";

	private static final String SIGNAL_THROW_EVENT_USER_GUIDE = "http://docs.camunda.org/latest/api-references/bpmn20/#events-signal-events-signal-intermediate-throwing-event";

	private static final String FOLLOW_UP_DATE_USER_GUIDE = "http://docs.camunda.org/latest/api-references/bpmn20/#tasks-user-task-follow-up-date";

	private static final String EXCLUSIVE_LINK_TO_USER_GUIDE = "http://docs.camunda.org/latest/guides/user-guide/#process-engine-the-job-executor-exclusive-jobs";

	public static final String ASYNC_FLAG = String.format(
			"More information on asynchronous continuation can be found in the <a href=\"%s\">user guide</a>.", ASYNC_LINK_TO_USER_GUIDE);

	public static final String CALL_ACTIVITY_CALLED_ELEMENT_VERSION = "Processdefinition version of called process (e.g. \"17\")"; 

	public static final String CALL_ACTIVITY_ALL_VARIABLES_IN = String.format(
	    "Pass all process variables from mainprocess to the subprocess. See for more information <a href=\"%s\">user guide</a>.", CALL_ACTIVITY_VARIABLES_GUIDE);

	public static final String CALL_ACTIVITY_ALL_VARIABLES_OUT = String.format(
	    "Pass all process variables from subprocess to mainprocess. See for more information <a href=\"%s\">user guide</a>.", CALL_ACTIVITY_VARIABLES_GUIDE);

	public static final String CALL_ACTIVITY_BUSINESS_KEY = String.format(
        "Pass business key from mainprocess to subprocess. See for more information <a href=\"%s\">user guide</a>.", CALL_ACTIVITY_BUSINESS_KEY_GUIDE);

	public static final String LINK_EVENT_DEFINITION_NAME = String.format("More information on link event definition can be found in the <a href=\"%s\">user guide</a>.", LINK_EVENT_DEFINITION_USER_GUIDE);

	public static final String STANDARD_LOOP_CHARACTERISTICS_NOTE = String.format("Please note, the loop activity is not supported by the camunda BPM engine. See for more information <a href=\"%s\">user guide</a>.", IS_LOOP_USER_GUIDE);

	public static final String WAIT_FOR_COMPLETION_NOTE = "Please note, only the default value \"true\" is supported by the camunda BPM engine.";

	public static final String COMPENSATION_THROWING_EVENT = String.format("More information on compensation intermediate throwing event can be found in the <a href=\"%s\">user guide</a>.", COMPENSATION_THROWING_EVENT_USER_GUIDE);

	public static final String MULTI_INSTANCE_CHARACTERISTICS = String.format("Please refer to our <a href=\"%s\">documentation</a> for multi instance.", MULTI_INSTANCE_USER_GUIDE);

	public static final String SIGNAL_THROW_EVENT_ASYNC_FLAG = String.format("Asynchronous notification of event listener. For more information, refer to our <a href=\"%s\">user guide</a>.", SIGNAL_THROW_EVENT_USER_GUIDE);

	public static final String FOLLOW_UP_DATE = String.format("The follow up date as an EL expression (e.g. ${someDate}) or an ISO date (e.g. 2012-03-01T15:30:23). See for more information <a href=\"%s\">user guide</a>.", FOLLOW_UP_DATE_USER_GUIDE);

	public static final String EXCLUSIVE_FLAG = String.format("More information on exclusive jobs can be found in the <a href=\"%s\">user guide</a>.", EXCLUSIVE_LINK_TO_USER_GUIDE);

}
