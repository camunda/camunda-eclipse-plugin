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

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractTestCommand;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramExport;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 *
 */
@Ignore
public class ImageExporterTest extends AbstractImportBpmn2ModelTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/bpmn/TerminateEndEventTest.testProcessTerminate.bpmn")
	public void testGeneratePng() {
		ModelImport bpmn2ModelImport = new ModelImport(diagramTypeProvider, resource);
		bpmn2ModelImport.execute();
		
		byte[] bytes = DiagramExport.exportAsPng(diagramTypeProvider, diagram);
		writeToFile("test.png", bytes);
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
