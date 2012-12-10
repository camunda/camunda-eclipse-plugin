package org.eclipse.bpmn2.modeler.core.test.feature.layout;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveFlowNodeOperation.move;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.ReconnectConnectionEndOperation.reconnect;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Collections;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.fest.assertions.core.Condition;
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
}
