package org.camunda.bpm.modeler.test.importer.dataitems;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Assert;
import org.junit.Test;

public class ImportDatastoreTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportDatastore() {
				
		ModelImport importer = createModelImport();
		importer.execute();
		
		assertThat(diagram).hasContainerShapeChildCount(2);
		
		EList<Shape> children = getDiagram().getChildren();
		Shape shape = children.get(0);
		PictogramLink link = shape.getLink();
		
		Assert.assertNotNull(link);
		EList<EObject> businessObjects = link.getBusinessObjects();
		Assert.assertNotNull(businessObjects);
		EObject businessObject = businessObjects.get(0);
		
		assertThat(businessObject).isInstanceOf(DataStoreReference.class);
		assertThat(((DataStoreReference)businessObject).getId()).isEqualTo("DataStoreRef_1");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/dataitems/ImportDataItems.testBaseSubProcess.bpmn")
	public void testImportOnSubProcess() {
		
		// when
		// importing diagram
		ModelImport importer = createModelImport();
		importer.execute();

		ContainerShape subprocessShape = (ContainerShape) Util.findShapeByBusinessObjectId(getDiagram(), "SubProcess_1");
		Shape dataItemShape = Util.findShapeByBusinessObjectId(getDiagram(), "_DataStoreReference_4");
		
		// then
		assertThat(dataItemShape)
			.isInFrontOf(subprocessShape);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/dataitems/ImportDataItems.testBaseParticipant.bpmn")
	public void testImportOnParticipantNoWarning() {
		
		// when
		// importing diagram with datastore on lane
		ModelImport importer = createModelImport();
		importer.execute();
		
		// then
		// no import warning should occur
		assertThat(importer.getImportWarnings())
			.isEmpty();
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/dataitems/ImportDataItems.testBaseParticipant.bpmn")
	public void testImportOnParticipant() {
		
		// when
		// importing diagram
		ModelImport importer = createModelImport();
		importer.execute();

		ContainerShape laneShape = (ContainerShape) Util.findShapeByBusinessObjectId(getDiagram(), "Lane_1");
		Shape dataItemShape = Util.findShapeByBusinessObjectId(getDiagram(), "_DataStoreReference_3");
		
		// then
		assertThat(dataItemShape)
			.isInFrontOf(laneShape);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/dataitems/ImportDatastoreTest.testImportDatastore.bpmn")
	public void testImportDatastoreShouldGetBoundsFromDi() {
		
		// given 
		IRectangle expectedBounds = rectangle(310, 210, 50, 50);

		// when
		ModelImport importer = createModelImport();
		importer.execute();
		
		Shape dataStoreShape = Util.findShapeByBusinessObjectId(diagram, "DataStoreRef_1");
		IRectangle dataStoreBounds = LayoutUtil.getAbsoluteBounds(dataStoreShape);
		
		assertThat(dataStoreBounds).isEqualTo(expectedBounds);
	}
	
	@Test
	@DiagramResource
	public void testImportDatastoreDrawnOutsideParticipant() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		// displaying two pools and a data store WITH label
		assertThat(diagram).hasContainerShapeChildCount(4);
	}
}
