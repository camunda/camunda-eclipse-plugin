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

import org.eclipse.bpmn2.modeler.core.importer.InvalidContentException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportEmptyDiagramTest extends AbstractImportBpmn2ModelTest {

	@Test
	@DiagramResource
	public void testImportEmptyCollaboration() {
		try {
			ModelImport importer = new ModelImport(diagramTypeProvider, resource);
			importer.execute();
			fail("Expected an exception: No participants in collaboration");
		} catch (InvalidContentException e) {
		}
		
	}
	
	// This test is supposed to fail as the underlaying ecore cannot be loaded
	@Test
	@Ignore
	@DiagramResource
	public void testImportNoDefinitions() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}

	@Test
	@DiagramResource
	public void testImportNoRootElements() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		EList<Shape> children = diagram.getChildren();
		Assert.assertEquals(0, children.size());
	}
}
