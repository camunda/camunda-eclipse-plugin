package org.eclipse.bpmn2.modeler.core.test.feature.add;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddPoolOperation.addPool;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.junit.Test;

public class AddPoolFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource
	public void testAddPoolToProcessWithBoundaryEvent() throws Exception {
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		// assume task is contained in diagram
		assertThat(taskShape)
			.isContainedIn(diagram);
		
		// when
		// adding pool to diagram
		addPool(diagramTypeProvider)
			.toContainer(diagram)
			.execute();

		// then
		// check new parent of taskShape ...
		EObject taskContainer = taskShape.eContainer();
		
		assertThat(taskContainer)
			.isInstanceOf(ContainerShape.class);
		
		BaseElement businessObject = BusinessObjectUtil.getFirstBaseElement((PictogramElement) taskContainer);

		// pool should contain task
		// (AND no exception is thrown)
		assertThat(businessObject)
			.isInstanceOf(Participant.class);
	}
}
