package org.eclipse.bpmn2.modeler.core.test.feature.layout;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * Tests the basic layouting behavior
 * 
 * @author nico.rehwaldt
 */
public class BasicTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/layout/BoundaryEventTest.testBase.bpmn")
	public void testLayoutOverlappingElements() {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_6");
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/layout/BoundaryEventTest.testBase.bpmn")
	public void testLayoutOverlappingAnchorPoints() {
		
	}
}
