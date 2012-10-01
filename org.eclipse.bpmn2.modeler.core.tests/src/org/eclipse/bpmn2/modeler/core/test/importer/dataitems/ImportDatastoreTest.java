package org.eclipse.bpmn2.modeler.core.test.importer.dataitems;

import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractTestCommand;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Assert;
import org.junit.Test;

public class ImportDatastoreTest extends AbstractImportBpmn2ModelTest {
	
	@Test
	@DiagramResource
	public void testImportDatastore() {
				
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
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

}
