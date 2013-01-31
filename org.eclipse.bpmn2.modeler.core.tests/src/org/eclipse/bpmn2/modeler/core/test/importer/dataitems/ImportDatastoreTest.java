package org.eclipse.bpmn2.modeler.core.test.importer.dataitems;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;

import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
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
		
		EList<Shape> children = diagram.getChildren();
		assertThat(children).hasSize(2);
		
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
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/importer/dataitems/ImportDatastoreTest.testImportDatastore.bpmn")
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
		
		EList<Shape> children = diagram.getChildren();
		
		// displaying two pools and a data store WITH label
		assertThat(children).hasSize(4);
	}
}
