package org.eclipse.bpmn2.modeler.core.test.importer.sequenceflow;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.junit.Test;

public class ImportSequenceFlowTest extends AbstractImportBpmn2ModelTest {
	
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
}
