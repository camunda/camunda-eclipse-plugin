package org.eclipse.bpmn2.modeler.core.test.importer.dataitems;

import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.TestUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Assert;
import org.junit.Test;

public class ImportDatastoreTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportDatastore() {
				
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		EList<Shape> children = diagram.getChildren();
		Assert.assertEquals(2, children.size());
		
		Shape shape = children.get(0);
		PictogramLink link = shape.getLink();
		
		Assert.assertNotNull(link);
		EList<EObject> businessObjects = link.getBusinessObjects();
		Assert.assertNotNull(businessObjects);
		EObject businessObject = businessObjects.get(0);
		
		Assert.assertTrue(businessObject instanceof DataStoreReference);
		Assert.assertEquals("DataStoreRef_1", ((DataStoreReference)businessObject).getId());
		
	}
	
	@Test
	@DiagramResource
	public void testImportDatastoreDrawnOutsideParticipant() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		EList<Shape> children = diagram.getChildren();
		
		// displaying two pools and a data store WITH label
		Assert.assertEquals(4, children.size());
	}
}
