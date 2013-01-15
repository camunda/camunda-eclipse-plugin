package org.eclipse.bpmn2.modeler.core.test.layout;

import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.ReconnectConnectionEndOperation.reconnectEnd;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil;
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
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class BoundaryEventTest extends AbstractFeatureTest {
	/////// TOP
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testTopReconnectingTopLeft() throws Exception {
		Shape boundaryEvent5Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_5");
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_10");
		reconnectConnectionAssertBoundarySector(sequenceFlow2, boundaryEvent5Shape, taskShape, Sector.TOP);
		
		assertThat(sequenceFlow2).hasBendpointCount(1);
		assertThat(sequenceFlow2).hasNoDiagonalEdges();
		assertThat(sequenceFlow2).anchorPointOn(taskShape).isAt(Sector.RIGHT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testTopReconnectingBottomLeft() throws Exception {
		Shape boundaryEvent5Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_5");
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_6");
		reconnectConnectionAssertBoundarySector(sequenceFlow2, boundaryEvent5Shape, taskShape, Sector.TOP);
		
		assertThat(sequenceFlow2).hasBendpointCount(2);
		assertThat(sequenceFlow2).hasNoDiagonalEdges();
		assertThat(sequenceFlow2).anchorPointOn(taskShape).isAt(Sector.TOP);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testTopReconnectingBottomRight() throws Exception {
		Shape boundaryEvent5Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_5");
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_5");
		reconnectConnectionAssertBoundarySector(sequenceFlow2, boundaryEvent5Shape, taskShape, Sector.TOP);
		
		assertThat(sequenceFlow2).hasBendpointCount(2);
		assertThat(sequenceFlow2).hasNoDiagonalEdges();
		assertThat(sequenceFlow2).anchorPointOn(taskShape).isAt(Sector.TOP);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testTopReconnectingTopDirectly() throws Exception {
		Shape boundaryEvent5Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_5");
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_17");
		reconnectConnectionAssertBoundarySector(sequenceFlow2, boundaryEvent5Shape, taskShape, Sector.TOP);
		
		assertThat(sequenceFlow2).hasBendpointCount(2);
		assertThat(sequenceFlow2).hasNoDiagonalEdges();
		assertThat(sequenceFlow2).anchorPointOn(taskShape).isAt(Sector.BOTTOM);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testTopReconnectingTop() throws Exception {
		Shape boundaryEvent5Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_5");
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_18");
		reconnectConnectionAssertBoundarySector(sequenceFlow2, boundaryEvent5Shape, taskShape, Sector.TOP);
		
		assertThat(sequenceFlow2).hasBendpointCount(2);
		assertThat(sequenceFlow2).hasNoDiagonalEdges();
		assertThat(sequenceFlow2).anchorPointOn(taskShape).isAt(Sector.BOTTOM);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testTopReconnectingLeft() throws Exception {
		Shape boundaryEvent5Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_5");
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_20");
		reconnectConnectionAssertBoundarySector(sequenceFlow2, boundaryEvent5Shape, taskShape, Sector.LEFT);
		
		assertThat(sequenceFlow2).hasBendpointCount(2);
		assertThat(sequenceFlow2).anchorPointOn(taskShape).isAt(Sector.RIGHT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testTopReconnectingRight() throws Exception {
		Shape boundaryEvent5Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_5");
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");

		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_19");
		reconnectConnectionAssertBoundarySector(sequenceFlow2, boundaryEvent5Shape, taskShape, Sector.RIGHT);
		
		assertThat(sequenceFlow2).hasBendpointCount(0);
		assertThat(sequenceFlow2).hasNoDiagonalEdges();
		assertThat(sequenceFlow2).anchorPointOn(taskShape).isAt(Sector.LEFT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testTopReconnectingTopRight() throws Exception {
		Shape boundaryEvent5Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_5");
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_3");
		reconnectConnectionAssertBoundarySector(sequenceFlow2, boundaryEvent5Shape, taskShape, Sector.TOP);
		
		assertThat(sequenceFlow2).hasBendpointCount(1);
		assertThat(sequenceFlow2).hasNoDiagonalEdges();
		assertThat(sequenceFlow2).anchorPointOn(taskShape).isAt(Sector.LEFT);
	}
	
	////// BOTTOM
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingTopLeft() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_10");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow5).hasBendpointCount(2);
		assertThat(sequenceFlow5).hasNoDiagonalEdges();
		assertThat(sequenceFlow5).anchorPointOn(taskShape).isAt(Sector.BOTTOM);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingBottomLeft() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_6");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow5).hasBendpointCount(1);
		assertThat(sequenceFlow5).hasNoDiagonalEdges();
		assertThat(sequenceFlow5).anchorPointOn(taskShape).isAt(Sector.RIGHT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingBottomRight() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_5");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow5).hasBendpointCount(1);
		assertThat(sequenceFlow5).hasNoDiagonalEdges();
		assertThat(sequenceFlow5).anchorPointOn(taskShape).isAt(Sector.LEFT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingBottomDirectly() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_15");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow5).hasBendpointCount(0);
		assertThat(sequenceFlow5).hasNoDiagonalEdges();
		assertThat(sequenceFlow5).anchorPointOn(taskShape).isAt(Sector.TOP);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingBottom() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_16");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow5).hasBendpointCount(2);
		assertThat(sequenceFlow5).hasNoDiagonalEdges();
		assertThat(sequenceFlow5).anchorPointOn(taskShape).isAt(Sector.TOP);
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
		assertThat(sequenceFlow5).hasNoDiagonalEdges();
		assertThat(sequenceFlow5).anchorPointOn(taskShape).isAt(Sector.BOTTOM);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingBottom() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_5");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow5).hasBendpointCount(2);
		assertThat(sequenceFlow5).anchorPointOn(taskShape).isAt(Sector.TOP);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingTop() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_14");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.TOP);
		
		assertThat(sequenceFlow5).hasBendpointCount(2);
		assertThat(sequenceFlow5).anchorPointOn(taskShape).isAt(Sector.BOTTOM);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingRight() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_8");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.RIGHT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingTopLeft() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_3");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.RIGHT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingTopRight() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_3");
		FreeFormConnection sequenceFlow3 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_3");
		reconnectConnectionAssertBoundarySector(sequenceFlow3, boundaryEvent1Shape, taskShape, Sector.RIGHT);
		
		assertThat(sequenceFlow3).hasBendpointCount(2);
		assertThat(sequenceFlow3).hasNoDiagonalEdges();
		assertThat(sequenceFlow3).anchorPointOn(taskShape).isAt(Sector.LEFT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingBottomRight() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_3");
		FreeFormConnection sequenceFlow3 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_3");
		reconnectConnectionAssertBoundarySector(sequenceFlow3, boundaryEvent1Shape, taskShape, Sector.RIGHT);
		
		assertThat(sequenceFlow3).hasBendpointCount(2);
		assertThat(sequenceFlow3).hasNoDiagonalEdges();
		assertThat(sequenceFlow3).anchorPointOn(taskShape).isAt(Sector.LEFT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingBottomLeft() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_3");
		FreeFormConnection sequenceFlow3 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_13");
		reconnectConnectionAssertBoundarySector(sequenceFlow3, boundaryEvent1Shape, taskShape, Sector.RIGHT);
		
		assertThat(sequenceFlow3).hasBendpointCount(2);
		assertThat(sequenceFlow3).hasNoDiagonalEdges();
		assertThat(sequenceFlow3).anchorPointOn(taskShape).isAt(Sector.RIGHT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testLeftReconnectingBottomRight() throws Exception {
		Shape boundaryEvent4Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_4");
		FreeFormConnection sequenceFlow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_5");
		reconnectConnectionAssertBoundarySector(sequenceFlow1, boundaryEvent4Shape, taskShape, Sector.LEFT);
		
		assertThat(sequenceFlow1).hasBendpointCount(2);
		assertThat(sequenceFlow1).hasNoDiagonalEdges();
		assertThat(sequenceFlow1).anchorPointOn(taskShape).isAt(Sector.LEFT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testLeftReconnectingBottomLeft() throws Exception {
		Shape boundaryEvent4Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_4");
		FreeFormConnection sequenceFlow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_6");
		reconnectConnectionAssertBoundarySector(sequenceFlow1, boundaryEvent4Shape, taskShape, Sector.LEFT);
		
		assertThat(sequenceFlow1).hasBendpointCount(2);
		assertThat(sequenceFlow1).hasNoDiagonalEdges();
		assertThat(sequenceFlow1).anchorPointOn(taskShape).isAt(Sector.RIGHT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testLeftReconnectingTopLeft() throws Exception {
		Shape boundaryEvent4Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_4");
		FreeFormConnection sequenceFlow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_10");
		reconnectConnectionAssertBoundarySector(sequenceFlow1, boundaryEvent4Shape, taskShape, Sector.LEFT);
		
		assertThat(sequenceFlow1).hasBendpointCount(2);
		assertThat(sequenceFlow1).hasNoDiagonalEdges();
		assertThat(sequenceFlow1).anchorPointOn(taskShape).isAt(Sector.RIGHT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testLeftReconnectingTopRight() throws Exception {
		Shape boundaryEvent4Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_4");
		FreeFormConnection sequenceFlow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_3");
		reconnectConnectionAssertBoundarySector(sequenceFlow1, boundaryEvent4Shape, taskShape, Sector.LEFT);
		
		assertThat(sequenceFlow1).hasBendpointCount(2);
		assertThat(sequenceFlow1).hasNoDiagonalEdges();
		assertThat(sequenceFlow1).anchorPointOn(taskShape).isAt(Sector.LEFT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testLeftReconnectingBottom() throws Exception {
		Shape boundaryEvent4Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_4");
		FreeFormConnection sequenceFlow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_13");
		reconnectConnectionAssertBoundarySector(sequenceFlow1, boundaryEvent4Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow1).hasBendpointCount(2);
		assertThat(sequenceFlow1).anchorPointOn(taskShape).isAt(Sector.TOP);
	}
	
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testLeftReconnectingTop() throws Exception {
		Shape boundaryEvent4Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_4");
		FreeFormConnection sequenceFlow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_17");
		reconnectConnectionAssertBoundarySector(sequenceFlow1, boundaryEvent4Shape, taskShape, Sector.TOP);
		
		assertThat(sequenceFlow1).hasBendpointCount(2);
		assertThat(sequenceFlow1).anchorPointOn(taskShape).isAt(Sector.BOTTOM);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testLeftReconnectingLeft() throws Exception {
		Shape boundaryEvent4Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_4");
		FreeFormConnection sequenceFlow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_9");
		reconnectConnectionAssertBoundarySector(sequenceFlow1, boundaryEvent4Shape, taskShape, Sector.LEFT);
		
		assertThat(sequenceFlow1).hasBendpointCount(2);
		assertThat(sequenceFlow1).hasNoDiagonalEdges();
		assertThat(sequenceFlow1).anchorPointOn(taskShape).isAt(Sector.RIGHT);
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
		assertThat(sequenceFlow).hasBendpointCount(2);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testMoveTaskWithBoundaryEvent.bpmn")
	public void testMoveTaskWithBoundaryEventRetainsBendpoints() {
		ContainerShape userTaskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "UserTask_1");
		FreeFormConnection sequenceFlow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");

		assertThat(sequenceFlow2.getBendpoints().size()).isEqualTo(2);
		
		Point lastPointBefore = ConversionUtil.point(sequenceFlow2.getBendpoints().get(1).getX(), sequenceFlow2.getBendpoints().get(1).getY());
		
		move(userTaskShape, diagramTypeProvider)
			.by(0, -5)
			.toContainer(diagram)
			.execute();
		
		// dont relayout connections from boundary events with more than one bendpoint
		assertThat(sequenceFlow2.getBendpoints().size()).isEqualTo(2);
		
		Point lastPointAfter = sequenceFlow2.getBendpoints().get(1);
		assertThat(lastPointAfter.getX()).isEqualTo(lastPointBefore.getX());
		assertThat(lastPointAfter.getY()).isEqualTo(lastPointBefore.getY());
	}
	
	
	private void reconnectConnectionAssertBoundarySector(FreeFormConnection connection, Shape boundaryEvent, Shape reconnectionTarget, Sector expectedSector) {

		reconnectEnd(connection, diagramTypeProvider)
			.toElement(reconnectionTarget)
			.execute();
		
		assertThat(connection).anchorPointOn(boundaryEvent).isAt(expectedSector);
	}
	
}
