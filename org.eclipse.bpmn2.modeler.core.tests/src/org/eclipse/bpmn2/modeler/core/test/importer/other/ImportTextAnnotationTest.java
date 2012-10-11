/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.test.importer.other;

import static org.eclipse.bpmn2.modeler.core.test.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.Group;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.TestUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportTextAnnotationTest extends AbstractImportBpmn2ModelTest {
	
	@Test
	@DiagramResource
	public void testAnnotateActivity() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateGateway() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}
	

	@Test
	@DiagramResource
	public void testAnnotateIntermediateThrowEvent() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateLane() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateNestedLane() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateBoundaryEvent() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}
	
	@Test
	@DiagramResource
	public void testAnnotatePool() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		assertThat(TestUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}
}
