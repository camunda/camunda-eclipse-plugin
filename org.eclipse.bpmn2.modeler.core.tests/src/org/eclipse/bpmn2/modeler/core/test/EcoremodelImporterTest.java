/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.test;

import org.eclipse.bpmn2.modeler.core.ecore.BpmnModelImport;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 *
 */
public class EcoremodelImporterTest extends AbstractImportBpmnModelTest {

	@Test
	public void testCmd() {

		createEditingDomain("org/eclipse/bpmn2/modeler/core/test/bpmn/TerminateEndEventTest.testProcessTerminate.bpmn");

		editingDomain.getCommandStack().execute(new AbstractTestCommand(editingDomain, "test.bpmn") {
			void test(IDiagramTypeProvider diagramTypeProvider) {
				BpmnModelImport bpmnModelImport = new BpmnModelImport(diagramTypeProvider, resource);
				bpmnModelImport.execute();
			}
		});
	}

}
