package org.camunda.bpm.modeler.ui.features.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.EscalationEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.LinkEventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;

public class MorphIntermediateThrowEventFeature extends AbstractMorphEventFeature {

	public MorphIntermediateThrowEventFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public List<MorphOption> getOptions(EObject bo) {
		if (!(bo instanceof IntermediateThrowEvent)) {
			return Collections.emptyList();
		}
		List<MorphOption> options = new ArrayList<MorphOption>();
		
		IntermediateThrowEvent throwEvent = (IntermediateThrowEvent) bo;
		
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(throwEvent);
		
		MessageEventDefinition message = getEventDefinition(MessageEventDefinition.class, eventDefinitions);
		LinkEventDefinition link = getEventDefinition(LinkEventDefinition.class, eventDefinitions);
		SignalEventDefinition signal = getEventDefinition(SignalEventDefinition.class, eventDefinitions);
		EscalationEventDefinition escalation = getEventDefinition(EscalationEventDefinition.class, eventDefinitions);
		CompensateEventDefinition compensate = getEventDefinition(CompensateEventDefinition.class, eventDefinitions);
		
		if (!eventDefinitions.isEmpty()) {
			MorphOption throwNoneEvent = new MorphEventOption("None (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent());
			options.add(throwNoneEvent);
		}
		
		if (message == null || eventDefinitions.size() > 1) {
			MorphOption throwMessageOption = new MorphEventOption("Message (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition());
			options.add(throwMessageOption);		
		}
		
		if (signal == null || eventDefinitions.size() > 1) {
			MorphOption throwSignalOption = new MorphEventOption("Signal (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition());
			options.add(throwSignalOption);
		}
		
		if (escalation == null || eventDefinitions.size() > 1) {
			MorphOption throwEscalationOption = new MorphEventOption("Escalation (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getEscalationEventDefinition());
			options.add(throwEscalationOption);		
		}
		
		if (compensate == null || eventDefinitions.size() > 1) {
			MorphOption throwCompensateOption = new MorphEventOption("Compensate (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getCompensateEventDefinition());
			options.add(throwCompensateOption);		
		}
		
		if (link == null || eventDefinitions.size() > 1) {
			MorphOption throwLinkOption = new MorphEventOption("Link (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getLinkEventDefinition());
			options.add(throwLinkOption);
		}
		
		MorphOption catchMessageOption = new MorphEventOption("Message (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition());
		options.add(catchMessageOption);			

		MorphOption catchTimerOption = new MorphEventOption("Timer (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getTimerEventDefinition());
		options.add(catchTimerOption);

		MorphOption catchConditionalOption = new MorphEventOption("Conditional (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getConditionalEventDefinition());
		options.add(catchConditionalOption);

		MorphOption catchSignalOption = new MorphEventOption("Signal (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition());
		options.add(catchSignalOption);			

		MorphOption catchLinkOption = new MorphEventOption("Link (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getLinkEventDefinition());
		options.add(catchLinkOption);			
		
		return options;
	}

}
