/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.camunda.bpm.modeler.test.importer.dataitems.legacy;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.camunda.bpm.modeler.core.importer.ImportException;
import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.StringUtil;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * Data Objects have been wrongly imported as visual elements in old versions of 
 * the modeler. Upon import, those dataObjects should be converted to 
 * dataObjectReferences. 
 * 
 * @author Nico Rehwaldt
 */
public class ImportDataObjectTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImport() {
		
		// when
		ModelImport importer = importDiagramIgnoreWarnings();
		
		// then
		assertConversionImportWarning(importer);
		
		// we display the data object AND its label
		assertThat(diagram).hasContainerShapeChildCount(2);
		
		// expect implicit conversion to data object reference
		assertThat(StringUtil.toDetailsString(diagram))
			.contains("DataObjectReferenceImpl");
	}

	@Test
	@DiagramResource
	public void testImportOnSubProcess() {

		// when
		ModelImport importer = importDiagramIgnoreWarnings();
		
		// then
		assertConversionImportWarning(importer);
		
		ContainerShape subprocessShape = (ContainerShape) Util.findShapeByBusinessObjectId(getDiagram(), "SubProcess_1");
		Shape dataItemShape = Util.findShapeByBusinessObjectId(getDiagram(), "DataObjectReference_1");

		// then
		assertThat(dataItemShape)
			.isInFrontOf(subprocessShape);
	}
	
	@Test
	@DiagramResource
	public void testImportOnParticipant() {

		// when
		ModelImport importer = importDiagramIgnoreWarnings();
		
		// then
		assertConversionImportWarning(importer);
		
		ContainerShape laneShape = (ContainerShape) Util.findShapeByBusinessObjectId(getDiagram(), "Lane_1");
		Shape dataItemShape = Util.findShapeByBusinessObjectId(getDiagram(), "DataObjectReference_1");

		// then
		assertThat(dataItemShape).isInFrontOf(laneShape);
	}
	
	@Test
	@DiagramResource
	public void testImportAssociation() {

		// when
		ModelImport importer = importDiagramIgnoreWarnings();
		
		// then
		assertConversionImportWarning(importer);
	}
	
	@Test
	@DiagramResource
	public void testImportDataInputOutputAssociation() {

		// when
		ModelImport importer = importDiagramIgnoreWarnings();
		
		// then
		assertConversionImportWarning(importer);
	}
	
	@Test
	@DiagramResource
	public void testImportReferencedFromLane() {

		// when
		ModelImport importer = importDiagramIgnoreWarnings();
		
		// then
		assertConversionImportWarning(importer);
	}

	protected void assertConversionImportWarning(ModelImport importer) {
		List<ImportException> importWarnings = importer.getImportWarnings();
		
		// we should have a conversion import warning
		assertThat(importWarnings).hasSize(1);
	}
}
