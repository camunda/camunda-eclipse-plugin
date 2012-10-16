/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.test.importer.broken;

import static org.eclipse.bpmn2.modeler.core.test.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.core.importer.InvalidContentException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.importer.ResourceImportException;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 *
 */
public class ImportErrorHandlingTest extends AbstractImportBpmn2ModelTest {

	@Test
	@DiagramResource
	public void testNoXml() throws Exception {
		
		try {
			ModelImport importer = new ModelImport(diagramTypeProvider, resource);
			importer.execute();
			
			Assert.fail("Expected import exception to be thrown");
		} catch (ResourceImportException e) {
			// expected
		}
	}
	
	@Test
	@DiagramResource
	public void testWrongSequenceFlowReference() throws Exception {
		try {
			ModelImport importer = new ModelImport(diagramTypeProvider, resource);
			importer.execute();
			
			Assert.fail("Expected import exception to be thrown");
		} catch (InvalidContentException e) {
			// expected
		}
	}

	@Test
	@DiagramResource
	public void testPartlyWrongSequenceFlowReference() throws Exception {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(importer.getImportWarnings()).isNotEmpty();
	}
	
	@Test
	@DiagramResource
	public void testLeftOverDiElement() throws Exception {

		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		assertThat(importer.getImportWarnings()).isNotEmpty();
	}

	@Test
	@DiagramResource
	public void testNoBpmn20() throws Exception {

		try {
			ModelImport importer = new ModelImport(diagramTypeProvider, resource);
			importer.execute();
			
			Assert.fail("Expected InvalidContentException to be thrown");
		} catch (InvalidContentException e) {
			// expected
		}
	}
}
