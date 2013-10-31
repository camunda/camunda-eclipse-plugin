package org.camunda.bpm.modeler.ui.features.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CancelEventDefinition;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.bpmn2.EscalationEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.Transaction;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;

public class MorphBoundaryEventFeature extends AbstractMorphEventFeature {

	public MorphBoundaryEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public List<MorphOption> getOptions(EObject bo) {
		if (!(bo instanceof BoundaryEvent)) {
			return Collections.emptyList();
		}
		
		List<MorphOption> options = new ArrayList<MorphOption>();
		
		BoundaryEvent boundaryEvent = (BoundaryEvent) bo;
		
		List<EventDefinition> eventDefinitions = getEventDefinitions(boundaryEvent);
		
		MessageEventDefinition messageDef = getEventDefinition(MessageEventDefinition.class, eventDefinitions);
		TimerEventDefinition timerDef = getEventDefinition(TimerEventDefinition.class, eventDefinitions);
		ConditionalEventDefinition conditionalDef = getEventDefinition(ConditionalEventDefinition.class, eventDefinitions);
		SignalEventDefinition signalDef = getEventDefinition(SignalEventDefinition.class, eventDefinitions);
		EscalationEventDefinition escalationDef = getEventDefinition(EscalationEventDefinition.class, eventDefinitions);
		
		if (messageDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Message", Bpmn2Package.eINSTANCE.getBoundaryEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition());
			options.add(newOption);
		}

		if (timerDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Timer", Bpmn2Package.eINSTANCE.getBoundaryEvent(), Bpmn2Package.eINSTANCE.getTimerEventDefinition());
			options.add(newOption);
		}

		if (conditionalDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Conditional", Bpmn2Package.eINSTANCE.getBoundaryEvent(), Bpmn2Package.eINSTANCE.getConditionalEventDefinition());
			options.add(newOption);
		}
		
		if (signalDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Signal", Bpmn2Package.eINSTANCE.getBoundaryEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition());
			options.add(newOption);
		}
		
		if (escalationDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Escalate", Bpmn2Package.eINSTANCE.getBoundaryEvent(), Bpmn2Package.eINSTANCE.getEscalationEventDefinition());
			options.add(newOption);
		}

		if (boundaryEvent.isCancelActivity()) {
			ErrorEventDefinition errorDef = getEventDefinition(ErrorEventDefinition.class, eventDefinitions);
			CompensateEventDefinition compensateDef = getEventDefinition(CompensateEventDefinition.class, eventDefinitions);

			if (errorDef == null || eventDefinitions.size() > 1) {
				MorphOption newOption = new MorphEventOption("Error", Bpmn2Package.eINSTANCE.getBoundaryEvent(), Bpmn2Package.eINSTANCE.getErrorEventDefinition());
				options.add(newOption);
			}
			
			if (compensateDef == null || eventDefinitions.size() > 1) {
				MorphOption newOption = new MorphEventOption("Compensate", Bpmn2Package.eINSTANCE.getBoundaryEvent(), Bpmn2Package.eINSTANCE.getCompensateEventDefinition());
				options.add(newOption);
			}
			
			if (boundaryEvent.getAttachedToRef() instanceof Transaction) {
				CancelEventDefinition cancelDef = getEventDefinition(CancelEventDefinition.class, eventDefinitions);
				
				if (cancelDef == null || eventDefinitions.size() > 1) {
					MorphOption newOption = new MorphEventOption("Cancel", Bpmn2Package.eINSTANCE.getBoundaryEvent(), Bpmn2Package.eINSTANCE.getCancelEventDefinition());
					options.add(newOption);
				}
			}
			
		}
		
		return options;
	}

}
