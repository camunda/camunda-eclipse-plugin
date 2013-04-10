package org.camunda.bpm.modeler.test.feature.resize;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.ResizeShapeOperation.resize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.RectangleUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
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
		assertResize(shapeId, resizeDelta, resizeDelta, resizeDirection);
	}
	
	protected void assertResize(String shapeId, Point resizeDelta, Point expectedResizeDelta, Sector resizeDirection) {

		// given
		Shape shape = Util.findShapeByBusinessObjectId(diagram, shapeId);
		
		IRectangle preResizeBounds = LayoutUtil.getAbsoluteBounds(shape);
		IRectangle expectedPostResizeBounds = RectangleUtil.resize(preResizeBounds, expectedResizeDelta, resizeDirection);
		
		// when
		resize(shape, getDiagramTypeProvider())
			.by(resizeDelta, resizeDirection)
			.execute();
		
		// then
		assertThat(shape)
			.bounds()
				.isEqualTo(expectedPostResizeBounds);
	}

	/**
	 * Assert that when resizing the element with the given id
	 * by resizeAmount in resizeDirection, the child elements identified by 
	 * childElementIds retain their visual position.
	 * 
	 * Additionally, the flow elements identified by flowIds retain their layout. 
	 * 
	 * @param resizeTargetId
	 * @param resizeAmount
	 * @param resizeDirection
	 * @param childElementIds
	 * @param flowIds
	 */
	protected void assertResizeRetainsChildPositionsAndFlowLayout(
			String resizeTargetId, Point resizeAmount, Sector resizeDirection, 
			List<String> childElementIds, List<String> flowIds) {

		Map<String, Shape> shapeMap = new HashMap<String, Shape>();
		Map<String, FreeFormConnection> connectionMap = new HashMap<String, FreeFormConnection>();

		Map<Shape, IRectangle> preResizeBoundsMap = new HashMap<Shape, IRectangle>();
		
		// given
		Shape resizeTarget = Util.findShapeByBusinessObjectId(diagram, resizeTargetId);

		for (String childId : childElementIds) {
			Shape childShape = Util.findShapeByBusinessObjectId(diagram, childId);
			
			assertThat(childShape).isNotNull();
			
			shapeMap.put(childId, childShape);
			preResizeBoundsMap.put(childShape, LayoutUtil.getAbsoluteBounds(childShape));
		}

		for (String flowId : flowIds) {
			FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, flowId);
			
			assertThat(connection).isNotNull();
			
			connectionMap.put(flowId, connection);
		}
		
		// when
		resize(resizeTarget, getDiagramTypeProvider())
			.by(resizeAmount, resizeDirection)
			.execute();

		// then
		for (Shape shape : shapeMap.values()) {

			assertThat(shape)
				.bounds()
					.isEqualTo(preResizeBoundsMap.get(shape));
		}
		
		for (FreeFormConnection connection : connectionMap.values()) {
			assertThat(connection)
				.hasNoDiagonalEdges();
		}
	}
}