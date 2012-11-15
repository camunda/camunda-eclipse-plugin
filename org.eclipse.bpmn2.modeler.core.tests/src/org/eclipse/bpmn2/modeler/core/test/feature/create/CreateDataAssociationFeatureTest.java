package org.eclipse.bpmn2.modeler.core.test.feature.create;

import static org.eclipse.bpmn2.modeler.core.test.util.operations.CreateDataAssocationOperation.createDataAssocation;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.bpmn2.modeler.core.test.util.TestUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * @author roman.smirnov
 */
public class CreateDataAssociationFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testCreateDataInputAssocation() {
		Shape taskShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_1");
		
		BaseElement taskElement = BusinessObjectUtil.getFirstBaseElement(taskShape);
		assertThat(taskElement).isInstanceOf(Task.class);
		Task task = (Task) taskElement;
		assertThat(task.getDataOutputAssociations()).isEmpty();
		
		Shape dataObjectShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "DataObject_1");
		
		BaseElement dataObjectElement = BusinessObjectUtil.getFirstBaseElement(dataObjectShape);
		assertThat(dataObjectElement).isInstanceOf(DataObject.class);
		DataObject dataObject = (DataObject) dataObjectElement;
		
		createDataAssocation(dataObjectShape, taskShape, diagramTypeProvider)
			.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("DataInputAssociationImpl");
		
		assertThat(task.getDataInputAssociations().size()).isEqualTo(1);
		
		DataInputAssociation dataInputAssocation = task.getDataInputAssociations().get(0);
		
		
		assertThat(dataInputAssocation.getSourceRef().size()).isEqualTo(1);
		assertThat(dataInputAssocation.getSourceRef().get(0)).isEqualTo(dataObject);
	}

	@Test
	@DiagramResource
	public void testCreateDataOutputAssocation() {
		Shape taskShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_1");

		BaseElement taskElement = BusinessObjectUtil.getFirstBaseElement(taskShape);
		assertThat(taskElement).isInstanceOf(Task.class);
		Task task = (Task) taskElement;
		assertThat(task.getDataOutputAssociations()).isEmpty();
		
		Shape dataObjectShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "DataObject_1");
		
		BaseElement dataObjectElement = BusinessObjectUtil.getFirstBaseElement(dataObjectShape);
		assertThat(dataObjectElement).isInstanceOf(DataObject.class);
		DataObject dataObject = (DataObject) dataObjectElement;
		
		createDataAssocation(taskShape, dataObjectShape, diagramTypeProvider)
			.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("DataOutputAssociationImpl");
		
		assertThat(task.getDataOutputAssociations().size()).isEqualTo(1);
		
		DataOutputAssociation dataOutputAssocation = task.getDataOutputAssociations().get(0);
		
		
		assertThat(dataOutputAssocation.getTargetRef()).isEqualTo(dataObject);
	}
	
}
