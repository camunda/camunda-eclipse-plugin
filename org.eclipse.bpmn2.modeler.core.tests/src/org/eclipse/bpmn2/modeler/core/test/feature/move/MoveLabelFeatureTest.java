package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class MoveLabelFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveLabelToOtherContainerNotAllowed() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		ContainerShape preMoveLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_6");
		
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_3");
		
		Shape labelShape = GraphicsUtil.getLabelShape(gatewayShape, getDiagram());
		
		// when moving label to target container
		move(labelShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.execute();
		
		// then 
		// label should not have been moved
		assertThat(labelShape)
			.isContainedIn(preMoveLaneShape);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveLabel() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		ContainerShape preMoveLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_6");
		
		Shape labelShape = GraphicsUtil.getLabelShape(gatewayShape, getDiagram());
		
		// when moving label to target container
		move(labelShape, diagramTypeProvider)
			.by(10, 20)
			.execute();
		
		// then 
		// label should not have been moved
		assertThat(labelShape)
			.isContainedIn(preMoveLaneShape);
	}
}
