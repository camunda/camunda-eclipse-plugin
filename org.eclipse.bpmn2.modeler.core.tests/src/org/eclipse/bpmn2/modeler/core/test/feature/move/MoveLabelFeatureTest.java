package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class MoveLabelFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveLabelToOtherContainerNotAllowed() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");

		ContainerShape sourceLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_6");
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_3");
		Shape labelShape = GraphicsUtil.getLabelShape(gatewayShape, getDiagram());
		
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
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveLabel() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		Shape labelShape = GraphicsUtil.getLabelShape(gatewayShape, getDiagram());
		
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
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveLabelAndElement() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		Shape labelShape = GraphicsUtil.getLabelShape(gatewayShape, getDiagram());
		
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
	@DiagramResource
	public void testMoveSequenceFlowLabel() {

		// given
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		SequenceFlow sequenceFlow = (SequenceFlow)sequenceFlow2.getLink().getBusinessObjects().get(0);
		Shape labelShape = GraphicsUtil.getLabelShape(sequenceFlow, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		BPMNEdge flowEdge = DIUtils.getEdge(labelShape);
		assertThat(flowEdge.getLabel()).isNull();
		
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
		
		assertThat(flowEdge.getLabel()).isNotNull();
		assertThat(flowEdge.getLabel().getBounds()).isNotNull();
		assertThat(flowEdge.getLabel().getBounds().getX()).isEqualTo(preMoveLabelBounds.getX()+10);
		assertThat(flowEdge.getLabel().getBounds().getY()).isEqualTo(preMoveLabelBounds.getY()+20);
		
		// move again
		move(labelShape, diagramTypeProvider)
			.by(10, 20)
			.execute();
		
		assertThat(flowEdge.getLabel()).isNotNull();
		assertThat(flowEdge.getLabel().getBounds()).isNotNull();
		assertThat(flowEdge.getLabel().getBounds().getX()).isEqualTo(preMoveLabelBounds.getX()+20);
		assertThat(flowEdge.getLabel().getBounds().getY()).isEqualTo(preMoveLabelBounds.getY()+40);
	}
	
	
	private static Point getShapesPosDiff(ContainerShape shape1, ContainerShape shape2) {
		IRectangle b1 = LayoutUtil.getAbsoluteBounds(shape1);
		IRectangle b2 = LayoutUtil.getAbsoluteBounds(shape2);
		
		return point(b2.getX() - b1.getX(), b2.getY() - b1.getY());
	}
}
