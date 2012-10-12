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

import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
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
public class ImportBrokenModelTest extends AbstractImportBpmn2ModelTest {

	@Test
	@DiagramResource
	public void testEmptyLaneSet() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		assertThat(importer.getImportWarnings()).isNotEmpty();
	}
	
	@Test
	@DiagramResource
	public void testParticipantReferencingNonExistingProcess() {
		try {
			ModelImport importer = new ModelImport(diagramTypeProvider, resource);
			importer.execute();
			Assert.fail("expected failure");
		} catch (ImportException e) {
			// expected failure
		}
	}
	
	@Test
	@DiagramResource
	public void testLaneSetSingleLaneUnreferencedFlowElements() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		// TODO: 12-10-2012 nre: test fails because the unreferenced element gets somehow associated with the lane
		assertThat(importer.getImportWarnings()).isNotEmpty();
	}

	@Test
	@DiagramResource
	public void testLaneSetMultipleLanesUnreferencedFlowElements() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		assertThat(importer.getImportWarnings()).isNotEmpty();
	}

	@Test
	@DiagramResource
	public void testEmptyCollaborationNoDI() {
		try {
			ModelImport importer = new ModelImport(diagramTypeProvider, resource);
			importer.execute();
			Assert.fail("expected failure");
		} catch (ImportException e) {
			// expected failure
		}
	}
}
