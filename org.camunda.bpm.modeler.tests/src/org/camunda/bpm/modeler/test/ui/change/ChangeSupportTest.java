package org.camunda.bpm.modeler.test.ui.change;

import static org.camunda.bpm.modeler.test.util.Util.findBusinessObjectById;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.activiti.model.InType;
import org.camunda.bpm.modeler.runtime.activiti.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.activiti.model.fox.FailedJobRetryTimeCycleType;
import org.camunda.bpm.modeler.runtime.activiti.model.fox.FoxFactory;
import org.camunda.bpm.modeler.runtime.activiti.model.fox.FoxPackage;
import org.camunda.bpm.modeler.runtime.activiti.model.util.ModelResourceFactoryImpl;
import org.camunda.bpm.modeler.runtime.activiti.util.AttributeUtil;
import org.camunda.bpm.modeler.test.feature.AbstractNonTransactionalFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.ui.change.AbstractEObjectChangeSupport;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.FeatureChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.NestedFeatureChangeFilter;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ChangeSupportTest extends AbstractNonTransactionalFeatureTest {

	static final FoxFactory foxFactory = FoxFactory.eINSTANCE;
	static final FoxPackage foxPackage = FoxPackage.eINSTANCE;
	
	static final Bpmn2Factory bpmn2factory = Bpmn2Factory.eINSTANCE;
	static final Bpmn2Package bpmn2package = Bpmn2Package.eINSTANCE;
	
	static final ModelFactory modelFactory = ModelFactory.eINSTANCE;
	static final ModelPackage modelPackage = ModelPackage.eINSTANCE;

	static final EStructuralFeature FLOW_ELEMENTS_FEATURE = bpmn2package.getFlowElementsContainer_FlowElements();
	static final EStructuralFeature CONDITION_EXPRESSION_FEATURE = bpmn2package.getSequenceFlow_ConditionExpression();
	
	static final EStructuralFeature ASYNC_FEATURE = modelPackage.getDocumentRoot_Async();
	static final EStructuralFeature RETRY_CYCLE_FEATURE = modelPackage.getDocumentRoot_FailedJobRetryTimeCycle();
	
	static final EStructuralFeature IN_FEATURE = modelPackage.getDocumentRoot_In();
	
	// default change support tests ///////////////////////////////////////////
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testListElementAdd() {
		final Process process1 = findBusinessObjectById(diagram, "Process_1", Process.class);

		final Task task2 = bpmn2factory.createTask();

		FeatureChangeFilter filter = new FeatureChangeFilter(process1, FLOW_ELEMENTS_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(process1, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				process1.getFlowElements().add(task2);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testListElementModify() {
		final Process process1 = findBusinessObjectById(diagram, "Process_1", Process.class);
		final SequenceFlow sequenceFlow1 = findBusinessObjectById(diagram, "SequenceFlow_1", SequenceFlow.class);

		final Task task1 = findBusinessObjectById(diagram, "Task_1", Task.class);

		FeatureChangeFilter filter = new FeatureChangeFilter(process1, FLOW_ELEMENTS_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(process1, filter);
		listener.register();

		// check what happenes when other changes connected
		// to flow elements occur
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				sequenceFlow1.setTargetRef(task1);
			}
		});

		// we have another filter to figure out exactly that
		assertThat(listener.getCapturedEvents()).isEmpty();
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testListElementRemove() {
		final Process process1 = findBusinessObjectById(diagram, "Process_1", Process.class);
		final ServiceTask serviceTask1 = findBusinessObjectById(diagram, "ServiceTask_1", ServiceTask.class);

		FeatureChangeFilter filter = new FeatureChangeFilter(process1, FLOW_ELEMENTS_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(process1, filter);
		listener.register();

		// assert that task is member of collection
		assertThat(process1.getFlowElements()).contains(serviceTask1);
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				process1.getFlowElements().remove(serviceTask1);

			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testAttributeAdd() {
		final SequenceFlow sequenceFlow1 = findBusinessObjectById(diagram, "SequenceFlow_1", SequenceFlow.class);

		FeatureChangeFilter filter = new FeatureChangeFilter(sequenceFlow1, CONDITION_EXPRESSION_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(sequenceFlow1, filter);
		listener.register();
		
		final FormalExpression expression = bpmn2factory.createFormalExpression();
		expression.setBody("FOOO");
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				sequenceFlow1.setConditionExpression(expression);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testAttributeRemove() {
		final SequenceFlow sequenceFlow2 = findBusinessObjectById(diagram, "SequenceFlow_2", SequenceFlow.class);

		FeatureChangeFilter filter = new FeatureChangeFilter(sequenceFlow2, CONDITION_EXPRESSION_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(sequenceFlow2, filter);
		listener.register();

		// assert that feature is set
		assertThat(sequenceFlow2.eIsSet(CONDITION_EXPRESSION_FEATURE)).isTrue();
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				sequenceFlow2.eUnset(CONDITION_EXPRESSION_FEATURE);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testAttributeUpdate() {
		final SequenceFlow sequenceFlow2 = findBusinessObjectById(diagram, "SequenceFlow_2", SequenceFlow.class);

		FeatureChangeFilter filter = new FeatureChangeFilter(sequenceFlow2, CONDITION_EXPRESSION_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(sequenceFlow2, filter);
		listener.register();
		
		final FormalExpression expression = bpmn2factory.createFormalExpression();
		expression.setBody("FOOO");
				
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				sequenceFlow2.setConditionExpression(expression);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	// extension attribute change support ///////////////////////////////
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testExtensionAttributeAdd() {
		final ServiceTask serviceTask1 = findBusinessObjectById(diagram, "ServiceTask_1", ServiceTask.class);

		FeatureChangeFilter filter = new FeatureChangeFilter(serviceTask1, ASYNC_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(serviceTask1, filter);
		listener.register();
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				serviceTask1.eSet(ASYNC_FEATURE, true);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testExtensionAttributeRemove() {
		final Task task1 = findBusinessObjectById(diagram, "Task_1", Task.class);

		FeatureChangeFilter filter = new FeatureChangeFilter(task1, ASYNC_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(task1, filter);
		listener.register();
		
		// assert that feature is set
		assertThat(task1.eIsSet(ASYNC_FEATURE)).isTrue();
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				task1.eUnset(ASYNC_FEATURE);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testExtensionAttributeUpdate() {
		final Task task1 = findBusinessObjectById(diagram, "Task_1", Task.class);

		FeatureChangeFilter filter = new FeatureChangeFilter(task1, ASYNC_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(task1, filter);
		listener.register();
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				task1.eSet(ASYNC_FEATURE, false);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	// extension element change support ///////////////////////////////

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testExtensionElementAdd() {
		final ServiceTask serviceTask1 = findBusinessObjectById(diagram, "ServiceTask_1", ServiceTask.class);

		ExtensionChangeFilter filter = new ExtensionChangeFilter(serviceTask1, RETRY_CYCLE_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(serviceTask1, filter);
		listener.register();
		
		final FailedJobRetryTimeCycleType retryCycle = foxFactory.createFailedJobRetryTimeCycleType();
		retryCycle.setText("R3/PT10S");
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				ExtensionUtil.addExtension(serviceTask1, RETRY_CYCLE_FEATURE, retryCycle);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testExtensionElementRemove() {
		final Task task1 = findBusinessObjectById(diagram, "Task_1", Task.class);

		ExtensionChangeFilter filter = new ExtensionChangeFilter(task1, RETRY_CYCLE_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(task1, filter);
		listener.register();
		
		// assert that feature is set
		assertThat(ExtensionUtil.getExtension(task1, RETRY_CYCLE_FEATURE, "text")).isNotNull();
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				ExtensionUtil.removeExtensionByFeature(task1, RETRY_CYCLE_FEATURE);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testExtensionElementRemoveAndCleanExtensionAttributeValues() {
		final Task task1 = findBusinessObjectById(diagram, "Task_1", Task.class);
		
		// assert that feature is set
		assertThat(ExtensionUtil.getExtension(task1, RETRY_CYCLE_FEATURE, "text")).isNotNull();
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				ExtensionUtil.removeExtensionByFeature(task1, RETRY_CYCLE_FEATURE);
			}
		});

		ExtensionChangeFilter filter = new ExtensionChangeFilter(task1, RETRY_CYCLE_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(task1, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				AttributeUtil.clearEmptyExtensionElements(task1);
			}
		});
		
		assertThat(listener.getCapturedEvents()).hasSize(1);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testExtensionElementUpdate() {
		final Task task1 = findBusinessObjectById(diagram, "Task_1", Task.class);

		ExtensionChangeFilter filter = new ExtensionChangeFilter(task1, RETRY_CYCLE_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(task1, filter);
		
		listener.register();

		final FailedJobRetryTimeCycleType retryCycle = foxFactory.createFailedJobRetryTimeCycleType();
		retryCycle.setText("R3/PT10S");

		// assert that feature is set
		assertThat(ExtensionUtil.getExtension(task1, RETRY_CYCLE_FEATURE, "text")).isNotNull();
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				ExtensionUtil.updateExtension(task1, RETRY_CYCLE_FEATURE, retryCycle);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}
	
	public InType addCallActivityInType(final CallActivity callActivity) {
		final InType inType = modelFactory.createInType();
		inType.setSource("ASD");
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				ExtensionUtil.addExtension(callActivity, IN_FEATURE, inType);
			}
		});
		
		return inType;
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testExtensionElementNestedAttributeUpdate() {
		final CallActivity callActivity1 = findBusinessObjectById(diagram, "CallActivity_1", CallActivity.class);

		// add stuff
		final InType type = addCallActivityInType(callActivity1);

		ExtensionChangeFilter filter = new ExtensionChangeFilter(callActivity1, IN_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(callActivity1, filter);
		listener.register();
		
		List<InType> inTypes = ExtensionUtil.getExtensions(callActivity1, InType.class);
		assertThat(inTypes).hasSize(1);
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				type.setSource("SDF");
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testExtensionElementNestedAttributeUnset() {
		final CallActivity callActivity1 = findBusinessObjectById(diagram, "CallActivity_1", CallActivity.class);

		// add stuff
		final InType type = addCallActivityInType(callActivity1);

		ExtensionChangeFilter filter = new ExtensionChangeFilter(callActivity1, IN_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(callActivity1, filter);
		listener.register();
		
		List<InType> inTypes = ExtensionUtil.getExtensions(callActivity1, InType.class);
		assertThat(inTypes).hasSize(1);
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				type.eUnset(modelPackage.getInType_Source());
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}
	
	// nested list change support ///////////////////////////////////////
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/ChangeSupportTest.testBase.bpmn")
	public void testListElementModifyNestedDetect() {
		final Process process1 = findBusinessObjectById(diagram, "Process_1", Process.class);
		final SequenceFlow sequenceFlow1 = findBusinessObjectById(diagram, "SequenceFlow_1", SequenceFlow.class);

		final Task task1 = findBusinessObjectById(diagram, "Task_1", Task.class);

		NestedFeatureChangeFilter filter = new NestedFeatureChangeFilter(process1, FLOW_ELEMENTS_FEATURE);
		
		CustomResourceSetListener listener = new CustomResourceSetListener(process1, filter);
		listener.register();

		// check what happenes when other changes connected
		// to flow elements occur
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				sequenceFlow1.setTargetRef(task1);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}
	
	// utility classes ////////////////////////////////////////////////////
	
	private class CustomResourceSetListener extends AbstractEObjectChangeSupport {

		private List<ResourceSetChangeEvent> capturedEvents = new ArrayList<ResourceSetChangeEvent>();

		public CustomResourceSetListener(EObject object, NotificationFilter filter) {
			super(object);

			this.filter = filter;
		}

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			capturedEvents.add(event);
		}

		/**
		 * Return a list of previously captured events
		 * 
		 * @return
		 */
		public List<ResourceSetChangeEvent> getCapturedEvents() {
			return capturedEvents;
		}

		/**
		 * Clear captured events
		 */
		public void clearCapturedEvents() {
			capturedEvents.clear();
		}
	}
	
	/**
	 * We want to use the model extensions
	 */
	protected Bpmn2ResourceImpl createBpmn2Resource(URI uri) {
		return (Bpmn2ResourceImpl) new ModelResourceFactoryImpl().createResource(uri);
	}
}
