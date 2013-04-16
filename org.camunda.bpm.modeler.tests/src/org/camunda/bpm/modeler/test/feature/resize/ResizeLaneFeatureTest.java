package org.camunda.bpm.modeler.test.feature.resize;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.ResizeShapeOperation.resize;

import java.util.Arrays;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.RectangleUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
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
public class ResizeLaneFeatureTest extends AbstractResizeFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeTop() {

		// y = 20 is allowed
		assertResize("Lane_8", point(0, 20), Sector.TOP);
		
		// y = 30 makes lane too small
		assertNoResize("Lane_8", point(0, 30), Sector.TOP);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeTopLeftResetHorizontalResize() {
		
		// when resizing lane horizontally from TOP_LEFT
		// then horizontal resizing should just be ignored
		assertResize("Lane_8", point(10, 20), point(0, 20), Sector.TOP_LEFT);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeBottomRightResetHorizontalResize() {

		// when resizing lane horizontally from BOTTOM_RIGHT
		// then horizontal resizing should just be ignored
		assertResize("Lane_8", point(10, -20), point(0, -20), Sector.BOTTOM_RIGHT);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeBottom() {

		// y = 20 is allowed
		assertResize("Lane_8", point(0, -20), Sector.BOTTOM_RIGHT);
		
		// y = 30 makes participant too small
		assertNoResize("Lane_8", point(0, -30), Sector.BOTTOM_RIGHT);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeWithSublanesTop() {
		
		// y = 50 is allowed
		assertResize("Lane_2", point(0, 60), Sector.TOP);
		
		// y = 60 makes participant too small
		assertNoResize("Lane_2", point(0, 70), Sector.TOP);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeNestedResizesSublanes() {

		// given
		Point resizeDelta = point(0, 60);
		Sector resizeDirection = Sector.TOP_LEFT;

		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_2");
		Shape subLaneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_3");
		Shape subSubLaneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_4");

		IRectangle expectedSubLanePostResizeBounds = LayoutUtil.getResizedAbsoluteBounds(subLaneShape, resizeDelta, resizeDirection);
		IRectangle expectedSubSubLanePostResizeBounds = LayoutUtil.getResizedAbsoluteBounds(subSubLaneShape, resizeDelta, resizeDirection);
		
		// when
		// resizing lane
		resize(laneShape, getDiagramTypeProvider())
			.by(resizeDelta, resizeDirection)
			.execute();

		// then
		// sublanes should have been resized accordingly
		assertThat(subLaneShape)
			.bounds()
				.isEqualTo(expectedSubLanePostResizeBounds);
		
		assertThat(subSubLaneShape)
			.bounds()
				.isEqualTo(expectedSubSubLanePostResizeBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeNestedTopResizesFirstSublane() {

		// given
		Point resizeDelta = point(0, 30);
		Sector resizeDirection = Sector.TOP_LEFT;

		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_11");
		Shape resizeCandidateSubLaneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_12");

		IRectangle expectedSubLanePostResizeBounds = LayoutUtil.getResizedAbsoluteBounds(resizeCandidateSubLaneShape, resizeDelta, resizeDirection);
		
		// when
		// resizing lane
		resize(laneShape, getDiagramTypeProvider())
			.by(resizeDelta, resizeDirection)
			.execute();

		// then
		// sublanes should have been resized accordingly
		assertThat(resizeCandidateSubLaneShape)
			.bounds()
				.isEqualTo(expectedSubLanePostResizeBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeNestedBottomResizesLastSublane() {

		// given
		Point resizeDelta = point(0, -30);
		Sector resizeDirection = Sector.BOTTOM_RIGHT;

		Shape laneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_11");
		Shape resizeCandidateSubLaneShape = Util.findShapeByBusinessObjectId(diagram, "Lane_13");

		IRectangle expectedSubLanePostResizeBounds = LayoutUtil.getResizedAbsoluteBounds(resizeCandidateSubLaneShape, resizeDelta, resizeDirection);
		
		// when
		// resizing lane
		resize(laneShape, getDiagramTypeProvider())
			.by(resizeDelta, resizeDirection)
			.execute();

		// then
		// sublanes should have been resized accordingly
		assertThat(resizeCandidateSubLaneShape)
			.bounds()
				.isEqualTo(expectedSubLanePostResizeBounds);
	}
		
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testShrinkAdjustsFlowElementsAndBendpoints() {
		
		assertResizeRetainsChildPositionsAndFlowLayout("Lane_8", point(0, 20), Sector.TOP_LEFT, Arrays.asList("Task_4"), Arrays.asList("SequenceFlow_4"));
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testEnlargeAdjustsFlowElementsAndBendpoints() {

		assertResizeRetainsChildPositionsAndFlowLayout("Lane_8", point(0, -20), Sector.BOTTOM_RIGHT, Arrays.asList("Task_4"), Arrays.asList("SequenceFlow_4"));
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testShrinkWithLaneAdjustsFlowElementsAndBendpoints() {
		
		assertResizeRetainsChildPositionsAndFlowLayout("Lane_11", point(0, 20), Sector.TOP_LEFT, Arrays.asList("Task_5"), Arrays.asList("SequenceFlow_7"));
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testShrinkWithLaneAdjustsFlowElementsAndBendpoints2() {
		
		assertResizeRetainsChildPositionsAndFlowLayout("Lane_5", point(0, 5), Sector.TOP_LEFT, Arrays.asList("Task_3"), Arrays.asList("SequenceFlow_3", "SequenceFlow_6"));
	}	
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testEnlargeWithLaneAdjustsFlowElementsAndBendpoints() {

		assertResizeRetainsChildPositionsAndFlowLayout("Lane_11", point(0, -20), Sector.TOP_LEFT, Arrays.asList("Task_5"), Arrays.asList("SequenceFlow_7"));
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testEnlargeLaneAdjustsLowerLaneBendpoints() {

		assertResizeRetainsChildPositionsAndFlowLayout("Lane_3", point(0, 20), Sector.TOP_LEFT, Arrays.asList("Task_3"), Arrays.asList("SequenceFlow_3"));
	}
}
