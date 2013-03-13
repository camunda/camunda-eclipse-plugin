package org.camunda.bpm.modeler.test.feature.create;

import static org.camunda.bpm.modeler.test.util.operations.CreateDataAssocationOperation.createDataAssocation;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.StringUtil;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * @author roman.smirnov
 */
public class CreateDataAssociationFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testCreateDataInputAssocation() {
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		BaseElement taskElement = BusinessObjectUtil.getFirstBaseElement(taskShape);
		assertThat(taskElement).isInstanceOf(Task.class);
		Task task = (Task) taskElement;
		assertThat(task.getDataOutputAssociations()).isEmpty();
		
		Shape dataObjectShape = Util.findShapeByBusinessObjectId(diagram, "DataObject_1");
		
		BaseElement dataObjectElement = BusinessObjectUtil.getFirstBaseElement(dataObjectShape);
		assertThat(dataObjectElement).isInstanceOf(DataObject.class);
		DataObject dataObject = (DataObject) dataObjectElement;
		
		createDataAssocation(dataObjectShape, taskShape, diagramTypeProvider)
			.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("DataInputAssociationImpl");
		
		assertThat(task.getDataInputAssociations().size()).isEqualTo(1);
		
		DataInputAssociation dataInputAssocation = task.getDataInputAssociations().get(0);
		
		
		assertThat(dataInputAssocation.getSourceRef().size()).isEqualTo(1);
		assertThat(dataInputAssocation.getSourceRef().get(0)).isEqualTo(dataObject);
	}

	@Test
	@DiagramResource
	public void testCreateDataOutputAssocation() {
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");

		BaseElement taskElement = BusinessObjectUtil.getFirstBaseElement(taskShape);
		assertThat(taskElement).isInstanceOf(Task.class);
		Task task = (Task) taskElement;
		assertThat(task.getDataOutputAssociations()).isEmpty();
		
		Shape dataObjectShape = Util.findShapeByBusinessObjectId(diagram, "DataObject_1");
		
		BaseElement dataObjectElement = BusinessObjectUtil.getFirstBaseElement(dataObjectShape);
		assertThat(dataObjectElement).isInstanceOf(DataObject.class);
		DataObject dataObject = (DataObject) dataObjectElement;
		
		createDataAssocation(taskShape, dataObjectShape, diagramTypeProvider)
			.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("DataOutputAssociationImpl");
		
		assertThat(task.getDataOutputAssociations().size()).isEqualTo(1);
		
		DataOutputAssociation dataOutputAssocation = task.getDataOutputAssociations().get(0);
		
		
		assertThat(dataOutputAssocation.getTargetRef()).isEqualTo(dataObject);
	}
	
}
