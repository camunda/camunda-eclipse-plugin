/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.camunda.bpm.modeler.test.importer.association.data;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.StringUtil;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportDataAssociationTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testDataAssociationsPool() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram))
			.contains("DataInputAssociationImpl")
			.contains("DataOutputAssociationImpl");
	}
	
	@Test
	@DiagramResource
	public void testDataAssociationsSimpleProcess() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram))
			.contains("DataInputAssociationImpl")
			.contains("DataOutputAssociationImpl");
	}
}
