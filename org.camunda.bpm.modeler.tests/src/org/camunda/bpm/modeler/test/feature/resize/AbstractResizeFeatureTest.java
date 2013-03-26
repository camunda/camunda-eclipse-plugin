package org.camunda.bpm.modeler.test.feature.resize;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.ResizeShapeOperation.resize;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.RectangleUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Abstract class for resize feature tests
 * 
 * @author nico.rehwaldt
 */
public class AbstractResizeFeatureTest extends AbstractFeatureTest {
	
	//// helpers ///////////////////////////////////////////////////
	
	protected void assertNoResize(String shapeId, Point resizeDelta, Sector resizeDirection) {

		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, shapeId);
		
		IRectangle preResizeBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		
		// when
		resize(participantShape, getDiagramTypeProvider())
			.by(resizeDelta, resizeDirection)
			.execute();
		
		// then
		assertThat(participantShape)
			.bounds()
				.isEqualTo(preResizeBounds);
	}
	
	protected void assertResize(String shapeId, Point resizeDelta, Sector resizeDirection) {

		// given
		Shape participantShape = Util.findShapeByBusinessObjectId(diagram, shapeId);
		
		IRectangle preResizeBounds = LayoutUtil.getAbsoluteBounds(participantShape);
		IRectangle expectedPostResizeBounds = RectangleUtil.resize(preResizeBounds, resizeDelta, resizeDirection);
		
		// when
		resize(participantShape, getDiagramTypeProvider())
			.by(resizeDelta, resizeDirection)
			.execute();
		
		// then
		assertThat(participantShape)
			.bounds()
				.isEqualTo(expectedPostResizeBounds);
	}
}