package org.camunda.bpm.modeler.test.feature;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.test.AbstractEditorTest;
import org.junit.Before;

public class AbstractFeatureTest extends AbstractEditorTest {
	
	protected ModelImport modelImport;

	@Before
	public void importModel() {
		
		if (isEditorLoaded()) {
			modelImport = new ModelImport(getDiagramTypeProvider(), getResource());
			modelImport.execute();
		}
	}
}
