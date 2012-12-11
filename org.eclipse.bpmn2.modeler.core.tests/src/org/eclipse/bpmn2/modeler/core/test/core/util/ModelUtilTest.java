package org.eclipse.bpmn2.modeler.core.test.core.util;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

import java.util.List;

import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.junit.Test;

/**
 * Testing magic around extension attribute values
 * 
 * @author nico.rehwaldt
 */
public class ModelUtilTest extends AbstractFeatureTest {

	@Test
	@DiagramResource
	public void testGetExtensionAttributeValues() {
		CallActivity callActivity1 = (CallActivity) Util.findBusinessObjectId(diagram, "CallActivity_1");
		
		assertThat(callActivity1).isNotNull();
		
		List<ExtensionAttributeValue> callActivity1ExtensionValues = ModelUtil.getExtensionAttributeValues(callActivity1);
		
		assertThat(callActivity1ExtensionValues).hasSize(1);

		Task task1 = (Task) Util.findBusinessObjectId(diagram, "Task_1");
		
		List<ExtensionAttributeValue> task1ExtensionValues = ModelUtil.getExtensionAttributeValues(task1);
		
		assertThat(task1ExtensionValues).hasSize(0);
	}

	@Test
	@DiagramResource("src/org/eclipse/bpmn2/modeler/core/test/core/util/ModelUtilTest.testGetExtensionAttributeValues.bpmn")
	public void testGetExtensionAttributes() {
		// FIXME test with valid extension attribute
		
		fail("Should test ModelUtil#getExtensionAttributes(EObject, Class<?> cls)");
	}
}
