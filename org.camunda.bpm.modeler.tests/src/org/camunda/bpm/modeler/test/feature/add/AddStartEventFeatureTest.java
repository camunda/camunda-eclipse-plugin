package org.camunda.bpm.modeler.test.feature.add;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.camunda.bpm.modeler.test.util.operations.AddSubProcessOperation.addSubProcess;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.BoxingStrategy;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class AddStartEventFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testSplitConnection.bpmn")
	public void testAddToDiagramSplitConnection() throws Exception {

		// given diagram
		// with event, task and a label
		FreeFormConnection targetConnection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		List<Point> preSplitBendpoints = new ArrayList<Point>(targetConnection.getBendpoints());

		List<Point> expectedBeforeSplitBendpoints = preSplitBendpoints.subList(0, 1);
		List<Point> expectedAfterSplitBendpoints = preSplitBendpoints.subList(2, preSplitBendpoints.size());
		
		Shape preSplitStart = (Shape) targetConnection.getStart().getParent();
		Shape preSplitConnectionEnd = (Shape) targetConnection.getEnd().getParent();
		
		// when
		// element is added to it
		addStartEvent(diagramTypeProvider)
			.toConnection(targetConnection)
			.atLocation(210, 139)
			.toContainer(diagram)
			.execute();
		
		// then
		// connection should be split and 
		Shape postSplitEndShape = (Shape) targetConnection.getEnd().getParent();
		Shape postSplitStartShape = (Shape) targetConnection.getStart().getParent();

		assertThat(postSplitEndShape)
			.isEqualTo(preSplitConnectionEnd);
		
		assertThat(postSplitStartShape)
			.isLinkedTo(elementOfType(StartEvent.class))
			.position()
				.isEqualTo(210 - 18, 139 - 18); // add context (x/y) is new shape midpoint
		
		assertThat(targetConnection)
			.hasExactBendpoints(expectedAfterSplitBendpoints);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testSplitConnectionParticipant.bpmn")
	public void testAddToParticipantSplitConnection() throws Exception {

		// given diagram
		// with event, task and a label
		FreeFormConnection targetConnection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		ContainerShape parent = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_3");
		
		IRectangle parentBounds = LayoutUtil.getAbsoluteBounds(parent);
		
		List<Point> preSplitBendpoints = new ArrayList<Point>(targetConnection.getBendpoints());

		List<Point> expectedAfterSplitBendpoints = preSplitBendpoints.subList(3, preSplitBendpoints.size());
		
		Shape preSplitConnectionEnd = (Shape) targetConnection.getEnd().getParent();
		
		// when
		// element is added to it
		addStartEvent(diagramTypeProvider)
			.toConnection(targetConnection)
			.atLocation(260, 183)
			.toContainer(parent)
			.execute();
		
		// then
		// connection should be split and 
		Shape postSplitEndShape = (Shape) targetConnection.getEnd().getParent();
		Shape postSplitStartShape = (Shape) targetConnection.getStart().getParent();

		assertThat(postSplitEndShape)
			.isEqualTo(preSplitConnectionEnd);
		
		assertThat(postSplitStartShape)
			.isLinkedTo(elementOfType(StartEvent.class))
			.position()
				.isEqualTo(parentBounds.getX() + 260 - 18, parentBounds.getY() + 183 - 18); // add context (x/y) is new shape midpoint
		
		assertThat(targetConnection)
			.hasExactBendpoints(expectedAfterSplitBendpoints);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddStartEventToParticipantAdjustLocationAndRetainSize() {
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "Participant_1");
		
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addStartEvent(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) participantShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 36 / 2, 
						  addPosition.getY() - 36 / 2, 36, 36), 
						  participantBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				participantBounds.getX() + 40, 
				participantBounds.getY() + box.getY());
		
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		assertThat(startEventShape)
			.isContainedIn(participantShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(startEventShape)
			.bounds()
			.width()
				.isEqualTo(36);
	
		assertThat(startEventShape)
			.bounds()
			.height()
				.isEqualTo(36);	
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddStartEventToParticipantAdjustLocationAndRetainSize_Cornercase() {
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "Participant_1");
		
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		Point addPosition = point(49, 41);
		
		// when
		addStartEvent(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) participantShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 36 / 2, 
						  addPosition.getY() - 36 / 2, 36, 36), 
						  participantBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				participantBounds.getX() + 40, 
				participantBounds.getY() + box.getY());
		
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		assertThat(startEventShape)
			.isContainedIn(participantShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(startEventShape)
			.bounds()
			.width()
				.isEqualTo(36);
	
		assertThat(startEventShape)
			.bounds()
			.height()
				.isEqualTo(36);	
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddStartEventToSmallParticipantAdjustLocationAndRetainSize() {
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "Participant_2");
		
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addStartEvent(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) participantShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 36 / 2, 
						  addPosition.getY() - 36 / 2, 36, 36), 
						  participantBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				participantBounds.getX() + 40, 
				participantBounds.getY() + box.getY());
		
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		assertThat(startEventShape)
			.isContainedIn(participantShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(startEventShape)
			.bounds()
			.width()
				.isEqualTo(36);
	
		assertThat(startEventShape)
			.bounds()
			.height()
				.isEqualTo(36);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddStartEventToLaneAdjustLocationAndRetainSize() {
		// given
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addStartEvent(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) laneShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 36 / 2, 
						  addPosition.getY() - 36 / 2, 36, 36), 
						  laneBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				laneBounds.getX() + 25, 
				laneBounds.getY() + box.getY());
		
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		assertThat(startEventShape)
			.isContainedIn(laneShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(startEventShape)
			.bounds()
			.width()
				.isEqualTo(36);
	
		assertThat(startEventShape)
			.bounds()
			.height()
				.isEqualTo(36);
		
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddStartEventToLaneAdjustLocationAndRetainSize_Cornercase() {
		// given
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		
		Point addPosition = point(44, 30);
		
		// when
		addStartEvent(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) laneShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 36 / 2, 
						  addPosition.getY() - 36 / 2, 36, 36), 
						  laneBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				laneBounds.getX() + 26, 
				laneBounds.getY() + box.getY());
		
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		assertThat(startEventShape)
			.isContainedIn(laneShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(startEventShape)
			.bounds()
			.width()
				.isEqualTo(36);
	
		assertThat(startEventShape)
			.bounds()
			.height()
				.isEqualTo(36);
		
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddStartEventToSmallLaneAdjustLocationAndRetainSize() {
		// given
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_2");
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addStartEvent(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) laneShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 36 / 2, 
						  addPosition.getY() - 36 / 2, 36, 36), 
						  laneBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				laneBounds.getX() + 25, 
				laneBounds.getY() + box.getY());
		
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		assertThat(startEventShape)
			.isContainedIn(laneShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(startEventShape)
			.bounds()
			.width()
				.isEqualTo(36);
	
		assertThat(startEventShape)
			.bounds()
			.height()
				.isEqualTo(36);

	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddStartEventToSubProcessAdjustLocationAndRetainSize() {
		// given
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		
		IRectangle subProcessBounds = LayoutUtil.getAbsoluteBounds(subProcessShape);
		
		Point addPosition = point(10, 10);
		
		// when
		addStartEvent(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) subProcessShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 36 / 2, 
						  addPosition.getY() - 36 / 2, 36, 36), 
						  subProcessBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				subProcessBounds.getX() + box.getX(), 
				subProcessBounds.getY() + box.getY());
		
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		assertThat(startEventShape)
			.isContainedIn(subProcessShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(startEventShape)
			.bounds()
			.width()
				.isEqualTo(36);
		
		assertThat(startEventShape)
			.bounds()
			.height()
				.isEqualTo(36);

	}
}
