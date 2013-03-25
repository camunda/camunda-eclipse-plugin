package org.camunda.bpm.modeler.test.feature.resize;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.ResizeShapeOperation.resize;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author nico.rehwaldt
 */
public class ResizeParticipantFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testResizeNoLanes.bpmn")
	public void testResizeNoLanesShouldRepositionFlowElementLabel() {
		
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		
		Shape elementShape = Util.findShapeByBusinessObjectId(diagram, "InclusiveGateway_1");
		Shape elementLabelShape = LabelUtil.getLabelShape(elementShape, getDiagram());
		
		Point shrinkAmount = point(0, 50);
		
		IRectangle preResizeLabelBounds = LayoutUtil.getAbsoluteBounds(elementLabelShape);
		
		// when
		resize(participantShape, getDiagramTypeProvider())
			.fromTopLeftBy(shrinkAmount)
			.execute();
		
		// then
		assertThat(elementLabelShape)
			.movedBy(shrinkAmount, preResizeLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testResizeNoLanes.bpmn")
	public void testResizeNoLanesShouldRepositionBoundaryEventLabel() {
		
		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, "_Participant_5");
		
		Shape elementShape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		Shape elementLabelShape = LabelUtil.getLabelShape(elementShape, getDiagram());
		
		Point shrinkAmount = point(0, 50);
		
		IRectangle preResizeLabelBounds = LayoutUtil.getAbsoluteBounds(elementLabelShape);
		
		// when
		resize(participantShape, getDiagramTypeProvider())
			.fromTopLeftBy(shrinkAmount)
			.execute();
		
		// then
		assertThat(elementLabelShape)
			.movedBy(shrinkAmount, preResizeLabelBounds);
	}
}
