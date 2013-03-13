package org.camunda.bpm.modeler.test.layout;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.location;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.MoveShapeOperation.move;
import static org.camunda.bpm.modeler.test.util.operations.ReconnectConnectionOperation.reconnectEnd;

import java.util.List;

import org.camunda.bpm.modeler.core.layout.util.ConversionUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.dd.dc.Point;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * Testing layouting after reconnection of flows
 * 
 * @author nico.rehwaldt
 */
public class FlowReconnectTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testManualReconnectSequenceFlow() {
		
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		Shape task2 = Util.findShapeByBusinessObjectId(diagram, "Task_2");
		
		BPMNEdge sequenceFlowBPMNEdge = BusinessObjectUtil.getFirstElementOfType(connection, BPMNEdge.class);
		
		reconnectEnd(connection, getDiagramTypeProvider())
			.toElement(task2)
			.execute();
		
		// check if way points are correct in graphiti model
		assertThat(task2.getAnchors().size()).isEqualTo(6);

		assertThat(connection.getEnd()).isInstanceOf(ChopboxAnchor.class);
		assertThat(connection.getEnd().getParent()).isSameAs(task2);

		// check if waypoints are correct in DI
		List<Point> waypoints = sequenceFlowBPMNEdge.getWaypoint();
		assertThat(waypoints).hasSize(4);

		Point lastBeforeLastWp = waypoints.get(2);
		Point lastWp = waypoints.get(3);
		
		IRectangle absoluteRectangle = LayoutUtil.getAbsoluteBounds(task2);
		ILocation intersectionPoint = LayoutUtil.getChopboxIntersectionPoint(absoluteRectangle, ConversionUtil.location(lastBeforeLastWp));
		
		assertThat(intersectionPoint).isEqualsToByComparingFields(location(lastWp));
	}
	
	@Test
	@DiagramResource
	public void testManualReconnectWithLanes() {
		Shape serviceTask1 = Util.findShapeByBusinessObjectId(diagram, "ServiceTask_1");
		ContainerShape lane2Shape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Lane_2");
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		BPMNEdge sequenceFlowBPMNEdge = BusinessObjectUtil.getFirstElementOfType(connection, BPMNEdge.class);
		
		// Move target shape
		move(serviceTask1, diagramTypeProvider)
			.by(0 , 40)
			.toContainer(lane2Shape)
			.execute();
		
		reconnectEnd(connection, getDiagramTypeProvider())
			.toElement(serviceTask1)
			.execute();
		
		ILocation endAnchorLocation = LayoutUtil.getVisibleAnchorLocation(connection.getEnd(), connection);
		
		assertThat(endAnchorLocation.getX()).isEqualTo(288);
		assertThat(endAnchorLocation.getY()).isEqualTo(550);
		
		List<Point> waypoints = sequenceFlowBPMNEdge.getWaypoint();
		Point lastWp = waypoints.get(3);
		
		assertThat(lastWp.getX()).isEqualTo(288);
		assertThat(lastWp.getY()).isEqualTo(550);
	}
	
	
}
