/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.test.importer.association.data;

import static org.eclipse.bpmn2.modeler.core.test.assertions.Bpmn2ModelAssertions.assertThat;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.TestUtil;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportDataAssociationTest extends AbstractImportBpmn2ModelTest {
	
	@Test
	@DiagramResource
	public void testDataAssociationsPool() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram))
			.contains("DataInputAssociationImpl")
			.contains("DataOutputAssociationImpl");
	}
	
	@Test
	@DiagramResource
	public void testDataAssociationsSimpleProcess() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram))
			.contains("DataInputAssociationImpl")
			.contains("DataOutputAssociationImpl");
	}
}
