package org.camunda.bpm.modeler.test.feature.morph;

import static org.camunda.bpm.modeler.test.util.operations.MorphEndEventOperation.morphEndEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature.MorphOption;
import org.camunda.bpm.modeler.ui.features.event.AbstractMorphEventFeature.MorphEventOption;
import org.camunda.bpm.modeler.ui.features.event.MorphEndEventFeature;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.Transaction;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class MorphEndEventFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testBase.bpmn")
	public void testMorph_BlancoEndEventToMessageEndEvent() {
		
		// given
		EndEvent endEvent = (EndEvent) Util.findBusinessObjectById(diagram, "EndEvent_1");
		Shape endEventShape = Util.findShapeByBusinessObjectId(diagram, "EndEvent_1");
		
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		
		// when
		morphEndEvent(endEventShape, diagramTypeProvider)
			.to(newEventDefinitionType)
			.execute();
		
		// then
		
		EventDefinition message = ModelUtil.getEventDefinition(endEvent, MessageEventDefinition.class);
		
		assertThat(message)
			.isNotNull();
		
		// still have data input association
		DataInputAssociation dataInput = (DataInputAssociation) Util.findBusinessObjectById(diagram, "DataInputAssociation_1");
		
		assertThat(endEvent.getDataInputAssociation())
			.contains(dataInput)
			.hasSize(1);

		// the containing associations have the right references
		Association association1 = (Association) Util.findBusinessObjectById(diagram, "Association_1");
		
		assertThat(association1.getTargetRef())
			.isEqualTo(endEvent);
		
		// check the incoming and outgoing sequence flows
		SequenceFlow incoming = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_1");
		
		assertThat(incoming.getTargetRef())
			.isEqualTo(endEvent);

		assertThat(endEvent.getIncoming())
			.contains(incoming);

		// check the message flow
		MessageFlow outgoingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_1");
		
		assertThat(outgoingMsg)
			.isNotNull();

		assertThat(outgoingMsg.getSourceRef())
			.isEqualTo(endEvent);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testBase.bpmn")
	public void testMorph_BlancoEndEventToSignalEndEvent_ShouldDeleteMessageFlow() {
		
		// given
		EndEvent endEvent = (EndEvent) Util.findBusinessObjectById(diagram, "EndEvent_1");
		Shape endEventShape = Util.findShapeByBusinessObjectId(diagram, "EndEvent_1");
		
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getSignalEventDefinition();
		
		// when
		morphEndEvent(endEventShape, diagramTypeProvider)
			.to(newEventDefinitionType)
			.execute();
		
		// then
		
		EventDefinition signal = ModelUtil.getEventDefinition(endEvent, SignalEventDefinition.class);
		
		assertThat(signal)
			.isNotNull();
	
		// check the message flows
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_1");
		
		assertThat(incomingMsg)
			.isNull();
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testOptions.bpmn")
	public void testAvailableOptions_EndEvent() {
		shouldContainRightOptions("EndEvent_1");
	}	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testOptions.bpmn")
	public void testAvailableOptions_MessageEndEvent() {
		shouldContainRightOptions("EndEvent_2");
	}	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testOptions.bpmn")
	public void testAvailableOptions_SignalEndEvent() {
		shouldContainRightOptions("EndEvent_3");
	}	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testOptions.bpmn")
	public void testAvailableOptions_ErrorEndEvent() {
		shouldContainRightOptions("EndEvent_4");
	}	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testOptions.bpmn")
	public void testAvailableOptions_EscalationEndEvent() {
		shouldContainRightOptions("EndEvent_5");
	}	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testOptions.bpmn")
	public void testAvailableOptions_TerminateEndEvent() {
		shouldContainRightOptions("EndEvent_6");
	}	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testOptions.bpmn")
	public void testAvailableOptions_CompensateEndEvent() {
		shouldContainRightOptions("EndEvent_7");
	}	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testOptions.bpmn")
	public void testAvailableOptions_MultipleEndEvent() {
		shouldContainRightOptions("EndEvent_8");
	}	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphEndEventTest.testOptions.bpmn")
	public void testAvailableOptions_EndEventInTransaction() {
		shouldContainRightOptions("EndEvent_9");
	}
	
	private void shouldContainRightOptions(String elementId) {
		EndEvent endEvent = (EndEvent) Util.findBusinessObjectById(diagram, elementId);
		Shape shape = Util.findShapeByBusinessObjectId(diagram, elementId);
		
		MorphEndEventFeature feature = (MorphEndEventFeature) morphEndEvent(shape, diagramTypeProvider).getFeature();
		
		List<MorphOption> options = feature.getOptions(endEvent);
		
		boolean isTransaction = endEvent.eContainer() instanceof Transaction;
		
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(endEvent);
		
		boolean isMultipleEvent = eventDefinitions.size() > 1;
		
		EClass eventDefinitionType = null;
		if (!isMultipleEvent && eventDefinitions.size() == 1) {
			eventDefinitionType = eventDefinitions.get(0).eClass();
		}
		
		List<MorphEventOption> expectedOptions = getExpectedList(endEvent.eClass(), eventDefinitionType, isMultipleEvent, isTransaction);
		
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
	
	private List<MorphEventOption> getExpectedList(EClass eventType, EClass eventDefinitionType, boolean multipleEvent, boolean isTransaction) {
		EClass endEventClass = Bpmn2Package.eINSTANCE.getEndEvent();
		
		EClass messageClass = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		EClass signalClass = Bpmn2Package.eINSTANCE.getSignalEventDefinition();
		EClass errorClass = Bpmn2Package.eINSTANCE.getErrorEventDefinition();
		EClass escalationClass = Bpmn2Package.eINSTANCE.getEscalationEventDefinition();
		EClass terminateClass = Bpmn2Package.eINSTANCE.getTerminateEventDefinition();
		EClass compensateClass = Bpmn2Package.eINSTANCE.getCompensateEventDefinition();
		
		List<MorphEventOption> result = new ArrayList<MorphEventOption>();
		
		if (eventDefinitionType != null || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", endEventClass);
			result.add(option);			
		}
		if (!messageClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", endEventClass, messageClass);
			result.add(option);
		}
		if (!signalClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", endEventClass, signalClass);
			result.add(option);
		}
		if (!errorClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", endEventClass, errorClass);
			result.add(option);
		}
		if (!escalationClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", endEventClass, escalationClass);
			result.add(option);
		}
		if (!terminateClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", endEventClass, terminateClass);
			result.add(option);
		}
		if (!compensateClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", endEventClass, compensateClass);
			result.add(option);
		}
		
		if (isTransaction) {
			EClass cancelClass = Bpmn2Package.eINSTANCE.getCancelEventDefinition();
			if (!cancelClass.equals(eventDefinitionType) || multipleEvent) {
				MorphEventOption option = new MorphEventOption("Test", endEventClass, cancelClass);
				result.add(option);
			}			
		}

		return result;
	}

}
