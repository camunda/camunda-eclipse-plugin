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

import static org.fest.assertions.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.core.importer.InvalidContentException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.importer.ResourceImportException;
import org.eclipse.bpmn2.modeler.core.importer.UnmappedElementException;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.fest.assertions.api.Fail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Categories.ExcludeCategory;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 *
 */
public class ImportErrorHandlingTest extends AbstractImportBpmnModelTest {

	@Test(expected=ResourceImportException.class)
	@DiagramResource
	public void testNoXml() throws Exception {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
	
	@Test
	@DiagramResource
	public void testWrongSequenceFlowReference() throws Exception {
		try {
			ModelImport importer = new ModelImport(diagramTypeProvider, resource);
			importer.execute();
			
			Assert.fail("Expected import exception to be thrown");
		} catch (ImportException e) {
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
	public void testMissingDiForParticipant() throws Exception {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		assertEquals(11, importer.getImportWarnings().size()); // Participant Error + 5 * 2 Referenced Elements in flows
		assertThat(((ContainerShape) diagram.getChildren().get(0)).getChildren()).hasSize(7);
	}
	
	/**
	 * Adonis Modeler will have Elements like DataObjects wich are lacking a DI Element. 
	 * Should be able to open the model anyway.
	 */
	@Test
	@DiagramResource
	public void testImportDataStoreWithoutDi() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		assertEquals(1, importer.getImportWarnings().size());
		assertTrue(importer.getImportWarnings().get(0) instanceof UnmappedElementException);
		assertThat(diagram.getChildren()).hasSize(1);
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
	public void testMissingReferenceInDIPlane() throws Exception {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
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
