package org.camunda.bpm.modeler.test.feature.morph;

import static org.camunda.bpm.modeler.test.util.operations.MorphBoundaryEventOperation.morphBoundaryEvent;
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
import org.camunda.bpm.modeler.ui.features.event.MorphBoundaryEventFeature;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.Transaction;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

public class MorphBoundaryEventFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testBase.bpmn")
	public void testMorph_BlancoBoundaryEventToMessageBoundaryEvent() {
		
		// given
		BoundaryEvent boundaryEvent = (BoundaryEvent) Util.findBusinessObjectById(diagram, "BoundaryEvent_1");
		Shape boundaryEventShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		
		// when
		morphBoundaryEvent(boundaryEventShape, diagramTypeProvider)
			.to(newEventDefinitionType)
			.execute();
		
		// then
		
		EventDefinition message = ModelUtil.getEventDefinition(boundaryEvent, MessageEventDefinition.class);
		
		assertThat(message)
			.isNotNull();
		
		// still have data input association
		DataOutputAssociation dataOutput = (DataOutputAssociation) Util.findBusinessObjectById(diagram, "DataOutputAssociation_1");
		
		assertThat(boundaryEvent.getDataOutputAssociation())
			.contains(dataOutput)
			.hasSize(1);

		// the containing associations have the right references
		Association association1 = (Association) Util.findBusinessObjectById(diagram, "Association_1");
		
		assertThat(association1.getSourceRef())
			.isEqualTo(boundaryEvent);
		
		// check the incoming and outgoing sequence flows
		SequenceFlow outgoing = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_1");
		
		assertThat(outgoing.getSourceRef())
			.isEqualTo(boundaryEvent);

		assertThat(boundaryEvent.getOutgoing())
			.contains(outgoing);

		// check the message flow
		MessageFlow outgoingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_1");
		
		assertThat(outgoingMsg)
			.isNotNull();

		assertThat(outgoingMsg.getTargetRef())
			.isEqualTo(boundaryEvent);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testBase.bpmn")
	public void testMorph_BlancoBoundaryEventToTimerBoundaryEvent_ShouldDeleteMessageFlow() {
		
		// given
		BoundaryEvent boundaryEvent = (BoundaryEvent) Util.findBusinessObjectById(diagram, "BoundaryEvent_1");
		Shape boundaryEventShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		
		EClass newEventDefinitionType = Bpmn2Package.eINSTANCE.getTimerEventDefinition();
		
		// when
		morphBoundaryEvent(boundaryEventShape, diagramTypeProvider)
			.to(newEventDefinitionType)
			.execute();
		
		// then
		
		EventDefinition timer = ModelUtil.getEventDefinition(boundaryEvent, TimerEventDefinition.class);
		
		assertThat(timer)
			.isNotNull();
	
		// check the message flows
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_1");
		
		assertThat(incomingMsg)
			.isNull();
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testOptions.bpmn")
	public void testAvailableOptions_BoundaryEvent() {
		shouldContainRightOptions("BoundaryEvent_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testOptions.bpmn")
	public void testAvailableOptions_MessageBoundaryEvent() {
		shouldContainRightOptions("BoundaryEvent_2");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testOptions.bpmn")
	public void testAvailableOptions_TimerBoundaryEvent() {
		shouldContainRightOptions("BoundaryEvent_3");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testOptions.bpmn")
	public void testAvailableOptions_ConditionalBoundaryEvent() {
		shouldContainRightOptions("BoundaryEvent_4");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testOptions.bpmn")
	public void testAvailableOptions_SignalBoundaryEvent() {
		shouldContainRightOptions("BoundaryEvent_5");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testOptions.bpmn")
	public void testAvailableOptions_EscalationBoundaryEvent() {
		shouldContainRightOptions("BoundaryEvent_6");
	}
		
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testOptions.bpmn")
	public void testAvailableOptions_ErrorBoundaryEvent() {
		shouldContainRightOptions("BoundaryEvent_7");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testOptions.bpmn")
	public void testAvailableOptions_CompensateBoundaryEvent() {
		shouldContainRightOptions("BoundaryEvent_8");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testOptions.bpmn")
	public void testAvailableOptions_BoundaryEventAttachedToTransaction() {
		shouldContainRightOptions("BoundaryEvent_9");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphBoundaryEventTest.testOptions.bpmn")
	public void testAvailableOptions_MultipleBoundaryEvent() {
		shouldContainRightOptions("BoundaryEvent_10");
	}
	
	private void shouldContainRightOptions(String elementId) {
		BoundaryEvent boundaryEvent = (BoundaryEvent) Util.findBusinessObjectById(diagram, elementId);
		Shape shape = Util.findShapeByBusinessObjectId(diagram, elementId);
		
		MorphBoundaryEventFeature feature = (MorphBoundaryEventFeature) morphBoundaryEvent(shape, diagramTypeProvider).getFeature();
		
		List<MorphOption> options = feature.getOptions(boundaryEvent);
		
		boolean isTransaction = boundaryEvent.getAttachedToRef() instanceof Transaction;
		
		List<EventDefinition> eventDefinitions = ModelUtil.getEventDefinitions(boundaryEvent);
		
		boolean isMultipleEvent = eventDefinitions.size() > 1;
		
		EClass eventDefinitionType = null;
		if (!isMultipleEvent && eventDefinitions.size() == 1) {
			eventDefinitionType = eventDefinitions.get(0).eClass();
		}
		
		List<MorphEventOption> expectedOptions = getExpectedList(boundaryEvent.eClass(), eventDefinitionType, isMultipleEvent, isTransaction);
		
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
		EClass boundaryEvent = Bpmn2Package.eINSTANCE.getBoundaryEvent();
		
		EClass messageClass = Bpmn2Package.eINSTANCE.getMessageEventDefinition();
		EClass timerClass = Bpmn2Package.eINSTANCE.getTimerEventDefinition();
		EClass conditionalClass = Bpmn2Package.eINSTANCE.getConditionalEventDefinition();
		EClass signalClass = Bpmn2Package.eINSTANCE.getSignalEventDefinition();
		EClass escalationClss = Bpmn2Package.eINSTANCE.getEscalationEventDefinition();
		EClass errorClss = Bpmn2Package.eINSTANCE.getErrorEventDefinition();		
		EClass compensateClass = Bpmn2Package.eINSTANCE.getCompensateEventDefinition();
		
		List<MorphEventOption> result = new ArrayList<MorphEventOption>();

		if (!messageClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", boundaryEvent, messageClass);
			result.add(option);
		}
		if (!timerClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", boundaryEvent, timerClass);
			result.add(option);
		}
		if (!conditionalClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", boundaryEvent, conditionalClass);
			result.add(option);
		}		
		if (!signalClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", boundaryEvent, signalClass);
			result.add(option);
		}
		if (!escalationClss.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", boundaryEvent, escalationClss);
			result.add(option);
		}
		if (!errorClss.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", boundaryEvent, errorClss);
			result.add(option);
		}
		if (!compensateClass.equals(eventDefinitionType) || multipleEvent) {
			MorphEventOption option = new MorphEventOption("Test", boundaryEvent, compensateClass);
			result.add(option);
		}
		
		if (isTransaction) {
			EClass cancelClass = Bpmn2Package.eINSTANCE.getCancelEventDefinition();
			
			if (!cancelClass.equals(eventDefinitionType) || multipleEvent) {
				MorphEventOption option = new MorphEventOption("Test", boundaryEvent, cancelClass);
				result.add(option);
			}
		}

		return result;
	}

}
