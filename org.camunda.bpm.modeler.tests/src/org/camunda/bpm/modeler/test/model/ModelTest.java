package org.camunda.bpm.modeler.test.model;

import static org.camunda.bpm.modeler.test.util.TestHelper.transactionalExecute;
import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;

import org.camunda.bpm.modeler.core.model.Bpmn2ModelerFactory;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.runtime.engine.model.CallActivity;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.TestHelper;
import org.camunda.bpm.modeler.test.util.TestHelper.ModelResources;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Process;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * 
 * @author nico.rehwaldt
 */
public class ModelTest extends AbstractImportBpmnModelTest {

	@Test
	public void shouldImportCallActivityWithoutExtension() throws Exception {
		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/model/ModelTest.callActivity.bpmn");
		Resource resource = resources.getResource();
		
		// when
		final EObject element = resource.getEObject("CallActivity_1");
		
		// then
		assertThat(element)
			.isInstanceOf(CallActivity.class);
	}

	@Test
	public void shouldImportCallActivityWithExtension() throws Exception {
		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/model/ModelTest.callActivityWithExtension.bpmn");
		Resource resource = resources.getResource();
		
		// when
		final EObject element = resource.getEObject("CallActivity_1");
		
		// then
		assertThat(element)
			.isInstanceOf(CallActivity.class);
	}
	
	@Test
	public void shouldExportCallActivityExtensionModel() throws Exception {
		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/model/ModelTest.callActivity.bpmn");
		final Resource resource = resources.getResource();
		
		// assume
		final EObject element = resource.getEObject("CallActivity_1");
		
		assertThat(element)
			.isInstanceOf(CallActivity.class);
		
		// when
		final CallActivity callActivity = (CallActivity) element;
		
		transactionalExecute(resources, new Runnable() {
			
			@Override
			public void run() {
				callActivity.setCalledElementBinding("deployment");
			}
		});

		// then
		String resultXML = TestHelper.saveToString(resource);
		
		assertThat(resultXML)
			.contains("camunda:calledElementBinding=\"deployment\"");
	}
	
	@Test
	public void shouldCreateElementOnEmptyModel() throws Exception {
		
		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/model/ModelTest.emptyDiagram.bpmn");
		final Resource resource = resources.getResource();
		
		// assume
		DocumentRoot documentRoot = (DocumentRoot) resource.getContents().get(0);
		final Definitions definitions = documentRoot.getDefinitions();
		
		// when
		transactionalExecute(resources, new Runnable() {
			@Override
			public void run() {
				
				Bpmn2ModelerFactory eobjectFactory = Bpmn2ModelerFactory.getInstance();
				
				org.eclipse.bpmn2.Process process = (Process) eobjectFactory.create(Bpmn2Package.eINSTANCE.getProcess());
				process.setId("Process_1");
				
				CallActivity callActivity = (CallActivity) eobjectFactory.create(Bpmn2Package.eINSTANCE.getCallActivity());
				callActivity.setId("CallActivity_1");
				
				process.getFlowElements().add(callActivity);
				definitions.getRootElements().add(process);
			}
		});
		
		// then
		String resultXML = TestHelper.saveToString(resource);

		assertThat(resultXML)
			.contains("callActivity id=\"CallActivity_1\"")
			.contains("process id=\"Process_1\"");
	}
}
