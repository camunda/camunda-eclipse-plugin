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

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.importer.InvalidContentException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportEmptyDiagramTest extends AbstractImportBpmnModelTest {

	@Test
	@DiagramResource
	public void testImportEmptyCollaboration() {
		try {
			ModelImport importer = new ModelImport(diagramTypeProvider, resource);
			importer.execute();
			fail("Expected an exception: No participants in collaboration");
		} catch (InvalidContentException e) {
		}
		
	}
	
	// This test is supposed to fail as the underlaying ecore cannot be loaded
	@Test
	@Ignore
	@DiagramResource
	public void testImportNoDefinitions() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}

	@Test
	@DiagramResource
	public void testImportNoRootElements() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		EList<Shape> children = diagram.getChildren();
		assertThat(children).isEmpty();
		
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
