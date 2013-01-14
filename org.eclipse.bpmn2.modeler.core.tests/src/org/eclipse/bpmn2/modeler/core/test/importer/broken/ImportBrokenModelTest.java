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

import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 * 
 */
public class ImportBrokenModelTest extends AbstractImportBpmnModelTest {

	@Test
	@DiagramResource
	public void testEmptyLaneSet() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(importer.getImportWarnings()).isNotEmpty();
	}

	@Test
	@DiagramResource
	public void testParticipantReferencingNonExistingProcess() {
		try {
			ModelImport importer = createModelImport();
			importer.execute();
			Assert.fail("expected failure");
		} catch (ImportException e) {
			// expected failure
		}
	}

	@Ignore // See: HEMERA-2988
	@Test
	@DiagramResource
	public void testLaneSetSingleLaneUnreferencedFlowElements() {
		ModelImport importer = createModelImport();
		importer.execute();

		// TODO: 12-10-2012 nre: test fails because the unreferenced element
		// gets somehow associated with the lane
		assertThat(importer.getImportWarnings()).isNotEmpty();
	}

	@Test
	@DiagramResource
	public void testLaneSetMultipleLanesUnreferencedFlowElements() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(importer.getImportWarnings()).isNotEmpty();
	}

	@Test
	@DiagramResource
	public void testCallActivityReferencedByMessageFlow() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(importer.getImportWarnings()).isNotEmpty();
	}

	@Test
	@DiagramResource
	public void testSubProcessReferencedByMessageFlow() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(importer.getImportWarnings()).isNotEmpty();
	}
}
