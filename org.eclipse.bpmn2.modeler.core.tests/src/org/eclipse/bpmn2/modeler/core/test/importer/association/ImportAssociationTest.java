/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.test.importer.association;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.TestUtil;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportAssociationTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testAssociateDataStore() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("AssociationImpl");
	}
	
	@Test
	@DiagramResource
	public void testAssociateDataStoreByXPath() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("AssociationImpl");
	}
	

	@Test
	@DiagramResource
	public void testUnresolvedAssociationOnDataStore() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		assertThat(TestUtil.toDetailsString(diagram)).contains("AssociationImpl");
	}

	@Test
	@DiagramResource
	public void testResolvedAssociationOnDataStore() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("AssociationImpl");
	}
}
