package org.camunda.bpm.modeler.test.feature.resize;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.ResizeShapeOperation.resize;

import java.util.Arrays;
import java.util.Collections;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
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
public class ResizeParticipantFeatureTest extends AbstractResizeFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testResizeNoLanes.bpmn")
	public void testResizeNoLanesShouldNotRepositionFlowElementLabel() {
		
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
			.bounds()
				.isEqualTo(preResizeLabelBounds);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testResizeNoLanes.bpmn")
	public void testResizeNoLanesShouldNotRepositionBoundaryEventLabel() {
		
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
			.bounds()
				.isEqualTo(preResizeLabelBounds);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeTop() {

		// y = 50 is allowed
		assertResize("Participant_1", point(0, 50), Sector.TOP_LEFT);
		
		// y = 60 makes participant too small
		assertNoResize("Participant_1", point(0, 60), Sector.TOP_LEFT);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeBottom() {

		// y = 50 is allowed
		assertResize("Participant_1", point(0, -50), Sector.BOTTOM_LEFT);
		
		// y = 60 makes participant too small
		assertNoResize("Participant_1", point(0, -60), Sector.BOTTOM_LEFT);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeWithFlowElementsTop() {

		// y = 50 is allowed
		assertResize("_Participant_3", point(0, 50), Sector.TOP_LEFT);
		
		// y = 60 makes participant too small
		assertNoResize("_Participant_3", point(0, 60), Sector.TOP_LEFT);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeWithFlowElementsBottom() {

		// y = 50 is allowed
		assertResize("_Participant_3", point(0, -50), Sector.BOTTOM_LEFT);
		
		// y = 60 makes participant too small
		assertNoResize("_Participant_3", point(0, -60), Sector.BOTTOM_LEFT);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeComplexTop() {

		// y = 70 is allowed
		assertResize("Participant_2", point(0, 70), Sector.TOP_LEFT);
		
		// y = 80 makes participant too small
		assertNoResize("Participant_2", point(0, 80), Sector.TOP_LEFT);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeComplexBottom() {

		// y = 40 is allowed
		assertResize("Participant_1", point(0, -40), Sector.BOTTOM_LEFT);
		
		// y = 50 makes participant too small
		assertNoResize("Participant_1", point(0, -50), Sector.BOTTOM_LEFT);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeComplexLeft() {

		// x = 10 is allowed
		assertResize("Participant_2", point(10, 0), Sector.LEFT);
		
		// x = 20 would collide (lane 4)
		assertNoResize("Participant_2", point(20, 0), Sector.LEFT);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testResizeComplexRight() {

		// y = 40 is allowed
		assertResize("Participant_2", point(-40, 0), Sector.RIGHT);
		
		// y = 50 would collide (lane 1)
		assertNoResize("Participant_2", point(-50, 0), Sector.RIGHT);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testShrinkAdjustsFlowElementsAndBendpoints() {
		
		assertResizeRetainsChildPositionsAndFlowLayout("_Participant_3", point(0, 50), Sector.TOP_LEFT, Arrays.asList("StartEvent_1"), Arrays.asList("SequenceFlow_1"));
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testEnlargeAdjustsFlowElementsAndBendpoints() {

		assertResizeRetainsChildPositionsAndFlowLayout("_Participant_3", point(0, -50), Sector.TOP_LEFT, Arrays.asList("StartEvent_1"), Arrays.asList("SequenceFlow_1"));
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testShrinkWithLaneAdjustsFlowElementsAndBendpoints() {
		
		assertResizeRetainsChildPositionsAndFlowLayout("Participant_4", point(0, 20), Sector.TOP_LEFT, Arrays.asList("Task_4"), Arrays.asList("SequenceFlow_4"));
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testEnlargeWithlaneAdjustsFlowElementsAndBendpoints() {

		assertResizeRetainsChildPositionsAndFlowLayout("Participant_4", point(0, -20), Sector.TOP_LEFT, Arrays.asList("Task_4"), Arrays.asList("SequenceFlow_4"));
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeParticipantFeatureTest.testBase.bpmn")
	public void testEnlargeLaneWithlaneAdjustsFlowElementsAndBendpoints() {

		assertResizeRetainsChildPositionsAndFlowLayout("Lane_4", point(0, 20), Sector.BOTTOM, Collections.<String>emptyList(), Arrays.asList("SequenceFlow_3"));
	}	
}
