package org.eclipse.bpmn2.modeler.core.test.feature.layout;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import org.eclipse.bpmn2.modeler.core.layout.util.Layouter;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class LayoutShapeFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/layout/LayoutShapeFeatureTest.testBase.bpmn")
	public void testLayoutAfterMoveRepairConnections() {
		
		// given
		Shape shape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		FreeFormConnection connection1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		FreeFormConnection connection2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		// when
		// layouting shape
		Layouter.layoutShapeAfterMove(shape, false, true, getFeatureProvider());
		
		// then
		// connection layouts should have been repaired
		assertThat(connection1)
			.hasNoDiagonalEdges()
			.hasBendpoint(0, point(280, 96));
		
		assertThat(connection2)
			.hasNoDiagonalEdges()
			.hasBendpointCount(2);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/layout/LayoutShapeFeatureTest.testBase.bpmn")
	public void testLayoutAfterMoveNoRepairConnections() {
		
		// given
		Shape shape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		FreeFormConnection connection1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		FreeFormConnection connection2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		// when
		// layouting shape
		Layouter.layoutShapeAfterMove(shape, false, false, getFeatureProvider());
		
		// then
		// connection layouts should not have been touched
		assertThat(connection1)
			.hasBendpoint(0, point(280, 96));
		
		assertThat(connection2)
			.hasBendpointCount(0);
	}
	
	/**
	 * Return the feature provider for use in layouting tests
	 * 
	 * @return
	 */
	protected IFeatureProvider getFeatureProvider() {
		return getDiagramTypeProvider().getFeatureProvider();
	}
}
