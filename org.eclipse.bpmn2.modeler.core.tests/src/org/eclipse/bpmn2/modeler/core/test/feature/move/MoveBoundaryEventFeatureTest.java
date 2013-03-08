package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.*;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.core.layout.util.BoundaryEventUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.ui.features.event.BoundaryAttachment;
import org.eclipse.bpmn2.modeler.ui.features.event.MoveBoundaryEventFeature;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author nico.rehwaldt
 */
public class MoveBoundaryEventFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveBoundaryEventFeatureTest.testBase.bpmn")
	public void testCorrectImport() {
		
		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		BoundaryEvent boundaryEvent = BusinessObjectUtil.getFirstElementOfType(boundaryShape, BoundaryEvent.class);
		Task task = BusinessObjectUtil.getFirstElementOfType(taskShape, Task.class);

		BoundaryAttachment boundaryAttachement = BoundaryEventUtil.getStoredBoundaryAttachment(boundaryShape);
		
		// when 
		// looking at diagram

		// then
		// boundary event and task are linked in model
		assertThat(boundaryEvent.getAttachedToRef())
			.isEqualTo(task);
		
		// boundary event and task share same container
		assertThat(boundaryShape)
			.isInFrontOf(taskShape)
			.isContainedIn(taskShape.getContainer());
		
		// sector should have been saved correctly
		assertThat(boundaryAttachement.getSector())
			.isEqualTo(Sector.RIGHT);
		
		assertThat(boundaryAttachement.getPercentage())
			.isEqualTo(54.0 / 80.0);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveBoundaryEventFeatureTest.testBase.bpmn")
	public void testAttachTopOnLine() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		ContainerShape parent = boundaryShape.getContainer();
		
		IRectangle taskPosition = LayoutUtil.getAbsoluteBounds(taskShape);

		Point boundaryAttachPosition = point(40, 0);
		
		Point expectedPosition = point(
			taskPosition.getX() + 40, 
			taskPosition.getY());
		
		// when 
		// dropping boundary event directly on the line
		move(boundaryShape, diagramTypeProvider)
			.toContainer(taskShape)
			.centerTo(boundaryAttachPosition)
			.execute();
		
		// now the usertask is contained in the process
		assertThat(boundaryShape)
			.isContainedIn(parent)
			.midPoint()
				.isEqualTo(expectedPosition);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveBoundaryEventFeatureTest.testBase.bpmn")
	public void testAttachInsideTask() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		ContainerShape parent = boundaryShape.getContainer();
		
		IRectangle taskPosition = LayoutUtil.getAbsoluteBounds(taskShape);

		Point boundaryAttachPosition = point(40, 6);
		
		Point expectedPosition = point(
			taskPosition.getX() + 40, 
			taskPosition.getY());
		
		// when 
		// dropping boundary event directly on the line
		move(boundaryShape, diagramTypeProvider)
			.toContainer(taskShape)
			.centerTo(boundaryAttachPosition)
			.execute();
		
		// then
		// the event should have been attached correctly
		assertThat(boundaryShape)
			.isContainedIn(parent)
			.midPoint()
				.isEqualTo(expectedPosition);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveBoundaryEventFeatureTest.testBase.bpmn")
	public void testAttachOutsideTask() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		ContainerShape parent = boundaryShape.getContainer();
		
		IRectangle taskPosition = LayoutUtil.getAbsoluteBounds(taskShape);

		Point boundaryAttachPosition = point(40, -2);
		
		Point expectedPosition = point(
			taskPosition.getX() + 40, 
			taskPosition.getY());
		
		// when 
		// dropping boundary event directly on the line
		move(boundaryShape, diagramTypeProvider)
			.toContainer(taskShape)
			.centerTo(boundaryAttachPosition)
			.execute();
		
		// then
		// the event should have been attached correctly
		assertThat(boundaryShape)
			.isContainedIn(parent)
			.midPoint()
				.isEqualTo(expectedPosition);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveBoundaryEventFeatureTest.testBase.bpmn")
	public void testAttachOnEdge() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		ContainerShape parent = boundaryShape.getContainer();
		
		IRectangle taskPosition = LayoutUtil.getAbsoluteBounds(taskShape);

		Point boundaryAttachPosition = point(0, 0);
		
		Point expectedPosition = point(
			taskPosition.getX() + 0, 
			taskPosition.getY());
		
		// when 
		// dropping boundary event directly on the line
		move(boundaryShape, diagramTypeProvider)
			.toContainer(taskShape)
			.centerTo(boundaryAttachPosition)
			.execute();
		
		// then
		// the event should have been attached correctly
		assertThat(boundaryShape)
			.isContainedIn(parent)
			.midPoint()
				.isEqualTo(expectedPosition);
		

		// and
		// the event should have been attached correctly
		BoundaryAttachment boundaryAttachement = BoundaryEventUtil.getStoredBoundaryAttachment(boundaryShape);

		// sector should have been saved correctly
		assertThat(boundaryAttachement.getSector())
			.isEqualTo(Sector.TOP_LEFT);
		
		assertThat(boundaryAttachement.getPercentage())
			.isEqualTo(-1);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveBoundaryEventFeatureTest.testBase.bpmn")
	public void testMoveUpdatesBoundaryAttachment() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Point boundaryAttachPosition = point(40, 0);
		
		// when 
		// dropping boundary event directly on the line
		move(boundaryShape, diagramTypeProvider)
			.toContainer(taskShape)
			.centerTo(boundaryAttachPosition)
			.execute();
		
		// then
		// the event should have been attached correctly
		BoundaryAttachment boundaryAttachement = BoundaryEventUtil.getStoredBoundaryAttachment(boundaryShape);

		// sector should have been saved correctly
		assertThat(boundaryAttachement.getSector())
			.isEqualTo(Sector.TOP);
		
		assertThat(boundaryAttachement.getPercentage())
			.isEqualTo(40.0 / 100.0);
	}
	

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/MoveBoundaryEventFeatureTest.testCollaborationBase.bpmn")
	public void testMoveToOtherParticipant() {

		// given
		Shape boundaryShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		ContainerShape targetParticipantShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Participant_1");
		
		Point boundaryPosition = point(40, 0);
		
		BoundaryAttachment preMoveAttachment = BoundaryEventUtil.getStoredBoundaryAttachment(boundaryShape);
		BoundaryEvent boundaryEvent = BusinessObjectUtil.getFirstElementOfType(boundaryShape, BoundaryEvent.class);

		Participant targetParticipant = BusinessObjectUtil.getFirstElementOfType(targetParticipantShape, Participant.class);
		
		// when 
		// moving boundary event to empty participant
		move(boundaryShape, diagramTypeProvider)
			.toContainer(targetParticipantShape)
			.to(boundaryPosition)
			// set move with activiy or move will not be allowed
			.withFlag(MoveBoundaryEventFeature.MOVE_WITH_ACTIVITY)
			.execute();

		BoundaryAttachment postMoveAttachment = BoundaryEventUtil.getStoredBoundaryAttachment(boundaryShape);
		
		// then
		// should have been moved
		assertThat(boundaryShape)
			.isContainedIn(targetParticipantShape);
		
		// participant is linked to process now
		assertThat(targetParticipant.getProcessRef())
			.isNotNull();
		
		// boundary event should have been updated to new participant
		assertThat(boundaryEvent.eContainer())
			.isEqualTo(targetParticipant.getProcessRef());
		
		// attachment should not have changed
		assertThat(postMoveAttachment)
			.isEqualsToByComparingFields(preMoveAttachment);
	}
}