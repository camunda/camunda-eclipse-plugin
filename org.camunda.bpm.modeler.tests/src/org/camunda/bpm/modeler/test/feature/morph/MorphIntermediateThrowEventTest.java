package org.camunda.bpm.modeler.test.feature.morph;

import static org.camunda.bpm.modeler.test.util.operations.MorphIntermediateThrowEventOperation.morphIntermediateThrowEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature.MorphOption;
import org.camunda.bpm.modeler.ui.features.event.AbstractMorphEventFeature.MorphEventOption;
import org.camunda.bpm.modeler.ui.features.event.MorphIntermediateThrowEventFeature;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class MorphIntermediateThrowEventTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateThrowEventTest.testBase.bpmn")
	public void testMorph_BlancoItermediateThrowEventToMessageIntermediateThrowEvent() {
		
		// given
		IntermediateThrowEvent throwEvent = (IntermediateThrowEvent) Util.findBusinessObjectById(diagram, "IntermediateThrowEvent_1");
		Shape throwEventShape = Util.findShapeByBusinessObjectId(diagram, "IntermediateThrowEvent_1");
		
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		
		// when
		morphIntermediateThrowEvent(throwEventShape, diagramTypeProvider)
			.to(throwEvent.eClass(), newEventDefinitionType)
			.execute();
		
		// then
		
		EventDefinition message = ModelUtil.getEventDefinition(throwEvent, MessageEventDefinition.class);
		
		assertThat(message)
			.isNotNull();
		
		// still have data input association
		DataInputAssociation dataInput = (DataInputAssociation) Util.findBusinessObjectById(diagram, "DataInputAssociation_1");
		
		assertThat(throwEvent.getDataInputAssociation())
			.contains(dataInput)
			.hasSize(1);

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

		// check the message flow
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_2");
		
		assertThat(incomingMsg)
			.isNotNull();

		assertThat(incomingMsg.getSourceRef())
			.isEqualTo(throwEvent);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateThrowEventTest.testBase.bpmn")
	public void testMorph_BlancoItermediateThrowEventToSignalIntermediateThrowEvent_ShouldDeleteMessageFlow() {
		
		// given
		IntermediateThrowEvent throwEvent = (IntermediateThrowEvent) Util.findBusinessObjectById(diagram, "IntermediateThrowEvent_1");
		Shape throwEventShape = Util.findShapeByBusinessObjectId(diagram, "IntermediateThrowEvent_1");
		
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getSignalEventDefinition();
		
		// when
		morphIntermediateThrowEvent(throwEventShape, diagramTypeProvider)
			.to(throwEvent.eClass(), newEventDefinitionType)
			.execute();
		
		// then
		
		EventDefinition timer = ModelUtil.getEventDefinition(throwEvent, SignalEventDefinition.class);
		
		assertThat(timer)
			.isNotNull();
	
		// check the message flows
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_2");
		
		assertThat(incomingMsg)
			.isNull();

	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateThrowEventTest.testBase.bpmn")
	public void testMorph_BlancoItermediateThrowEventToMessageIntermediateCatchEvent() {
		
		// given
		Shape throwEventShape = Util.findShapeByBusinessObjectId(diagram, "IntermediateThrowEvent_1");
		
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		
		// when
		morphIntermediateThrowEvent(throwEventShape, diagramTypeProvider)
			.to(Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), newEventDefinitionType)
			.execute();
		
		// then
		
		IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) Util.findBusinessObjectById(diagram, "IntermediateThrowEvent_1");
		EventDefinition message = ModelUtil.getEventDefinition(catchEvent, MessageEventDefinition.class);
		
		assertThat(message)
			.isNotNull();
		
		assertThat(catchEvent)
			.isInstanceOf(IntermediateCatchEvent.class);

		// check the data input association
		DataInputAssociation dataInput = (DataInputAssociation) Util.findBusinessObjectById(diagram, "DataInputAssociation_1");
		
		assertThat(dataInput)
			.isNull();
		
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
		
		// check the message flows
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_2");
		
		assertThat(incomingMsg)
			.isNull();

	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateThrowEventTest.testOptions.bpmn")
	public void testAvailableOptions_IntermediateThrowEvent() {
		shouldContainRightOptions("IntermediateThrowEvent_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateThrowEventTest.testOptions.bpmn")
	public void testAvailableOptions_MessageIntermediateThrowEvent() {
		shouldContainRightOptions("IntermediateThrowEvent_2");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateThrowEventTest.testOptions.bpmn")
	public void testAvailableOptions_SignalIntermediateThrowEvent() {
		shouldContainRightOptions("IntermediateThrowEvent_3");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateThrowEventTest.testOptions.bpmn")
	public void testAvailableOptions_EscalationIntermediateThrowEvent() {
		shouldContainRightOptions("IntermediateThrowEvent_4");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateThrowEventTest.testOptions.bpmn")
	public void testAvailableOptions_CompensateIntermediateThrowEvent() {
		shouldContainRightOptions("IntermediateThrowEvent_5");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateThrowEventTest.testOptions.bpmn")
	public void testAvailableOptions_LinkIntermediateThrowEvent() {
		shouldContainRightOptions("IntermediateThrowEvent_6");
	}
		
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphIntermediateThrowEventTest.testOptions.bpmn")
	public void testAvailableOptions_MultipleIntermediateThrowEvent() {
		shouldContainRightOptions("IntermediateThrowEvent_7");
	}
	
	private void shouldContainRightOptions(String elementId) {
		IntermediateThrowEvent throwEvent = (IntermediateThrowEvent) Util.findBusinessObjectById(diagram, elementId);
		Shape shape = Util.findShapeByBusinessObjectId(diagram, elementId);
		
		MorphIntermediateThrowEventFeature feature = (MorphIntermediateThrowEventFeature) morphIntermediateThrowEvent(shape, diagramTypeProvider).getFeature();
		
		List<MorphOption> options = feature.getOptions(throwEvent);
		
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(throwEvent);
		
		boolean isMultipleEvent = eventDefinitions.size() > 1;
		
		EClass eventDefinitionType = null;
		if (!isMultipleEvent && eventDefinitions.size() == 1) {
			eventDefinitionType = eventDefinitions.get(0).eClass();
		}
		
		List<MorphEventOption> expectedOptions = getExpectedList(throwEvent.eClass(), eventDefinitionType, isMultipleEvent);
		
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
		EClass intermediateThrowEventClass = Bpmn2Package.eINSTANCE.getIntermediateThrowEvent();
		
		EClass messageClass = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		EClass signalClass = Bpmn2Package.eINSTANCE.getSignalEventDefinition();
		EClass escalationClss = Bpmn2Package.eINSTANCE.getEscalationEventDefinition();
		EClass compensateClass = Bpmn2Package.eINSTANCE.getCompensateEventDefinition();
		EClass linkClass = Bpmn2Package.eINSTANCE.getLinkEventDefinition();
		
		List<MorphEventOption> result = new ArrayList<MorphEventOption>();

		if (eventDefinitionType != null || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateThrowEventClass);
			result.add(option);			
		}
		if (!messageClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateThrowEventClass, messageClass);
			result.add(option);
		}
		if (!signalClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateThrowEventClass, signalClass);
			result.add(option);
		}
		if (!escalationClss.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateThrowEventClass, escalationClss);
			result.add(option);
		}
		if (!compensateClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateThrowEventClass, compensateClass);
			result.add(option);
		}
		if (!linkClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", intermediateThrowEventClass, linkClass);
			result.add(option);
		}

		MorphEventOption catchMessageOption = new MorphEventOption("Message (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getMessageEventDefinition());
		result.add(catchMessageOption);			

		MorphEventOption catchTimerOption = new MorphEventOption("Timer (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getTimerEventDefinition());
		result.add(catchTimerOption);

		MorphEventOption catchConditionalOption = new MorphEventOption("Conditional (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getConditionalEventDefinition());
		result.add(catchConditionalOption);

		MorphEventOption catchSignalOption = new MorphEventOption("Signal (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getSignalEventDefinition());
		result.add(catchSignalOption);			

		MorphEventOption catchLinkOption = new MorphEventOption("Link (catch)", Bpmn2Package.eINSTANCE.getIntermediateCatchEvent(), Bpmn2Package.eINSTANCE.getLinkEventDefinition());
		result.add(catchLinkOption);	
		
		return result;
	}	

}
