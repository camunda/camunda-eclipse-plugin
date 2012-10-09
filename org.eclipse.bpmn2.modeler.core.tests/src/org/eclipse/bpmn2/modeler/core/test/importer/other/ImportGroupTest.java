package org.eclipse.bpmn2.modeler.core.test.importer.other;

import static org.eclipse.bpmn2.modeler.core.test.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.Group;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class ImportGroupTest extends AbstractImportBpmn2ModelTest {
	
	@Test
	@DiagramResource
	public void testImportGroup() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		EList<Shape> children = diagram.getChildren();
		assertThat(children).hasSize(1);
		
		Shape shape = children.get(0);
		assertThat(shape).isLinkedTo(elementOfType(Group.class).identifiedBy("Group_1"));
	}

	@Test
	@DiagramResource
	public void testImportGroupInProcessWithLanes() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		EList<Shape> children = diagram.getChildren();
		assertThat(children).hasSize(2);
		
		Shape shape = children.get(1);
		assertThat(shape).isLinkedTo(elementOfType(Group.class).identifiedBy("Group_1"));
	}
}
