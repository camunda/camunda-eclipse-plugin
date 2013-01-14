package org.eclipse.bpmn2.modeler.core.test.feature;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.AbstractEditorTest;
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
