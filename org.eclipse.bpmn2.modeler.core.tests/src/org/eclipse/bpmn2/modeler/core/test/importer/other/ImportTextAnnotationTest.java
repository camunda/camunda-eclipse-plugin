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

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.Group;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.StringUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportTextAnnotationTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testAnnotateActivity() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateGateway() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}
	

	@Test
	@DiagramResource
	public void testAnnotateIntermediateThrowEvent() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateLane() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateNestedLane() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateBoundaryEvent() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}
	
	@Test
	@DiagramResource
	public void testAnnotatePool() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}
	
	@Test
	@DiagramResource
	public void testAnnotationOutsideParticipant() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
		
		// text annotation directly rendered on diagram
		assertThat(diagram.getChildren()).hasSize(3);
	}
}
