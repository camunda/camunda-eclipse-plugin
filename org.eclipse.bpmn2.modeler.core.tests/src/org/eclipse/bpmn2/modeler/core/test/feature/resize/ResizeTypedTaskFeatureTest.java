package org.eclipse.bpmn2.modeler.core.test.feature.resize;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.ResizeShapeOperation.resize;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.layout.util.BoundaryEventUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.ui.features.event.BoundaryAttachment;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.data.Offset;
import org.junit.Test;

/**
 * Test resize of a typed task (user task)
 * 
 * @author nico.rehwaldt
 */
public class ResizeTypedTaskFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/resize/ResizeMoveBoundaryEvent.testBase.bpmn")
	public void testEnlargeTopLeftMovesBoundaryEvent() {
		
		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_3");
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
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/resize/ResizeMoveBoundaryEvent.testBase.bpmn")
	public void testShrinkTopLeftMovesBoundaryEvent() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_3");
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
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/resize/ResizeMoveBoundaryEvent.testBase.bpmn")
	public void testEnlargeBottomRightMovesBoundaryEvent() {
		
		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_3");
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
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/resize/ResizeMoveBoundaryEvent.testBase.bpmn")
	public void testShrinkBottomRightMovesBoundaryEvent() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_3");
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
			.isEqualTo(expected.getPercentage(), Offset.offset(0.02));
	}
}
