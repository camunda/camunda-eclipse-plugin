package org.camunda.bpm.modeler.test.core.utils.transform;

import static org.camunda.bpm.modeler.test.util.TestHelper.transactionalExecute;
import static org.fest.assertions.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.transform.CopyProblem;
import org.camunda.bpm.modeler.core.utils.transform.Transformer;
import org.camunda.bpm.modeler.core.utils.transform.Transformer.IgnoredStructuralFeature;
import org.camunda.bpm.modeler.test.util.TestHelper;
import org.camunda.bpm.modeler.test.util.TestHelper.ModelResources;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.api.StringAssert;
import org.junit.Test;

public class TransformerTest {
	
	@Test
	public void testMorph_shouldMorphFromTimerToCompensate() throws Exception {
		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/core/utils/transform/TransformerTest.testEventDefinition.bpmn");
		Resource resource = resources.getResource();
		
		String elementId = "TimerEventDefinition_1";
		
		final EObject target = resource.getEObject(elementId);

		final Transformer transformer = new Transformer(target);
		
		// when
		transactionalExecute(resources, new Runnable() {
			@Override
			public void run() {
				transformer.morph(Bpmn2Package.eINSTANCE.getCompensateEventDefinition());
			}
		});

		// then
		List<CopyProblem> warnings = transformer.getRecordedWarnings();
		
		assertThat(warnings).hasSize(1);
		
		EObject morph = resource.getEObject(elementId);
		
		assertThat(morph)
			.isNotNull()
			.isInstanceOf(CompensateEventDefinition.class);
	}
	
	@Test
	public void testMorph_shouldRetainReferences() throws Exception {
		
		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/core/utils/transform/TransformerTest.base.bpmn");
		Resource resource = resources.getResource();
		
		String elementId = "DataObject_1";
		
		final EObject target = resource.getEObject(elementId);
		final BPMNShape bpmnShape = (BPMNShape) resource.getEObject("BPMNShape_DataObject_1");

		final Transformer transformer = new Transformer(target);
		
		// when
		transactionalExecute(resources, new Runnable() {
			@Override
			public void run() {
				transformer.morph(Bpmn2Package.eINSTANCE.getDataObjectReference());
			}
		});

		// then
		List<CopyProblem> warnings = transformer.getRecordedWarnings();
		
		assertThat(warnings).isEmpty();
		
		EObject morph = resource.getEObject(elementId);
		
		assertThat(morph)
			.isNotNull()
			.isInstanceOf(DataObjectReference.class)
			.isEqualTo(bpmnShape.getBpmnElement());
	}
	
	@Test
	public void testMorph_shouldRetainMessageFlows() throws Exception {
		
		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/core/utils/transform/TransformerTest.testMessageFlow.bpmn");
		Resource resource = resources.getResource();
		
		String elementId = "Task_1";
		
		final EObject target = resource.getEObject(elementId);
		final BPMNShape bpmnShape = (BPMNShape) resource.getEObject("_BPMNShape_Task_2");
		
		final Transformer transformer = new Transformer(target);
		
		// when
		transactionalExecute(resources, new Runnable() {
			@Override
			public void run() {
				try {
					transformer.morph(Bpmn2Package.eINSTANCE.getCallActivity());
				} catch (ClassCastException e) {
					Assertions.fail("Unexpected excpetion: " + e.getMessage());
				}
			}
		});

		// then
		List<CopyProblem> warnings = transformer.getRecordedWarnings();
		
		assertThat(warnings).hasSize(2);
		assertThat(warnings.get(0)).isInstanceOf(IgnoredStructuralFeature.class);
		assertThat(warnings.get(1)).isInstanceOf(IgnoredStructuralFeature.class);
		
		EObject morph = resource.getEObject(elementId);
		
		assertThat(morph)
			.isNotNull()
			.isInstanceOf(CallActivity.class)
			.isEqualTo(bpmnShape.getBpmnElement());
	}	
	
