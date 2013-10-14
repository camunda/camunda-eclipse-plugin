package org.camunda.bpm.modeler.test.feature.add;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.AddExclusiveGatewayOperation.addExclusiveGateway;

import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.di.BPMNShape;
import org.junit.Test;

public class AddGatewayFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testLocation.bpmn")
	public void testAddToDiagramOutsideTheDiagram() throws Exception {

		// when
		// element is added to it
		addExclusiveGateway(diagramTypeProvider)
			.atLocation(133, 1299)
			.toContainer(diagram)
			.execute();
		
		// get the added task
		BPMNShape addedGateway = (BPMNShape) Util.findBpmnShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		// then
		assertThat(addedGateway)
			.bounds()
			.x()
				.isEqualTo(133 - 25);

		assertThat(addedGateway)
			.bounds()
			.y()
				.isEqualTo(1299 - 25);
	}	

}
