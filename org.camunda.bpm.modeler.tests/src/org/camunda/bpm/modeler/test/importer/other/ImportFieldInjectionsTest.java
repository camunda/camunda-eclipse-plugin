package org.camunda.bpm.modeler.test.importer.other;

import static org.camunda.bpm.modeler.test.util.Util.findBusinessObjectById;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import java.util.List;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.FieldType;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.Task;
import org.junit.Assert;
import org.junit.Test;

public class ImportFieldInjectionsTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportFieldInjectionsWithAttributes() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		assertThat(diagram).hasContainerShapeChildCount(7);
		
		final Task serviceTask = findBusinessObjectById(diagram, "ServiceTask_1", ServiceTask.class);
		
		List<FieldType> fieldTypeList = ExtensionUtil.getExtensions(serviceTask, FieldType.class);
		Assert.assertEquals(2, fieldTypeList.size());
		
		Assert.assertEquals("attribute", fieldTypeList.get(0).getName());
		Assert.assertEquals("myAttributeString", fieldTypeList.get(0).getStringValue());
		
		Assert.assertEquals("attributeExpression", fieldTypeList.get(1).getName());
		Assert.assertEquals("myAttributeExpression", fieldTypeList.get(1).getExpression1());
	}
	
	@Test
	@DiagramResource
	public void testImportFieldInjectionsWithElements() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		assertThat(diagram).hasContainerShapeChildCount(7);
		
		final Task serviceTask = findBusinessObjectById(diagram, "ServiceTask_1", ServiceTask.class);
		
		List<FieldType> fieldTypeList = ExtensionUtil.getExtensions(serviceTask, FieldType.class);
		Assert.assertEquals(4, fieldTypeList.size());
		
		Assert.assertEquals("elementString", fieldTypeList.get(0).getName());
		Assert.assertEquals("myStringValue", fieldTypeList.get(0).getString());
		
		Assert.assertEquals("elementExpression", fieldTypeList.get(1).getName());
		Assert.assertEquals("myExpressionValue", fieldTypeList.get(1).getExpression());
		
		Assert.assertEquals("onlyName", fieldTypeList.get(2).getName());
		Assert.assertNull(fieldTypeList.get(2).getString());
		Assert.assertNull(fieldTypeList.get(2).getExpression());
		
		Assert.assertEquals("expressionAndString", fieldTypeList.get(3).getName());
		Assert.assertEquals("myString", fieldTypeList.get(3).getString());
		Assert.assertEquals("myExpression", fieldTypeList.get(3).getExpression());
	}
	
}
