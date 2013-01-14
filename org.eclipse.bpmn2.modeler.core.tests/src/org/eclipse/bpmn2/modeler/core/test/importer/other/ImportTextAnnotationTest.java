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

import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.StringUtil;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportTextAnnotationTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testAnnotateActivity() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateGateway() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}
	

	@Test
	@DiagramResource
	public void testAnnotateIntermediateThrowEvent() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateLane() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateNestedLane() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}

	@Test
	@DiagramResource
	public void testAnnotateBoundaryEvent() {
		ModelImport importer = createModelImport();
		importer.execute();

		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}
	
	@Test
	@DiagramResource
	public void testAnnotatePool() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
	}
	
	@Test
	@DiagramResource
	public void testAnnotationOutsideParticipant() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		assertThat(StringUtil.toDetailsString(diagram)).contains("TextAnnotationImpl");
		
		// text annotation directly rendered on diagram
		assertThat(diagram.getChildren()).hasSize(3);
	}
}
