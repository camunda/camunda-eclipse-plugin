package org.eclipse.bpmn2.modeler.core.test.feature.create;


import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.test.util.operations.AddEventOperation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class CreateStartEventFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testCreateOnLane() {
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		assertThat(laneShape).isNotNull();
		
		new AddEventOperation<StartEvent>(getDiagramTypeProvider())
			.atLocation(10, 10)
			.inContainer((ContainerShape) laneShape)
			.execute();
	}
}
