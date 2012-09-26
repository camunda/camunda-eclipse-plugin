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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramExport;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 *
 */
public class ImageExporterTest extends AbstractImportBpmnModelTest {

	@Test
	public void testGeneratePng() {
		TransactionalEditingDomain editingDomain = createEditingDomain("org/eclipse/bpmn2/modeler/core/test/bpmn/TerminateEndEventTest.testProcessTerminate.bpmn");
		
		editingDomain.getCommandStack().execute(new AbstractTestCommand(editingDomain, "test.bpmn", resource) {
			void test(IDiagramTypeProvider diagramTypeProvider) {
				Bpmn2ModelImport bpmn2ModelImport = new Bpmn2ModelImport(diagramTypeProvider, resource);
				bpmn2ModelImport.execute();
				
				
				byte[] bytes = DiagramExport.exportAsPng(diagramTypeProvider, diagram);
				writeToFile("test.png", bytes);
			}
		});	
	}

	private void writeToFile(String fileName, byte[] bytes) {

		File file = new File(fileName);
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(file);
			
			for (byte b: bytes) {
				fos.write(b);
			}
			
			fos.flush();
			fos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
