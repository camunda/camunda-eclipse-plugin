package org.camunda.bpm.modeler.test.feature.add;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.camunda.bpm.modeler.test.util.operations.AddCallActivityOperation.addCallActivity;
import static org.camunda.bpm.modeler.test.util.operations.AddSubProcessOperation.addSubProcess;
import static org.camunda.bpm.modeler.test.util.operations.AddTaskOperation.addTask;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.BoxingStrategy;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author nico.rehwaldt
 */
public class AddSubProcessFeatureTest extends AbstractFeatureTest {
	
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
		addTask(diagramTypeProvider)
			.toConnection(targetConnection)
			.atLocation(210, 139)
			.toContainer(diagram)
			.execute();
		
		// then
		// connection should be split and 
		Shape postSplitEndShape = (Shape) targetConnection.getEnd().getParent();
		Shape postSplitStartShape = (Shape) targetConnection.getStart().getParent();

		assertThat(postSplitEndShape)
			.isLinkedTo(elementOfType(Task.class));
		
		assertThat(targetConnection)
			.hasExactBendpoints(expectedBeforeSplitBendpoints);
		
		assertThat(postSplitStartShape)
			.isSameAs(preSplitStart);
		
		assertThat(postSplitEndShape)
			.outgoingConnectionTo(preSplitConnectionEnd)
				.hasExactBendpoints(expectedAfterSplitBendpoints);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddSubProcessToParticipantAdjustLocationAndRetainSize() {
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "Participant_1");
		
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addSubProcess(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) participantShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 200 / 2, 
						  addPosition.getY() - 150 / 2, 200, 200), 
						  participantBounds, 10, BoxingStrategy.POSITION_AND_SIZE);
		
		Point expectedPosition = point(
				participantBounds.getX() + 40, 
				participantBounds.getY() + box.getY());
		
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_2");
		
		assertThat(subProcessShape)
			.isContainedIn(participantShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(subProcessShape)
			.bounds()
			.width()
				.isEqualTo(200);
	
		assertThat(subProcessShape)
			.bounds()
			.height()
				.isEqualTo(150);	
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddSubProcessToParticipantAdjustLocationAndRetainSize_Cornercase() {
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "Participant_1");
		
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		Point addPosition = point(131, 41);
		
		// when
		addSubProcess(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) participantShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 200 / 2, 
						  addPosition.getY() - 150 / 2, 200, 150), 
						  participantBounds, 10, BoxingStrategy.POSITION_AND_SIZE);
		
		Point expectedPosition = point(
				participantBounds.getX() + 40, 
				participantBounds.getY() + box.getY());
		
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_2");
		
		assertThat(subProcessShape)
			.isContainedIn(participantShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(subProcessShape)
			.bounds()
			.width()
				.isEqualTo(200);
	
		assertThat(subProcessShape)
			.bounds()
			.height()
				.isEqualTo(150);	
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddSubProcessToParticipantAdjustLocationAndSize() {
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "Participant_2");
		
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addSubProcess(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) participantShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 200 / 2, 
						  addPosition.getY() - 150 / 2, 200, 150), 
						  participantBounds, 10, BoxingStrategy.POSITION_AND_SIZE);
		
		Point expectedPosition = point(
				participantBounds.getX() + 40, 
				participantBounds.getY() + box.getY());
		
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_2");
		
		assertThat(subProcessShape)
			.isContainedIn(participantShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(subProcessShape)
			.bounds()
			.width()
				.isEqualTo(box.getWidth());
	
		assertThat(subProcessShape)
			.bounds()
			.height()
				.isEqualTo(box.getHeight());

		assertThat(subProcessShape)
			.bounds()
			.height()
				.isLessThan(150);			
		
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddSubProcessToLaneAdjustLocationAndRetainSize() {
		// given
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addSubProcess(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) laneShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 200 / 2, 
						  addPosition.getY() - 150 / 2, 200, 150), 
						  laneBounds, 10, BoxingStrategy.POSITION_AND_SIZE);
		
		Point expectedPosition = point(
				laneBounds.getX() + 25, 
				laneBounds.getY() + box.getY());
		
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_2");
		
		assertThat(subProcessShape)
			.isContainedIn(laneShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(subProcessShape)
			.bounds()
			.width()
				.isEqualTo(200);
	
		assertThat(subProcessShape)
			.bounds()
			.height()
				.isEqualTo(150);
		
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddSubProcessToLaneAdjustLocationAndRetainSize_Cornercase() {
		// given
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		
		Point addPosition = point(116, 30);
		
		// when
		addSubProcess(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) laneShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 200 / 2, 
						  addPosition.getY() - 150 / 2, 200, 150), 
						  laneBounds, 10, BoxingStrategy.POSITION_AND_SIZE);
		
		Point expectedPosition = point(
				laneBounds.getX() + 25, 
				laneBounds.getY() + box.getY());
		
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_2");
		
		assertThat(subProcessShape)
			.isContainedIn(laneShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(subProcessShape)
			.bounds()
			.width()
				.isEqualTo(200);
	
		assertThat(subProcessShape)
			.bounds()
			.height()
				.isEqualTo(150);
		
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddSubProcessToLaneAdjustLocationAndSize() {
		// given
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_2");
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addSubProcess(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) laneShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 200 / 2, 
						  addPosition.getY() - 150 / 2, 200, 150), 
						  laneBounds, 10, BoxingStrategy.POSITION_AND_SIZE);
		
		Point expectedPosition = point(
				laneBounds.getX() + 25, 
				laneBounds.getY() + box.getY());
		
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_2");
		
		assertThat(subProcessShape)
			.isContainedIn(laneShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(subProcessShape)
			.bounds()
			.width()
				.isEqualTo(box.getWidth());
	
		assertThat(subProcessShape)
			.bounds()
			.height()
				.isEqualTo(box.getHeight());

		assertThat(subProcessShape)
			.bounds()
			.height()
				.isLessThan(150);			
		
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddSubProcessToSubProcessAdjustLocationAndSize() {
		// given
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		
		IRectangle subProcessBounds = LayoutUtil.getAbsoluteBounds(subProcessShape);
		
		Point addPosition = point(10, 10);
		
		// when
		addSubProcess(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) subProcessShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 200 / 2, 
						  addPosition.getY() - 150 / 2, 200, 150), 
						  subProcessBounds, 10, BoxingStrategy.POSITION_AND_SIZE);
		
		Point expectedPosition = point(
				subProcessBounds.getX() + box.getX(), 
				subProcessBounds.getY() + box.getY());
		
		Shape subProcessShape1 = Util.findShapeByBusinessObjectId(diagram, "SubProcess_2");
		
		assertThat(subProcessShape1)
			.isContainedIn(subProcessShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(subProcessShape1)
			.bounds()
			.width()
				.isEqualTo(box.getWidth());
		
		assertThat(subProcessShape1)
			.bounds()
			.width()
				.isLessThan(200);		
	
		assertThat(subProcessShape1)
			.bounds()
			.height()
				.isEqualTo(box.getHeight());

		assertThat(subProcessShape1)
			.bounds()
			.height()
				.isLessThan(150);			
		
	}
	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFeatureTestBase.testAddToDiagram.bpmn")
	public void testShapeExpanded() throws Exception {

		// given
		Point addPosition = point(10, 10);

		// when
		addSubProcess(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer(getDiagram())
			.execute();

		// then
		BPMNShape subProcessDiShape = (BPMNShape) Util.findBpmnShapeByBusinessObjectId(diagram, "SubProcess_1");

		assertThat(subProcessDiShape.isIsExpanded()).isTrue();

	}
}
