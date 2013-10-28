package org.camunda.bpm.modeler.test.feature.morph;

import static org.camunda.bpm.modeler.test.util.operations.MorphActivityOperation.morphActivity;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class MorphGatewayFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphFlowNode.testBase.bpmn")
	public void testMorphExclusiveGatewayToParallelGateway() {
		
		// given
		Shape exclusiveGatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		EClass newType = Bpmn2Package.eINSTANCE.getParallelGateway();
		
		// when
		morphActivity(exclusiveGatewayShape, diagramTypeProvider)
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

}
