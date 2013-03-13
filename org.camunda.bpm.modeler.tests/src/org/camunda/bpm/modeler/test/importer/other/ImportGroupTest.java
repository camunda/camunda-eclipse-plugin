package org.camunda.bpm.modeler.test.importer.other;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.eclipse.bpmn2.Group;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class ImportGroupTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportGroup() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		EList<Shape> children = diagram.getChildren();
		assertThat(diagram).hasContainerShapeChildCount(1);
		
		Shape shape = children.get(0);
		assertThat(shape).isLinkedTo(elementOfType(Group.class).identifiedBy("Group_1"));
	}

	@Test
	@DiagramResource
	public void testImportGroupInProcessWithLanes() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		EList<Shape> children = diagram.getChildren();
		assertThat(diagram).hasContainerShapeChildCount(2);
		
		Shape shape = children.get(1);
		assertThat(shape).isLinkedTo(elementOfType(Group.class).identifiedBy("Group_1"));
	}
}
