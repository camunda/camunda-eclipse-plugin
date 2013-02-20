/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.test.importer.dataitems;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.StringUtil;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Fail;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportDataInputTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportDataInput() {
		ModelImport importer = createModelImport();
		importer.execute();

		// we display the data input AND its label
		assertThat(diagram.getChildren()).hasSize(2);
		
		assertThat(StringUtil.toDetailsString(diagram))
			.contains("DataInput");
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/importer/dataitems/ImportDataItems.testBaseSubProcess.bpmn")
	public void testImportOnSubProcess() {
		
		// when
		// importing diagram
		ModelImport importer = createModelImport();
		importer.execute();

		ContainerShape subprocessShape = (ContainerShape) Util.findShapeByBusinessObjectId(getDiagram(), "SubProcess_1");
		Shape dataItemShape = Util.findShapeByBusinessObjectId(getDiagram(), "DataInput_1");

		// then
		assertThat(dataItemShape)
			.isInFrontOf(subprocessShape);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/importer/dataitems/ImportDataItems.testBaseParticipant.bpmn")
	public void testImportOnParticipant() {
		
		// when
		// importing diagram
		ModelImport importer = createModelImport();
		importer.execute();

		ContainerShape laneShape = (ContainerShape) Util.findShapeByBusinessObjectId(getDiagram(), "Lane_1");
		Shape dataItemShape = Util.findShapeByBusinessObjectId(getDiagram(), "DataInput_1");

		// then
		assertThat(dataItemShape)
			.isInFrontOf(laneShape);
	}
	
	@Ignore
	@Test
	@DiagramResource
	public void testImportAssociatedDataInput() {
		ModelImport importer = createModelImport();
		importer.execute();

		Fail.fail("INVALID XML ACCORDING TO MATTHIAS!");
		
		assertThat(StringUtil.toDetailsString(diagram))
			.contains("DataAssociationImpl")
			.contains("DataInput");
	}
}
