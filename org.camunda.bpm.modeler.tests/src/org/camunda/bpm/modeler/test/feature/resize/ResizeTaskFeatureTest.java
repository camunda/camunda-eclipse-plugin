package org.camunda.bpm.modeler.test.feature.resize;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.ResizeShapeOperation.resize;

import org.camunda.bpm.modeler.core.layout.util.BoundaryEventUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.camunda.bpm.modeler.ui.features.event.BoundaryAttachment;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.data.Offset;
import org.junit.Test;

/**
 * Test resize of an untyped task
 * 
 * @author nico.rehwaldt
 */
public class ResizeTaskFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/move/ParticipantTaskConnectedBoundaryEvent.testBase.bpmn")
	public void testResizeNoLanesShouldRepositionFlowElementLabel() {
		
		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Point resizeAmount = point(0, -50);
		
		IRectangle preResizeTaskBounds = LayoutUtil.getAbsoluteBounds(taskShape);
		
		// when
		resize(taskShape, getDiagramTypeProvider())
			.fromTopLeftBy(resizeAmount)
			.execute();
		
		// then
		assertThat(taskShape)
			.movedBy(resizeAmount, preResizeTaskBounds);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeMoveBoundaryEvent.testBase.bpmn")
	public void testEnlargeTopLeftMovesBoundaryEvent() {
		
		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		Shape taskShape = BoundaryEventUtil.getAttachedToShape(boundaryShape, diagram);
		
		BoundaryAttachment preResizeAttachment = BoundaryEventUtil.getAttachment(boundaryShape, diagram);
		
		Point resizeAmount = point(-30, -40);
		
		// when
		resize(taskShape, getDiagramTypeProvider())
			.fromTopLeftBy(resizeAmount)
			.execute();
		
		// then
		BoundaryAttachment postResizeAttachment = BoundaryEventUtil.getAttachment(boundaryShape, diagram);

		// attachments before and after resize should be same
		assertThatAttachmentsSame(postResizeAttachment, preResizeAttachment);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeMoveBoundaryEvent.testBase.bpmn")
	public void testShrinkTopLeftMovesBoundaryEvent() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		Shape taskShape = BoundaryEventUtil.getAttachedToShape(boundaryShape, diagram);
		
		BoundaryAttachment preResizeAttachment = BoundaryEventUtil.getAttachment(boundaryShape, diagram);
		
		Point resizeAmount = point(30, 40);
		
		// when
		resize(taskShape, getDiagramTypeProvider())
			.fromTopLeftBy(resizeAmount)
			.execute();
		
		// then
		BoundaryAttachment postResizeAttachment = BoundaryEventUtil.getAttachment(boundaryShape, diagram);

		// attachments before and after resize should be same
		assertThatAttachmentsSame(postResizeAttachment, preResizeAttachment);
	}
	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeMoveBoundaryEvent.testBase.bpmn")
	public void testEnlargeBottomRightMovesBoundaryEvent() {
		
		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		Shape taskShape = BoundaryEventUtil.getAttachedToShape(boundaryShape, diagram);
		
		BoundaryAttachment preResizeAttachment = BoundaryEventUtil.getAttachment(boundaryShape, diagram);
		
		Point resizeAmount = point(30, 40);
		
		// when
		resize(taskShape, getDiagramTypeProvider())
			.fromBottomLeftBy(resizeAmount)
			.execute();
		
		// then
		BoundaryAttachment postResizeAttachment = BoundaryEventUtil.getAttachment(boundaryShape, diagram);

		// attachments before and after resize should be same
		assertThatAttachmentsSame(postResizeAttachment, preResizeAttachment);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeMoveBoundaryEvent.testBase.bpmn")
	public void testShrinkBottomRightMovesBoundaryEvent() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		Shape taskShape = BoundaryEventUtil.getAttachedToShape(boundaryShape, diagram);
		
		BoundaryAttachment preResizeAttachment = BoundaryEventUtil.getAttachment(boundaryShape, diagram);
		
		Point resizeAmount = point(-30, -40);
		
		// when
		resize(taskShape, getDiagramTypeProvider())
			.fromTopLeftBy(resizeAmount)
			.execute();
		
		// then
		BoundaryAttachment postResizeAttachment = BoundaryEventUtil.getAttachment(boundaryShape, diagram);
		
		// attachments before and after resize should be same
		assertThatAttachmentsSame(postResizeAttachment, preResizeAttachment);
	}

	private void assertThatAttachmentsSame(BoundaryAttachment actual, BoundaryAttachment expected) {
		assertThat(actual.getSector())
			.isEqualTo(expected.getSector());
	
		assertThat(actual.getPercentage())
			.isEqualTo(expected.getPercentage(), Offset.offset(0.01));
	}
}
