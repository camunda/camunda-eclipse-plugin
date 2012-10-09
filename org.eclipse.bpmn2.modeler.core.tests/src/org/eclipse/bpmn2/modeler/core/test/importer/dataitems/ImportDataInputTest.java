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

import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.Group;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.TestUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class ImportDataInputTest extends AbstractImportBpmn2ModelTest {
	
	@Test
	@DiagramResource
	public void testImportDataInput() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();

		EList<Shape> children = diagram.getChildren();
		Assert.assertEquals(1, children.size());

		System.out.println(TestUtil.toDetailsString(diagram));
		
		Shape shape = children.get(0);
		PictogramLink link = shape.getLink();
		
		Assert.assertNotNull(link);
		EList<EObject> businessObjects = link.getBusinessObjects();
		Assert.assertNotNull(businessObjects);
		EObject businessObject = businessObjects.get(0);
		
		Assert.assertTrue(businessObject instanceof DataInput);
		Assert.assertEquals("DataInput_1", ((Group)businessObject).getId());
	}
	
	@Test
	@DiagramResource
	public void testImportAssociatedDataInput() {
		Bpmn2ModelImport importer = new Bpmn2ModelImport(diagramTypeProvider, resource);
		importer.execute();
	}
}
