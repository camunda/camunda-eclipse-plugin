package org.camunda.bpm.modeler.test.model;

import static org.camunda.bpm.modeler.test.util.TestHelper.transactionalExecute;
import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Set;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.NonTransactional;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.ui.internal.services.GraphitiUiInternal;
import org.junit.Test;

/**
 * Test case testing a simple editor create diagram -> save roundtrip
 * 
 * @author nico.rehwaldt
 */
public class CreateSaveRoundtripTest extends AbstractImportBpmnModelTest {
	
	@Test
	@NonTransactional
	@DiagramResource("org/camunda/bpm/modeler/test/model/ModelTest.emptyDiagram.bpmn")
	public void shouldCreateElementOnEmptyModel() throws Exception {

		// this test ensures that no cryptical
		//
		// 	java.lang.IllegalStateException: Cannot modify resource set without a write transaction
		// 
		// is thrown when wrong model resources are created during BPMN 2.0 import

		transactionalExecute(getEditingDomain(), new Runnable() {
			@Override
			public void run() {
				ModelImport importer = createModelImport();
				importer.execute();
			}
		});
		
		// when
		// edit
		transactionalExecute(getEditingDomain(), new Runnable() {
			@Override
			public void run() {
				
				// given
				addStartEvent(getDiagramTypeProvider())
					.atLocation(20, 20)
					.toContainer(getDiagram())
					.execute();
			}
		});
		
		// and save
		saveDiagram();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		FileUtil.transferStreams(
				new FileInputStream("target/org/camunda/bpm/modeler/test/model/ModelTest.emptyDiagram.bpmn"), 
				outputStream, "/FOOO", new NullProgressMonitor());
		
		String exportXml = outputStream.toString("UTF-8");
		
		// then
		assertThat(exportXml)
			.contains("startEvent")
			.contains("process");
	}

	private Set<Resource> saveDiagram() {
		return GraphitiUiInternal.getEmfService().save(getEditingDomain());
	}
}
