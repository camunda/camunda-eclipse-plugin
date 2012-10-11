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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractTestCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 *
 */
@Ignore
public class Bpmn2ModelImporterTest extends AbstractImportBpmn2ModelTest {

	@Test
	public void testCmd() {
		importDiagram("org/eclipse/bpmn2/modeler/core/test/bpmn/TerminateEndEventTest.testProcessTerminate.bpmn");
	}
	
	@Test
	public void shouldBatchOpenDiagrams() {
		List<String> importedResourceNames = new ArrayList<String>();
		Map<String, Exception> notImportedResourceNames = new HashMap<String, Exception>();
		
		List<String> resourceNames = getDiagramResources();
		int importedCount = 0;
		for (String name: resourceNames) {
			String resourceName = "org/eclipse/bpmn2/modeler/core/test/bpmn/diagrams/" + name;
			try {
				importDiagram(resourceName);
				importedResourceNames.add(resourceName);
				importedCount++;
			} catch (Exception e) {
				notImportedResourceNames.put(resourceName, e);
			}
		}
		
		System.out.println("Imported " + importedCount + "/" + resourceNames.size() + " diagrams: ");
		for (String imported: importedResourceNames) {
			System.out.println("\t" + imported);
		}
		
		System.out.println("Failed to import: ");
		for (Map.Entry<String, Exception> entry: notImportedResourceNames.entrySet()) {
			Exception exception = entry.getValue();
			System.out.println("\t" + entry.getKey() + " due to: " + exception);
			exception.printStackTrace();
		}
		
		Assert.assertEquals(resourceNames.size(), importedCount);
	}

	private void importDiagram(String resourceName) {
		System.out.println("Importing " + resourceName);
		
		try {
			TransactionalEditingDomain editingDomain = createEditingDomain(resourceName);
			editingDomain.getCommandStack().execute(new AbstractTestCommand(this, "test.bpmn") {
				public void test(IDiagramTypeProvider diagramTypeProvider, Diagram diagram) {
					Bpmn2ModelImport bpmnModelImport = new Bpmn2ModelImport(diagramTypeProvider, resource);
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
