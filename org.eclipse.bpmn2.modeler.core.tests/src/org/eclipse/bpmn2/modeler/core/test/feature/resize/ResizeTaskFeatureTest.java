package org.eclipse.bpmn2.modeler.core.test.feature.resize;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.ResizeShapeOperation.resize;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author nico.rehwaldt
 */
public class ResizeTaskFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/ParticipantTaskConnectedBoundaryEvent.testBase.bpmn")
	public void testResizeNoLanesShouldRepositionFlowElementLabel() {
		
		// given
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Point resizeAmount = point(0, -50);
		
		IRectangle preResizeTaskBounds = LayoutUtil.getAbsoluteBounds(taskShape);
		
		// when
		resize(taskShape, getDiagramTypeProvider())
			.fromTopLeftBy(resizeAmount)
			.execute();
		
		// then
		assertThat(taskShape)
			.movedBy(resizeAmount, preResizeTaskBounds);
	}
}
