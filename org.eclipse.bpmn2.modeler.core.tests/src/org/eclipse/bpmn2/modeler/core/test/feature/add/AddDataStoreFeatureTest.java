package org.eclipse.bpmn2.modeler.core.test.feature.add;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddDataObjectOperation.addDataObject;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddDataStoreOperation.addDataStore;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;

import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class AddDataStoreFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddFeatureTestBase.testAddToDiagram.bpmn")
	public void testAddToDiagram() throws Exception {

		// given empty diagram
		
		// when
		// element is added to it
		addDataStore(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		Shape shape = diagram.getChildren().get(0);
		Shape labelShape = LabelUtil.getLabelShape(shape, diagram);
		
		// then
		// diagram should contain the element and a label
		assertThat(diagram)
			.hasContainerShapeChildCount(2);
		
		assertThat(shape)
			.isLinkedTo(elementOfType(DataStoreReference.class));
		
		assertThat(labelShape)
			.isNotNull()
			.isContainedIn(diagram);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddFeatureTestBase.testAddToEmptyParticipant.bpmn")
	public void testAddToParticipant() throws Exception {

		// given
		// diagram with participant
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		
		// when
		// element is added to it
		addDataStore(diagramTypeProvider)
			.toContainer(containerShape)
			.execute();

		EList<Shape> children = containerShape.getChildren();
		
		Shape shape = children.get(children.size() - 1); // last child should be the element
		Shape labelShape = LabelUtil.getLabelShape(shape, diagram);
		
		// then
		// the container should contain the element
		assertThat(diagram)
			.hasContainerShapeChildCount(2);
		
		assertThat(shape)
			.isLinkedTo(elementOfType(DataStoreReference.class));
		
		// and the label should be contained in the diagram
		assertThat(labelShape)
			.isNotNull()
			.isContainedIn(diagram);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddFeatureTestBase.testAddToEmptyParticipant.bpmn")
	public void testAddToDiagramWithParticipant() throws Exception {

		// given
		// diagram with participant
		
		// when
		// element is added to diagram
		addDataStore(diagramTypeProvider)
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
			.isLinkedTo(elementOfType(DataStoreReference.class));
		
		// and the label should be contained in the diagram
		assertThat(labelShape)
			.isNotNull()
			.isContainedIn(diagram);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddFeatureTestBase.testAddToEmptyParticipant.bpmn")
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
