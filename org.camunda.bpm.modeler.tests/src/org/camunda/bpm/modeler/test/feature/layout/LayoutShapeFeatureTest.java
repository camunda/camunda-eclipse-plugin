package org.camunda.bpm.modeler.test.feature.layout;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.camunda.bpm.modeler.core.layout.util.Layouter;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class LayoutShapeFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/layout/LayoutShapeFeatureTest.testBase.bpmn")
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
	@DiagramResource("org/camunda/bpm/modeler/test/feature/layout/LayoutShapeFeatureTest.testBase.bpmn")
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
