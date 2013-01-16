package org.eclipse.bpmn2.modeler.core.test.layout;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.junit.Test;

/**
 * Tests the basic layouting behavior
 * 
 * @author nico.rehwaldt
 */
public class BasicTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testLayoutManuallyAddedBendpoints() {

		FreeFormConnection flow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "SendTask_1");
		
		List<Point> bendpoints = flow.getBendpoints();
		
		move(taskShape, diagramTypeProvider)
			.by(-130, 0)
			.execute();
		
		Point lastBendpoint = bendpoints.get(bendpoints.size() - 1);
		IRectangle taskBounds = LayoutUtil.getAbsoluteBounds(taskShape);
		
		assertThat(taskBounds).doesNotContain(lastBendpoint);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testLayoutOverlappingElements() {
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_6");
		
		// when moving shape over connected shape
		move(taskShape, getDiagramTypeProvider())
			.by(130, -91)
			.execute();
		
		// make sure we can do this (no exception thrown)
		
		IRectangle rect = LayoutUtil.getAbsoluteBounds(taskShape);
		
		// then movement was executed
		assertThat(point(rect)).isEqualTo(point(380, 459));
	}

	/**
	 * Vertical movement of a shape next to another shape should not trigger
	 * relayout, otherwise complex custom layouting is broken. 
	 * 
	 * Nice behavior here stands in conflict to {@link GatewayTest#testVerticalMoveBetween()}.
	 * 
	 * @see GatewayTest#testVerticalMoveBetween()
	 */
	@Test
	@DiagramResource
	public void testNoRepairOnSameAxis() {
		Shape task1Shape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape task2Shape = Util.findShapeByBusinessObjectId(diagram, "Task_2");
		
		FreeFormConnection flow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		// when
		// moving task1 on same line with task2
		move(task1Shape, diagramTypeProvider)
			.by(0, -60)
			.execute();
		
		// bend points should still connect shapes in a 180 degree rotated U
		
		assertThat(flow)
			.hasNoDiagonalEdges()
			.hasBendpointCount(2);
		
		assertThat(flow)
			.anchorPointOn(task1Shape)
				.isAboveShape();
			
		assertThat(flow)
			.anchorPointOn(task2Shape)
				.isAboveShape();
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testLayoutOverlappingAnchorPoints() {
			
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_6");
		
		// when moving both anchor points above each other
		move(taskShape, getDiagramTypeProvider())
			.by(110, -130)
			.execute();
		
		// make sure we can do this (no exception thrown)
		
		IRectangle rect = LayoutUtil.getAbsoluteBounds(taskShape);
		
		// then movement was executed
		assertThat(point(rect)).isEqualTo(point(360, 420));
	}
	
	@Test
	@DiagramResource
	public void testLayoutAfterIntermediateEventMove() {

		FreeFormConnection flow3 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		FreeFormConnection flow8 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_8");
		
		Shape eventShape = Util.findShapeByBusinessObjectId(diagram, "IntermediateCatchEvent_1");
		
		// when
		// moving intermediate event
		move(eventShape, diagramTypeProvider)
			.by(-5, -30)
			.execute();
		
		// then connection should be layouted
		assertThat(flow3).hasNoDiagonalEdges();
		assertThat(flow8).hasNoDiagonalEdges();
	}

	@Test
	@DiagramResource
	public void testMoveTask2Vertical() {
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "ReceiveTask_1");
		ContainerShape laneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_2");
		
		// Move target shape
		move(taskShape, diagramTypeProvider)
			.by(0 , 176)
			.toContainer(laneShape)
			.execute();
		
		// The MoveFlowNodeFeature will call AnchorUtil.reConnect, which will in turn recalculate the
		// boundary anchors to update them, we need to hook in there
		
		// see also DefaultMoveBendPointFeature to see how a bend point is created 
		
		// Get bendpoints incoming sequence flow
		FreeFormConnection seqConnection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_7");
		
		// and check
		assertEquals(1, seqConnection.getBendpoints().size());
		assertEquals(Graphiti.getPeLayoutService().getLocationRelativeToDiagram(seqConnection.getEnd()).getY(), seqConnection.getBendpoints().get(0).getY());

		// Get bendpoints outgoing sequence flow
		seqConnection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_8");
		
		// and check
		assertEquals(2, seqConnection.getBendpoints().size());		
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BasicTest.testMoveGatewayVertical.bpmn")
	public void testMoveGatewayUp80Vertical() {
		assertMoveUpByWorks(-80);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BasicTest.testMoveGatewayVertical.bpmn")
	public void testMoveGatewayUp60Vertical() {
		assertMoveUpByWorks(-60);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BasicTest.testMoveGatewayVertical.bpmn")
	public void testMoveGatewayUp62Vertical() {
		assertMoveUpByWorks(-62);
	}
	
	private void assertMoveUpByWorks(int moveBy) {
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		ContainerShape laneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_1");

		move(gatewayShape, diagramTypeProvider)
			.by(0 , moveBy)
			.toContainer(laneShape)
			.execute();
		
		FreeFormConnection seq2Connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		FreeFormConnection seq3Connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		FreeFormConnection seq7Connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_7");
		
		// check incoming sequence flow
		assertThat(seq2Connection.getBendpoints()).hasSize(2);
		
		//check outgoing sequence flow right
		assertThat(seq3Connection.getBendpoints()).hasSize(2);
		
		//check outgoing sequence flow bottom
		assertThat(seq7Connection.getBendpoints()).hasSize(1);
		
		assertThat(seq2Connection).hasNoDiagonalEdges();
		
		// start anchor must be centered on the right side

		assertThat(seq2Connection).anchor(seq2Connection.getStart()).isRightOfShape();
		
		// end anchor must be centered on the left side
		assertThat(seq2Connection).anchor(seq2Connection.getEnd()).isLeftOfShape();
		
		// start anchor must be centered on the right side

		assertThat(seq3Connection).anchor(seq3Connection.getStart()).isRightOfShape();
		
		// end anchor must be centered on the left side
		assertThat(seq3Connection).anchor(seq3Connection.getEnd()).isLeftOfShape();
		
		// start anchor must be centered on the right side

		assertThat(seq7Connection).anchor(seq7Connection.getStart()).isBeneathShape();
		
		// end anchor must be centered on the left side
		assertThat(seq7Connection).anchor(seq7Connection.getEnd()).isLeftOfShape();
		
		// test bendbpoint strategy treshold
		move(gatewayShape, diagramTypeProvider)
			.by(0 , -1)
			.toContainer(laneShape)
			.execute();
		
		// check outgoing sequence flow right
		assertEquals(2, seq3Connection.getBendpoints().size());
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BasicTest.testBaseVertical.bpmn")
	public void testRemoveBendpointsStraightVerticalLine() {

		FreeFormConnection sequenceFlow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");

		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");

		// assume
		// sequence flow is straight line
		assertThat(sequenceFlow).hasBendpointCount(0);
		
		// do and ...
		move(taskShape, diagramTypeProvider)
			.by(50, 0)
			.execute();
		
		// undo movement on x-axis
		move(taskShape, diagramTypeProvider)
			.by(-50, 0)
			.execute();
		
		// bendpoints should be removed when shape is moved back 
		// so that connection forms straight line
		assertThat(sequenceFlow)
			.hasNoDiagonalEdges()
			.hasBendpointCount(0);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BasicTest.testMoveEventVertical.bpmn")
	public void testRemoveBendpointsStraightHorizontalLine() {

		FreeFormConnection sequenceFlow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");

		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");

		// assume
		// sequence flow is straight line
		assertThat(sequenceFlow).hasBendpointCount(0);
		
		// do and ...
		move(taskShape, diagramTypeProvider)
			.by(0, 50)
			.execute();
		
		// undo movement on y-axis (vertical)
		move(taskShape, diagramTypeProvider)
			.by(0, -50)
			.execute();

		// bendpoints should be removed when shape is moved back 
		// so that connection forms straight line
		assertThat(sequenceFlow)
			.hasNoDiagonalEdges()
			.hasBendpointCount(0);
	}

	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BasicTest.testMoveEventVertical.bpmn")
	public void testReconnectStraightLine() {

		FreeFormConnection sequenceFlow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");

		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");

		// assume
		// sequence flow is straight line
		assertThat(sequenceFlow).hasBendpointCount(0);

		// when moving horizontally
		move(taskShape, diagramTypeProvider)
			.by(20, 0)
			.execute();
		
		// bendpoints should be removed when shape is moved back to straight line
		assertThat(sequenceFlow)
			.hasNoDiagonalEdges()
			.hasBendpointCount(0);
	}
	
	@Test
	@DiagramResource
	public void testMoveTaskVertical() {
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Participant_1");

		FreeFormConnection seq1Connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		FreeFormConnection seq2Connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");

		assertThat(seq1Connection.getBendpoints()).isEmpty();
		assertThat(seq2Connection.getBendpoints()).isEmpty();

		move(taskShape, diagramTypeProvider)
			.by(0, 180)
			.toContainer(participantShape)
			.execute();

		ILocation seq1StartLoc = LayoutUtil.getVisibleAnchorLocation(seq1Connection.getStart(), seq1Connection);
		assertThat(seq1StartLoc.getX()).isEqualTo(216);
		assertThat(seq1StartLoc.getY()).isEqualTo(165);

		ILocation seq1EndLoc = LayoutUtil.getVisibleAnchorLocation(seq1Connection.getEnd(), seq1Connection);
		assertThat(seq1EndLoc.getX()).isEqualTo(320);
		assertThat(seq1EndLoc.getY()).isEqualTo(345);

		assertThat(seq1Connection.getBendpoints().size()).isEqualTo(2);

		Point seq1FirstPoint = seq1Connection.getBendpoints().get(0);
		assertThat(seq1FirstPoint.getX()).isEqualTo(268);
		assertThat(seq1FirstPoint.getY()).isEqualTo(165);

		Point seq1SecondPoint = seq1Connection.getBendpoints().get(1);
		assertThat(seq1SecondPoint.getX()).isEqualTo(268);
		assertThat(seq1SecondPoint.getY()).isEqualTo(345);

		ILocation seq2StartLoc = LayoutUtil.getVisibleAnchorLocation(seq2Connection.getStart(), seq2Connection);
		assertThat(seq2StartLoc.getX()).isEqualTo(430);
		assertThat(seq2StartLoc.getY()).isEqualTo(345);

		ILocation seq2EndLoc = LayoutUtil.getVisibleAnchorLocation(seq2Connection.getEnd(), seq2Connection);
		assertThat(seq2EndLoc.getX()).isEqualTo(534);
		assertThat(seq2EndLoc.getY()).isEqualTo(165);

		assertThat(seq2Connection.getBendpoints().size()).isEqualTo(2);

		Point seq2FirstPoint = seq2Connection.getBendpoints().get(0);
		assertThat(seq2FirstPoint.getX()).isEqualTo(482);
		assertThat(seq2FirstPoint.getY()).isEqualTo(345);

		Point seq2SecondPoint = seq2Connection.getBendpoints().get(1);
		assertThat(seq2SecondPoint.getX()).isEqualTo(482);
		assertThat(seq2SecondPoint.getY()).isEqualTo(165);

	}

	@Test
	@DiagramResource
	public void testMoveEventVertical() {
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		move(gatewayShape, diagramTypeProvider)
			.by(0 , 100)
			.execute();
		
		// The MoveFlowNodeFeature will call AnchorUtil.reConnect, which will in turn recalculate the
		// boundary anchors to update them, we need to hook in there
		
		// see also DefaultMoveBendPointFeature to see how a bend point is created 
		
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		assertThat(connection)
			.hasNoDiagonalEdges()
			.hasBendpointCount(2);
	}

	@Test
	@DiagramResource
	public void testMoveTaskVerticalAndHorizontal() {
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_2");
		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Participant_1");

		FreeFormConnection seq2Connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		FreeFormConnection seq3Connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");

		assertThat(seq2Connection.getBendpoints()).isEmpty();
		assertThat(seq3Connection.getBendpoints()).isEmpty();

		move(taskShape, diagramTypeProvider)
			.by(-105, -95)
			.toContainer(participantShape)
			.execute();
		
		ILocation seq2StartLoc = LayoutUtil.getVisibleAnchorLocation(seq2Connection.getStart(), seq2Connection);
		assertThat(seq2StartLoc.getX()).isEqualTo(313);
		assertThat(seq2StartLoc.getY()).isEqualTo(325);

		ILocation seq2EndLoc = LayoutUtil.getVisibleAnchorLocation(seq2Connection.getEnd(), seq2Connection);
		assertThat(seq2EndLoc.getX()).isEqualTo(368);
		assertThat(seq2EndLoc.getY()).isEqualTo(280);

		assertThat(seq2Connection.getBendpoints().size()).isEqualTo(2);

		Point seq2FirstPoint = seq2Connection.getBendpoints().get(0);
		assertThat(seq2FirstPoint.getX()).isEqualTo(313);
		assertThat(seq2FirstPoint.getY()).isEqualTo(303);

		Point seq2SecondPoint = seq2Connection.getBendpoints().get(1);
		assertThat(seq2SecondPoint.getX()).isEqualTo(368);
		assertThat(seq2SecondPoint.getY()).isEqualTo(303);

		ILocation seq3StartLoc = LayoutUtil.getVisibleAnchorLocation(seq3Connection.getStart(), seq3Connection);
		assertThat(seq3StartLoc.getX()).isEqualTo(423);
		assertThat(seq3StartLoc.getY()).isEqualTo(255);

		ILocation seq3EndLoc = LayoutUtil.getVisibleAnchorLocation(seq3Connection.getEnd(), seq3Connection);
		assertThat(seq3EndLoc.getX()).isEqualTo(578);
		assertThat(seq3EndLoc.getY()).isEqualTo(350);

		assertThat(seq3Connection.getBendpoints().size()).isEqualTo(2);

		Point seq3FirstPoint = seq3Connection.getBendpoints().get(0);
		assertThat(seq3FirstPoint.getX()).isEqualTo(500);
		assertThat(seq3FirstPoint.getY()).isEqualTo(255);

		Point seq3SecondPoint = seq3Connection.getBendpoints().get(1);
		assertThat(seq3SecondPoint.getX()).isEqualTo(500);
		assertThat(seq3SecondPoint.getY()).isEqualTo(350);
	}
	
	@Test
	@DiagramResource
	public void testMoveTaskWithMessageFlow() {
		Shape serviceTaskShape = Util.findShapeByBusinessObjectId(diagram, "ServiceTask_2");
		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Participant_2");

		FreeFormConnection messageFlow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "MessageFlow_1");

		assertThat(messageFlow.getBendpoints()).isEmpty();

		move(serviceTaskShape, diagramTypeProvider)
			.by(100, 20)
			.toContainer(participantShape)
			.execute();

		ILocation messageFlowStartLoc = LayoutUtil.getVisibleAnchorLocation(messageFlow.getStart(), messageFlow);
		assertThat(messageFlowStartLoc.getX()).isEqualTo(445);
		assertThat(messageFlowStartLoc.getY()).isEqualTo(95);

		ILocation messageFlowEndLoc = LayoutUtil.getVisibleAnchorLocation(messageFlow.getEnd(), messageFlow);
		assertThat(messageFlowEndLoc.getX()).isEqualTo(345);
		assertThat(messageFlowEndLoc.getY()).isEqualTo(225);
		
		assertThat(messageFlow).hasNoDiagonalEdges();
		assertThat(messageFlow).hasBendpointCount(2);

		Point messageFlow3FirstPoint = messageFlow.getBendpoints().get(0);
		assertThat(messageFlow3FirstPoint.getX()).isEqualTo(445);
		assertThat(messageFlow3FirstPoint.getY()).isEqualTo(160);

		Point messageFlow3SecondPoint = messageFlow.getBendpoints().get(1);
		assertThat(messageFlow3SecondPoint.getX()).isEqualTo(345);
		assertThat(messageFlow3SecondPoint.getY()).isEqualTo(160);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BasicTest.testLayoutMaxLength.bpmn")
	public void testLayoutMaxLengthReached() {
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "reviewSuccessful_gw");
		ContainerShape laneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Approver");
		
		FreeFormConnection before = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "reviewSuccessful");
		
		Point beforePoint = point(before.getBendpoints().get(0).getX(), before.getBendpoints().get(0).getY());  
		
		// hit the max length, no bendpoints should be added / changed here
		move(gatewayShape, diagramTypeProvider)
			.by(0 , 230)
			.toContainer(laneShape)
			.execute();
		
		FreeFormConnection after = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "reviewSuccessful");

		Point afterPoint = point(after.getBendpoints().get(0).getX(), after.getBendpoints().get(0).getY());  

		assertThat(beforePoint).isEqualTo(afterPoint);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BasicTest.testLayoutMaxLength.bpmn")
	public void testLayoutMaxLengthNotReached() {
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "reviewSuccessful_gw");
		ContainerShape laneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "teamAssistant");
		
		FreeFormConnection before = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "reviewSuccessful");
		
		Point beforePoint = point(before.getBendpoints().get(0).getX(), before.getBendpoints().get(0).getY());  
		
		// dont hit the max length, bendpoints should be changed here
		move(gatewayShape, diagramTypeProvider)
			.by(0 , -10)
			.toContainer(laneShape)
			.execute();
		
		FreeFormConnection after = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "reviewSuccessful");

		Point afterPoint = point(after.getBendpoints().get(0).getX(), after.getBendpoints().get(0).getY());  

		assertThat(beforePoint).isNotEqualTo(afterPoint);
	}
}
