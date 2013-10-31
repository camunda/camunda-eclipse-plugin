package org.camunda.bpm.modeler.test.feature.morph;

import static org.camunda.bpm.modeler.test.util.operations.MorphGatewayOperation.morphGateway;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature.MorphOption;
import org.camunda.bpm.modeler.ui.features.activity.MorphActivityFeature;
import org.camunda.bpm.modeler.ui.features.gateway.MorphGatewayFeature;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class MorphGatewayFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphFlowNode.testBase.bpmn")
	public void testMorphExclusiveGatewayToParallelGateway() {
		
		// given
		Shape exclusiveGatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		EClass newType = Bpmn2Package.eINSTANCE.getParallelGateway();
		
		// when
		morphGateway(exclusiveGatewayShape, diagramTypeProvider)
			.to(newType)
			.execute();
		
		// then
		EObject bo = Util.findBusinessObjectById(diagram, "ExclusiveGateway_1");
		
		// check whether it is a service task
		assertThat(bo.eClass())
			.isEqualTo(newType);
		
		ParallelGateway parallelGateway = (ParallelGateway) bo;
		
		assertThat(parallelGateway.getName())
			.isEqualTo("Exclusive Gateway");
	
		assertThat(parallelGateway.getId())
			.isEqualTo("ExclusiveGateway_1");
		
		// check the incoming and outgoing sequence flows
		SequenceFlow incoming = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_2");
		SequenceFlow outgoing1 = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_3");
		SequenceFlow outgoing2 = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_4");
		
		assertThat(incoming.getTargetRef())
			.isEqualTo(parallelGateway);
		
		assertThat(parallelGateway.getIncoming())
			.contains(incoming);
		
		assertThat(outgoing1.getSourceRef())
			.isEqualTo(parallelGateway);

		assertThat(outgoing2.getSourceRef())
		.isEqualTo(parallelGateway);
		
		assertThat(parallelGateway.getOutgoing())
			.contains(outgoing1, outgoing2);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphGatewayTest.testOptions.bpmn")
	public void testAvailableOptions_InclusiveGateway() {
		shouldContainRightOptions("InclusiveGateway_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphGatewayTest.testOptions.bpmn")
	public void testAvailableOptions_ExclusiveGateway() {
		shouldContainRightOptions("ExclusiveGateway_1");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphGatewayTest.testOptions.bpmn")
	public void testAvailableOptions_ParallelGateway() {
		shouldContainRightOptions("ParallelGateway_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphGatewayTest.testOptions.bpmn")
	public void testAvailableOptions_EventBasedGateway() {
		shouldContainRightOptions("EventBasedGateway_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphGatewayTest.testOptions.bpmn")
	public void testAvailableOptions_ComplexGateway() {
		shouldContainRightOptions("ComplexGateway_1");
	}
	
	private void shouldContainRightOptions(String elementId) {
		Gateway gateway = (Gateway) Util.findBusinessObjectById(diagram, elementId);
		Shape shape = Util.findShapeByBusinessObjectId(diagram, elementId);
		
		MorphGatewayFeature feature = morphGateway(shape, diagramTypeProvider).getFeature();
		
		List<MorphOption> options = feature.getOptions(gateway);
		List<EClass> expectedTypes = getExpectedList(gateway.eClass());
		
		assertThat(options.size())
			.isEqualTo(expectedTypes.size());
		
		for (MorphOption morphOption : options) {
			if (!expectedTypes.contains(morphOption.getNewType())) {
				Assertions.fail("The list of available options contains a unexpected type: " + morphOption.getNewType().getName());
			}
		}
	}
	
	private List<EClass> getExpectedList(EClass cls) {
		EClass inclusiveGtwClass = Bpmn2Package.eINSTANCE.getInclusiveGateway();
		EClass exclusiveGtwClass = Bpmn2Package.eINSTANCE.getExclusiveGateway();
		EClass parallelGtwClass = Bpmn2Package.eINSTANCE.getParallelGateway();
		EClass eventBaseGtwClass = Bpmn2Package.eINSTANCE.getEventBasedGateway();
		EClass complexGtwClass = Bpmn2Package.eINSTANCE.getComplexGateway();
		
		List<EClass> result = new ArrayList<EClass>();
		
		if (!inclusiveGtwClass.equals(cls)) {
			result.add(inclusiveGtwClass);
		}
		if (!exclusiveGtwClass.equals(cls)) {
			result.add(exclusiveGtwClass);
		}
		if (!parallelGtwClass.equals(cls)) {
			result.add(parallelGtwClass);
		}
		if (!eventBaseGtwClass.equals(cls)) {
			result.add(eventBaseGtwClass);
		}
		if (!complexGtwClass.equals(cls)) {
			result.add(complexGtwClass);
		}

		return result;
	}
	
}
