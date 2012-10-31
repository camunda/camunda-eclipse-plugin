/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.test.importer.base;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.importer.UnmappedElementException;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.junit.Test;

/**
 * Added for HEMERA-3021
 * 
 * Adonis will have Elements like DataObjects wich are lacking a DI Element. 
 * Should be able to open the model anyway.
 * 
 * @author adrobisch
 */
public class ImportWithMissingDiElements extends AbstractImportBpmnModelTest {

	@Test
	@DiagramResource
	public void testImportDataStoreWithoutDi() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		assertEquals(1, importer.getImportWarnings().size());
		assertTrue(importer.getImportWarnings().get(0) instanceof UnmappedElementException);
		assertThat(diagram.getChildren()).hasSize(1);
	}
	
}
