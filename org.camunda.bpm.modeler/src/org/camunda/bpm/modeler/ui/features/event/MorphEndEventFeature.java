package org.camunda.bpm.modeler.ui.features.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CancelEventDefinition;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.bpmn2.EscalationEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.TerminateEventDefinition;
import org.eclipse.bpmn2.Transaction;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;

public class MorphEndEventFeature extends AbstractMorphEventFeature {

	public MorphEndEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public List<MorphOption> getOptions(EObject bo) {
		if (!(bo instanceof EndEvent)) {
			return Collections.emptyList();
		}
		
		List<MorphOption> options = new ArrayList<MorphOption>();
		
		EndEvent endEvent = (EndEvent) bo;
		
		List<EventDefinition> eventDefinitions = getEventDefinitions(endEvent);
		
		MessageEventDefinition messageDef = getEventDefinition(MessageEventDefinition.class, eventDefinitions);
		SignalEventDefinition signalDef = getEventDefinition(SignalEventDefinition.class, eventDefinitions);
		ErrorEventDefinition errorDef = getEventDefinition(ErrorEventDefinition.class, eventDefinitions);
		EscalationEventDefinition escalationDef = getEventDefinition(EscalationEventDefinition.class, eventDefinitions);
		TerminateEventDefinition terminateDef = getEventDefinition(TerminateEventDefinition.class, eventDefinitions);
		CompensateEventDefinition compensateDef = getEventDefinition(CompensateEventDefinition.class, eventDefinitions);
		
		if (!eventDefinitions.isEmpty()) {
			MorphOption newOption = new MorphEventOption("None", Bpmn2Package.eINSTANCE.getEndEvent());
			options.add(newOption);
		}
		
		if (messageDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Message", Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition());
			options.add(newOption);
		}

		if (signalDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Signal", Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition());
			options.add(newOption);
		}

		if (errorDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Error", Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getErrorEventDefinition());
			options.add(newOption);
		}
		
		if (escalationDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Escalation", Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getEscalationEventDefinition());
			options.add(newOption);
		}

		if (terminateDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Terminate", Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getTerminateEventDefinition());
			options.add(newOption);
		}
		
		if (compensateDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Compensate", Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getCompensateEventDefinition());
			options.add(newOption);
		}
		
		EObject container = endEvent.eContainer();
		if (container instanceof Transaction) {
			CancelEventDefinition cancelDef = getEventDefinition(CancelEventDefinition.class, eventDefinitions);
			
			if (cancelDef == null || eventDefinitions.size() > 1) {
				MorphOption newOption = new MorphEventOption("Cancel", Bpmn2Package.eINSTANCE.getEndEvent(), Bpmn2Package.eINSTANCE.getCancelEventDefinition());
				options.add(newOption);				
			}
		}
		
		return options;
	}
	
}
