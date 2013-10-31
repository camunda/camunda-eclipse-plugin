package org.camunda.bpm.modeler.test.feature.morph;

import static org.camunda.bpm.modeler.test.util.operations.MorphIntermediateCatchEventOperation.morphIntermediateCatchEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature.MorphOption;
import org.camunda.bpm.modeler.ui.features.event.AbstractMorphEventFeature.MorphEventOption;
import org.camunda.bpm.modeler.ui.features.event.MorphIntermediateCatchEventFeature;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class MorphIntermediateCatchEventTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateCatchEventTest.testBase.bpmn")
	public void testMorph_BlancoItermediateCatchEventToMessageIntermediateCatchEvent() {
		
		// given
		IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) Util.findBusinessObjectById(diagram, "IntermediateCatchEvent_1");
		Shape catchEventShape = Util.findShapeByBusinessObjectId(diagram, "IntermediateCatchEvent_1");
		
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		
		// when
		morphIntermediateCatchEvent(catchEventShape, diagramTypeProvider)
			.to(catchEvent.eClass(), newEventDefinitionType)
			.execute();
		
		// then
		
		EventDefinition message = ModelUtil.getEventDefinition(catchEvent, MessageEventDefinition.class);
		
		assertThat(message)
			.isNotNull();
		
		// still have dataoutputassociation
		DataOutputAssociation dataOutput = (DataOutputAssociation) Util.findBusinessObjectById(diagram, "DataOutputAssociation_1");
		
		assertThat(catchEvent.getDataOutputAssociation())
			.contains(dataOutput)
			.hasSize(1);

		// the containing associations have the right references
		Association association1 = (Association) Util.findBusinessObjectById(diagram, "Association_1");
		
		assertThat(association1.getSourceRef())
			.isEqualTo(catchEvent);
		
		// check the incoming and outgoing sequence flows
		SequenceFlow outgoing = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_1");
		
		assertThat(outgoing.getSourceRef())
			.isEqualTo(catchEvent);

		assertThat(catchEvent.getOutgoing())
			.contains(outgoing);

		// check the message flow
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_3");
		
		assertThat(incomingMsg)
			.isNotNull();

		assertThat(incomingMsg.getTargetRef())
			.isEqualTo(catchEvent);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateCatchEventTest.testBase.bpmn")
	public void testMorph_BlancoItermediateCatchEventToTimerIntermediateCatchEvent_ShouldDeleteMessageFlow() {
		
		// given
		IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) Util.findBusinessObjectById(diagram, "IntermediateCatchEvent_1");
		Shape catchEventShape = Util.findShapeByBusinessObjectId(diagram, "IntermediateCatchEvent_1");
		
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getTimerEventDefinition();
		
		// when
		morphIntermediateCatchEvent(catchEventShape, diagramTypeProvider)
			.to(catchEvent.eClass(), newEventDefinitionType)
			.execute();
		
		// then
		
		EventDefinition timer = ModelUtil.getEventDefinition(catchEvent, TimerEventDefinition.class);
		
		assertThat(timer)
			.isNotNull();
	
		// check the message flows
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_3");
		
		assertThat(incomingMsg)
			.isNull();

	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateCatchEventTest.testBase.bpmn")
	public void testMorph_BlancoItermediateCatchEventToMessageIntermediateThrowEvent() {
		
		// given
		Shape catchEventShape = Util.findShapeByBusinessObjectId(diagram, "IntermediateCatchEvent_1");
		
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		
		// when
		morphIntermediateCatchEvent(catchEventShape, diagramTypeProvider)
			.to(Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), newEventDefinitionType)
			.execute();
		
		// then
		
		IntermediateThrowEvent throwEvent = (IntermediateThrowEvent) Util.findBusinessObjectById(diagram, "IntermediateCatchEvent_1");
		EventDefinition message = ModelUtil.getEventDefinition(throwEvent, MessageEventDefinition.class);
		
		assertThat(message)
			.isNotNull();
		
		assertThat(throwEvent)
			.isInstanceOf(IntermediateThrowEvent.class);

		// check the dataoutputassociation
		DataOutputAssociation dataOutput = (DataOutputAssociation) Util.findBusinessObjectById(diagram, "DataOutputAssociation_1");
		
		assertThat(dataOutput)
			.isNull();
		
		// the containing associations have the right references
		Association association1 = (Association) Util.findBusinessObjectById(diagram, "Association_1");
		
		assertThat(association1.getSourceRef())
			.isEqualTo(throwEvent);
		
		// check the incoming and outgoing sequence flows
		SequenceFlow outgoing = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_1");
		
		assertThat(outgoing.getSourceRef())
			.isEqualTo(throwEvent);

		assertThat(throwEvent.getOutgoing())
			.contains(outgoing);	
		
		// check the message flows
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_3");
		
		assertThat(incomingMsg)
			.isNull();

	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateCatchEventTest.testOptions.bpmn")
	public void testAvailableOptions_IntermediateCatchEvent() {
		shouldContainRightOptions("IntermediateCatchEvent_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateCatchEventTest.testOptions.bpmn")
	public void testAvailableOptions_MessageIntermediateCatchEvent() {
		shouldContainRightOptions("IntermediateCatchEvent_2");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateCatchEventTest.testOptions.bpmn")
	public void testAvailableOptions_TimerIntermediateCatchEvent() {
		shouldContainRightOptions("IntermediateCatchEvent_3");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateCatchEventTest.testOptions.bpmn")
	public void testAvailableOptions_ConditionalIntermediateCatchEvent() {
		shouldContainRightOptions("IntermediateCatchEvent_4");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateCatchEventTest.testOptions.bpmn")
	public void testAvailableOptions_SignalIntermediateCatchEvent() {
		shouldContainRightOptions("IntermediateCatchEvent_5");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateCatchEventTest.testOptions.bpmn")
	public void testAvailableOptions_LinkIntermediateCatchEvent() {
		shouldContainRightOptions("IntermediateCatchEvent_6");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateCatchEventTest.testOptions.bpmn")
	public void testAvailableOptions_MultipleIntermediateCatchEvent() {
		shouldContainRightOptions("IntermediateCatchEvent_7");
	}
	
	private void shouldContainRightOptions(String elementId) {
		IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) Util.findBusinessObjectById(diagram, elementId);
		Shape shape = Util.findShapeByBusinessObjectId(diagram, elementId);
		
		MorphIntermediateCatchEventFeature feature = (MorphIntermediateCatchEventFeature) morphIntermediateCatchEvent(shape, diagramTypeProvider).getFeature();
		
		List<MorphOption> options = feature.getOptions(catchEvent);
		
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(catchEvent);
		
		boolean isMultipleEvent = eventDefinitions.size() > 1;
		
		EClass eventDefinitionType = null;
		if (!isMultipleEvent && eventDefinitions.size() == 1) {
			eventDefinitionType = eventDefinitions.get(0).eClass();
		}
		
		List<MorphEventOption> expectedOptions = getExpectedList(catchEvent.eClass(), eventDefinitionType, isMultipleEvent);
		
		assertThat(options.size())
			.isEqualTo(expectedOptions.size());
		
		for (MorphOption morphOption : options) {
			MorphEventOption morphEventOption = (MorphEventOption) morphOption;
			
			boolean found = false;
			
			for (MorphEventOption morphEventOption2 : expectedOptions) {
				if (morphEventOption.getNewType().equals(morphEventOption2.getNewType())) {
					if ((morphEventOption.getNewEventDefinitionType() == null && morphEventOption2.getNewEventDefinitionType() == null) ||
						 morphEventOption.getNewEventDefinitionType().equals(morphEventOption2.getNewEventDefinitionType())) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				Assertions.fail("The list of available options contains a unexpected type: " + morphEventOption.getNewType().getName() + " and " + morphEventOption.getNewEventDefinitionType().getName());
			}
			
		}
	}
	
	private List<MorphEventOption> getExpectedList(EClass eventType, EClass eventDefinitionType, boolean multipleEvent) {
		EClass intermediateCatchEventClass = Bpmn2Package.eINSTANCE.getIntermediateCatchEvent();
		
		EClass messageClass = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		EClass timerClass = Bpmn2Package.eINSTANCE.getTimerEventDefinition();
		EClass conditionalClass = Bpmn2Package.eINSTANCE.getConditionalEventDefinition();
		EClass signalClass = Bpmn2Package.eINSTANCE.getSignalEventDefinition();
		EClass linkClass = Bpmn2Package.eINSTANCE.getLinkEventDefinition();
		
		List<MorphEventOption> result = new ArrayList<MorphEventOption>();

		if (!messageClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateCatchEventClass, messageClass);
			result.add(option);
		}
		if (!timerClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateCatchEventClass, timerClass);
			result.add(option);
		}	
		if (!conditionalClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateCatchEventClass, conditionalClass);
			result.add(option);
		}	
		if (!signalClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateCatchEventClass, signalClass);
			result.add(option);
		}
		if (!linkClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateCatchEventClass, linkClass);
			result.add(option);
		}

		MorphEventOption throwNoneEvent = new MorphEventOption("None (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent());
		result.add(throwNoneEvent);

		MorphEventOption throwMessageOption = new MorphEventOption("Message (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition());
		result.add(throwMessageOption);		
		
		MorphEventOption throwSignalOption = new MorphEventOption("Signal (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition());
		result.add(throwSignalOption);
		
		MorphEventOption throwEscalationOption = new MorphEventOption("Escalation (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getEscalationEventDefinition());
		result.add(throwEscalationOption);		

		MorphEventOption throwCompensateOption = new MorphEventOption("Compensate(throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getCompensateEventDefinition());
		result.add(throwCompensateOption);	
		
		MorphEventOption throwLinkOption = new MorphEventOption("Link (throw)", Bpmn2Package.eINSTANCE.getIntermediateThrowEvent(), Bpmn2Package.eINSTANCE.getLinkEventDefinition());
		result.add(throwLinkOption);
		
		return result;
	}

}
