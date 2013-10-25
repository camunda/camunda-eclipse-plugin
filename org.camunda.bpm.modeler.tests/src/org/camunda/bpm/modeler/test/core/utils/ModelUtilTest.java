package org.camunda.bpm.modeler.test.core.utils;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractNonTransactionalFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

/**
 * Business object util tests
 * 
 * @author roman.smirnov
 *
 */
public class ModelUtilTest extends AbstractNonTransactionalFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseDiagram.bpmn")
	public void testCollectDataObjectsInDiagram() {

		EObject be = Util.findBusinessObjectById(diagram, "Process_1");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(3);
	}
	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseDiagram.bpmn")
	public void testCollectDataObjectsInAdHocSubProcess() {
	
		EObject be = Util.findBusinessObjectById(diagram, "AdHocSubProcess_1");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(4);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseDiagram.bpmn")
	public void testCollectDataObjectsInTransaction() {
	
		EObject be = Util.findBusinessObjectById(diagram, "Transaction_1");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(5);
	}	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseDiagram.bpmn")
	public void testCollectDataObjectsInSubProcess() {
	
		EObject be = Util.findBusinessObjectById(diagram, "SubProcess_1");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);		
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(6);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseDiagram.bpmn")
	public void testCollectDataObjectsInSubProcess2() {
	
		EObject be = Util.findBusinessObjectById(diagram, "SubProcess_2");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);		
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(4);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseDiagram.bpmn")
	public void testCollectDataObjectsInNestedSubProcess() {
	
		EObject be = Util.findBusinessObjectById(diagram, "SubProcess_3");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(5);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseParticipant.bpmn")
	public void testCollectDataObjectsInSimplePool() {

		EObject be = Util.findBusinessObjectById(diagram, "_Participant_2");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(2);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseParticipant.bpmn")
	public void testCollectDataObjectsInLane() {
	
		EObject be = Util.findBusinessObjectById(diagram, "Lane_1");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);		
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(1);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseParticipant.bpmn")
	public void testCollectDataObjectsInNestedLane() {

		EObject be = Util.findBusinessObjectById(diagram, "Lane_5");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(2);
	}
	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseParticipant.bpmn")
	public void testCollectDataObjectsInParticipantWithNestedSubProcess() {

		EObject be = Util.findBusinessObjectById(diagram, "SubProcess_1");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);
	
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(2);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testBaseParticipant.bpmn")
	public void testCollectDataObjectsInParticipantWithSiblingNestedSubProcess() {
	
		EObject be = Util.findBusinessObjectById(diagram, "SubProcess_2");
		
		List<DataObject> dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(2);
		
		be = Util.findBusinessObjectById(diagram, "SubProcess_3");
		
		dataObjects = ModelUtil.getAllReachableObjectsIncludingParents(be, DataObject.class);
		
		assertThat(dataObjects).isNotEmpty();
		
		assertThat(dataObjects).hasSize(3);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testGetFlowElementsContainer.bpmn")
	public void testGetFlowElementsContainerForActivities() {

		BaseElement businessObject  = Util.findBusinessObjectById(diagram, "CallActivity_2");
		FlowElementsContainer parentContainer = ModelUtil.getFlowElementsContainer(businessObject);
		assertThat(parentContainer.getId()).isEqualTo("SubProcess_2");

		businessObject  = Util.findBusinessObjectById(diagram, "Task_2");
		parentContainer = ModelUtil.getFlowElementsContainer(businessObject);
		assertThat(parentContainer.getId()).isEqualTo("SubProcess_2");

		businessObject  = Util.findBusinessObjectById(diagram, "CallActivity_1");
		parentContainer = ModelUtil.getFlowElementsContainer(businessObject);
		assertThat(parentContainer.getId()).isEqualTo("Process_1");

		businessObject  = Util.findBusinessObjectById(diagram, "SubProcess_1");
		parentContainer = ModelUtil.getFlowElementsContainer(businessObject);
		assertThat(parentContainer.getId()).isEqualTo("Process_1");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testGetFlowElementsContainer.bpmn")
	public void testGetFlowElementsContainerForEvents() {

		BaseElement businessObject  = Util.findBusinessObjectById(diagram, "IntermediateThrowEvent_2");
		FlowElementsContainer parentContainer = ModelUtil.getFlowElementsContainer(businessObject);
		assertThat(parentContainer.getId()).isEqualTo("SubProcess_2");

		businessObject  = Util.findBusinessObjectById(diagram, "IntermediateThrowEvent_1");
		parentContainer = ModelUtil.getFlowElementsContainer(businessObject);
		assertThat(parentContainer.getId()).isEqualTo("Process_1");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ModelUtilTest.testGetFlowElementsContainer.bpmn")
	public void testGetFlowElementsContainerForProcess() {

		BaseElement businessObject = Util.findBusinessObjectById(diagram, "_Participant_3"); 
		FlowElementsContainer parentContainer = ModelUtil.getFlowElementsContainer(businessObject);
		assertThat(parentContainer).isNull();
	}
}
