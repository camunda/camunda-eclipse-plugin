package org.camunda.bpm.modeler.test.layout;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.MoveShapeOperation.move;

import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author nico.rehwaldt
 */
public class MessageFlowTest extends AbstractFeatureTest {

	@Test
	@DiagramResource
	public void testPoolMessageFlowAnchorFixAfterMove() {
		FreeFormConnection messageFlow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "MessageFlow_1");
		
		Shape participant2Shape = Util.findShapeByBusinessObjectId(diagram, "Participant_2");
		
		move(participant2Shape, getDiagramTypeProvider())
			.by(20, 10)
			.execute();
		
		assertThat(messageFlow).anchorPointOn(participant2Shape).isAboveShape();
	}
}
