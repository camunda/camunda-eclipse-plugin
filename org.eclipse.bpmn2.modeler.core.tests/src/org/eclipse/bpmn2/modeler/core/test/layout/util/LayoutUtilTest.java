package org.eclipse.bpmn2.modeler.core.test.layout.util;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class LayoutUtilTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testHorizontalTreshold() {
		Shape task1 = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape start2 = Util.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		Shape task2 = Util.findShapeByBusinessObjectId(diagram, "Task_2");

		assertThat(LayoutUtil.getHorizontalLayoutTreshold(LayoutUtil.getAbsoluteShapeCenter(task1), LayoutUtil.getAbsoluteShapeCenter(start2))).isGreaterThan(0);
		assertThat(LayoutUtil.getHorizontalLayoutTreshold(LayoutUtil.getAbsoluteShapeCenter(start2), LayoutUtil.getAbsoluteShapeCenter(task1))).isLessThan(0);
		assertThat(LayoutUtil.getHorizontalLayoutTreshold(LayoutUtil.getAbsoluteShapeCenter(start2), LayoutUtil.getAbsoluteShapeCenter(task2))).isEqualTo(0);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testAssertNoDiagonalEdgesPass() {
		Shape start1 = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape task1 = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Shape start2 = Util.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		Shape task2 = Util.findShapeByBusinessObjectId(diagram, "Task_2");

		assertThat(LayoutUtil.getLayoutTreshold(start1, task1)).isEqualTo(-0.683);
		assertThat(LayoutUtil.getLayoutTreshold(task1, start1)).isEqualTo(0.682); // 45 degree
		
		double treshold1 = LayoutUtil.getLayoutTreshold(start2, task2);
		assertThat(treshold1).isEqualTo(1.0);

		double treshold2 = LayoutUtil.getLayoutTreshold(task1, task2);
		assertThat(treshold2).isEqualTo(0.0);
		
		double treshold3 = LayoutUtil.getLayoutTreshold(task2, task1);
		assertThat(treshold3).isEqualTo(0.0);
		
		double treshold4 = LayoutUtil.getLayoutTreshold(start2, task1);
		assertThat(treshold4).isEqualTo(0.668);
		
		double treshold5 = LayoutUtil.getLayoutTreshold(task2, start1); // target is top right
		assertThat(treshold5).isEqualTo(0.69);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testGetShapeLocationMidPoint() {
		
		// <dc:Bounds height="50.0" width="110.0" x="205.0" y="35.0"/>
		
		Shape task1 = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		// when
		ILocation location = LayoutUtil.getAbsoluteShapeCenter(task1);
		
		// then
		assertThat(point(location)).isEqualTo(point(110 / 2 + 205, 50 / 2 + 35));
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testLeftRightDetection () {
		Shape start1 = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape task1 = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Shape start2 = Util.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		Shape task2 = Util.findShapeByBusinessObjectId(diagram, "Task_2");
		
		assertTrue(LayoutUtil.isRightToStartShape(start2, task1));
		assertTrue(LayoutUtil.isLeftToStartShape(start1, task2));
		
		assertFalse(LayoutUtil.isRightToStartShape(task1, task2));
		assertFalse(LayoutUtil.isLeftToStartShape(task1, task2));
	}
	
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testAboveBeneathDetection () {
		Shape start1 = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape task1 = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Shape start2 = Util.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		Shape task2 = Util.findShapeByBusinessObjectId(diagram, "Task_2");
		
		assertTrue(LayoutUtil.isAboveStartShape(start2, task1));
		assertTrue(LayoutUtil.isBeneathStartShape(task1, start1));
		
		assertFalse(LayoutUtil.isAboveStartShape(start2, task2));
		assertFalse(LayoutUtil.isBeneathStartShape(start2, task2));
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testZoneDetection () {
		FreeFormConnection flow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		assertThat(LayoutUtil.getEndShapeSector(flow2)).isEqualTo(Sector.BOTTOM_RIGHT);
		
		FreeFormConnection flow3 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		assertThat(LayoutUtil.getEndShapeSector(flow3)).isEqualTo(Sector.BOTTOM);
		
		FreeFormConnection flow4 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		assertThat(LayoutUtil.getEndShapeSector(flow4)).isEqualTo(Sector.BOTTOM_LEFT);
		
		FreeFormConnection flow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		assertThat(LayoutUtil.getEndShapeSector(flow5)).isEqualTo(Sector.LEFT);
		
		FreeFormConnection flow6 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_6");
		assertThat(LayoutUtil.getEndShapeSector(flow6)).isEqualTo(Sector.TOP_LEFT);
		
		FreeFormConnection flow7 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_7");
		assertThat(LayoutUtil.getEndShapeSector(flow7)).isEqualTo(Sector.TOP);
		
		FreeFormConnection flow8 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_8");
		assertThat(LayoutUtil.getEndShapeSector(flow8)).isEqualTo(Sector.TOP_RIGHT);
		
		FreeFormConnection flow9 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_9");
		assertThat(LayoutUtil.getEndShapeSector(flow9)).isEqualTo(Sector.RIGHT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testGetSourceBpmnElement () {
		FreeFormConnection flow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		assertThat(LayoutUtil.getSourceBaseElement(flow2).getId()).isEqualTo("StartEvent_11");
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testGetBoundaryEventRelativeSector() {
		Shape boundaryEvent1 = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		assertThat(LayoutUtil.getBoundaryEventRelativeSector(boundaryEvent1)).isEqualTo(Sector.TOP_LEFT);

		Shape boundaryEvent2 = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		assertThat(LayoutUtil.getBoundaryEventRelativeSector(boundaryEvent2)).isEqualTo(Sector.TOP_RIGHT);
		
		Shape boundaryEvent3 = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_3");
		assertThat(LayoutUtil.getBoundaryEventRelativeSector(boundaryEvent3)).isEqualTo(Sector.BOTTOM_RIGHT);
		
		Shape boundaryEvent4 = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_4");
		assertThat(LayoutUtil.getBoundaryEventRelativeSector(boundaryEvent4)).isEqualTo(Sector.BOTTOM_LEFT);
		
		Shape boundaryEvent5 = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_5");
		assertThat(LayoutUtil.getBoundaryEventRelativeSector(boundaryEvent5)).isEqualTo(Sector.TOP);

		Shape boundaryEvent6 = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_6");
		assertThat(LayoutUtil.getBoundaryEventRelativeSector(boundaryEvent6)).isEqualTo(Sector.BOTTOM);
		
		Shape boundaryEvent7 = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_7");
		assertThat(LayoutUtil.getBoundaryEventRelativeSector(boundaryEvent7)).isEqualTo(Sector.RIGHT);
		
		Shape boundaryEvent8 = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_8");
		assertThat(LayoutUtil.getBoundaryEventRelativeSector(boundaryEvent8)).isEqualTo(Sector.LEFT);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testDefaultAnchors.bpmn")
	public void testTaskDefaultAnchors() throws Exception {
		Shape task = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		
		// no specific anchors attached to element
		// all anchors should be default
		
		for (Anchor a: task.getAnchors()) {
			assertThat(LayoutUtil.isDefaultAnchor(a)).isTrue();
		}
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testDefaultAnchors.bpmn")
	public void testEventDefaultAnchors() throws Exception {
		Shape event = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		// no specific anchors attached to element
		// all anchors should be default
		
		for (Anchor a: event.getAnchors()) {
			assertThat(LayoutUtil.isDefaultAnchor(a)).isTrue();
		}
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testDefaultAnchors.bpmn")
	public void testGatewayDefaultAnchors() throws Exception {
		Shape gateway = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");

		// no specific anchors attached to element
		// all anchors should be default
		
		for (Anchor a: gateway.getAnchors()) {
			assertThat(LayoutUtil.isDefaultAnchor(a)).isTrue();
		}
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testSharedConnections.bpmn")
	public void testGetSharedConnectionIncomming() throws Exception {
		
		// given
		Shape s1 = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape s2 = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");

		// when
		Set<Connection> sharedConnections = LayoutUtil.getSharedConnections(s1, Arrays.asList(s2));
		
		// then
		assertThat(sharedConnections).hasSize(1);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testSharedConnections.bpmn")
	public void testGetSharedConnectionOutgoing() throws Exception {
		
		// given
		Shape s1 = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape s2 = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");

		// when
		Set<Connection> sharedConnections = LayoutUtil.getSharedConnections(s2, Arrays.asList(s1));
		
		// then
		assertThat(sharedConnections).hasSize(1);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testSharedConnections.bpmn")
	public void testGetSharedConnectionSelfReferencing() throws Exception {
		
		// given
		Shape s1 = Util.findShapeByBusinessObjectId(diagram, "Task_1");

		// when
		Set<Connection> sharedConnections = LayoutUtil.getSharedConnections(s1, Arrays.asList(s1));
		
		// then
		assertThat(sharedConnections).hasSize(1);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testSharedConnections.bpmn")
	public void testGetSharedConnectionOutgoingMultiple() throws Exception {
		
		// given
		Shape s1 = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape s2 = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		Shape s3 = Util.findShapeByBusinessObjectId(diagram, "EndEvent_2");
		Shape s4 = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");

		// when
		Set<Connection> sharedConnections = LayoutUtil.getSharedConnections(s2, Arrays.asList(s1, s3, s4));
		
		// then
		assertThat(sharedConnections).hasSize(2);
	}
	
	@Test
	@DiagramResource
	public void testContainerBoundsInCollaboration() {
		// given
		Diagram diagramUnderTest = diagram;
		
		// when
		IRectangle diagramBounds = LayoutUtil.getBounds(diagramUnderTest, 0, 0, 0, 0);
		
		// then
		assertThat(diagramBounds).isEqualTo(ConversionUtil.rect(230, 236, 503, 416));
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testContainerBoundsInCollaboration.bpmn")
	public void testContainerBoundsWithPadding() {
		// given
		Diagram diagramUnderTest = diagram;
		
		// when
		IRectangle diagramBounds = LayoutUtil.getBounds(diagramUnderTest, 0, 0, 100, 100);
		
		// then
		assertThat(diagramBounds).isEqualTo(ConversionUtil.rect(130, 136, 603, 516));
	}
	
	@Test
	@DiagramResource
	public void testContainerBoundsInProcess() {
		// given
		Diagram diagramUnderTest = diagram;
		
		// when
		IRectangle diagramBounds = LayoutUtil.getBounds(diagramUnderTest, 0, 0, 0, 0);
		
		// then
		assertThat(diagramBounds).isEqualTo(ConversionUtil.rect(270, 276, 442, 85));
	}
	
}
