package org.camunda.bpm.modeler.test.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rect;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.AddLaneOperation.addLane;
import static org.camunda.bpm.modeler.test.util.operations.CreateParticipantOperation.createParticipant;
import static org.camunda.bpm.modeler.test.util.operations.MoveShapeOperation.move;
import static org.camunda.bpm.modeler.test.util.operations.ResizeShapeOperation.resize;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author adrobisch
 */
public class ScrollUtilTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/layout/util/LayoutUtilTest.testContainerBoundsInProcess.bpmn")
	public void testScrollShapeUpdate() {
		// given
		Diagram diagramUnderTest = diagram;
		
		// when
		Shape scrollShape = ScrollUtil.getScrollShape(diagramUnderTest);
		
		// then
		assertThat(scrollShape).position().isEqualTo(846, 506);
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");

		Point movement = point(0, 200);

		move(taskShape, diagramTypeProvider)
			.by(movement)
			.execute();
		
		IRectangle diagramBounds = LayoutUtil.getChildrenBBox(diagramUnderTest);
		
		assertThat(diagramBounds).isEqualTo(rect(288, 298, 408, 258));
		assertThat(scrollShape).position().isEqualTo(846, 706);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/layout/util/LayoutUtilTest.testContainerBoundsInProcess.bpmn")
	public void testScrollShapeAfterParticipantAdd() {
		// given
		Diagram diagramUnderTest = diagram;
		assertThat(ScrollUtil.getScrollShape(diagramUnderTest)).position().isEqualTo(846, 506);
		
		// when
		createParticipant(20, 20, 400, 200, getDiagram(), getDiagramTypeProvider()).execute();
		
		Shape scrollShape = ScrollUtil.getScrollShape(diagramUnderTest);
		
		// then
		assertThat(scrollShape).position().isEqualTo(896, 556);

		// scroll shape is still in diagram, not in participant
		assertThat(diagram).hasChild(scrollShape);
	}
	
	@Test
	@DiagramResource
	public void testScrollShapeAfterResize() {
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		
		Point growAmount = point(200, 200);

		Shape scrollShape = ScrollUtil.getScrollShape(diagram);	
		
		assertThat(scrollShape).position().isEqualTo(920, 545);
		
		// when
		resize(participantShape, getDiagramTypeProvider())
			.fromBottomRightBy(growAmount)
			.execute();
		
		// then
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		Point expectedPoint = point(1120, 745);
		
		assertThat(participantBounds.getX() + participantBounds.getWidth() + ScrollUtil.SCROLL_PADDING).isEqualTo(expectedPoint.getX());
		assertThat(participantBounds.getY() + participantBounds.getHeight() + ScrollUtil.SCROLL_PADDING).isEqualTo(expectedPoint.getY());
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFeatureTestBase.testAddToNonEmptyParticipant.bpmn")
	public void testScrollShapeAfterLaneAdd() {
		// given participant
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		
		// when
		// lane is added to it
		addLane(diagramTypeProvider)
			.toContainer(containerShape)
			.execute();
		
		// then
		Shape scrollShape = ScrollUtil.getScrollShape(diagram);	
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(containerShape);
		
		assertThat(scrollShape).position().isEqualTo(participantBounds.getX() + participantBounds.getWidth() +  ScrollUtil.SCROLL_PADDING, participantBounds.getY() + participantBounds.getHeight() +  ScrollUtil.SCROLL_PADDING);
	}
	
}
