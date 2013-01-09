package org.eclipse.bpmn2.modeler.core.test.layout;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.test.util.operations.MoveParticipantOperation;
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
		
		MoveParticipantOperation.move(participant2Shape, getDiagramTypeProvider())
			.by(20, 10)
			.execute();
		
		assertThat(messageFlow).anchorPointOn(participant2Shape).isAboveShape();
	}
}
