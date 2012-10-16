/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.test.importer.dataitems;

import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.TestUtil;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportDataObjectTest extends AbstractImportBpmn2ModelTest {
	
	@Test
	@DiagramResource
	public void testImportDataObject() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		// we display the data object AND its label
		assertThat(diagram.getChildren()).hasSize(2);
		
		assertThat(TestUtil.toDetailsString(diagram))
			.contains("DataObjectImpl");
	}
	
	@Test
	@DiagramResource
	public void testImportAssociatedDataObject() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
	
	@Test
	@DiagramResource
	public void testImportDataObjectReferencedFromLane() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
}
