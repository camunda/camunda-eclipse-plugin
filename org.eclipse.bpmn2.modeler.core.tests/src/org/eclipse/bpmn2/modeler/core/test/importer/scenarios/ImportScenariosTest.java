/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.test.importer.scenarios;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportScenariosTest extends AbstractImportBpmnModelTest {

	@Test
	@DiagramResource
	public void testImportComplexCase() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
}
