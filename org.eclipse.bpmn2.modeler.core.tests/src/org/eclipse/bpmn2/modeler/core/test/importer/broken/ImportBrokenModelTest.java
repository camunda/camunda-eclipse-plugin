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

import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
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
	public void testImportEmptyLaneSet() {
		try {
			Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
			importer.execute();
			Assert.fail("expected failure");
		} catch (ImportException e) {
			// expected failure
		}
	}
	
	@Test
	@DiagramResource
	public void testImportParticipantReferencingNonExistingProcess() {
		try {
			Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
			importer.execute();
			Assert.fail("expected failure");
		} catch (ImportException e) {
			// expected failure
		}
	}
	
	@Test
	@DiagramResource
	public void testImportLaneSetSingleLaneUnreferencedFlowElements1() {
		try {
			Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
			importer.execute();
			
			// TODO: Fails because elements __NOT__ referenced in lane get somehow associated
			Assert.fail("expected failure");
		} catch (ImportException e) {
			// expected failure
		}
	}

	@Test
	@DiagramResource
	public void testImportLaneSetMultipleLanesUnreferencedFlowElements() {
		try {
			Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
			importer.execute();
			Assert.fail("expected failure");
		} catch (ImportException e) {
			// expected failure
		}
	}

	@Test
	@DiagramResource
	public void testImportEmptyCollaborationNoDI() {
		try {
			Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
			importer.execute();
			Assert.fail("expected failure");
		} catch (ImportException e) {
			// expected failure
		}
	}
}
