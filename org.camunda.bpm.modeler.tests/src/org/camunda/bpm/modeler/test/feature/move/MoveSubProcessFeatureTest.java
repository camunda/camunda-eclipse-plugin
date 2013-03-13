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

public class MoveSubProcessFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveLabelToOtherContainerWorks() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");

		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_3");
		Shape labelShape = LabelUtil.getLabelShape(gatewayShape, getDiagram());
		
		// assume
		// label is on diagram
		assertThat(labelShape).isContainedIn(diagram);
		
		// when
		// moving label to target container
		move(labelShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.by(30, 30)
			.execute();
		
		// then 
		// label should have been moved
		assertThat(labelShape)
			.isContainedIn(diagram);
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

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveSubProcessFeatureTest.testBase.bpmn")
	public void testMoveSubProcessMovesContainedEventLabel() {

		// given
		Shape elementShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape elementLabelShape = LabelUtil.getLabelShape(elementShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(elementLabelShape);
		
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		
		Point subProcessMovement = point(10, 20);
		
		// assume
		// label is on diagram
		assertThat(elementLabelShape).isContainedIn(diagram);
		
		// when moving label in container
		move(subProcessShape, diagramTypeProvider)
			.by(subProcessMovement)
			.execute();
		
		// then
		// label should not have changed container
		assertThat(elementLabelShape)
			.isContainedIn(diagram)
			.movedBy(subProcessMovement, preMoveLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveSubProcessFeatureTest.testBase.bpmn")
	public void testMoveSubProcessMovesContainedDataObjectLabel() {

		// given
		Shape elementShape = Util.findShapeByBusinessObjectId(diagram, "DataObject_1");
		Shape elementLabelShape = LabelUtil.getLabelShape(elementShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(elementLabelShape);
		
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_2");
		
		Point subProcessMovement = point(10, 20);
		
		// assume
		// label is on diagram
		assertThat(elementLabelShape).isContainedIn(diagram);
		
		// when moving label in container
		move(subProcessShape, diagramTypeProvider)
			.by(subProcessMovement)
			.execute();
		
		// then
		// label should not have changed container
		assertThat(elementLabelShape)
			.isContainedIn(diagram)
			.movedBy(subProcessMovement, preMoveLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveSubProcessFeatureTest.testBase.bpmn")
	public void testMoveSubProcessMovesContainedDataStoreReferenceLabel() {

		// given
		Shape elementShape = Util.findShapeByBusinessObjectId(diagram, "_DataStoreReference_3");
		Shape elementLabelShape = LabelUtil.getLabelShape(elementShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(elementLabelShape);
		
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_2");
		
		Point subProcessMovement = point(10, 20);
		
		// assume
		// label is on diagram
		assertThat(elementLabelShape).isContainedIn(diagram);
		
		// when moving label in container
		move(subProcessShape, diagramTypeProvider)
			.by(subProcessMovement)
			.execute();
		
		// then
		// label should not have changed container
		assertThat(elementLabelShape)
			.isContainedIn(diagram)
			.movedBy(subProcessMovement, preMoveLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/MoveSubProcessFeatureTest.testBase.bpmn")
	public void testMoveSubProcessMovesContainedBoundaryEventLabel() {

		// given
		Shape elementShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		Shape elementLabelShape = LabelUtil.getLabelShape(elementShape, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(elementLabelShape);
		
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_3");
		
		Point subProcessMovement = point(10, 20);
		
		// assume
		// label is on diagram
		assertThat(elementLabelShape).isContainedIn(diagram);
		
		// when moving label in container
		move(subProcessShape, diagramTypeProvider)
			.by(subProcessMovement)
			.execute();
		
		// then
		// label should not have changed container
		assertThat(elementLabelShape)
			.isContainedIn(diagram)
			.movedBy(subProcessMovement, preMoveLabelBounds);
	}
}
