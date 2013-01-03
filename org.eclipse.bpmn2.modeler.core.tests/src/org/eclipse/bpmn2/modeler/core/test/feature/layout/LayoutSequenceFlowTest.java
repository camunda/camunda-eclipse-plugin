package org.eclipse.bpmn2.modeler.core.test.feature.layout;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveFlowNodeOperation.move;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.test.util.operations.MoveParticipantOperation;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Fail;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class LayoutSequenceFlowTest extends AbstractFeatureTest {
	
	@Test
	@Ignore
	@DiagramResource
	public void testMoveCloseBy() {
		Shape fixedShape = Util.findShapeByBusinessObjectId(diagram, "Task_2");
		Shape movingShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		// when being moved close to the other task up to a given distance (45)
		// connection should still be | -> |
		move(movingShape, diagramTypeProvider)
			.by(-30, 0)
			.execute();
		
		// edge task_2 right x-pos 535
		// min edge task_1 left x-pos: 580
		// min distance between borders: 45
		
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		assertThat(connection).hasNoDiagonalEdges();
		assertThat(connection).hasBendpointCount(2);
		
		assertThat(connection)
			.anchorPointOn(fixedShape)
				.isRightOfShape()
				.end()
			.anchorPointOn(movingShape)
				.isLeftOfShape();
		
		// when being moved even closer
		// connection should change to 
		// ---
		//  |
		//  v
		// ---
		move(movingShape, diagramTypeProvider)
			.by(-10, 0)
			.execute();
		
		assertThat(connection).hasNoDiagonalEdges();
		assertThat(connection).hasBendpointCount(2);
		
		assertThat(connection)
			.anchorPointOn(fixedShape)
				.isAboveShape()
				.end()
			.anchorPointOn(movingShape)
				.isBeneathShape();
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/layout/FlowReconnectTest.testManualReconnectSequenceFlow.bpmn")
	public void testAnchorPlacementAfterImport() {
		Shape task2 = Util.findShapeByBusinessObjectId(diagram, "Task_2");
		
		// whenever we import an shape it will have four anchors per default
		// (0) a chopbox anchor we can use to circle around that shape
		// (1..4) four custom made anchors on each directin N / E / S / W
		
		assertThat(task2.getAnchors().size()).isEqualTo(5); 
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/layout/LayoutSequenceFlowTest.testBaseHorizontal.bpmn")
	public void testMoveLayoutedHorizontalNonBreaking() {
		// given
		
		FreeFormConnection sequenceFlow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		Shape userTask1 = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		
		List<Point> oldBendpoints = getBendpoints(sequenceFlow1);
		
		// when
		move(userTask1, getDiagramTypeProvider())
			.by(10, 0)
			.execute();

		List<Point> newBendpoints = getBendpoints(sequenceFlow1);

		// then
		// bendpoints should stay as they are (no change)
		assertPointsEqual(oldBendpoints, newBendpoints);
	}

	@Test
	@DiagramResource
	public void testAnchorFixAfterMove() {
		FreeFormConnection sequenceFlow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		Shape exclusiveGateway2Shape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_2");
		Shape userTask1Shape = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		
		move(exclusiveGateway2Shape, getDiagramTypeProvider())
			.by(20, 10)
			.execute();
		
		assertThat(sequenceFlow1).anchorPointOn(userTask1Shape).isAboveShape();
	}

	@Test
	@DiagramResource
	public void testPoolMessageFlowAnchorFixAfterMove() {
		FreeFormConnection messageFlow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "MessageFlow_1");
		
//		Shape task1Shape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape participant2Shape = Util.findShapeByBusinessObjectId(diagram, "Participant_2");
		
		MoveParticipantOperation.move(participant2Shape, getDiagramTypeProvider())
			.by(20, 10)
			.execute();
		
		System.out.println(LayoutUtil.getAbsoluteRectangle(participant2Shape));
		
		System.out.println(getConnectionPoints(messageFlow));
		
		assertThat(messageFlow).anchorPointOn(participant2Shape).isAboveShape();
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/layout/LayoutSequenceFlowTest.testBaseHorizontal.bpmn")
	public void testMoveLayoutedVerticalBreaking() {
		// given
		
		FreeFormConnection sequenceFlow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		Shape userTask1 = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		
		List<Point> oldBendpoints = getBendpoints(sequenceFlow1);
		
		// when
		move(userTask1, getDiagramTypeProvider())
			.by(0, 10)
			.execute();

		List<Point> newBendpoints = getBendpoints(sequenceFlow1);

		// then
		// first bendpoint should stay as it is 
		// AND 
		// second bendpoint should be adjusted to new y-position (10)

		assertThat(oldBendpoints.get(0)).isEqualTo(newBendpoints.get(0));
		
		Point diffBendpoint1 = diffPoints(oldBendpoints.get(1), newBendpoints.get(1));
		
		assertThat(diffBendpoint1).isEqualTo(point(0, -10));
	}
	
	protected Point diffPoints(Point p1, Point p2) {
		return point(p1.getX() - p2.getX(), p1.getY() - p2.getY());
	}
	
	protected List<Point> diffPointList(List<Point> l1, List<Point> l2) {
		ArrayList<Point> diff = new ArrayList<Point>();
		
		int s = Math.max(l1.size(), l2.size());
		
		try {
			for (int i = 0; i < s; i++) {
				diff.add(diffPoints(l1.get(i), l2.get(i)));
			}
		} catch (IndexOutOfBoundsException e) {
			Fail.fail(String.format("Expected <%s> and <%s> have same size", new Object[] { l1, l2 }), e);
		}
		
		return diff;
	}
	
	protected boolean isPointListDiffZero(List<Point> diff) {
		for (Point p: diff) {
			if (p.getX() != 0 || p.getY() != 0) {
				return false;
			}
		}
		
		return true;
	}
	
	protected void assertPointsEqual(List<Point> l1, List<Point> l2) {
		List<Point> diff = diffPointList(l1, l2);
		if (!isPointListDiffZero(diff)) {
			Fail.fail(String.format("Expected diff between <%s> and <%s> to be zero but was <%s>", l1, l2, diff));
		}
	}

	protected List<Point> getBendpoints(FreeFormConnection connection) {
		ArrayList<Point> points = new ArrayList<Point>();
		
		for (Point p: connection.getBendpoints()) {
			points.add(point(p.getX(), p.getY()));
		}
		
		return points;
	}
	
	protected List<Point> getConnectionPoints(FreeFormConnection connection) {
		ArrayList<Point> points = new ArrayList<Point>();
		

		ILocation startAnchorLocation = LayoutUtil.getVisibleAnchorLocation(connection.getStart(), connection);
		ILocation endAnchorLocation = LayoutUtil.getVisibleAnchorLocation(connection.getEnd(), connection);
		
		points.add(point(startAnchorLocation));
		points.addAll(getBendpoints(connection));
		points.add(point(endAnchorLocation));
		
		return points;
	}
}
