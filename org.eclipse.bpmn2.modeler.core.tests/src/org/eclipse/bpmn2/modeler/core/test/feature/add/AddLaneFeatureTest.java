package org.eclipse.bpmn2.modeler.core.test.feature.add;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddLaneOperation.addLane;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.junit.Test;

public class AddLaneFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testAddLaneOnDiagramNotAllowed() throws Exception {

		// given empty diagram
		
		// when
		// lane is added to it
		addLane(diagramTypeProvider)
			.toContainer(diagram)
			.execute();
		
		// then
		// operation should be rejected
		assertThat(diagram)
			.hasNoChildren();
	}
}
