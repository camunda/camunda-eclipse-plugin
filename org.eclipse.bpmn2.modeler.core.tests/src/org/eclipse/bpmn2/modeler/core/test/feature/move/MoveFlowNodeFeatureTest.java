package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveElementOperation.move;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
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
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveEventToSiblingLane() {
		
		// given
		Shape eventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_5");
		
		// when moving event to sibling lane
		move(eventShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.execute();
	
		// then
		// event label should have been moved, too
		assertThat(eventShape)
			.isContainedIn(targetLaneShape);
		
		Shape labelShape = GraphicsUtil.getLabel(eventShape, getDiagram());
		
		assertThat(labelShape).isContainedIn(targetLaneShape);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveEventToParentsSiblingLane() {

		// given
		Shape eventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_3");

		// when moving event to the parents sibling lane
		move(eventShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.execute();

		// then
		// event label should have been moved, too
		assertThat(eventShape)
			.isContainedIn(targetLaneShape);
		
		Shape labelShape = GraphicsUtil.getLabel(eventShape, getDiagram());
		
		assertThat(labelShape).isContainedIn(targetLaneShape);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveGatewayToSiblingLane() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_3");

		// when gateway event to sibling lane
		move(gatewayShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.execute();
	
		// then 
		// gateway label should have been moved, too
		assertThat(gatewayShape)
			.isContainedIn(targetLaneShape);
		
		
		Shape labelShape = GraphicsUtil.getLabel(gatewayShape, getDiagram());
		
		assertThat(labelShape).isContainedIn(targetLaneShape);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveGatewayToParticipantWithLanesNotAllowed() {

		// given
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		ContainerShape preMoveLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_6");
		ContainerShape targetParticipantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_2");
		
		Shape labelShape = GraphicsUtil.getLabel(gatewayShape, getDiagram());
		
		// when gateway event to sibling lane
		move(gatewayShape, diagramTypeProvider)
			.toContainer(targetParticipantShape)
			.execute();
	
		// then 
		// gateway label should still be contained in original lane
		assertThat(gatewayShape)
			.isContainedIn(preMoveLaneShape);
		
		// same applies to label
		assertThat(labelShape).isContainedIn(preMoveLaneShape);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveFlowNodeFeature.testMoveBetweenLanes.bpmn")
	public void testMoveTaskWithBoundaryEventAttachedToSiblingLane() {
		
		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape boundaryEventShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		
		ContainerShape targetLaneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_3");
		
		// when moving task to sibling lane
		move(taskShape, diagramTypeProvider)
			.toContainer(targetLaneShape)
			.execute();

		// then
		// boundary event shapes label should have moved, too
		assertThat(boundaryEventShape)
			.isContainedIn(targetLaneShape);
		
		Shape labelShape = GraphicsUtil.getLabel(boundaryEventShape, getDiagram());
		
		assertThat(labelShape).isContainedIn(targetLaneShape);
	}
}