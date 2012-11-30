package org.eclipse.bpmn2.modeler.core.test.importer.scenarios;

import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class CompatibilityTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testAdonisDiExportCompatibility() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "_16951");
		Shape startShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "_16770");
		Shape endShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "_16773");

		// when importing a diagram from, e.g. from adonis
		// which exports the endpoints of connections as the center of the connected shape
		// assert that the chopbox anchor is used for the diagram
		assertThat(connection.getStart()).isSameAs(LayoutUtil.getCenterAnchor(startShape));
		assertThat(connection.getEnd()).isSameAs(LayoutUtil.getCenterAnchor(endShape));
	}
}
