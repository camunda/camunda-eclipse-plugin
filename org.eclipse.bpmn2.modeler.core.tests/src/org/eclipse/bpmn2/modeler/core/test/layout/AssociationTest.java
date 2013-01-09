package org.eclipse.bpmn2.modeler.core.test.layout;

import static org.eclipse.bpmn2.modeler.core.test.util.operations.CreateDataAssocationOperation.createDataAssocation;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveFlowElementOperation.move;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
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
		
		Shape dataObjectShape = Util.findShapeByBusinessObjectId(diagram, "DataObject_1");
		
		createDataAssocation(taskShape, dataObjectShape, diagramTypeProvider)
			.execute();
		
		FreeFormConnection association = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, task.getDataOutputAssociations().get(0).getId());

		assertThat(association.getBendpoints().size()).isEqualTo(0);
		
		move(dataObjectShape, diagramTypeProvider)
			.by(0 , 20)
			.execute();
		
		// should not be layouted after move
		assertThat(association.getBendpoints().size()).isEqualTo(0);
	}
	
}
