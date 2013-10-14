package org.camunda.bpm.modeler.test.layout;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.CreateDataAssocationOperation.createDataAssocation;
import static org.camunda.bpm.modeler.test.util.operations.MoveShapeOperation.move;

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author drobisch
 *
 */
public class AssociationTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testDataAssociationHasNoBendboints() {
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");

		BaseElement taskElement = BusinessObjectUtil.getFirstBaseElement(taskShape);
		Task task = (Task) taskElement;
		
		Shape dataObjectShape = Util.findShapeByBusinessObjectId(diagram, "DataObjectReference_1");
		
		createDataAssocation(taskShape, dataObjectShape, diagramTypeProvider).execute();
		
		FreeFormConnection association = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, task.getDataOutputAssociations().get(0).getId());

		assertThat(association.getBendpoints().size()).isEqualTo(0);
		
		move(dataObjectShape, diagramTypeProvider)
			.by(0 , 20)
			.execute();
		
		// should not be layouted after move
		assertThat(association.getBendpoints().size()).isEqualTo(0);
	}
	
}
