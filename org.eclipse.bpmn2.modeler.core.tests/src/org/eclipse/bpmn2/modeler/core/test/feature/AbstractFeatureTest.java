package org.eclipse.bpmn2.modeler.core.test.feature;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.AbstractBpmnEditorTest;
import org.junit.Before;

public class AbstractFeatureTest extends AbstractBpmnEditorTest {
	
	protected ModelImport modelImport;

	@Before
	public void importModel() {
		modelImport = new ModelImport(diagramTypeProvider, resource);
		modelImport.execute();
	}
	
	

}