	@Test
	public void testMorph_shouldKeepDataAssocations() throws Exception {
		
		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/core/utils/transform/TransformerTest.testDataAssociation.bpmn");
		Resource resource = resources.getResource();
		
		String elementId = "Task_1";
		
		final EObject task = resource.getEObject(elementId);
		final DataInputAssociation dataInputAssocation = (DataInputAssociation) resource.getEObject("DataInputAssociation_1");
		final DataOutputAssociation dataOutputAssocation = (DataOutputAssociation) resource.getEObject("DataOutputAssociation_1");
		
		final BPMNEdge dataInputAssocationShape = (BPMNEdge) resource.getEObject("BPMNEdge_DataInputAssociation_1");
		final BPMNEdge dataOutputAssocationShape = (BPMNEdge) resource.getEObject("BPMNEdge_DataOutputAssociation_1");
		
		final Transformer transformer = new Transformer(task);
		
		// when
		transactionalExecute(resources, new Runnable() {
			@Override
			public void run() {
				transformer.morph(Bpmn2Package.eINSTANCE.getServiceTask());
			}
		});

		// then
		List<CopyProblem> warnings = transformer.getRecordedWarnings();

		assertThat(warnings).isEmpty();
		
		EObject morph = resource.getEObject(elementId);
		
		assertThat(morph)
			.isNotNull()
			.isInstanceOf(ServiceTask.class);
		
		assertThat(morph)
			.isEqualTo(dataInputAssocation.eContainer());

		assertThat(morph)
			.isEqualTo(dataOutputAssocation.eContainer());
		
		assertThat(dataInputAssocation)
			.isEqualTo((DataInputAssociation) dataInputAssocationShape.getBpmnElement());
		
		assertThat(dataOutputAssocation)			
			.isEqualTo((DataOutputAssociation) dataOutputAssocationShape.getBpmnElement());
	}
	
	@Test
	public void testMorph_shouldRetainExtensions() throws Exception {

		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/core/utils/transform/TransformerTest.base.bpmn");
		Resource resource = resources.getResource();
		
		String elementId = "DataObject_2";
		
		final EObject target = resource.getEObject(elementId);

		final Transformer transformer = new Transformer(target);
		
		// when
		transactionalExecute(resources, new Runnable() {
			@Override
			public void run() {
				transformer.morph(Bpmn2Package.eINSTANCE.getDataObjectReference());
			}
		});

		// then
		List<CopyProblem> warnings = transformer.getRecordedWarnings();
		
		assertThat(warnings).isEmpty();
		
		EObject morph = resource.getEObject(elementId);
		
		assertThat(morph)
			.isNotNull()
			.isInstanceOf(DataObjectReference.class);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		resource.save(outputStream, null);
		
		String resultXML = outputStream.toString("UTF-8");
				
		assertThat(resultXML)
			.contains("custom:attr=\"ATTR\"")
			.contains("<custom:foo a=\"a\" b=\"\"/>")
			.contains("<custom:bar AAA=\"AAA\" BBB=\"0\"/>")
			.contains("<custom2:asd>")
			.contains("<custom2:sdf custom:attr=\"BOO\"/>")
			.contains("</custom2:asd>");
	}

	@Test
	public void testMorph_shouldRetainRefProperty() throws Exception {

		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/core/utils/transform/TransformerTest.base.bpmn");
		Resource resource = resources.getResource();
		
		String elementId = "DataObjectReference_1";
		
		final EObject target = resource.getEObject(elementId);

		final Transformer transformer = new Transformer(target);
		
		// when
		transactionalExecute(resources, new Runnable() {
			@Override
			public void run() {
				transformer.morph(Bpmn2Package.eINSTANCE.getDataObjectReference());
			}
		});

		List<CopyProblem> warnings = transformer.getRecordedWarnings();
		
		assertThat(warnings).isEmpty();
		
		EObject morph = resource.getEObject(elementId);
		
		assertThat(morph)
			.isNotNull()
			.isInstanceOf(DataObjectReference.class);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		resource.save(outputStream, null);
		
		String resultXML = outputStream.toString("UTF-8");
		
		assertThat(resultXML)
			.contains("<bpmn2:dataObjectReference id=\"DataObjectReference_1\" name=\"Data Object Reference 1\" dataObjectRef=\"DataObject_3\"/>");
	}
	
	
	@Test
	public void testMorph_shouldIgnoreNonExistentTargetRefProperty() throws Exception {

		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/core/utils/transform/TransformerTest.base.bpmn");
		Resource resource = resources.getResource();
		
		String elementId = "DataObjectReference_1";
		
		final EObject target = resource.getEObject(elementId);

		final Transformer transformer = new Transformer(target);
		
		// when
		transactionalExecute(resources, new Runnable() {
			@Override
			public void run() {
				transformer.morph(Bpmn2Package.eINSTANCE.getDataObject());
			}
		});

		List<CopyProblem> warnings = transformer.getRecordedWarnings();
		
		assertThat(warnings).hasSize(1);
		assertThat(warnings.get(0)).isInstanceOf(IgnoredStructuralFeature.class);
		
		EObject morph = resource.getEObject(elementId);
		
		assertThat(morph)
			.isNotNull()
			.isInstanceOf(DataObject.class);
		
		String resultXML = TestHelper.saveToString(resource);
		
		assertThat(resultXML)
			.contains("<bpmn2:dataObject id=\"DataObjectReference_1\" name=\"Data Object Reference 1\"/>");
	}

