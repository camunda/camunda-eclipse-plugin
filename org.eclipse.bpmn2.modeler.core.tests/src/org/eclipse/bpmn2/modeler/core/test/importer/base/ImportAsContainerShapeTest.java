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

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportAsContainerShapeTest extends AbstractImportBpmnModelTest {

	@Test
	@DiagramResource
	public void testElementOutsideContainer() {
		ModelImport importer = createModelImport();
		importer.execute();

		// we simulate the following behavior here:
		// * a flow element is contained in a process but drawn outside it
		// * it may not be drawn in the participant container shape of the 
		//   process because it would not show the element
		// * must be drawn in the first outer container (the diagram)
		
		// element is drawn on diagram along with its label
		assertThat(diagram).hasContainerShapeChildCount(3);
	}
	
	@Test
	@DiagramResource
	public void testElementInsideContainer() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		// element is drawn inside process
		assertThat(diagram).hasContainerShapeChildCount(2);
	}
	
	@Test
	@DiagramResource
	public void testLaneInPoolFloatingPointPositioning() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		// underlaying DI uses floating points to denote coordinates / sizes etc.
		
		// element is drawn inside process
		assertThat(diagram).hasContainerShapeChildCount(1);
	}
	
	@Test
	@DiagramResource
	public void testElementPartlyInsideContainer() {
		ModelImport importer = createModelImport();
		importer.execute();

		// we simulate the following behavior here:
		// * a flow element is contained in a process but drawn partly outside it
		// * it must still be in the participant container shape of the 
		//   process because we want to retain the containment relation 
		// (think about subprocesses)
		
		// element is drawn in participant along with its label
		assertThat(diagram).hasContainerShapeChildCount(2);
	}
}
