/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.camunda.bpm.modeler.test.importer.dataitems;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.StringUtil;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportDataObjectReferenceTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImport() {
		
		// when
		importDiagram();

		// we display the two data object references and their labels
		assertThat(diagram).hasContainerShapeChildCount(4);
		
		assertThat(StringUtil.toDetailsString(diagram))
			.contains("DataObjectReferenceImpl");
	}
	
	@Test
	@DiagramResource
	public void testImportNoDataObject() {
		
		// when
		importDiagram();

		// we display the data object reference AND its label
		assertThat(diagram).hasContainerShapeChildCount(2);
		
		assertThat(StringUtil.toDetailsString(diagram))
			.contains("DataObjectReferenceImpl");
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/dataitems/ImportDataItems.testBaseSubProcess.bpmn")
	public void testImportOnSubProcess() {

		// when
		importDiagram();

		ContainerShape subprocessShape = (ContainerShape) Util.findShapeByBusinessObjectId(getDiagram(), "SubProcess_1");
		Shape dataItemShape = Util.findShapeByBusinessObjectId(getDiagram(), "DataObjectReference_1");

		// then
		assertThat(dataItemShape)
			.isNotNull()
			.isInFrontOf(subprocessShape);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/dataitems/ImportDataItems.testBaseParticipant.bpmn")
	public void testImportOnParticipant() {
		
		// when
		importDiagram();

		ContainerShape laneShape = (ContainerShape) Util.findShapeByBusinessObjectId(getDiagram(), "Lane_1");
		Shape dataItemShape = Util.findShapeByBusinessObjectId(getDiagram(), "DataObjectReference_1");

		// then
		assertThat(dataItemShape)
			.isNotNull()
			.isInFrontOf(laneShape);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/dataitems/ImportDataObjectReferenceTest.testImport.bpmn")
	public void testImportBoundsFromDi() {
		
		// given 
		IRectangle expectedBounds = rectangle(490, 66, 36, 65);

		// when
		importDiagram();
		
		Shape dataObjectShape = Util.findShapeByBusinessObjectId(diagram, "_DataObjectReference_2");
		
		assertThat(dataObjectShape).isNotNull();
		
		IRectangle dataStoreBounds = LayoutUtil.getAbsoluteBounds(dataObjectShape);
		
		assertThat(dataStoreBounds).isEqualTo(expectedBounds);
	}
	
	@Test
	@DiagramResource
	public void testImportAssociated() {
		// when
		importDiagram();
	}
	
	@Test
	@DiagramResource
	public void testImportReferencedFromLane() {
		// when
		importDiagram();
	}
}
