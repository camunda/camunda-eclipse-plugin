package org.eclipse.bpmn2.modeler.core.test.feature.layout;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveFlowNodeOperation.move;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
@Ignore
public class LayoutSequenceFlowTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testMoveCloseBy() {
		Shape fixedShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_2");
		Shape movingShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_1");
		
		// when being moved close to the other task up to a given distance (45)
		// connection should still be | -> |
		move(movingShape, diagramTypeProvider)
			.by(-30, 0)
			.execute();
		
		// edge task_2 right x-pos 535
		// min edge task_1 left x-pos: 580
		// min distance between borders: 45
		
		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
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

	// helpers /////////////////////////////////////
	
	private Point point(int x, int y) {
		return Graphiti.getGaService().createPoint(x, y);
	}
}
