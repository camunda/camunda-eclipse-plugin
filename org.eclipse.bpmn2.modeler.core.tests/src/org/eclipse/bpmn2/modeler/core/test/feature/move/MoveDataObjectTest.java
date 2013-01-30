package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class MoveDataObjectTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/AbstractMoveDataItemTest.testBase.bpmn")
	public void testRetrieveLabel() {
		Shape shape = Util.findShapeByBusinessObjectId(diagram, "DataObject_1");
		Shape label = GraphicsUtil.getLabelShape(shape, diagram);
		
		// then label should be retrievable via the get label utility
		assertThat(label).isNotNull();
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/AbstractMoveDataItemTest.testBase.bpmn")
	public void testSameContainer() {
		Shape shape = Util.findShapeByBusinessObjectId(diagram, "DataObject_1");
		Shape label = GraphicsUtil.getLabelShape(shape, diagram);
		
		// label and shape should have same container
		assertThat(label.eContainer()).isEqualTo(shape.eContainer());
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/AbstractMoveDataItemTest.testBase.bpmn")
	public void testMoveLabelOnShape() {
		Shape shape = Util.findShapeByBusinessObjectId(diagram, "DataObject_1");
		Shape label = GraphicsUtil.getLabelShape(shape, diagram);
		
		move(label, diagramTypeProvider)
			.by(0, -20)
			.toContainer((ContainerShape) shape)
			.execute();
		
		// operation should be rejected
		assertThat(label).isNotContainedIn(shape);
	}
}
