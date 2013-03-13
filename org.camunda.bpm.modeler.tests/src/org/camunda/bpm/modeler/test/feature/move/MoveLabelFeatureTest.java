package org.camunda.bpm.modeler.test.feature.move;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.MoveShapeOperation.move;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class MoveLabelFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveLabelRemainsOnDiagram() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");

		ContainerShape sourceLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_6");
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_3");
		Shape labelShape = LabelUtil.getLabelShape(gatewayShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		Point lanesPosDiff = getShapesPosDiff(sourceLaneShape, targetLaneShape);
		
		// assume
		// label is on diagram
		assertThat(labelShape).isContainedIn(diagram);
		
		// when
		// moving label to target container
		move(labelShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.by(0, 0)
			.execute();
		
		// then 
		// label should have been moved
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(lanesPosDiff, preMoveLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveLabel() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		Shape labelShape = LabelUtil.getLabelShape(gatewayShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// assume
		// label is on diagram
		assertThat(labelShape).isContainedIn(diagram);

		// when moving label in container
		move(labelShape, diagramTypeProvider)
			.by(10, 20)
			.execute();
		
		// then
		// label should not have changed container
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(point(10, 20), preMoveLabelBounds);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveLabelAndElement() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		Shape labelShape = LabelUtil.getLabelShape(gatewayShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// assume
		// label is on diagram
		assertThat(labelShape).isContainedIn(diagram);
		
		// when moving label in container
		move(labelShape, diagramTypeProvider)
			.by(10, 20)
			.execute();
		
		// then
		// label should not have changed container
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(point(10, 20), preMoveLabelBounds);
	}
	
	private static Point getShapesPosDiff(ContainerShape shape1, ContainerShape shape2) {
		IRectangle b1 = LayoutUtil.getAbsoluteBounds(shape1);
		IRectangle b2 = LayoutUtil.getAbsoluteBounds(shape2);
		
		return point(b2.getX() - b1.getX(), b2.getY() - b1.getY());
	}
}
