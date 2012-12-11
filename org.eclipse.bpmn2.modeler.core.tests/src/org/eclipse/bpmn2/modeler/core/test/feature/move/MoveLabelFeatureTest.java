package org.eclipse.bpmn2.modeler.core.test.feature.move;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class MoveLabelFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/importer/scenarios/CompatibilityTest.testAdonisDiExportCompatibility.bpmn")
	public void testMoveLabelInAdonisImport() {
		Shape startEventShape = Util.findShapeByBusinessObjectId(diagram, "_16770");
		
		System.out.println(startEventShape.getLink().getBusinessObjects());
	}
}
