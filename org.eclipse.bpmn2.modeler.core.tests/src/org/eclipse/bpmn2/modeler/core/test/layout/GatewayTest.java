package org.eclipse.bpmn2.modeler.core.test.layout;

import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

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
public class GatewayTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/GatewayTest.testVertical.bpmn")
	public void testVerticalLayoutingTopTwoBendpoints() {

		Shape task = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape gateway = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		FreeFormConnection flow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		// gateway is vertically centered above task
		
		// when moved by 30 px to the right
		move(gateway, getDiagramTypeProvider())
			.by(30, 0)
			.execute();
		
		// then two bendpoint strategy should kick in
		assertThat(flow)
			.hasNoDiagonalEdges()
			
			.hasBendpointCount(2)
			
			.anchorPointOn(task)
				.isAboveShape();
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/GatewayTest.testVertical.bpmn")
	public void testVerticalLayoutingTopTwoBendpointsTolerance() {

		Shape task = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape gateway = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		FreeFormConnection flow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		// gateway is vertically centered above task
		
		// when moved by 55 px to the right
		move(gateway, getDiagramTypeProvider())
			// shape width is 100 -> mid + 55 is outside shape but still in tolerance box
			.by(55, 0)
			.execute();
		
		// then two bendpoint strategy should kick in
		assertThat(flow)
			.hasNoDiagonalEdges()
			
			.hasBendpointCount(2)
			
			.anchorPointOn(task)
				.isAboveShape();
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/GatewayTest.testVertical.bpmn")
	public void testVerticalLayoutingTopSingleBendpoint() {

		Shape task = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape gateway = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		FreeFormConnection flow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		
		// gateway is vertically centered above task
		
		// when moved by 80 px to the right
		move(gateway, getDiagramTypeProvider())
			.by(80, 0)
			.execute();
		
		// then one point bendpoint strategy should kick in
		assertThat(flow)
			.hasNoDiagonalEdges()
			
			.hasBendpointCount(1)
			
			.anchorPointOn(task)
				.isRightOfShape();
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/GatewayTest.testVertical.bpmn")
	public void testVerticalLayoutingBottomTwoBendpoints() {

		Shape task = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape gateway = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_2");
		FreeFormConnection flow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		// gateway is vertically centered above task
		
		// when moved by 30 px to the left
		move(gateway, getDiagramTypeProvider())
			.by(-30, 0)
			.execute();
		
		// then two bendpoint strategy should kick in
		assertThat(flow)
			.hasNoDiagonalEdges()
			
			.hasBendpointCount(2)
			
			.anchorPointOn(task)
				.isBeneathShape();
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/GatewayTest.testVertical.bpmn")
	public void testVerticalLayoutingBottomTwoBendpointsTolerance() {

		Shape task = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape gateway = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_2");
		FreeFormConnection flow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		// gateway is vertically centered above task
		
		// when moved by 55 px to the left
		move(gateway, getDiagramTypeProvider())
			// shape width is 100 -> mid + 55 is outside shape but still in tolerance box
			.by(-55, 0)
			.execute();
		
		// then two bendpoint strategy should kick in
		assertThat(flow)
			.hasNoDiagonalEdges()
			
			.hasBendpointCount(2)
			
			.anchorPointOn(task)
				.isBeneathShape();
	}

	@Test
	@DiagramResource
	public void testVerticalMoveBetween() {

		Shape task1 = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape task2 = Util.findShapeByBusinessObjectId(diagram, "Task_2");
		
		Shape gateway = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");

		FreeFormConnection flow1 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		FreeFormConnection flow2 = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		// gateway is positioned to the left of both tasks
		
		// when moved by some amount to the right
		move(gateway, getDiagramTypeProvider())
			.by(125, 0)
			.execute();
		
		// then 
		// the two point bendpoint strategy should kick in
		// on both sides
		assertThat(flow1)
			.hasNoDiagonalEdges()
			
			.hasBendpointCount(2)
			
			.anchorPointOn(task1)
				.isBeneathShape();
		
		assertThat(flow2)
			.hasNoDiagonalEdges()
			
			.hasBendpointCount(2)
			
			.anchorPointOn(task2)
				.isAboveShape();
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/GatewayTest.testVertical.bpmn")
	public void testVerticalLayoutingBottomSingleBendpoint() {

		Shape task = Util.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape gateway = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_2");
		FreeFormConnection flow = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
		// gateway is vertically centered above task
		
		// when moved by 80 px to the left
		move(gateway, getDiagramTypeProvider())
			.by(-80, 0)
			.execute();
		
		// then one point bendpoint strategy should kick in
		assertThat(flow)
			.hasNoDiagonalEdges()
			
			.hasBendpointCount(1)
			
			.anchorPointOn(task)
				.isLeftOfShape();
	}
}
