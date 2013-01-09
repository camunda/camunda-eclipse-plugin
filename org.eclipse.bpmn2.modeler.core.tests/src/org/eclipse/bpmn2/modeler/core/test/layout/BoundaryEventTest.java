package org.eclipse.bpmn2.modeler.core.test.layout;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.ReconnectConnectionEndOperation.reconnectEnd;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class BoundaryEventTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingBottomLeft() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_6");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingLeft() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_7");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testBottomReconnectingTopRight() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_1");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_5");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_4");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow5).hasBendpointCount(2);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingBottom() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_5");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
		
		assertThat(sequenceFlow5).hasBendpointCount(2);
	}

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingRight() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_8");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/BoundaryEventTest.testBase.bpmn")
	public void testRightReconnectingTopLeft() throws Exception {
		Shape boundaryEvent1Shape = Util.findShapeByBusinessObjectId(diagram, "BoundaryEvent_2");
		FreeFormConnection sequenceFlow5 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_4");
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "Task_3");
		reconnectConnectionAssertBoundarySector(sequenceFlow5, boundaryEvent1Shape, taskShape, Sector.BOTTOM);
	}
	
	private void reconnectConnectionAssertBoundarySector(FreeFormConnection connection, Shape boundaryEvent, Shape reconnectionTarget, Sector expectedSector) {

		reconnectEnd(connection, diagramTypeProvider)
			.toElement(reconnectionTarget)
			.execute();
		
		assertThat(connection).anchorPointOn(boundaryEvent).isAt(expectedSector);
	}
	
}
