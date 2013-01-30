package org.eclipse.bpmn2.modeler.core.test.feature.add;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddPoolOperation.addPool;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.junit.Test;

public class AddParticipantFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource
	public void testAddPoolToProcessWithBoundaryEvent() throws Exception {
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		// assume task is contained in diagram
		assertThat(taskShape)
			.isContainedIn(diagram);
		
		// when
		// adding pool to diagram
		addPool(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		// then
		// check new parent of taskShape ...
		EObject taskContainer = taskShape.eContainer();
		
		assertThat(taskContainer)
			.isInstanceOf(ContainerShape.class);
		
		BaseElement businessObject = BusinessObjectUtil.getFirstBaseElement((PictogramElement) taskContainer);

		// pool should contain task
		// (AND no exception is thrown)
		assertThat(businessObject)
			.isInstanceOf(Participant.class);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddParticipantFeatureTest.testAddPoolToProcessWithBoundaryEvent.bpmn")
	public void testAddPoolRetailsElementPositioning() throws Exception {
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		ContainerShape boundaryEventShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		
		// assume task is contained in diagram
		assertThat(taskShape).isContainedIn(diagram);
		assertThat(boundaryEventShape).isContainedIn(diagram);

		IRectangle preMoveTaskBounds = LayoutUtil.getAbsoluteBounds(taskShape);
		IRectangle preMoveBoundaryBounds = LayoutUtil.getAbsoluteBounds(boundaryEventShape);
		
		// when
		// adding pool to diagram
		addPool(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		// then
		// positions should be equal
		IRectangle postMoveTaskBounds = LayoutUtil.getAbsoluteBounds(taskShape);
		IRectangle postMoveBoundaryBounds = LayoutUtil.getAbsoluteBounds(boundaryEventShape);

		assertThat(postMoveTaskBounds).isEqualTo(preMoveTaskBounds);
		assertThat(preMoveBoundaryBounds).isEqualTo(postMoveBoundaryBounds);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddParticipantFeatureTest.testAddPoolRetailsLabelPositioning.bpmn")
	public void testAddPoolRetailsEventLabelPositioning() throws Exception {
		ContainerShape eventShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");

		ContainerShape eventLabelShape = GraphicsUtil.getLabelShape(eventShape, diagram);

		IRectangle preMoveEventLabelBounds = LayoutUtil.getAbsoluteBounds(eventLabelShape);
		
		// when
		// adding pool to diagram
		addPool(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		// then
		// label position should be the same

		IRectangle postMoveEventLabelBounds = LayoutUtil.getAbsoluteBounds(eventLabelShape);
		
		assertThat(postMoveEventLabelBounds).isEqualTo(preMoveEventLabelBounds);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddParticipantFeatureTest.testAddPoolRetailsLabelPositioning.bpmn")
	public void testAddPoolLeavesGatewayLabelOnDiagram() throws Exception {
		ContainerShape gatewayShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		
		ContainerShape gatewayLabelShape = GraphicsUtil.getLabelShape(gatewayShape, diagram);

		// assume task is contained in diagram
		assertThat(gatewayShape)
			.isContainedIn(diagram);
		
		// when
		// adding pool to diagram
		addPool(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		// then
		// check new parent of taskShape ...
		EObject gatewayShapeContainer = gatewayShape.eContainer();

		// make sure the shapes are contained in the process
		assertThat(gatewayShapeContainer).isInstanceOf(ContainerShape.class);

		// but their labels are not
		assertThat(gatewayLabelShape).isContainedIn(diagram);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddParticipantFeatureTest.testAddPoolRetailsLabelPositioning.bpmn")
	public void testAddPoolLeavesEventLabelOnDiagram() throws Exception {
		ContainerShape eventShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		ContainerShape eventLabelShape = GraphicsUtil.getLabelShape(eventShape, diagram);
		
		// assume task is contained in diagram
		assertThat(eventShape)
			.isContainedIn(diagram);
		
		// when
		// adding pool to diagram
		addPool(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		// then
		// check new parent of taskShape ...
		EObject eventShapeContainer = eventShape.eContainer();

		// make sure the shapes are contained in the process
		assertThat(eventShapeContainer).isInstanceOf(ContainerShape.class);

		// but their labels are not
		assertThat(eventLabelShape).isContainedIn(diagram);
	}
}
