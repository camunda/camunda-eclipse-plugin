/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.modeler.ui.diagram.Bpmn2ToolBehaviour;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;

/**
 * The holder of all available modeler features.
 * 
 * Used in {@link Bpmn2ToolBehaviour} to render the palette.
 * 
 * @author nico.rehwaldt
 */
public class FeatureMap {

	public static final List<EClass> CONNECTORS;
	public static final List<EClass> EVENT_DEFINITIONS;
	public static final List<EClass> EVENTS;
	public static final List<EClass> GATEWAYS;
	public static final List<EClass> TASKS;
	public static final List<EClass> DATA;
	public static final List<EClass> OTHER;

	public static final List<EClass> ALL_BASE_ELEMENTS;
	
	static {
		CONNECTORS = Arrays.asList(
				Bpmn2Package.eINSTANCE.getSequenceFlow(), 
				Bpmn2Package.eINSTANCE.getMessageFlow(),
				Bpmn2Package.eINSTANCE.getAssociation(), 
				Bpmn2Package.eINSTANCE.getDataAssociation());

		EVENT_DEFINITIONS = Arrays.asList(
				Bpmn2Package.eINSTANCE.getConditionalEventDefinition(),
				Bpmn2Package.eINSTANCE.getTimerEventDefinition(),
				Bpmn2Package.eINSTANCE.getSignalEventDefinition(),
				Bpmn2Package.eINSTANCE.getMessageEventDefinition(),
				Bpmn2Package.eINSTANCE.getEscalationEventDefinition(),
				Bpmn2Package.eINSTANCE.getCompensateEventDefinition(),
				Bpmn2Package.eINSTANCE.getLinkEventDefinition(),
				Bpmn2Package.eINSTANCE.getErrorEventDefinition(),
				Bpmn2Package.eINSTANCE.getCancelEventDefinition(),
				Bpmn2Package.eINSTANCE.getTerminateEventDefinition());

		EVENTS = Arrays.asList(
				Bpmn2Package.eINSTANCE.getStartEvent(),
				Bpmn2Package.eINSTANCE.getEndEvent(),
				Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(),
				Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(),
				Bpmn2Package.eINSTANCE.getBoundaryEvent());

		GATEWAYS = Arrays.asList(
				Bpmn2Package.eINSTANCE.getInclusiveGateway(),
				Bpmn2Package.eINSTANCE.getExclusiveGateway(),
				Bpmn2Package.eINSTANCE.getParallelGateway(),
				Bpmn2Package.eINSTANCE.getEventBasedGateway(),
				Bpmn2Package.eINSTANCE.getComplexGateway());

		TASKS = Arrays.asList(
				Bpmn2Package.eINSTANCE.getTask(),
				Bpmn2Package.eINSTANCE.getManualTask(),
				Bpmn2Package.eINSTANCE.getUserTask(),
				Bpmn2Package.eINSTANCE.getScriptTask(),
				Bpmn2Package.eINSTANCE.getBusinessRuleTask(),
				Bpmn2Package.eINSTANCE.getServiceTask(),
				Bpmn2Package.eINSTANCE.getSendTask(),
				Bpmn2Package.eINSTANCE.getReceiveTask(),
				Bpmn2Package.eINSTANCE.getChoreographyTask());

		DATA = Arrays.asList(
				Bpmn2Package.eINSTANCE.getDataObjectReference(),
				Bpmn2Package.eINSTANCE.getDataStoreReference(),
				Bpmn2Package.eINSTANCE.getDataInput(),
				Bpmn2Package.eINSTANCE.getDataOutput());

		OTHER = Arrays.asList(
				Bpmn2Package.eINSTANCE.getLane(),
				Bpmn2Package.eINSTANCE.getParticipant(),
				Bpmn2Package.eINSTANCE.getTextAnnotation(),
				Bpmn2Package.eINSTANCE.getSubProcess(),
				Bpmn2Package.eINSTANCE.getTransaction(),
				Bpmn2Package.eINSTANCE.getGroup(),
				Bpmn2Package.eINSTANCE.getAdHocSubProcess(),
				Bpmn2Package.eINSTANCE.getCallActivity(),
				Bpmn2Package.eINSTANCE.getMessage(),
				Bpmn2Package.eINSTANCE.getConversation(),
				Bpmn2Package.eINSTANCE.getSubChoreography(),
				Bpmn2Package.eINSTANCE.getCallChoreography());
		
		ALL_BASE_ELEMENTS = Collections.unmodifiableList(new ArrayList<EClass>() {{
			addAll(CONNECTORS);
			addAll(EVENTS);
			addAll(GATEWAYS);
			addAll(TASKS);
			addAll(DATA);
			addAll(OTHER);
		}});
	}
}