	@Test
	public void testMorph_shouldFixIsManyCrossReference() throws Exception {
		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/core/utils/transform/TransformerTest.testUpdateMultiReferences.bpmn");
		Resource resource = resources.getResource();
		
		String elementId = "DataObject_1";
		
		final EObject target = resource.getEObject(elementId);
		
		final Transformer transformer = new Transformer(target);
		
		// when
		transactionalExecute(resources, new Runnable() {
			@Override
			public void run() {
				transformer.morph(Bpmn2Package.eINSTANCE.getDataObjectReference());
			}
		});
		
		// then
		List<CopyProblem> warnings = transformer.getRecordedWarnings();
		
		// ensure we got the diagram transformed
		// without warnings
		assertThat(warnings).isEmpty();
	}

	@Test
	public void testMorph_shouldIgnoreNonExistentTargetAttributeProperty() throws Exception {

		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/core/utils/transform/TransformerTest.base.bpmn");
		Resource resource = resources.getResource();
		
		String elementId = "DataObject_4";
		
		final EObject target = resource.getEObject(elementId);

		final Transformer transformer = new Transformer(target);
		
		// when
		transactionalExecute(resources, new Runnable() {
			
			@Override
			public void run() {
				transformer.morph(Bpmn2Package.eINSTANCE.getDataObjectReference());
			}
		});

		List<CopyProblem> warnings = transformer.getRecordedWarnings();
		
		assertThat(warnings).hasSize(1);
		assertThat(warnings.get(0)).isInstanceOf(IgnoredStructuralFeature.class);
		
		EObject morph = resource.getEObject(elementId);
		
		assertThat(morph)
			.isNotNull()
			.isInstanceOf(DataObjectReference.class);
		
		String resultXML = TestHelper.saveToString(resource);
		
		assertThat(resultXML)
			.contains("<bpmn2:dataObjectReference id=\"DataObject_4\" name=\"Data Object 4\"/>");
	}

	@Test
	public void testMorph_scenario_shouldReplace_ExclusiveGateway_w_ParallelGateway() throws Exception {
		assertMorph("ExclusiveGateway_1", Bpmn2Package.eINSTANCE.getParallelGateway(), 1, "<bpmn2:parallelGateway id=\"ExclusiveGateway_1\" name=\"Exclusive Gateway\">");	
	}
	
	@Test
	public void testMorph_scenario_shouldReplace_ExclusiveGateway_w_ServiceTask() throws Exception {
		assertMorph("ExclusiveGateway_1", Bpmn2Package.eINSTANCE.getServiceTask(), 1, "<bpmn2:serviceTask id=\"ExclusiveGateway_1\" name=\"Exclusive Gateway\">");
	}

	@Test
	public void testMorph_scenario_shouldReplace_ServiceTask_w_UserTask() throws Exception {
		// extensions will be copied to user task
		assertMorph("ServiceTask_1", Bpmn2Package.eINSTANCE.getUserTask(), 0, "<bpmn2:userTask id=\"ServiceTask_1\" camunda:expression=\"${fooBar}\" name=\"Service Task\">");
	}

	@Test
	public void testMorph_scenario_shouldReplace_StartEvent_w_EndEvent() throws Exception {
		// outgoing sequence flows are retained
		assertMorph("StartEvent_1", Bpmn2Package.eINSTANCE.getEndEvent(), 0, "<bpmn2:endEvent id=\"StartEvent_1\" name=\"Start Event\">");
	}
	
	@Test
	public void testMorph_scenario_shouldReplace_EndEvent_w_StartEvent() throws Exception {
		// incoming sequence flows are retained
		assertMorph("EndEvent_1", Bpmn2Package.eINSTANCE.getStartEvent(), 0, "<bpmn2:startEvent id=\"EndEvent_1\" name=\"End Event\">");
	}
	
	private static void assertMorph(String elementId, final EClass morphTargetCls, int warningCount, String ... expectedStrings) throws Exception {

		// given
		ModelResources resources = TestHelper.createModel("org/camunda/bpm/modeler/test/core/utils/transform/TransformerTest.advanced.bpmn");
		Resource resource = resources.getResource();
		
		final EObject target = resource.getEObject(elementId);

		final Transformer transformer = new Transformer(target);
		
		// when
		transactionalExecute(resources, new Runnable() {
			@Override
			public void run() {
				transformer.morph(morphTargetCls);
			}
		});

		List<CopyProblem> warnings = transformer.getRecordedWarnings();
		
		assertThat(warnings).hasSize(warningCount);
		
		EObject morph = resource.getEObject(elementId);
		
		assertThat(morph).isNotNull();
		assertThat(morphTargetCls.isInstance(morph)).as(String.format("%s is instance of %s", morph, morphTargetCls)).isTrue();
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		resource.save(outputStream, null);
		
		String resultXML = outputStream.toString("UTF-8");
		
		StringAssert assertResult = assertThat(resultXML);
		
		for (String s: expectedStrings) {
			assertResult.contains(s);
		}
	}
}
