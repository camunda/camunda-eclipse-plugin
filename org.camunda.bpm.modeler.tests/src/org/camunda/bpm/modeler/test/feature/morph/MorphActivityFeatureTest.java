package org.camunda.bpm.modeler.test.feature.morph;

import static org.camunda.bpm.modeler.test.util.operations.MorphActivityOperation.morphActivity;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.runtime.engine.model.CallActivity;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.NonTransactional;
import org.camunda.bpm.modeler.test.util.Util;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature.MorphOption;
import org.camunda.bpm.modeler.ui.features.activity.MorphActivityFeature;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.Task;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;
import org.junit.Test;


public class MorphActivityFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphFlowNode.testBase.bpmn")
	public void testMorphTaskToServiceTask() {
		
		// given
		Task oldTask = (Task) Util.findBusinessObjectById(diagram, "Task_1");
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		EClass newType = Bpmn2Package.eINSTANCE.getServiceTask();
		
		// when
		morphActivity(taskShape, diagramTypeProvider)
			.to(newType)
			.execute();
		
		// then
		EObject bo = Util.findBusinessObjectById(diagram, "Task_1");
		
		// check whether it is a service task
		assertThat(bo.eClass())
			.isEqualTo(newType);
		
		ServiceTask serviceTask = (ServiceTask) bo;
		
		assertThat(serviceTask.getName())
			.isEqualTo("Task");
		
		assertThat(serviceTask.getId())
			.isEqualTo("Task_1");
		
		// still have datainput- and dataoutputassociations
		DataOutputAssociation dataOutput = (DataOutputAssociation) Util.findBusinessObjectById(diagram, "DataOutputAssociation_1");
		DataInputAssociation dataInput = (DataInputAssociation) Util.findBusinessObjectById(diagram, "DataInputAssociation_1");
		
		assertThat(serviceTask.getDataInputAssociations())
			.contains(dataInput)
			.hasSize(1);

		assertThat(serviceTask.getDataOutputAssociations())
			.contains(dataOutput)
			.hasSize(1);

		// the containing associations have the right references
		Association association1 = (Association) Util.findBusinessObjectById(diagram, "Association_1");
		Association association2 = (Association) Util.findBusinessObjectById(diagram, "Association_2");
		Association association3 = (Association) Util.findBusinessObjectById(diagram, "Association_3");
		
		assertThat(association1.getSourceRef())
			.isEqualTo(serviceTask)
			.isNotEqualTo(oldTask);
		
		assertThat(association2.getSourceRef())
			.isEqualTo(serviceTask)
			.isNotEqualTo(oldTask);
		
		assertThat(association3.getTargetRef())
			.isEqualTo(serviceTask)
			.isNotEqualTo(oldTask);
		
		// check the incoming and outgoing sequence flows
		SequenceFlow incoming = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_1");
		SequenceFlow outgoing = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_2");
		
		assertThat(incoming.getTargetRef())
			.isEqualTo(serviceTask);
		
		assertThat(serviceTask.getIncoming())
			.contains(incoming);
		
		assertThat(outgoing.getSourceRef())
			.isEqualTo(serviceTask);

		assertThat(serviceTask.getOutgoing())
			.contains(outgoing);
		
		// check the message flows
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_1");
		MessageFlow outgoingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_2");
		
		assertThat(incomingMsg.getTargetRef())
			.isEqualTo(serviceTask);
		
		assertThat(outgoingMsg.getSourceRef())
			.isEqualTo(serviceTask);
		
		// check the asynchronous attribute
		assertThat(serviceTask.eGet(ModelPackage.eINSTANCE.getDocumentRoot_Async()))
			.isEqualTo(true);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphFlowNode.testBase.bpmn")
	public void testMorphTaskToCallActivity() {
		
		// given
		Task oldTask = (Task) Util.findBusinessObjectById(diagram, "Task_1");
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		EClass newType = Bpmn2Package.eINSTANCE.getCallActivity();
		
		// when
		morphActivity(taskShape, diagramTypeProvider)
			.to(newType)
			.execute();
		
		// then
		EObject bo = Util.findBusinessObjectById(diagram, "Task_1");
		
		// check whether it is a service task
		assertThat(bo.eClass())
			.isEqualTo(ModelPackage.eINSTANCE.getCallActivity());
		
		CallActivity callActivity = (CallActivity) bo;
		
		assertThat(callActivity.getName())
			.isEqualTo("Task");
	
		assertThat(callActivity.getId())
			.isEqualTo("Task_1");
		
		// still have datainput- and dataoutputassociations
		DataOutputAssociation dataOutput = (DataOutputAssociation) Util.findBusinessObjectById(diagram, "DataOutputAssociation_1");
		DataInputAssociation dataInput = (DataInputAssociation) Util.findBusinessObjectById(diagram, "DataInputAssociation_1");
		
		assertThat(callActivity.getDataInputAssociations())
			.contains(dataInput)
			.hasSize(1);

		assertThat(callActivity.getDataOutputAssociations())
			.contains(dataOutput)
			.hasSize(1);

		// the containing associations have the right references
		Association association1 = (Association) Util.findBusinessObjectById(diagram, "Association_1");
		Association association2 = (Association) Util.findBusinessObjectById(diagram, "Association_2");
		Association association3 = (Association) Util.findBusinessObjectById(diagram, "Association_3");
		
		assertThat(association1.getSourceRef())
			.isEqualTo(callActivity)
			.isNotEqualTo(oldTask);
		
		assertThat(association2.getSourceRef())
			.isEqualTo(callActivity)
			.isNotEqualTo(oldTask);
		
		assertThat(association3.getTargetRef())
			.isEqualTo(callActivity)
			.isNotEqualTo(oldTask);
		
		// check the incoming and outgoing sequence flows
		SequenceFlow incoming = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_1");
		SequenceFlow outgoing = (SequenceFlow) Util.findBusinessObjectById(diagram, "SequenceFlow_2");
		
		assertThat(incoming.getTargetRef())
			.isEqualTo(callActivity);
		
		assertThat(callActivity.getIncoming())
			.contains(incoming);
		
		assertThat(outgoing.getSourceRef())
			.isEqualTo(callActivity);

		assertThat(callActivity.getOutgoing())
			.contains(outgoing);
		
		// check the message flows (they should be deleted
		MessageFlow incomingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_1");
		MessageFlow outgoingMsg = (MessageFlow) Util.findBusinessObjectById(diagram, "MessageFlow_2");
		
		assertThat(incomingMsg)
			.isNull();

		assertThat(outgoingMsg)
			.isNull();
		
		// check the asynchronous attribute
		assertThat(callActivity.eGet(ModelPackage.eINSTANCE.getDocumentRoot_Async()))
			.isEqualTo(true);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphActivityTest.testOptions.bpmn")
	public void testAvailableOptions_Task() {
		shouldContainRightOptions("Task_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphActivityTest.testOptions.bpmn")
	public void testAvailableOptions_ManualTask() {
		shouldContainRightOptions("ManualTask_1");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphActivityTest.testOptions.bpmn")
	public void testAvailableOptions_UserTask() {
		shouldContainRightOptions("UserTask_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphActivityTest.testOptions.bpmn")
	public void testAvailableOptions_ScriptTask() {
		shouldContainRightOptions("ScriptTask_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphActivityTest.testOptions.bpmn")
	public void testAvailableOptions_BusinessRuleTask() {
		shouldContainRightOptions("BusinessRuleTask_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphActivityTest.testOptions.bpmn")
	public void testAvailableOptions_ServiceTask() {
		shouldContainRightOptions("ServiceTask_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphActivityTest.testOptions.bpmn")
	public void testAvailableOptions_SendTask() {
		shouldContainRightOptions("SendTask_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphActivityTest.testOptions.bpmn")
	public void testAvailableOptions_ReceiveTask() {
		shouldContainRightOptions("ReceiveTask_1");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/morph/MorphActivityTest.testOptions.bpmn")
	public void testAvailableOptions_CallActivity() {
		shouldContainRightOptions("CallActivity_1");
	}
	
	private void shouldContainRightOptions(String elementId) {
		Activity activity = (Activity) Util.findBusinessObjectById(diagram, elementId);
		Shape shape = Util.findShapeByBusinessObjectId(diagram, elementId);
		
		MorphActivityFeature feature = morphActivity(shape, diagramTypeProvider).getFeature();
		
		List<MorphOption> options = feature.getOptions(activity);
		List<EClass> expectedTypes = getExpectedList(activity.eClass());
		
		assertThat(options.size())
			.isEqualTo(expectedTypes.size());
		
		for (MorphOption morphOption : options) {
			if (!expectedTypes.contains(morphOption.getNewType())) {
				Assertions.fail("The list of available options contains a unexpected type: " + morphOption.getNewType().getName());
			}
		}
	}
	
	private List<EClass> getExpectedList(EClass cls) {
		EClass taskClass = Bpmn2Package.eINSTANCE.getTask();
		EClass manualTaskClass = Bpmn2Package.eINSTANCE.getManualTask();
		EClass userTaskClass = Bpmn2Package.eINSTANCE.getUserTask();
		EClass scriptTaskClass = Bpmn2Package.eINSTANCE.getScriptTask();
		EClass businessRuleTaskClass = Bpmn2Package.eINSTANCE.getBusinessRuleTask();
		EClass serviceTaskClass = Bpmn2Package.eINSTANCE.getServiceTask();
		EClass sendTaskClass = Bpmn2Package.eINSTANCE.getSendTask();
		EClass receiveTaskClass = Bpmn2Package.eINSTANCE.getReceiveTask();
		EClass callActivityClass = Bpmn2Package.eINSTANCE.getCallActivity();
		
		List<EClass> result = new ArrayList<EClass>();
		
		if (!taskClass.equals(cls)) {
			result.add(taskClass);
		}
		if (!manualTaskClass.equals(cls)) {
			result.add(manualTaskClass);
		}
		if (!userTaskClass.equals(cls)) {
			result.add(userTaskClass);
		}
		if (!scriptTaskClass.equals(cls)) {
			result.add(scriptTaskClass);
		}
		if (!businessRuleTaskClass.equals(cls)) {
			result.add(businessRuleTaskClass);
		}
		if (!serviceTaskClass.equals(cls)) {
			result.add(serviceTaskClass);
		}
		if (!sendTaskClass.equals(cls)) {
			result.add(sendTaskClass);
		}
		if (!receiveTaskClass.equals(cls)) {
			result.add(receiveTaskClass);
		}
		if (!callActivityClass.equals(cls) && !callActivityClass.isSuperTypeOf(cls)) {
			result.add(callActivityClass);
		}

		return result;
	}
	
}
