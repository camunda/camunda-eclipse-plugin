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

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.StringUtil;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportDataObjectTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportDataObject() {
		ModelImport importer = createModelImport();
		importer.execute();

		// we display the data object AND its label
		assertThat(diagram).hasContainerShapeChildCount(2);
		
		assertThat(StringUtil.toDetailsString(diagram))
			.contains("DataObjectImpl");
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/importer/dataitems/ImportDataItems.testBaseSubProcess.bpmn")
	public void testImportOnSubProcess() {
		
		// when
		// importing diagram
		ModelImport importer = createModelImport();
		importer.execute();

		ContainerShape subprocessShape = (ContainerShape) Util.findShapeByBusinessObjectId(getDiagram(), "SubProcess_1");
		Shape dataItemShape = Util.findShapeByBusinessObjectId(getDiagram(), "DataObject_1");

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
		Shape dataItemShape = Util.findShapeByBusinessObjectId(getDiagram(), "DataObject_1");

		// then
		assertThat(dataItemShape)
			.isInFrontOf(laneShape);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/importer/dataitems/ImportDataObjectTest.testImportDataObject.bpmn")
	public void testImportDataObjectShouldGetBoundsFromDi() {
		
		// given 
		IRectangle expectedBounds = rectangle(490, 66, 36, 65);

		// when
		ModelImport importer = createModelImport();
		importer.execute();
		
		Shape dataObjectShape = Util.findShapeByBusinessObjectId(diagram, "DataObject_1");
		IRectangle dataStoreBounds = LayoutUtil.getAbsoluteBounds(dataObjectShape);
		
		assertThat(dataStoreBounds).isEqualTo(expectedBounds);
	}
	
	@Test
	@DiagramResource
	public void testImportAssociatedDataObject() {
		ModelImport importer = createModelImport();
		importer.execute();
	}
	
	@Test
	@DiagramResource
	public void testImportDataObjectReferencedFromLane() {
		ModelImport importer = createModelImport();
		importer.execute();
	}
}
