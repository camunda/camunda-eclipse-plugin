package org.camunda.bpm.modeler.ui.features.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.bpmn2.EscalationEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;

public class MorphStartEventFeature extends AbstractMorphEventFeature {

	public MorphStartEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		if (!super.canExecute(context)) {
			return false;
		}
		
		EObject bo = (EObject) getBusinessObjectForPictogramElement(context.getPictogramElements()[0]);
		
		if (bo.eContainer() instanceof SubProcess) {
			SubProcess subProcessContainer = (SubProcess) bo.eContainer();
			return subProcessContainer.isTriggeredByEvent();
		}
		
		return true;
	}
	
	@Override
	public List<MorphOption> getOptions(EObject bo) {
		if (!(bo instanceof StartEvent)) {
			return Collections.emptyList();
		}
		
		List<MorphOption> options = new ArrayList<MorphOption>();
		
		StartEvent startEvent = (StartEvent) bo;
		
		EObject container = startEvent.eContainer();
		
		SubProcess subProcessContainer = null;
		if (container instanceof SubProcess) {
			subProcessContainer = (SubProcess) container;
		}
		
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(startEvent);
		
		MessageEventDefinition messageDef = getEventDefinition(MessageEventDefinition.class, eventDefinitions);
		TimerEventDefinition timerDef = getEventDefinition(TimerEventDefinition.class, eventDefinitions);
		SignalEventDefinition signalDef = getEventDefinition(SignalEventDefinition.class, eventDefinitions);
		ConditionalEventDefinition conditionDef = getEventDefinition(ConditionalEventDefinition.class, eventDefinitions);
		
		if (!eventDefinitions.isEmpty() && subProcessContainer == null) {
			MorphOption newOption = new MorphEventOption("None", Bpmn2Package.eINSTANCE.getStartEvent());
			options.add(newOption);
		}
		
		if (messageDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Message", Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition());
			options.add(newOption);
		}

		if (timerDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Timer", Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getTimerEventDefinition());
			options.add(newOption);
		}

		if (signalDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Signal", Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition());
			options.add(newOption);
		}
		
		if (conditionDef == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Conditional", Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getConditionalEventDefinition());
			options.add(newOption);
		}
		
		if (subProcessContainer != null && subProcessContainer.isTriggeredByEvent()) {
			
			EscalationEventDefinition escalationDef = getEventDefinition(EscalationEventDefinition.class, eventDefinitions);
			
			if (escalationDef == null || eventDefinitions.size() > 1) {
				MorphOption newOption = new MorphEventOption("Escalation", Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getEscalationEventDefinition());
				options.add(newOption);				
			}
			
			if (startEvent.isIsInterrupting()) {
				ErrorEventDefinition errorDef = getEventDefinition(ErrorEventDefinition.class, eventDefinitions);
				CompensateEventDefinition compensationDef = getEventDefinition(CompensateEventDefinition.class, eventDefinitions);
				
				if (errorDef == null || eventDefinitions.size() > 1) {
					MorphOption newOption = new MorphEventOption("Error", Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getErrorEventDefinition());
					options.add(newOption);				
				}
				
				if (compensationDef == null || eventDefinitions.size() > 1) {
					MorphOption newOption = new MorphEventOption("Compensate", Bpmn2Package.eINSTANCE.getStartEvent(), Bpmn2Package.eINSTANCE.getCompensateEventDefinition());
					options.add(newOption);				
				}				
				
			}
			
		}
		return options;
	}

}
