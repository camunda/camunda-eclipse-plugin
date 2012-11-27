package org.eclipse.bpmn2.modeler.core.test.importer.sequenceflow;

import static org.junit.Assert.assertEquals;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.junit.Test;

public class ImportSequenceFlowTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportConditionalFlow() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}

	@Test
	@DiagramResource
	public void testImportUnconditionalFlow() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
	
	@Test
	@DiagramResource
	public void testImportDefaultFlow() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
	
	@Test
	@DiagramResource
	public void testPermanentAnchorPoints() {
		FreeFormConnection seq6Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_6");
//		seq6Connection.setStart(Anchor);
	}	
}
