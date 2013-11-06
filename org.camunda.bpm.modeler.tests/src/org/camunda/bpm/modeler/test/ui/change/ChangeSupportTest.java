package org.camunda.bpm.modeler.test.ui.change;

import static org.camunda.bpm.modeler.test.util.Util.findBusinessObjectById;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.ConstraintType;
import org.camunda.bpm.modeler.runtime.engine.model.FormDataType;
import org.camunda.bpm.modeler.runtime.engine.model.FormFieldType;
import org.camunda.bpm.modeler.runtime.engine.model.InType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.PropertiesType;
import org.camunda.bpm.modeler.runtime.engine.model.PropertyType;
import org.camunda.bpm.modeler.runtime.engine.model.ValidationType;
import org.camunda.bpm.modeler.runtime.engine.model.ValueType;
import org.camunda.bpm.modeler.runtime.engine.model.fox.FailedJobRetryTimeCycleType;
import org.camunda.bpm.modeler.runtime.engine.model.fox.FoxFactory;
import org.camunda.bpm.modeler.runtime.engine.model.fox.FoxPackage;
import org.camunda.bpm.modeler.runtime.engine.model.util.ModelResourceFactoryImpl;
import org.camunda.bpm.modeler.runtime.engine.util.AttributeUtil;
import org.camunda.bpm.modeler.test.feature.AbstractNonTransactionalFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.ui.change.AbstractEObjectChangeSupport;
import org.camunda.bpm.modeler.ui.change.filter.AnyNestedChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.FeatureChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.IsManyAttributeAnyChildChangeFilter;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.EList;
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

	static final EStructuralFeature FORM_DATA_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_FormData();
	static final EStructuralFeature FORM_FIELD_VALIDATION_FEATURE = ModelPackage.eINSTANCE.getFormFieldType_Validation();
	static final EStructuralFeature FORM_FIELD_PROPERTIES_FEATURE = ModelPackage.eINSTANCE.getFormFieldType_Properties();
	static final EStructuralFeature FORM_FIELD_VALUE_FEATURE = ModelPackage.eINSTANCE.getFormFieldType_Value();
	
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

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldChangeSupportTest.bpmn")
	public void testFormFieldValueElementAdd() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();

		FeatureChangeFilter filter = new FeatureChangeFilter(formFieldType, FORM_FIELD_VALUE_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				ValueType formFieldValueType = ModelFactory.eINSTANCE.createValueType();
				formFieldValueType.setId("test");
				formFieldValueType.setName("test");
				formFieldType.getValue().add(formFieldValueType);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldEditAndRemoveChangeSupportTest.bpmn")
	public void testFormFieldValueElementRemove() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();

		FeatureChangeFilter filter = new FeatureChangeFilter(formFieldType, FORM_FIELD_VALUE_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		assertThat(formFieldType.getValue()).hasSize(3);

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				formFieldType.getValue().remove(0);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldChangeSupportTest.bpmn")
	public void testFormFieldPropertiesElementAdd() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();
		
		assertThat(formFieldType.getProperties()).isNull();

		FeatureChangeFilter filter = new FeatureChangeFilter(formFieldType, FORM_FIELD_PROPERTIES_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				PropertiesType propertiesType = ModelFactory.eINSTANCE.createPropertiesType();
				PropertyType propertyType = ModelFactory.eINSTANCE.createPropertyType();
				propertyType.setId("test");
				propertyType.setValue("test");
				
				propertiesType.getProperty().add(propertyType);
				formFieldType.setProperties(propertiesType);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldEditAndRemoveChangeSupportTest.bpmn")
	public void testFormFieldPropertiesElementRemove() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();		

		assertThat(formFieldType.getProperties()).isNotNull();

		FeatureChangeFilter filter = new FeatureChangeFilter(formFieldType, FORM_FIELD_PROPERTIES_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				formFieldType.eUnset(FORM_FIELD_PROPERTIES_FEATURE);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldChangeSupportTest.bpmn")
	public void testFormFieldValidationElementAdd() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();		

		assertThat(formFieldType.getValidation()).isNull();

		FeatureChangeFilter filter = new FeatureChangeFilter(formFieldType, FORM_FIELD_VALIDATION_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				ValidationType validationType = ModelFactory.eINSTANCE.createValidationType();
				ConstraintType constraintType = ModelFactory.eINSTANCE.createConstraintType();
				constraintType.setName("test");
				constraintType.setConfig("test");
				
				validationType.getConstraint().add(constraintType);
				formFieldType.setValidation(validationType);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldEditAndRemoveChangeSupportTest.bpmn")
	public void testFormFieldValidationElementRemove() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();		

		assertThat(formFieldType.getValidation()).isNotNull();

		FeatureChangeFilter filter = new FeatureChangeFilter(formFieldType, FORM_FIELD_VALIDATION_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				formFieldType.eUnset(FORM_FIELD_VALIDATION_FEATURE);
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

		IsManyAttributeAnyChildChangeFilter filter = new IsManyAttributeAnyChildChangeFilter(process1, FLOW_ELEMENTS_FEATURE);

		CustomResourceSetListener listener = new CustomResourceSetListener(process1, filter);
		listener.register();

		// check what happens when other changes connected
		// to flow elements occur
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				sequenceFlow1.setTargetRef(task1);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldEditAndRemoveChangeSupportTest.bpmn")
	public void testFormFieldValueElementUpdate() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();

		IsManyAttributeAnyChildChangeFilter filter = new IsManyAttributeAnyChildChangeFilter(formFieldType, FORM_FIELD_VALUE_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		assertThat(formFieldType.getValue()).hasSize(3);
		assertThat(formFieldType.getValue().get(0).getId()).isNotNull();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				formFieldType.getValue().get(0).setId("newValue");
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}
	
	// any nested change support ///////////////////////////////////////

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormDataAddChangeSupportTest.bpmn")
	public void testFormDataAndFormFieldElementAdd() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).isEmpty();

		AnyNestedChangeFilter filter = new AnyNestedChangeFilter(userTask, FORM_DATA_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(userTask, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				// add form data with form field
				FormDataType formData = ModelFactory.eINSTANCE.createFormDataType();
				ExtensionUtil.addExtension(userTask, FORM_DATA_FEATURE, formData);

				FormFieldType formField = ModelFactory.eINSTANCE.createFormFieldType();
				formField.setId("test");
				formField.setType("string");
				formData.getFormField().add(formField);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldChangeSupportTest.bpmn")
	public void testFormFieldElementUpdate() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		final EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);

		AnyNestedChangeFilter filter = new AnyNestedChangeFilter(userTask, FORM_DATA_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(userTask, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				formFieldList.get(0).setId("newValue");
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldChangeSupportTest.bpmn")
	public void testFormFieldElementRemove() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		final EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);

		AnyNestedChangeFilter filter = new AnyNestedChangeFilter(userTask, FORM_DATA_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(userTask, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				formFieldList.remove(0);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldEditAndRemoveChangeSupportTest.bpmn")
	public void testFormFieldPropertyElementAdd() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();

		assertThat(formFieldType.getProperties()).isNotNull();

		AnyNestedChangeFilter filter = new AnyNestedChangeFilter(formFieldType, FORM_FIELD_PROPERTIES_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				PropertyType propertyType = ModelFactory.eINSTANCE.createPropertyType();
				propertyType.setId("test");
				propertyType.setValue("test");
				formFieldType.getProperties().getProperty().add(propertyType);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldEditAndRemoveChangeSupportTest.bpmn")
	public void testFormFieldPropertyElementUpdate() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();

		assertThat(formFieldType.getProperties().getProperty().get(0).getId()).isNotNull();

		AnyNestedChangeFilter filter = new AnyNestedChangeFilter(formFieldType, FORM_FIELD_PROPERTIES_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				formFieldType.getProperties().getProperty().get(0).setId("newValue");
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldEditAndRemoveChangeSupportTest.bpmn")
	public void testFormFieldPropertyElementRemove() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();
		
		assertThat(formFieldType.getProperties().getProperty()).isNotNull();

		AnyNestedChangeFilter filter = new AnyNestedChangeFilter(formFieldType, FORM_FIELD_PROPERTIES_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				formFieldType.getProperties().getProperty().remove(0);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldEditAndRemoveChangeSupportTest.bpmn")
	public void testFormFieldConstraintElementAdd() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();

		assertThat(formFieldType.getValidation()).isNotNull();

		AnyNestedChangeFilter filter = new AnyNestedChangeFilter(formFieldType, FORM_FIELD_VALIDATION_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				ConstraintType constraintType = ModelFactory.eINSTANCE.createConstraintType();
				constraintType.setName("test");
				constraintType.setConfig("test");
				formFieldType.getValidation().getConstraint().add(constraintType);
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldEditAndRemoveChangeSupportTest.bpmn")
	public void testFormFieldConstraintElementUpdate() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();

		assertThat(formFieldType.getValidation().getConstraint().get(0).getName()).isNotNull();

		AnyNestedChangeFilter filter = new AnyNestedChangeFilter(formFieldType, FORM_FIELD_VALIDATION_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();
		
		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				formFieldType.getValidation().getConstraint().get(0).setName("newValue");
			}
		});

		assertThat(listener.getCapturedEvents()).hasSize(1);
	}	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/ui/change/FormFieldEditAndRemoveChangeSupportTest.bpmn")
	public void testFormFieldConstraintElementRemove() {
		final UserTask userTask = findBusinessObjectById(diagram, "UserTask_1", UserTask.class);

		List<FormDataType> formDataTypeList = ExtensionUtil.getExtensions(userTask, FormDataType.class);
		assertThat(formDataTypeList).hasSize(1);

		EList<FormFieldType> formFieldList = formDataTypeList.get(0).getFormField();
		assertThat(formFieldList).hasSize(1);
		
		final FormFieldType formFieldType = formFieldList.get(0);
		assertThat(formFieldType).isNotNull();
		
		assertThat(formFieldType.getValidation().getConstraint()).isNotNull();

		AnyNestedChangeFilter filter = new AnyNestedChangeFilter(formFieldType, FORM_FIELD_VALIDATION_FEATURE);
		CustomResourceSetListener listener = new CustomResourceSetListener(formFieldType, filter);
		listener.register();

		transactionalExecute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				formFieldType.getValidation().getConstraint().remove(0);
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
