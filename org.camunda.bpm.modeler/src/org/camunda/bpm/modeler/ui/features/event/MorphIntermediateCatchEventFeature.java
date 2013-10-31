package org.camunda.bpm.modeler.ui.features.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.LinkEventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;

public class MorphIntermediateCatchEventFeature extends AbstractMorphEventFeature {

	public MorphIntermediateCatchEventFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public List<MorphOption> getOptions(EObject bo) {
		if (!(bo instanceof IntermediateCatchEvent)) {
			return Collections.emptyList();
		}
		List<MorphOption> options = new ArrayList<MorphOption>();
		
		IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) bo;
		
		List<EventDefinition> eventDefinitions = getEventDefinitions(catchEvent);
		
		MessageEventDefinition message = getEventDefinition(MessageEventDefinition.class, eventDefinitions);
		TimerEventDefinition timer = getEventDefinition(TimerEventDefinition.class, eventDefinitions);
		ConditionalEventDefinition conditional = getEventDefinition(ConditionalEventDefinition.class, eventDefinitions);
		LinkEventDefinition link = getEventDefinition(LinkEventDefinition.class, eventDefinitions);
		SignalEventDefinition signal = getEventDefinition(SignalEventDefinition.class, eventDefinitions);
		
		if (message == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Message (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition());
			options.add(newOption);			
		}
		
		if (timer == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Timer (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getTimerEventDefinition());
			options.add(newOption);
		}
		
		if (conditional == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Conditional (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getConditionalEventDefinition());
			options.add(newOption);
		}
		
		if (signal == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Signal (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition());
			options.add(newOption);			
		}
		
		if (link == null || eventDefinitions.size() > 1) {
			MorphOption newOption = new MorphEventOption("Link (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getLinkEventDefinition());
			options.add(newOption);			
		}
		
		MorphOption throwNoneEvent = new MorphEventOption("None (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent());
		options.add(throwNoneEvent);

		MorphOption throwMessageOption = new MorphEventOption("Message (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition());
		options.add(throwMessageOption);		
		
		MorphOption throwSignalOption = new MorphEventOption("Signal (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition());
		options.add(throwSignalOption);
		
		MorphOption throwEscalationOption = new MorphEventOption("Escalation (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getEscalationEventDefinition());
		options.add(throwEscalationOption);		

		MorphOption throwCompensateOption = new MorphEventOption("Compensate(throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getCompensateEventDefinition());
		options.add(throwCompensateOption);	
		
		MorphOption throwLinkOption = new MorphEventOption("Link (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getLinkEventDefinition());
		options.add(throwLinkOption);
		
		return options;
	}

}
