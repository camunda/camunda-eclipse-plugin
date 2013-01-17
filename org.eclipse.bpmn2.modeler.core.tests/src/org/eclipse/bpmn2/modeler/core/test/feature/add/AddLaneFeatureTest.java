package org.eclipse.bpmn2.modeler.core.test.feature.add;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddLaneOperation.addLane;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.junit.Test;

public class AddLaneFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource
	public void testChildrenMoveOnLaneAdd() throws Exception {

		ContainerShape participantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		IRectangle taskOriginalBounds = LayoutUtil.getAbsoluteBounds(taskShape);
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		// when
		// lane is added
		addLane(diagramTypeProvider)
			.toContainer(participantShape)
			.execute();
		
		// connections should still be ok
		assertThat(connection)
			.hasNoDiagonalEdges();
		

		ContainerShape laneShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		
		assertThat(taskShape)
			.isContainedIn(laneShape);
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		IRectangle taskMovedBounds = LayoutUtil.getAbsoluteBounds(taskShape);
		
		// diff = lanePos - poolPos
		Point p2lDiff = point(laneBounds.getX() - participantBounds.getX(), laneBounds.getY() - participantBounds.getY());
		
		// expect taskPos = taskOldPos - (lanePos - poolPos)
		// so that effective position stays the same
		Point expectedTaskPosition = point(taskOriginalBounds.getX() - p2lDiff.getX(), taskOriginalBounds.getY() - p2lDiff.getY());
		
		assertThat(taskMovedBounds)
			.position()
				.isEqualTo(expectedTaskPosition);
		
		assertThat(taskShape)
			.bounds()
				.doNotContainAnyOf(connection.getBendpoints());
	}
}
