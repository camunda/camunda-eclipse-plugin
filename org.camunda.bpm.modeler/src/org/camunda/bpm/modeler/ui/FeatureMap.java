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
import java.util.Collections;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;

public class FeatureMap {
	
	public static final List<EClass> CONNECTORS;
	public static final List<EClass> EVENT_DEFINITIONS;
	public static final List<EClass> EVENTS;
	public static final List<EClass> GATEWAYS;
	public static final List<EClass> TASKS;
	public static final List<EClass> DATA;
	public static final List<EClass> OTHER;
	
	static {
		
		ArrayList<EClass> features = new ArrayList<EClass>();
		features.add(Bpmn2Package.eINSTANCE.getSequenceFlow());
		features.add(Bpmn2Package.eINSTANCE.getMessageFlow());
		features.add(Bpmn2Package.eINSTANCE.getAssociation());
		features.add(Bpmn2Package.eINSTANCE.getDataAssociation());
		CONNECTORS = Collections.unmodifiableList(features);

		features = new ArrayList<EClass>();
		features.add(Bpmn2Package.eINSTANCE.getConditionalEventDefinition());
		features.add(Bpmn2Package.eINSTANCE.getTimerEventDefinition());
		features.add(Bpmn2Package.eINSTANCE.getSignalEventDefinition());
		features.add(Bpmn2Package.eINSTANCE.getMessageEventDefinition());
		features.add(Bpmn2Package.eINSTANCE.getEscalationEventDefinition());
		features.add(Bpmn2Package.eINSTANCE.getCompensateEventDefinition());
		features.add(Bpmn2Package.eINSTANCE.getLinkEventDefinition());
		features.add(Bpmn2Package.eINSTANCE.getErrorEventDefinition());
		features.add(Bpmn2Package.eINSTANCE.getCancelEventDefinition());
		features.add(Bpmn2Package.eINSTANCE.getTerminateEventDefinition());
		EVENT_DEFINITIONS = Collections.unmodifiableList(features);

		features = new ArrayList<EClass>();
		features.add(Bpmn2Package.eINSTANCE.getStartEvent());
		features.add(Bpmn2Package.eINSTANCE.getEndEvent());
		features.add(Bpmn2Package.eINSTANCE.getIntermediateThrowEvent());
		features.add(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent());
		features.add(Bpmn2Package.eINSTANCE.getBoundaryEvent());
		EVENTS = Collections.unmodifiableList(features);

		features = new ArrayList<EClass>();
		features.add(Bpmn2Package.eINSTANCE.getInclusiveGateway());
		features.add(Bpmn2Package.eINSTANCE.getExclusiveGateway());
		features.add(Bpmn2Package.eINSTANCE.getParallelGateway());
		features.add(Bpmn2Package.eINSTANCE.getEventBasedGateway());
		features.add(Bpmn2Package.eINSTANCE.getComplexGateway());

		GATEWAYS = Collections.unmodifiableList(features);

		features = new ArrayList<EClass>();
		features.add(Bpmn2Package.eINSTANCE.getTask());
		features.add(Bpmn2Package.eINSTANCE.getManualTask());
		features.add(Bpmn2Package.eINSTANCE.getUserTask());
		features.add(Bpmn2Package.eINSTANCE.getScriptTask());
		features.add(Bpmn2Package.eINSTANCE.getBusinessRuleTask());
		features.add(Bpmn2Package.eINSTANCE.getServiceTask());
		features.add(Bpmn2Package.eINSTANCE.getSendTask());
		features.add(Bpmn2Package.eINSTANCE.getReceiveTask());
		features.add(Bpmn2Package.eINSTANCE.getChoreographyTask());
		TASKS = Collections.unmodifiableList(features);

		features = new ArrayList<EClass>();
		features.add(Bpmn2Package.eINSTANCE.getDataObjectReference());
		features.add(Bpmn2Package.eINSTANCE.getDataStoreReference());
		features.add(Bpmn2Package.eINSTANCE.getDataInput());
		features.add(Bpmn2Package.eINSTANCE.getDataOutput());
		DATA = Collections.unmodifiableList(features);

		features = new ArrayList<EClass>();
		features.add(Bpmn2Package.eINSTANCE.getLane());
		features.add(Bpmn2Package.eINSTANCE.getParticipant());
		features.add(Bpmn2Package.eINSTANCE.getTextAnnotation());
		features.add(Bpmn2Package.eINSTANCE.getSubProcess());
		features.add(Bpmn2Package.eINSTANCE.getTransaction());
		features.add(Bpmn2Package.eINSTANCE.getGroup());
		features.add(Bpmn2Package.eINSTANCE.getAdHocSubProcess());
		features.add(Bpmn2Package.eINSTANCE.getCallActivity());
		features.add(Bpmn2Package.eINSTANCE.getMessage());
		features.add(Bpmn2Package.eINSTANCE.getConversation());
		features.add(Bpmn2Package.eINSTANCE.getSubChoreography());
		features.add(Bpmn2Package.eINSTANCE.getCallChoreography());
		OTHER = Collections.unmodifiableList(features);

	}
}
