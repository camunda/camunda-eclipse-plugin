package org.camunda.bpm.modeler.test.feature.add;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.AddExclusiveGatewayOperation.addExclusiveGateway;
import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.BoxingStrategy;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class AddGatewayFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testLocation.bpmn")
	public void testAddToDiagramOutsideTheDiagram() throws Exception {

		// when
		// element is added to it
		addExclusiveGateway(diagramTypeProvider)
			.atLocation(133, 1299)
			.toContainer(diagram)
			.execute();
		
		// get the added task
		BPMNShape addedGateway = (BPMNShape) Util.findBpmnShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		// then
		assertThat(addedGateway)
			.bounds()
			.x()
				.isEqualTo(133 - 25);

		assertThat(addedGateway)
			.bounds()
			.y()
				.isEqualTo(1299 - 25);
	}	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddExclusiveGatewayToParticipantAdjustLocationAndRetainSize() {
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "Participant_1");
		
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addExclusiveGateway(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) participantShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 50 / 2, 
						  addPosition.getY() - 50 / 2, 50, 50), 
						  participantBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				participantBounds.getX() + 40, 
				participantBounds.getY() + box.getY());
		
		Shape exclusiveGatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		assertThat(exclusiveGatewayShape)
			.isContainedIn(participantShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.width()
				.isEqualTo(50);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.height()
				.isEqualTo(50);	
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddExclusiveGatewayToParticipantAdjustLocationAndRetainSize_Cornercase() {
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "Participant_1");
		
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		Point addPosition = point(56, 41);
		
		// when
		addExclusiveGateway(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) participantShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 50 / 2, 
						  addPosition.getY() - 50 / 2, 50, 50), 
						  participantBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				participantBounds.getX() + 40, 
				participantBounds.getY() + box.getY());
		
		Shape exclusiveGatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		assertThat(exclusiveGatewayShape)
			.isContainedIn(participantShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.width()
				.isEqualTo(50);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.height()
				.isEqualTo(50);	
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddExclusiveGatewayToSmallParticipantAdjustLocationAndRetainSize() {
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "Participant_2");
		
		IRectangle participantBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addExclusiveGateway(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) participantShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 50 / 2, 
						  addPosition.getY() - 50 / 2, 50, 50), 
						  participantBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				participantBounds.getX() + 40, 
				participantBounds.getY() + box.getY());
		
		Shape exclusiveGatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		assertThat(exclusiveGatewayShape)
			.isContainedIn(participantShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.width()
				.isEqualTo(50);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.height()
				.isEqualTo(50);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddExclusiveGatewayToLaneAdjustLocationAndRetainSize() {
		// given
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addExclusiveGateway(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) laneShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 50 / 2, 
						  addPosition.getY() - 50 / 2, 50, 50), 
						  laneBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				laneBounds.getX() + 25, 
				laneBounds.getY() + box.getY());
		
		Shape exclusiveGatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		assertThat(exclusiveGatewayShape)
			.isContainedIn(laneShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.width()
				.isEqualTo(50);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.height()
				.isEqualTo(50);
		
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddExclusiveGatewayToLaneAdjustLocationAndRetainSize_Cornercase() {
		// given
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_1");
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		
		Point addPosition = point(41, 30);
		
		// when
		addExclusiveGateway(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) laneShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 50 / 2, 
						  addPosition.getY() - 50 / 2, 50, 50), 
						  laneBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				laneBounds.getX() + 25, 
				laneBounds.getY() + box.getY());
		
		Shape exclusiveGatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		assertThat(exclusiveGatewayShape)
			.isContainedIn(laneShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.width()
				.isEqualTo(50);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.height()
				.isEqualTo(50);
		
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddExclusiveGatewayToSmallLaneAdjustLocationAndRetainSize() {
		// given
		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_2");
		
		IRectangle laneBounds = LayoutUtil.getAbsoluteBounds(laneShape);
		
		Point addPosition = point(20, 30);
		
		// when
		addExclusiveGateway(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) laneShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 50 / 2, 
						  addPosition.getY() - 50 / 2, 50, 50), 
						  laneBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				laneBounds.getX() + 25, 
				laneBounds.getY() + box.getY());
		
		Shape exclusiveGatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		assertThat(exclusiveGatewayShape)
			.isContainedIn(laneShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.width()
				.isEqualTo(50);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.height()
				.isEqualTo(50);

	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
	public void testAddExclusiveGatewayToSubProcessAdjustLocationAndRetainSize() {
		// given
		Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		
		IRectangle subProcessBounds = LayoutUtil.getAbsoluteBounds(subProcessShape);
		
		Point addPosition = point(10, 10);
		
		// when
		addExclusiveGateway(getDiagramTypeProvider())
			.atLocation(addPosition)
			.toContainer((ContainerShape) subProcessShape)
			.execute();
		
		// then
		IRectangle box = LayoutUtil.box(
				rectangle(addPosition.getX() - 50 / 2, 
						  addPosition.getY() - 50 / 2, 50, 50), 
						  subProcessBounds, 10, BoxingStrategy.POSITION);
		
		Point expectedPosition = point(
				subProcessBounds.getX() + box.getX(), 
				subProcessBounds.getY() + box.getY());
		
		Shape exclusiveGatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		assertThat(exclusiveGatewayShape)
			.isContainedIn(subProcessShape)
			.position()
				.isEqualTo(expectedPosition);
	
		assertThat(exclusiveGatewayShape)
			.bounds()
			.width()
				.isEqualTo(50);
		
		assertThat(exclusiveGatewayShape)
			.bounds()
			.height()
				.isEqualTo(50);

	}
	
}
