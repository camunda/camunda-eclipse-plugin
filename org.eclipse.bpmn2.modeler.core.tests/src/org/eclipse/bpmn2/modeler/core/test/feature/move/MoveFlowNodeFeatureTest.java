package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.ShapeOperation.move;
import static org.junit.Assert.assertEquals;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.junit.Test;

/**
 * 
 * @author Daniel Meyer
 * 
 */
public class MoveFlowNodeFeatureTest extends AbstractFeatureTest {
	

	@Test
	@DiagramResource
	public void testMoveTaskVerticalFindGatewayAnchorThreshold() {         //Shoe
		Shape taskShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "UserTask_1");
		ContainerShape laneShape = (ContainerShape) ShapeUtil.findShapeByBusinessObjectId(diagram, "Lane_1");
		
		// Move target shape --> UserTask_1 is lower than upper edge of GateWay_1
		move(taskShape, diagramTypeProvider)
		.by(0 , -49)	
		.toContainer(laneShape)
		.execute();			
		
		FreeFormConnection seq3Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");	
		// Get y-Value of sequence start and compare
		assertEquals(230,Graphiti.getPeLayoutService().getLocationRelativeToDiagram(seq3Connection.getStart()).getY());
		
		// Move target shape --> UserTask_1 is equal to upper edge of GateWay_1
		move(taskShape, diagramTypeProvider)
		.by(0 , -1)	
		.toContainer(laneShape)
		.execute();		
		
		seq3Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		Shape gatewayShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		// Get y-Value of sequence start and compare		
		assertEquals((230 - (gatewayShape.getGraphicsAlgorithm().getHeight()/2)) ,Graphiti.getPeLayoutService().getLocationRelativeToDiagram(seq3Connection.getStart()).getY());
	}	
	
	@Test
	@DiagramResource
	public void testMoveTask2VerticalLayout() {         
		Shape taskShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "ReceiveTask_1");
		ContainerShape laneShape = (ContainerShape) ShapeUtil.findShapeByBusinessObjectId(diagram, "Lane_2");
		
		// Move target shape
		move(taskShape, diagramTypeProvider)
		.by(0 , 176)
		.toContainer(laneShape)
		.execute();
		
		// The MoveFlowNodeFeature will call AnchorUtil.reConnect, which will in turn recalculate the
		// boundary anchors to update them, we need to hook in there
		
		// see also DefaultMoveBendPointFeature to see how a bend point is created 
		
		// Get bendpoints incoming sequence flow
		FreeFormConnection seqConnection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_7");
		// and check
		assertEquals(1, seqConnection.getBendpoints().size());
		assertEquals(Graphiti.getPeLayoutService().getLocationRelativeToDiagram(seqConnection.getEnd()).getY(), seqConnection.getBendpoints().get(0).getY());

		// Get bendpoints outgoing sequence flow
		seqConnection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_8");
		// and check
		assertEquals(2, seqConnection.getBendpoints().size());		
	}
	
	@Test
	@DiagramResource
	public void testMoveGatewayVerticalLayout() {
		Shape gatewayShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		ContainerShape laneShape = (ContainerShape) ShapeUtil.findShapeByBusinessObjectId(diagram, "Lane_1");
		
		move(gatewayShape, diagramTypeProvider)
		.by(0 , -85)
		.toContainer(laneShape)
		.execute();
		
		// The MoveFlowNodeFeature will call AnchorUtil.reConnect, which will in turn recalculate the
		// boundary anchors to update them, we need to hook in there
		
		// see also DefaultMoveBendPointFeature to see how a bend point is created 
		
		FreeFormConnection seq2Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		
//		org.eclipse.dd.dc.Point point = DcFactory.eINSTANCE.createPoint();
//		point.setX(10);
//		point.setY(20);
//		
//		DIUtils.addBendPoint(seq2Connection, point);
		assertEquals(2, seq2Connection.getBendpoints().size());
	}
	
	@Test
	@DiagramResource
	public void testMoveShapeOutOfContainer() {
		
		// find shapes
		Shape userTaskShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "UserTask_1");
		ContainerShape subProcessShape = (ContainerShape) ShapeUtil.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		ContainerShape processShape = (ContainerShape) diagram;
		BaseElement subProcessElement = BusinessObjectUtil.getFirstBaseElement(subProcessShape);

		// first, the usertask is contained in the subprocess
		assertThat(subProcessShape).hasChild(userTaskShape);
		assertThat(processShape).doesNotHaveChild(userTaskShape);
		assertThat(userTaskShape).hasParentModelElement(subProcessElement);

		// move the usertask out from under the subprocess into the process
		move(userTaskShape, diagramTypeProvider)
			.toContainer(processShape)
			.execute();
		
		// now the usertask is contained in the process
		assertThat(subProcessShape).doesNotHaveChild(userTaskShape);
		assertThat(processShape).hasChild(userTaskShape);
		assertThat(userTaskShape).hasParentModelElement(subProcessElement.eContainer());
		
	}
	
	@Test
	@DiagramResource
	public void testMoveShapeIntoContainer() {
		
		// find shapes
		Shape userTaskShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "UserTask_1");
		ContainerShape subProcessShape = (ContainerShape) ShapeUtil.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		ContainerShape processShape = (ContainerShape) diagram;
		BaseElement subProcessElement = BusinessObjectUtil.getFirstBaseElement(subProcessShape);

		// first, the usertask is contained in the process
		assertThat(subProcessShape).doesNotHaveChild(userTaskShape);
		assertThat(processShape).hasChild(userTaskShape);
		assertThat(userTaskShape).hasParentModelElement(subProcessElement.eContainer());

		// move the usertask into the subprocess
		move(userTaskShape, diagramTypeProvider)
			.toContainer(subProcessShape)
			.execute();
		
		// now the usertask is contained in the subprocess
		assertThat(subProcessShape).hasChild(userTaskShape);
		assertThat(processShape).doesNotHaveChild(userTaskShape);		
		assertThat(userTaskShape).hasParentModelElement(subProcessElement);
		
	}

}
