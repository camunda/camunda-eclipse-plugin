package org.eclipse.bpmn2.modeler.core.test.feature.layout;

import static org.eclipse.bpmn2.modeler.core.test.util.operations.ReconnectConnectionEndOperation.reconnect;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.*;

import java.util.List;

import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.dd.dc.Point;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.junit.Test;

public class FlowReconnectTest extends AbstractFeatureTest {

	
	@Test
	@DiagramResource
	public void testManualReconnectSequenceFlow() {
		
		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		Shape task2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_2");
		
		BPMNEdge sequenceFlowBPMNEdge = BusinessObjectUtil.getFirstElementOfType(connection, BPMNEdge.class);
		
		reconnect(connection, getDiagramTypeProvider())
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
		
		IRectangle absoluteRectangle = LayoutUtil.getAbsoluteRectangle(task2);
		ILocation intersectionPoint = LayoutUtil.getChopboxIntersectionPoint(absoluteRectangle, ConversionUtil.location(lastBeforeLastWp));
		
		assertThat(intersectionPoint).isEqualsToByComparingFields(location(lastWp));
	}
}
