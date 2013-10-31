package org.camunda.bpm.modeler.test.feature.morph;

import static org.camunda.bpm.modeler.test.util.operations.MorphStartEventOperation.morphStartEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature.MorphOption;
import org.camunda.bpm.modeler.ui.features.event.AbstractMorphEventFeature.MorphEventOption;
import org.camunda.bpm.modeler.ui.features.event.MorphStartEventFeature;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class MorphStartEventFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testBase.bpmn")
	public void testMorph_BlancoStartEventToMessageStartEvent() {
		
		// given
		StartEvent startEvent = (StartEvent) Util.findBusinessObjectById(diagram, "StartEvent_1");
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		
		// when
		morphStartEvent(startEventShape, diagramTypeProvider)
			.to(newEventDefinitionType)
			.execute();
		
		// then
		
		// check whether it is a service task
		EventDefinition message = ModelUtil.getEventDefinition(startEvent, MessageEventDefinition.class);
		
		assertThat(message)
			.isNotNull();
		
		// still have dataoutputassociation
		DataOutputAssociation dataOutput = (DataOutputAssociation) Util.findBusinessObjectById(diagram, "DataOutputAssociation_1");
		
		assertThat(startEvent.getDataOutputAssociation())
			.contains(dataOutput)
			.hasSize(1);

		// the containing associations have the right references
		Association association1 = (Association) Util.findBusinessObjectById(diagram, "Association_1");
		
		assertThat(association1.getSourceRef())
			.isEqualTo(startEvent);
		
		// check the incoming and outgoing sequence flows
		SequenceFlow outgoing = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_1");
		
		assertThat(outgoing.getSourceRef())
			.isEqualTo(startEvent);

		assertThat(startEvent.getOutgoing())
			.contains(outgoing);

	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testBase.bpmn")
	public void testMorph_MessageStartEventToSignalStartEvent_shouldDeleteMessageFlow() {
		
		// given
		StartEvent startEvent = (StartEvent) Util.findBusinessObjectById(diagram, "StartEvent_2");
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getSignalEventDefinition();
		
		// when
		morphStartEvent(startEventShape, diagramTypeProvider)
			.to(newEventDefinitionType)
			.execute();
		
		// then
		
		// check whether it is a service task
		EventDefinition message = ModelUtil.getEventDefinition(startEvent, SignalEventDefinition.class);
		
		assertThat(message)
			.isNotNull();
		
		// check the message flows
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_1");
		
		assertThat(incomingMsg)
			.isNull();
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_StartEvent() {
		shouldContainRightOptions("StartEvent_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_MessageStartEvent() {
		shouldContainRightOptions("StartEvent_2");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_TimerStartEvent() {
		shouldContainRightOptions("StartEvent_3");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_SignalStartEvent() {
		shouldContainRightOptions("StartEvent_4");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_ConditionalStartEvent() {
		shouldContainRightOptions("StartEvent_5");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_MultipleStartEvent() {
		shouldContainRightOptions("StartEvent_20");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_StartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_6");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_MessageStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_7");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_TimerStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_8");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_SignalStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_9");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_ConditionalStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_10");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_EscalationStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_11");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_ErrorStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_12");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_CompensateStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_13");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_MultipleStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_21");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_NonInterruptingStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_14");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_NonInterruptingMessageStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_15");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_NonInterruptingTimerStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_16");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_NonInterruptingSignalStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_17");
	}	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_NonInterruptingConditionalStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_18");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_NonInterruptingEscalationStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_19");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphStartEventTest.testOptions.bpmn")
	public void testAvailableOptions_NonInterruptingMultipleStartEventInEventSubProcess() {
		shouldContainRightOptions("StartEvent_22");
	}
	
	private void shouldContainRightOptions(String elementId) {
		StartEvent startEvent = (StartEvent) Util.findBusinessObjectById(diagram, elementId);
		Shape shape = Util.findShapeByBusinessObjectId(diagram, elementId);
		
		MorphStartEventFeature feature = (MorphStartEventFeature) morphStartEvent(shape, diagramTypeProvider).getFeature();
		
		List<MorphOption> options = feature.getOptions(startEvent);
		
		boolean isEventSubProcess = startEvent.eContainer() instanceof SubProcess;
		boolean isInterrupting = startEvent.isIsInterrupting();
		
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(startEvent);
		
		boolean isMultipleEvent = eventDefinitions.size() > 1;
		
		EClass eventDefinitionType = null;
		if (!isMultipleEvent && eventDefinitions.size() == 1) {
			eventDefinitionType = eventDefinitions.get(0).eClass();
		}
		
		List<MorphEventOption> expectedOptions = getExpectedList(startEvent.eClass(), eventDefinitionType, isMultipleEvent, isEventSubProcess, isInterrupting);
		
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
	
	private List<MorphEventOption> getExpectedList(EClass eventType, EClass eventDefinitionType, boolean multipleEvent, boolean isEventSubProcess, boolean isInterrupting) {
		EClass startEventClass = Bpmn2Package.eINSTANCE.getStartEvent();
		
		EClass messageClass = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		EClass timerClass = Bpmn2Package.eINSTANCE.getTimerEventDefinition();
		EClass signalClass = Bpmn2Package.eINSTANCE.getSignalEventDefinition();
		EClass conditionalClass = Bpmn2Package.eINSTANCE.getConditionalEventDefinition();
		
		List<MorphEventOption> result = new ArrayList<MorphEventOption>();
		
		if ((eventDefinitionType != null || multipleEvent) && !isEventSubProcess) {
			MorphEventOption option = new MorphEventOption("Test", startEventClass);
			result.add(option);			
		}
		if (!messageClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", startEventClass, messageClass);
			result.add(option);
		}
		if (!timerClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", startEventClass, timerClass);
			result.add(option);
		}
		if (!signalClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", startEventClass, signalClass);
			result.add(option);
		}
		if (!conditionalClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", startEventClass, conditionalClass);
			result.add(option);
		}
		
		if (isEventSubProcess) {
			EClass escalationClass = Bpmn2Package.eINSTANCE.getEscalationEventDefinition();
			
			if (!escalationClass.equals(eventDefinitionType) || multipleEvent) {
				MorphEventOption option = new MorphEventOption("Test", startEventClass, escalationClass);
				result.add(option);
			}
			
			if (isInterrupting) {
				EClass errorClass = Bpmn2Package.eINSTANCE.getErrorEventDefinition();
				EClass compensateClass = Bpmn2Package.eINSTANCE.getCompensateEventDefinition();
				
				if (!errorClass.equals(eventDefinitionType) || multipleEvent) {
					MorphEventOption option = new MorphEventOption("Test", startEventClass, errorClass);
					result.add(option);
				}			

				if (!compensateClass.equals(eventDefinitionType) || multipleEvent) {
					MorphEventOption option = new MorphEventOption("Test", startEventClass, compensateClass);
					result.add(option);
				}	
			}
		}

		return result;
	}
	
}
