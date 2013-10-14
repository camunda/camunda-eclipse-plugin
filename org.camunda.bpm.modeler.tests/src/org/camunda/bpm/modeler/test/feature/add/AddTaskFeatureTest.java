package org.camunda.bpm.modeler.test.feature.add;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.camunda.bpm.modeler.test.util.operations.AddTaskOperation.addTask;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
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

public class AddTaskFeatureTest extends AbstractFeatureTest {
	
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
			.isLinkedTo(elementOfType(Task.class))
			.position()
				.isEqualTo(210 - 50, 139 - 40); // add context (x/y) is new shape midpoint
		
		assertThat(targetConnection)
			.hasExactBendpoints(expectedBeforeSplitBendpoints);
		
		assertThat(postSplitStartShape)
			.isSameAs(preSplitStart);
		
		assertThat(postSplitEndShape)
			.outgoingConnectionTo(preSplitConnectionEnd)
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

		List<Point> expectedBeforeSplitBendpoints = preSplitBendpoints.subList(0, 2);
		List<Point> expectedAfterSplitBendpoints = preSplitBendpoints.subList(5, preSplitBendpoints.size());
		
		Shape preSplitStart = (Shape) targetConnection.getStart().getParent();
		Shape preSplitConnectionEnd = (Shape) targetConnection.getEnd().getParent();
		
		// when
		// element is added to it
		addTask(diagramTypeProvider)
			.toConnection(targetConnection)
			.atLocation(260, 183)
			.toContainer(parent)
			.execute();
		
		// then
		// connection should be split and 
		Shape postSplitEndShape = (Shape) targetConnection.getEnd().getParent();
		Shape postSplitStartShape = (Shape) targetConnection.getStart().getParent();

		assertThat(postSplitEndShape)
			.isLinkedTo(elementOfType(Task.class))
			.position()
				.isEqualTo(parentBounds.getX() + 260 - 50, parentBounds.getY() + 183 - 40); // add context (x/y) is new shape midpoint
		
		assertThat(targetConnection)
			.hasExactBendpoints(expectedBeforeSplitBendpoints);
		
		assertThat(postSplitStartShape)
			.isSameAs(preSplitStart);
		
		assertThat(postSplitEndShape)
			.outgoingConnectionTo(preSplitConnectionEnd)
				.hasExactBendpoints(expectedAfterSplitBendpoints);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testLocation.bpmn")
	public void testAddToDiagramOutsideTheDiagram() throws Exception {

		// when
		// element is added to it
		addTask(diagramTypeProvider)
			.atLocation(133, 1299)
			.toContainer(diagram)
			.execute();
		
		// get the added task
		BPMNShape addedTask = (BPMNShape) Util.findBpmnShapeByBusinessObjectId(diagram, "Task_1");
		
		// then
		assertThat(addedTask)
			.bounds()
			.x()
				.isEqualTo(133 - 50);

		assertThat(addedTask)
			.bounds()
			.y()
				.isEqualTo(1299 - 40);
	}	
}
