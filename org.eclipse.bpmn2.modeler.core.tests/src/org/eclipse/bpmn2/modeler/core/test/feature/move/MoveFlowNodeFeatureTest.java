package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Daniel Meyer
 * @author Nico Rehwaldt
 */
public class MoveFlowNodeFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testMoveShapeOutOfContainer() {

		// find shapes
		Shape userTaskShape = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		ContainerShape subProcessShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		ContainerShape processShape = (ContainerShape) diagram;
		BaseElement subProcessElement = BusinessObjectUtil.getFirstBaseElement(subProcessShape);

		// first, the usertask is contained in the subprocess
		assertThat(subProcessShape).hasChild(userTaskShape);
		assertThat(processShape).doesNotHaveChild(userTaskShape);
		assertThat(userTaskShape).hasParentModelElement(subProcessElement);

		// move the usertask out from under the subprocess into the process
		move(userTaskShape, diagramTypeProvider)
			.toContainer(processShape)
			.execute();

		// now the usertask is contained in the process
		assertThat(subProcessShape).doesNotHaveChild(userTaskShape);
		
		assertThat(processShape).hasChild(userTaskShape);
		assertThat(userTaskShape).isContainedIn(processShape);
	}
	
	@Test
	@DiagramResource
	public void testMoveShapeIntoContainer() {

		// find shapes
		Shape userTaskShape = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		ContainerShape subProcessShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		ContainerShape processShape = (ContainerShape) diagram;
		BaseElement subProcessElement = BusinessObjectUtil.getFirstBaseElement(subProcessShape);

		// first, the usertask is contained in the process
		assertThat(subProcessShape).doesNotHaveChild(userTaskShape);
		assertThat(processShape).hasChild(userTaskShape);
		assertThat(userTaskShape).hasParentModelElement(subProcessElement.eContainer());

		// move the usertask into the subprocess
		move(userTaskShape, diagramTypeProvider)
			.toContainer(subProcessShape)
			.execute();

		// now the usertask is contained in the subprocess
		assertThat(subProcessShape).hasChild(userTaskShape);
		assertThat(processShape).doesNotHaveChild(userTaskShape);
		assertThat(userTaskShape).hasParentModelElement(subProcessElement);
	}
	
	@Test
	@DiagramResource
	public void testMoveAnnotationIntoSubprocess() {

		// find shapes
		Shape textAnnotationShape = Util.findShapeByBusinessObjectId(diagram, "TextAnnotation_1");
		ContainerShape subProcessShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		ContainerShape processShape = (ContainerShape) diagram;
		BaseElement subProcessElement = BusinessObjectUtil.getFirstBaseElement(subProcessShape);

		// first, the annotation is contained in the process
		assertThat(subProcessShape).doesNotHaveChild(textAnnotationShape);
		assertThat(processShape).hasChild(textAnnotationShape);
		assertThat(textAnnotationShape).hasParentModelElement(subProcessElement.eContainer());

		// move the annotation into the subprocess
		move(textAnnotationShape, diagramTypeProvider)
			.toContainer(subProcessShape)
			.execute();

		// now the annotation is contained in the subprocess
		assertThat(subProcessShape).hasChild(textAnnotationShape);
		assertThat(processShape).doesNotHaveChild(textAnnotationShape);
		
		// annotation is still inside the process
		assertThat(textAnnotationShape).hasParentModelElement(subProcessElement.eContainer());
	}

	@Test
	@DiagramResource
	public void testMoveOutOfParticipantNotAllowed() {

		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "_Participant_3");

		// assume
		// taks shape is part of participant
		assertThat(taskShape)
			.isContainedIn(participantShape);
		
		// when 
		// trying to move the task shape
		move(taskShape, diagramTypeProvider)
			.toContainer(diagram)
			.by(300, 200)
			.execute();
		
		// then 
		// it should still be contained in participant
		// (movement not allowed)
		assertThat(participantShape)
			.hasChild(taskShape);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveEventToSiblingLane() {
		
		// given
		Shape eventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_5");

		Shape labelShape = LabelUtil.getLabelShape(eventShape, getDiagram());
		
		// when moving event to sibling lane
		move(eventShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.execute();
	
		// then
		// event label should have been moved, too
		assertThat(eventShape)
			.isContainedIn(targetLaneShape);
		
		assertThat(labelShape)
			.isContainedIn(diagram);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testTaskWithBoundaryEvent.bpmn")
	public void testMoveTaskWithBoundaryEvent() {

		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		Shape boundaryEventShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		Shape boundaryEventLabelShape = LabelUtil.getLabelShape(boundaryEventShape, diagram);
		
		Point movement = point(30, 24);

		IRectangle taskBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(taskShape);
		IRectangle boundaryBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(boundaryEventShape);
		
		IRectangle labelBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(boundaryEventLabelShape);
		
		// when moving event to the parents sibling lane
		move(taskShape, diagramTypeProvider)
			.by(movement)
			.execute();
		
		// then
		// task should have moved
		assertThat(taskShape)
			.movedBy(movement, taskBoundsBeforeMove);
		
		// boundary event should have moved
		assertThat(boundaryEventShape)
			.movedBy(movement, boundaryBoundsBeforeMove);
		
		// label should have been moved
		assertThat(boundaryEventLabelShape)
			.movedBy(movement, labelBoundsBeforeMove);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/ParticipantTaskConnectedBoundaryEvent.testBase.bpmn")
	public void testMoveTaskIncomingBoundaryEventFlow() {

		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Point movement = point(30, 24);

		IRectangle taskBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(taskShape);
		
		// when moving event to the parents sibling lane
		move(taskShape, diagramTypeProvider)
			.by(movement)
			.execute();
		
		// then
		// task should have moved
		assertThat(taskShape)
			.movedBy(movement, taskBoundsBeforeMove);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testTaskWithBoundaryEvent.bpmn")
	public void testMoveTaskWithBoundaryEvent2() {

		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		Shape boundaryEventShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		Shape boundaryEventLabelShape = LabelUtil.getLabelShape(boundaryEventShape, diagram);
		
		Point movement = point(-30, 40);

		IRectangle taskBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(taskShape);
		IRectangle boundaryBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(boundaryEventShape);
		
		IRectangle labelBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(boundaryEventLabelShape);
		
		// when moving event to the parents sibling lane
		move(taskShape, diagramTypeProvider)
			.by(movement)
			.execute();
		
		// then
		// task should have moved
		assertThat(taskShape)
			.movedBy(movement, taskBoundsBeforeMove);
		
		// boundary event should have moved
		assertThat(boundaryEventShape)
			.movedBy(movement, boundaryBoundsBeforeMove);
		
		// label should have been moved
		assertThat(boundaryEventLabelShape)
			.movedBy(movement, labelBoundsBeforeMove);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testTaskWithBoundaryEvent.bpmn")
	public void testMoveTaskWithBoundaryEventAfterLabelMove() {

		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		Shape boundaryEventShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		Shape boundaryEventLabelShape = LabelUtil.getLabelShape(boundaryEventShape, diagram);
		
		Point labelMovement = point(20, 30);
		Point taskMovement = point(-30, 40);
		Point expectedTotalLabelMovement = point(-10, 70);

		IRectangle taskBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(taskShape);
		IRectangle boundaryBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(boundaryEventShape);
		
		IRectangle labelBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(boundaryEventLabelShape);
		
		// when
		// moving label
		move(boundaryEventLabelShape, diagramTypeProvider)
			.by(labelMovement)
			.execute();
		
		// and moving task
		move(taskShape, diagramTypeProvider)
			.by(taskMovement)
			.execute();
		
		// then
		// task should have moved
		assertThat(taskShape)
			.movedBy(taskMovement, taskBoundsBeforeMove);
		
		// boundary event should have moved
		assertThat(boundaryEventShape)
			.movedBy(taskMovement, boundaryBoundsBeforeMove);
		
		// label should have been moved by label movement + task movement
		assertThat(boundaryEventLabelShape)
			.movedBy(expectedTotalLabelMovement, labelBoundsBeforeMove);
	}
	
	@Test
	@DiagramResource
	public void testMoveTaskWithBoundaryEventIntoSubprocess() {

		// given
		ContainerShape subProcessShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		Shape boundaryEventShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");

		IRectangle subProcessBounds = LayoutUtil.getAbsoluteBounds(subProcessShape);
		IRectangle taskBounds = LayoutUtil.getAbsoluteBounds(taskShape);
		
		Point containerOffset = point(30, 40);
		Point movement = point(subProcessBounds.getX() - taskBounds.getX() + containerOffset.getX(), subProcessBounds.getY() - taskBounds.getY() + containerOffset.getY());
		
		IRectangle taskBoundsBeforeMove = LayoutUtil.getAbsoluteBounds(taskShape);
		
		// when
		// moving event to the parents sibling lane
		move(taskShape, diagramTypeProvider)
			.toContainer(subProcessShape)
			.to(containerOffset)
			.execute();
		
		// then
		// task should have moved
		assertThat(taskShape)
			.movedBy(movement, taskBoundsBeforeMove);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveEventToParentsSiblingLane() {

		// given
		Shape eventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_3");
		
		Shape labelShape = LabelUtil.getLabelShape(eventShape, getDiagram());
		
		// when moving event to the parents sibling lane
		move(eventShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.execute();

		// then
		// event label should have been moved, too
		assertThat(eventShape)
			.isContainedIn(targetLaneShape);
		
		assertThat(labelShape)
			.isContainedIn(diagram);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveGatewayToSiblingLane() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_3");

		Shape labelShape = LabelUtil.getLabelShape(gatewayShape, getDiagram());
		
		// when gateway event to sibling lane
		move(gatewayShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.execute();
	
		// then 
		// gateway label should have been moved, too
		assertThat(gatewayShape)
			.isContainedIn(targetLaneShape);
		
		assertThat(labelShape)
			.isContainedIn(diagram);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveGatewayToParticipantWithLanesNotAllowed() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		ContainerShape preMoveLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_6");
		ContainerShape targetParticipantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_2");
		
		Shape labelShape = LabelUtil.getLabelShape(gatewayShape, getDiagram());
		
		// when gateway event to sibling lane
		move(gatewayShape, diagramTypeProvider)
			.toContainer(targetParticipantShape)
			.execute();
	
		// then 
		// gateway label should still be contained in original lane
		assertThat(gatewayShape)
			.isContainedIn(preMoveLaneShape);
		
		// same applies to label
		assertThat(labelShape)
			.isContainedIn(diagram);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveTaskWithBoundaryEventToSiblingLane() {
		
		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape boundaryEventShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_3");
		Shape labelShape = LabelUtil.getLabelShape(boundaryEventShape, getDiagram());

		Task task = BusinessObjectUtil.getFirstElementOfType(taskShape, Task.class);
		BoundaryEvent event = BusinessObjectUtil.getFirstElementOfType(boundaryEventShape, BoundaryEvent.class);
		Lane targetLane = BusinessObjectUtil.getFirstElementOfType(targetLaneShape, Lane.class);
		
		// when moving task to sibling lane
		move(taskShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.execute();

		// then
		// boundary event shapes label should have moved, too
		assertThat(boundaryEventShape)
			.isContainedIn(targetLaneShape);
		
		assertThat(labelShape)
			.isContainedIn(diagram);
		
		// and make sure the business object connections have changed accordingly
		assertThat(targetLane.getFlowNodeRefs())
			.contains(task, event);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testAdjustLabelTestBase.bpmn")
	public void testMoveGatewayMovesLabel() {
		
		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "InclusiveGateway_1");

		Shape labelShape = LabelUtil.getLabelShape(gatewayShape, diagram);
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// when moving task by 20,20
		move(gatewayShape, diagramTypeProvider)
			.by(20, 20)
			.execute();
		
		// then 
		// label should have been moved accordingly
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(point(20, 20), preMoveLabelBounds);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testAdjustLabelTestBase.bpmn")
	public void testMoveEventMovesLabel() {

		// given
		Shape eventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");

		Shape labelShape = LabelUtil.getLabelShape(eventShape, diagram);
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// when moving task by 20,20
		move(eventShape, diagramTypeProvider)
			.by(20, 20)
			.execute();
		
		// then 
		// label should have been moved accordingly
		assertThat(labelShape)
			.isContainedIn(diagram)
			.movedBy(point(20, 20), preMoveLabelBounds);
	}
}