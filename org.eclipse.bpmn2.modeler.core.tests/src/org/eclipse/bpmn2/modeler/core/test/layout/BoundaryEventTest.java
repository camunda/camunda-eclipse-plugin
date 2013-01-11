package org.eclipse.bpmn2.modeler.core.test.layout;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveElementOperation.move;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.ReconnectConnectionEndOperation.reconnectEnd;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeLayoutService;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class BoundaryEventTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingBottomLeft() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_6");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingLeft() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_7");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingTopRight() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_4");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow5).hasBendpointCount(2);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingBottom() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_5");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow5).hasBendpointCount(2);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingRight() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_8");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingTopLeft() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_3");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
	}
	
	@Test
	@DiagramResource
	public void testMoveTaskWithBoundaryEvent() {
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_2");
		
		FreeFormConnection sequenceFlow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		assertThat(sequenceFlow).hasBendpointCount(0);
		
		move(taskShape, diagramTypeProvider)
			.by(0, 50)
			.toContainer(diagram)
			.execute();
		
		assertThat(sequenceFlow).hasNoDiagonalEdges();
		assertThat(sequenceFlow).hasBendpointCount(1);

		ILocation sequenceFlowStartLoc = LayoutUtil.getVisibleAnchorLocation(sequenceFlow.getStart(), sequenceFlow);
		assertThat(sequenceFlowStartLoc.getX()).isEqualTo(180);
		assertThat(sequenceFlowStartLoc.getY()).isEqualTo(118);

		ILocation sequenceFlowEndLoc = LayoutUtil.getVisibleAnchorLocation(sequenceFlow.getEnd(), sequenceFlow);
		assertThat(sequenceFlowEndLoc.getX()).isEqualTo(315);
		assertThat(sequenceFlowEndLoc.getY()).isEqualTo(150);
		
		Point seq3FirstPoint = sequenceFlow.getBendpoints().get(0);
		assertThat(seq3FirstPoint.getX()).isEqualTo(180);
		assertThat(seq3FirstPoint.getY()).isEqualTo(150);

		move(taskShape, diagramTypeProvider)
			.by(0, -75)
			.toContainer(diagram)
			.execute();

		assertThat(sequenceFlow).hasNoDiagonalEdges();
		assertThat(sequenceFlow).hasBendpointCount(1);

		sequenceFlowStartLoc = LayoutUtil.getVisibleAnchorLocation(sequenceFlow.getStart(), sequenceFlow);
		assertThat(sequenceFlowStartLoc.getX()).isEqualTo(180);
		assertThat(sequenceFlowStartLoc.getY()).isEqualTo(118);

		sequenceFlowEndLoc = LayoutUtil.getVisibleAnchorLocation(sequenceFlow.getEnd(), sequenceFlow);
		assertThat(sequenceFlowEndLoc.getX()).isEqualTo(315);
		assertThat(sequenceFlowEndLoc.getY()).isEqualTo(75);
		
		seq3FirstPoint = sequenceFlow.getBendpoints().get(0);
		assertThat(seq3FirstPoint.getX()).isEqualTo(180);
		assertThat(seq3FirstPoint.getY()).isEqualTo(75);
		
		
		ContainerShape userTaskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		assertThat(sequenceFlow2.getBendpoints().size()).isEqualTo(2);
		
		move(userTaskShape, diagramTypeProvider)
			.by(0, -5)
			.toContainer(diagram)
			.execute();
		
		// dont relayout connections from boundary events with more than one bendpoint
		assertThat(sequenceFlow2.getBendpoints().size()).isEqualTo(2);
	}
	
	private void reconnectConnectionAssertBoundarySector(FreeFormConnection connection, Shape boundaryEvent, Shape reconnectionTarget, Sector expectedSector) {

		reconnectEnd(connection, diagramTypeProvider)
			.toElement(reconnectionTarget)
			.execute();
		
		assertThat(connection).anchorPointOn(boundaryEvent).isAt(expectedSector);
	}
	
}
