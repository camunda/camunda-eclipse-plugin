package org.eclipse.bpmn2.modeler.core.test.feature.add;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddPoolOperation.addPool;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.junit.Test;

public class AddPoolFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource
	public void testAddPoolToProcessWithBoundaryEvent() throws Exception {
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		// assume task is contained in diagram
		assertThat(taskShape)
			.isContainedIn(diagram);
		
		// when
		// adding pool to diagram
		addPool(diagramTypeProvider)
			.toContainer(diagram)
			.execute();
		
		ContainerShape poolShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_2");
		
		// then
		// pool should contain task
		// (AND no exception is thrown)
		assertThat(poolShape)
			.isNotNull()
			.hasChild(taskShape);
	}
}
