/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.test.importer.dataitems;

import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportDataObjectTest extends AbstractImportBpmn2ModelTest {
	
	@Test
	@DiagramResource
	public void testImportDataObject() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
	
	@Test
	@DiagramResource
	public void testImportAssociatedDataObject() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
}
