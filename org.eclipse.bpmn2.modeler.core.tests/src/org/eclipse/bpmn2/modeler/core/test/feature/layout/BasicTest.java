package org.eclipse.bpmn2.modeler.core.test.feature.layout;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.test.util.operations.MoveFlowElementOperation;
import org.eclipse.graphiti.datatypes.IRectangle;
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
		
		// when moving shape above connected shape
		MoveFlowElementOperation.move(taskShape, getDiagramTypeProvider())
			.by(130, -91)
			.execute();
		
		// make sure we can do this (no exception thrown)
		
		IRectangle rect = LayoutUtil.getAbsoluteRectangle(taskShape);
		
		// then movement was executed
		assertThat(point(rect)).isEqualTo(point(360, 289));
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/layout/BoundaryEventTest.testBase.bpmn")
	public void testLayoutOverlappingAnchorPoints() {
			
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_6");
		
		// when moving both anchor points above each other
		MoveFlowElementOperation.move(taskShape, getDiagramTypeProvider())
			.by(110, -130)
			.execute();
		
		// make sure we can do this (no exception thrown)
		
		IRectangle rect = LayoutUtil.getAbsoluteRectangle(taskShape);
		
		// then movement was executed
		assertThat(point(rect)).isEqualTo(point(340, 250));
	}
}
