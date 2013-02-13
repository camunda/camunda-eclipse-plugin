package org.eclipse.bpmn2.modeler.core.test.feature.resize;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.ResizeShapeOperation.resize;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author nico.rehwaldt
 */
public class ResizeParticipantFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/resize/ResizeParticipantFeatureTest.testResizeNoLanes.bpmn")
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
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/resize/ResizeParticipantFeatureTest.testResizeNoLanes.bpmn")
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
