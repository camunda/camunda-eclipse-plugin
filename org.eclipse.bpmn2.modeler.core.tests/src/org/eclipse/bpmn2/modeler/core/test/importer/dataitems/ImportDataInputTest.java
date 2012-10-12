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

import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.Group;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.TestUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Fail;
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
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		assertThat(TestUtil.toDetailsString(diagram))
			.contains("DataInput");
	}
	
	@Test
	@DiagramResource
	public void testImportAssociatedDataInput() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		Fail.fail("INVALID XML ACCORDING TO MATTHIAS!!!!!!111");
		
		assertThat(TestUtil.toDetailsString(diagram))
			.contains("DataAssociationImpl")
			.contains("DataInput");
	}
}
