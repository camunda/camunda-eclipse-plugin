/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.camunda.bpm.modeler.test.importer.base;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

import org.camunda.bpm.modeler.core.importer.InvalidContentException;
import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.core.importer.ResourceImportException;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportEmptyDiagramTest extends AbstractImportBpmnModelTest {

	@Test
	@DiagramResource
	public void testImportEmptyCollaboration() {
		try {
			ModelImport importer = createModelImport();
			importer.execute();
			
			fail("Expected an exception: No participants in collaboration");
		} catch (InvalidContentException e) {
		}
		
	}
	
	@Test
	@DiagramResource
	public void testImportNoDefinitions() {
		try {
			ModelImport importer = createModelImport();
			importer.execute();
		
			fail("Expected exception: No definitions");
		} catch (ResourceImportException e) {
			;
		}
	}

	@Test
	@DiagramResource
	public void testImportNoRootElements() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		// no actual shapes imported
		assertThat(getDiagram()).hasContainerShapeChildCount(0);
		
		// should create a new process and diagram on the fly
		BaseElement element = BusinessObjectUtil.getFirstBaseElement(diagram);
		
		assertThat(element)
			.isNotNull()
			.isInstanceOf(Process.class);
		
		BPMNDiagram bpmnDiagram = BusinessObjectUtil.getFirstElementOfType(diagram, BPMNDiagram.class);
		
		assertThat(bpmnDiagram)
			.isNotNull();
	}
}
