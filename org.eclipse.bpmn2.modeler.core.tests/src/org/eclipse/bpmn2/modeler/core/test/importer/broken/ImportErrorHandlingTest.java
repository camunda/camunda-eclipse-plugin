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

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.bpmn2.Group;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.core.importer.InvalidContentException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.importer.ResourceImportException;
import org.eclipse.bpmn2.modeler.core.importer.UnmappedElementException;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.core.Condition;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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
		ModelImport importer = createModelImport();
		importer.execute();
	}
	
	@Test
	@DiagramResource
	public void testWrongSequenceFlowReference() throws Exception {
		try {
			ModelImport importer = createModelImport();
			importer.execute();
			
			Assert.fail("Expected import exception to be thrown");
		} catch (ImportException e) {
			assertThatNotUnhandledException(e);
			// expected
		}
	}

	@Test
	@Ignore
	// FIXME: Unresolvable reference error is not caught by model import in test case but in real plugin
	@DiagramResource
	public void testPartlyWrongSequenceFlowReference() throws Exception {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(importer.getImportWarnings()).isNotEmpty();
	}
	
	@Test
	@DiagramResource
	public void testMissingDiForParticipant() throws Exception {
		
		// when importing the stuff
		ModelImport importer = createModelImport();
		importer.execute();
		
		// then
		// we have 11 warnings
		assertThat(importer.getImportWarnings()).hasSize(11); 
		
		EList<Shape> children = diagram.getChildren();
		assertThat(children).isNotEmpty();
		
		Shape s = children.get(0);
		assertThat(s)
			.isLinkedTo(elementOfType(Participant.class))
			.hasContainerShapeChildCount(3);
	}
	
	/**
	 * Adonis Modeler will have Elements like DataObjects wich are lacking a DI Element. 
	 * Should be able to open the model anyway.
	 */
	@Test
	@DiagramResource
	public void testImportDataStoreWithoutDi() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		assertEquals(1, importer.getImportWarnings().size());
		assertTrue(importer.getImportWarnings().get(0) instanceof UnmappedElementException);
		assertThat(diagram).hasContainerShapeChildCount(1);
	}
	
	@Test
	@DiagramResource
	public void testLeftOverDiElement() throws Exception {

		ModelImport importer = createModelImport();
		importer.execute();
		
		assertThat(importer.getImportWarnings()).isNotEmpty();
	}
	
	@Test
	@DiagramResource
	public void testMissingReferenceInDIPlane() throws Exception {
		ModelImport importer = createModelImport();
		importer.execute();
	}

	@Test
	@DiagramResource
	public void testNoBpmn20() throws Exception {

		try {
			ModelImport importer = createModelImport();
			importer.execute();
			
			Assert.fail("Expected InvalidContentException to be thrown");
		} catch (InvalidContentException e) {
			// expected
		}
	}
	
	protected void assertThatNotUnhandledException(Exception e) {
		String message = e.getMessage();
		
		assertThat(message).doesNotContain("Unhandled exception occured");
	}
}
