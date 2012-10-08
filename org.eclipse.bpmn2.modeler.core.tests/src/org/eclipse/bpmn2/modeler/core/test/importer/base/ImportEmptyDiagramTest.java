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

import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportEmptyDiagramTest extends AbstractImportBpmn2ModelTest {

	// This test is supposed to fail as the underlaying ecore cannot be loaded
	@Test
	@Ignore
	@DiagramResource
	public void testImportNoDefinitions() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
  
	@Test
	@DiagramResource
	public void testImportNonProcessRootElements() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}

	@Test
	@DiagramResource
	public void testImportNoRootElements() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
}
