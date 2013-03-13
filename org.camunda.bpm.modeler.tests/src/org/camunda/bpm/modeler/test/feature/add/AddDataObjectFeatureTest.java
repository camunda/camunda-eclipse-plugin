package org.camunda.bpm.modeler.test.feature.add;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.camunda.bpm.modeler.test.util.operations.AddDataObjectOperation.addDataObject;
import static org.camunda.bpm.modeler.test.util.operations.MoveShapeOperation.move;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class AddDataObjectFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFeatureTestBase.testAddToDiagram.bpmn")
	public void testAddToDiagram() throws Exception {

		// given empty diagram
		
		// when
		// element is added to it
		addDataObject(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		Shape shape = diagram.getChildren().get(1);
		Shape labelShape = LabelUtil.getLabelShape(shape, diagram);
		
		// then
		// diagram should contain the element and a label
		assertThat(diagram)
			.hasContainerShapeChildCount(2);
		
		assertThat(shape)
			.isLinkedTo(elementOfType(DataObject.class));
		
		assertThat(labelShape)
			.isNotNull()
			.isContainedIn(diagram);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFeatureTestBase.testAddToEmptyParticipant.bpmn")
	public void testAddToParticipant() throws Exception {

		// given
		// diagram with participant
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		
		// when
		// element is added to it
		addDataObject(diagramTypeProvider)
			.toContainer(containerShape)
			.execute();

		EList<Shape> children = containerShape.getChildren();
		
		Shape shape = children.get(children.size() - 1); // last child should be the store
		Shape labelShape = LabelUtil.getLabelShape(shape, diagram);
		
		// then
		// the container should contain the element
		assertThat(diagram)
			.hasContainerShapeChildCount(2);
		
		assertThat(shape)
			.isLinkedTo(elementOfType(DataObject.class));
		
		// and the label should be contained in the diagram
		assertThat(labelShape)
			.isNotNull()
			.isContainedIn(diagram);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFeatureTestBase.testAddToEmptyParticipant.bpmn")
	public void testAddToDiagramWithParticipant() throws Exception {

		// given
		// diagram with participant
		
		// when
		// element is added to diagram
		addDataObject(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		EList<Shape> children = diagram.getChildren();
		
		Shape shape = children.get(children.size() - 1); // last child should be the store
		Shape labelShape = LabelUtil.getLabelShape(shape, diagram);
		
		// then
		// the container should contain the element, the pool and the elements label
		assertThat(diagram)
			.hasContainerShapeChildCount(3);
		
		assertThat(shape)
			.isLinkedTo(elementOfType(DataObject.class));
		
		// and the label should be contained in the diagram
		assertThat(labelShape)
			.isNotNull()
			.isContainedIn(diagram);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFeatureTestBase.testAddToEmptyParticipant.bpmn")
	public void testLabelMoveAfterAddToParticipant() throws Exception {

		// given
		// diagram with participant
		// with element freshly added
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		
		addDataObject(diagramTypeProvider)
			.toContainer(containerShape)
			.execute();

		EList<Shape> children = containerShape.getChildren();
		
		Shape shape = children.get(children.size() - 1); // last child should be the element
		Shape labelShape = LabelUtil.getLabelShape(shape, diagram);
		
		IRectangle oldShapeBounds = LayoutUtil.getAbsoluteBounds(labelShape);
		
		// when
		// element label is moved
		
		move(labelShape, diagramTypeProvider)
			.by(20, 20)
			.execute();
		
		// then
		// movement should have actually happened
		
		assertThat(labelShape)
			.movedBy(point(20, 20), oldShapeBounds)
			.isContainedIn(diagram);
	}
}
