package org.eclipse.bpmn2.modeler.core.test.feature.add;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddLaneOperation.addLane;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.junit.Test;

public class AddLaneFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testAddOnDiagramNotAllowed() throws Exception {

		// given empty diagram
		
		// when
		// lane is added to it
		addLane(diagramTypeProvider)
			.toContainer(diagram)
			.execute();
		
		// then
		// operation should be rejected
		assertThat(diagram)
			.hasNoChildren();
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddLaneFeatureTest.testAddOnToPool.bpmn")
	public void testAddOnToParticipantChangesContainer() throws Exception {

		// given participant
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		ContainerShape childShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		// when
		// lane is added to it
		addLane(diagramTypeProvider)
			.toContainer(containerShape)
			.execute();
		
		ContainerShape newChildContainer = childShape.getContainer();
		
		// then
		// lane should be a container shape between participant and child shape
		assertThat(containerShape).hasChild(newChildContainer);
		assertThat(newChildContainer).isLinkedTo(elementOfType(Lane.class));
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddLaneFeatureTest.testAddOnToPool.bpmn")
	public void testAddOnToParticipantRetainsContainedEventPosition() throws Exception {

		// given participant
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		ContainerShape childShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		IRectangle preMoveShapePosition = LayoutUtil.getAbsoluteBounds(childShape);
		
		// when
		// lane is added to it
		addLane(diagramTypeProvider)
			.toContainer(containerShape)
			.execute();
		
		IRectangle postMoveShapePosition = LayoutUtil.getAbsoluteBounds(childShape);
		
		// then
		// contained element pos should be retained
		assertThat(postMoveShapePosition).isEqualTo(preMoveShapePosition);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddLaneFeatureTest.testAddOnToPool.bpmn")
	public void testAddOnToParticipantRetainsContainedTaskPosition() throws Exception {

		// given participant
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		ContainerShape childShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		IRectangle preMoveShapePosition = LayoutUtil.getAbsoluteBounds(childShape);
		
		// when
		// lane is added to it
		addLane(diagramTypeProvider)
			.toContainer(containerShape)
			.execute();
		
		IRectangle postMoveShapePosition = LayoutUtil.getAbsoluteBounds(childShape);
		
		// then
		// contained element pos should be retained
		assertThat(postMoveShapePosition).isEqualTo(preMoveShapePosition);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddLaneFeatureTest.testAddOnToPool.bpmn")
	public void testAddOnToParticipantRetainsContainedLabelPositions() throws Exception {

		// given participant
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		ContainerShape childShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		ContainerShape labelShape = GraphicsUtil.getLabelShape(childShape, diagram);
		
		IRectangle preMoveLabelShapePosition = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// when
		// lane is added to it
		addLane(diagramTypeProvider)
			.toContainer(containerShape)
			.execute();
		
		IRectangle postMoveLabelShapePosition = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// then
		// label position of contained element pos should be retained
		assertThat(postMoveLabelShapePosition).isEqualTo(preMoveLabelShapePosition);
	}
	

	

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddLaneFeatureTest.testAddOnToLane.bpmn")
	public void testAddOnToLaneChangesContainer() throws Exception {

		// given participant
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		ContainerShape childShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		// when
		// lane is added to it
		addLane(diagramTypeProvider)
			.toContainer(containerShape)
			.execute();
		
		ContainerShape newChildContainer = childShape.getContainer();
		
		// then
		// lane should be a container shape between participant and child shape
		assertThat(containerShape).hasChild(newChildContainer);
		assertThat(newChildContainer).isLinkedTo(elementOfType(Lane.class));
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddLaneFeatureTest.testAddOnToLane.bpmn")
	public void testAddOnToLaneRetainsContainedEventPosition() throws Exception {

		// given participant
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		ContainerShape childShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		IRectangle preMoveShapePosition = LayoutUtil.getAbsoluteBounds(childShape);
		
		// when
		// lane is added to it
		addLane(diagramTypeProvider)
			.toContainer(containerShape)
			.execute();
		
		IRectangle postMoveShapePosition = LayoutUtil.getAbsoluteBounds(childShape);
		
		// then
		// contained element pos should be retained
		assertThat(postMoveShapePosition).isEqualTo(preMoveShapePosition);
	}
}
