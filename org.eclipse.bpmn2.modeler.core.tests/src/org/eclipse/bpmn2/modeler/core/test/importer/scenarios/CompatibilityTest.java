package org.eclipse.bpmn2.modeler.core.test.importer.scenarios;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class CompatibilityTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testAdonisDiImport() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "_16951");
		Shape startShape = Util.findShapeByBusinessObjectId(diagram, "_16770");
		Shape endShape = Util.findShapeByBusinessObjectId(diagram, "_16773");

		// when importing a diagram from, e.g. from adonis
		// which exports the endpoints of connections as the center of the connected shape
		// assert that the chopbox anchor is used for the diagram
		assertThat(connection.getStart()).isSameAs(LayoutUtil.getCenterAnchor(startShape));
		assertThat(connection.getEnd()).isSameAs(LayoutUtil.getCenterAnchor(endShape));
	}
	
	@Test
	@DiagramResource
	public void testLabelNoBoundsImport() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		Shape event = Util.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		Shape label = GraphicsUtil.getLabel(event, diagram);
		
		// when moving a bpmn element with a label and no bounds attached
		// there should be no null pointer exception
		
		assertThat(label).isNotNull();
		
		move(label, diagramTypeProvider)
			.by(20, 100)
			.execute();
	}
}
