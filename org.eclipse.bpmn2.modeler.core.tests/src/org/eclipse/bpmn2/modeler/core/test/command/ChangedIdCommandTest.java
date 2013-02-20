package org.eclipse.bpmn2.modeler.core.test.command;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ChangedIdCommandTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/command/ChangedIdCommandTest.bpmn")
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
