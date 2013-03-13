package org.camunda.bpm.modeler.test.command;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ChangedIdCommandTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/command/ChangedIdCommandTest.bpmn")
	public void testChangeTaskId() throws Exception {
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		BaseElement taskElement = BusinessObjectUtil.getFirstBaseElement(taskShape);
		
		setNewId(taskElement, "myTask");
		
		assertThat(DIUtils.getShape(taskShape).getBpmnElement().getId()).isEqualTo("myTask");
	}
	
	private void setNewId(BaseElement element, String newId) {
		ModelUtil.setValue(editingDomain, element, Bpmn2Package.eINSTANCE.getBaseElement_Id(), newId);
	}

}
