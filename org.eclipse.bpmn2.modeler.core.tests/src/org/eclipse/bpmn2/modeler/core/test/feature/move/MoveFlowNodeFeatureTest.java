package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.ShapeOperation.move;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Daniel Meyer
 * 
 */
public class MoveFlowNodeFeatureTest extends AbstractFeatureTest {
	
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
		//assertEquals(2, seq2Connection.getBendpoints().size());
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

//	@Test
//	@DiagramResource
//	public void testMoveStartEventVerticalLayoutSequenceFlow() {
//		Shape gatewayShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_1");
//		
//		move(gatewayShape, diagramTypeProvider)
//			.by(0 , 100)
//			.execute();
//		
//		// The MoveFlowNodeFeature will call AnchorUtil.reConnect, which will in turn recalculate the
//		// boundary anchors to update them, we need to hook in there
//		
//		// see also DefaultMoveBendPointFeature to see how a bend point is created 
//		
//		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
//		
//		assertThat(connection).hasNoDiagonalEdges();
//		assertThat(connection).hasBendpoints(2);
//	}

	@Test
	@DiagramResource
	public void testMoveStartEventVerticalLayoutSequenceFlow2() {
		Shape gatewayShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_1");
//		
//		move(gatewayShape, diagramTypeProvider)
//			.by(0 , 100)
//			.execute();
		
		// The MoveFlowNodeFeature will call AnchorUtil.reConnect, which will in turn recalculate the
		// boundary anchors to update them, we need to hook in there
		
		// see also DefaultMoveBendPointFeature to see how a bend point is created 
		
		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		assertThat(connection).hasNoDiagonalEdges();
		assertThat(connection).hasBendpointCount(2);
	}

}
