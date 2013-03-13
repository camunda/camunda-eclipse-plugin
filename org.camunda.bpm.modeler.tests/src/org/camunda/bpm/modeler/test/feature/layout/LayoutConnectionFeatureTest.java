package org.camunda.bpm.modeler.test.feature.layout;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.MoveBendpointOperation.moveBendpoint;
import static org.camunda.bpm.modeler.test.util.operations.MoveShapeOperation.move;
import static org.camunda.bpm.modeler.test.util.operations.RemoveBendpointOperation.removeBendpoint;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.Layouter;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class LayoutConnectionFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/layout/LayoutConnectionFeatureTest.testBase.bpmn")
	public void testLayoutAfterReconnect() {
		
		// given
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		// when
		// layouting it after reconnect with forceLayout = false
		Layouter.layoutConnectionAfterReconnect(connection, false, getFeatureProvider());
		
		// then
		// old layout should be repaired
		assertThat(connection)
			.hasNoDiagonalEdges()
			.hasBendpoint(0, point(280, 96));
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/layout/LayoutConnectionFeatureTest.testBase.bpmn")
	public void testLayoutAfterReconnectForce() {

		// given
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		// when
		// layouting it after reconnect with forceLayout = true
		Layouter.layoutConnectionAfterReconnect(connection, true, getFeatureProvider());
		
		// then
		// connection should be newly layouted
		assertThat(connection)
			.hasNoDiagonalEdges()
			.hasBendpointCount(2)
			.hasBendpoint(0, point(409, 180));
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/layout/LayoutConnectionFeatureTest.testRepairFailTestBase.bpmn")
	public void testLayoutAfterShapeMoveRepairFail() {

		// given
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		// when
		// layouting it after shape move with a non-repairable connection
		Layouter.layoutConnectionAfterShapeMove(connection, getFeatureProvider());
		
		// then
		// connection should be newly layouted
		assertThat(connection)
			.hasNoDiagonalEdges()
			.hasBendpointCount(2)
			.hasBendpoint(0, point(409, 180));
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/layout/LayoutConnectionFeatureTest.testBase.bpmn")
	public void testNoLayoutAfterBendpointMove() {

		// given
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		Point bendpoint = connection.getBendpoints().get(2);
		
		// when
		// moving bendpoint
		moveBendpoint(bendpoint, connection, getDiagramTypeProvider())
			.toLocation(point(405, 320))
			.execute();
		
		// then
		// old layout should untouched
		assertThat(connection)
			.hasBendpoint(2, point(405, 320));
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/layout/LayoutConnectionFeatureTest.testBase.bpmn")
	public void testNoLayoutAfterBendpointRemove() {

		// given
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		Point bendpoint = connection.getBendpoints().get(2);
		
		// when
		// removing bendpoint
		removeBendpoint(bendpoint, connection, getDiagramTypeProvider())
			.execute();
		
		// then
		// old layout should untouched
		assertThat(connection)
			.hasBendpoint(2, point(398, 320));
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/layout/LayoutConnectionFeatureTest.testAdjustLabelPositionBase.bpmn")
	public void testNoAdjustLabelOnUnaffectedChange() {

		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		Shape connectionLabel = LabelUtil.getLabelShape(connection, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(connectionLabel);
				
		// when
		// moving task by 20 px down
		move(taskShape, getDiagramTypeProvider())
			.by(0, 20)
			.execute();
		
		// then
		// label should not have been affected 
		// (because attached connection part did not move)
		
		IRectangle postMoveLabelPosition = LayoutUtil.getAbsoluteBounds(connectionLabel);
		assertThat(postMoveLabelPosition)
			.isEqualTo(preMoveLabelBounds);
	}


	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/layout/LayoutConnectionFeatureTest.testAdjustLabelPositionBase.bpmn")
	public void testAdjustLabelOnAttachedConnectionPartChange() {

		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		Shape connectionLabel = LabelUtil.getLabelShape(connection, getDiagram());
		
		IRectangle preMoveLabelBounds = LayoutUtil.getAbsoluteBounds(connectionLabel);
				
		// when
		// moving task by 20 px down
		move(taskShape, getDiagramTypeProvider())
			.by(0, 20)
			.execute();
		
		// then
		// label should have been affected 
		// (because attached connection part moved downwards)
		
		assertThat(connectionLabel)
			.movedBy(point(0, 20), preMoveLabelBounds);
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
