package org.camunda.bpm.modeler.test.core.utils;

import static org.camunda.bpm.modeler.test.util.Util.findBusinessObjectById;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.fox.FailedJobRetryTimeCycleType;
import org.camunda.bpm.modeler.runtime.engine.model.fox.FoxFactory;
import org.camunda.bpm.modeler.runtime.engine.model.fox.FoxPackage;
import org.camunda.bpm.modeler.runtime.engine.model.util.ModelResourceFactoryImpl;
import org.camunda.bpm.modeler.test.feature.AbstractNonTransactionalFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.junit.Test;

/**
 * Model util test
 * 
 * @author nico.rehwaldt
 */
public class ExtensionUtilTest extends AbstractNonTransactionalFeatureTest {

	static final FoxFactory foxFactory = FoxFactory.eINSTANCE;
	static final FoxPackage foxPackage = FoxPackage.eINSTANCE;
	
	static final ModelFactory modelFactory = ModelFactory.eINSTANCE;
	static final ModelPackage modelPackage = ModelPackage.eINSTANCE;
	
	static final EStructuralFeature ASYNC_FEATURE = modelPackage.getDocumentRoot_Async();
	static final EStructuralFeature RETRY_CYCLE_FEATURE = modelPackage.getDocumentRoot_FailedJobRetryTimeCycle();

	@Test
	@DiagramResource
	public void testGetExtensionAttributeValues() {
		CallActivity callActivity1 = (CallActivity) Util.findBusinessObjectById(diagram, "CallActivity_1");
		
		assertThat(callActivity1).isNotNull();
		
		List<ExtensionAttributeValue> callActivity1ExtensionValues = ExtensionUtil.getExtensionAttributeValues(callActivity1);
		
		assertThat(callActivity1ExtensionValues).hasSize(1);

		Task task1 = (Task) Util.findBusinessObjectById(diagram, "Task_1");
		
		List<ExtensionAttributeValue> task1ExtensionValues = ExtensionUtil.getExtensionAttributeValues(task1);
		
		assertThat(task1ExtensionValues).hasSize(0);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testGetExtensionAttributeValues.bpmn")
	public void testGetExtensionAttribute() {
		final CallActivity callActivity1 = findBusinessObjectById(diagram, "CallActivity_1", CallActivity.class);
		
		Object value = ExtensionUtil.getExtension(callActivity1, RETRY_CYCLE_FEATURE, "text");
		
		assertThat(value).isEqualTo("R3/PT10S");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testGetExtensionAttributeValues.bpmn")
	public void testAddExtension() {
		final ServiceTask serviceTask1 = findBusinessObjectById(diagram, "ServiceTask_1", ServiceTask.class);
		
		final FailedJobRetryTimeCycleType retryCycle = foxFactory.createFailedJobRetryTimeCycleType();
		retryCycle.setText("R3/PT10S");

		transactionalExecute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				ExtensionUtil.addExtension(serviceTask1, RETRY_CYCLE_FEATURE, retryCycle);
			}
		});
		
		Object value = ExtensionUtil.getExtension(serviceTask1, RETRY_CYCLE_FEATURE, "text");
		
		assertThat(value).isEqualTo("R3/PT10S");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testGetExtensionAttributeValues.bpmn")
	public void testUpdateExtension() {
		final CallActivity callActivity1 = findBusinessObjectById(diagram, "CallActivity_1", CallActivity.class);
		
		Object valueBefore = ExtensionUtil.getExtension(callActivity1, RETRY_CYCLE_FEATURE, "text");
		
		assertThat(valueBefore).isEqualTo("R3/PT10S");
		
		final FailedJobRetryTimeCycleType retryCycle = foxFactory.createFailedJobRetryTimeCycleType();
		retryCycle.setText("R3/PT200000S");
		
		transactionalExecute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				ExtensionUtil.updateExtension(callActivity1, RETRY_CYCLE_FEATURE, retryCycle);
			}
		});
		
		Object valueAfter = ExtensionUtil.getExtension(callActivity1, RETRY_CYCLE_FEATURE, "text");
		
		assertThat(valueAfter).isEqualTo("R3/PT200000S");
	}
	
	/**
	 * We want to use the model extensions
	 */
	protected Bpmn2ResourceImpl createBpmn2Resource(URI uri) {
		return (Bpmn2ResourceImpl) new ModelResourceFactoryImpl().createResource(uri);
	}
}
