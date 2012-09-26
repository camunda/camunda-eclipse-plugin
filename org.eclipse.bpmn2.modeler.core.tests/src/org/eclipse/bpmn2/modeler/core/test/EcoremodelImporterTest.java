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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.bpmn2.modeler.core.ecore.BpmnModelImport;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramExport;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
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
		importDiagramAndDispose("org/eclipse/bpmn2/modeler/core/test/bpmn/TerminateEndEventTest.testProcessTerminate.bpmn");
	}
	
	@Test
	public void shouldBatchOpenDiagrams() {
		List<String> resourceNames = getDiagramResources();
		int importedCount = 0;
		for (String name: resourceNames) {
			try {
				importDiagramAndDispose("org/eclipse/bpmn2/modeler/core/test/bpmn/diagrams/" + name);
				importedCount++;
			} catch (Exception e) {
				System.out.println("Failed to import due to " + e);
				e.printStackTrace(System.err);
				System.err.flush();
			}
		}
		
		System.out.println("Imported " + importedCount + "/" + resourceNames.size() + " diagrams");
		Assert.assertEquals(resourceNames.size(), importedCount);
	}

	@Test
	public void testGeneratePng() {
		TransactionalEditingDomain editingDomain = createEditingDomain("org/eclipse/bpmn2/modeler/core/test/bpmn/TerminateEndEventTest.testProcessTerminate.bpmn");
		
		editingDomain.getCommandStack().execute(new AbstractTestCommand(editingDomain, "test.bpmn") {
			void test(IDiagramTypeProvider diagramTypeProvider) {
				BpmnModelImport bpmnModelImport = new BpmnModelImport(diagramTypeProvider, resource);
				bpmnModelImport.execute();
				
				byte[] bytes = DiagramExport.exportAsPng(diagramTypeProvider, diagram);
			}
		});	
	}
	
	private void importDiagramAndDispose(String resourceName) {
		System.out.println("Importing " + resourceName);
		
		try {
			TransactionalEditingDomain editingDomain = createEditingDomain(resourceName);
			editingDomain.getCommandStack().execute(new AbstractTestCommand(editingDomain, "test.bpmn") {
				void test(IDiagramTypeProvider diagramTypeProvider) {
					BpmnModelImport bpmnModelImport = new BpmnModelImport(diagramTypeProvider, resource);
					bpmnModelImport.execute();
				}
			});
			
			System.out.println("Done.");
		} finally {
			disposeEditingDomain();
		}
	}
	
	private List<String> getDiagramResources() {
		File directory = new File("src/org/eclipse/bpmn2/modeler/core/test/bpmn/diagrams/");
		if (!directory.exists()) {
			return Collections.emptyList();
		}
		
		File[] contents = directory.listFiles();
		
		ArrayList<String> names = new ArrayList<String>();
		for (File f: contents) {
			if (f.isFile()) {
				names.add(f.getName());
			}
		}
		
		return names;
	}

}
