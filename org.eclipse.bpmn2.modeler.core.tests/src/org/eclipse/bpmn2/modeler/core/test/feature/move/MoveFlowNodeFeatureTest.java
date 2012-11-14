package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import static org.eclipse.bpmn2.modeler.core.test.util.operations.ShapeOperation.move;
import static org.junit.Assert.assertEquals;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeLayoutService;
import org.junit.Test;

/**
 * 
 * @author Daniel Meyer
 * @author Nico Rehwaldt
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
		
		FreeFormConnection seq2Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		FreeFormConnection seq3Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		FreeFormConnection seq7Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_7");
		
		// check incoming sequence flow
		assertEquals(2, seq2Connection.getBendpoints().size());
		
		// check bendboints coordiantes
		assertThat(seq2Connection.getBendpoints().get(0)).isEqualTo(point(420, 229));
		
		assertThat(seq2Connection.getBendpoints().get(1)).isEqualTo(point(420, 144));
		
		// start anchor must be centered on the right side
		assertEquals(110, ((FixPointAnchor) seq2Connection.getStart()).getLocation().getX());
		assertEquals(25, ((FixPointAnchor) seq2Connection.getStart()).getLocation().getY());

		// end anchor must be centered on the left side
		assertEquals(0, ((FixPointAnchor) seq2Connection.getEnd()).getLocation().getX());
		assertEquals(25, ((FixPointAnchor) seq2Connection.getEnd()).getLocation().getY());
		
		//check outgoing sequence flow left
		assertEquals(2, seq3Connection.getBendpoints().size());
		
		assertThat(seq3Connection.getBendpoints().get(0)).isEqualTo(point(511, 144));
		assertThat(seq3Connection.getBendpoints().get(1)).isEqualTo(point(511, 229));

		// start anchor must be centered on the right side
		assertEquals(51, ((FixPointAnchor) seq3Connection.getStart()).getLocation().getX());
		assertEquals(25, ((FixPointAnchor) seq3Connection.getStart()).getLocation().getY());
		
		// end anchor must be centered on the left side
		assertEquals(0, ((FixPointAnchor) seq3Connection.getEnd()).getLocation().getX());
		assertEquals(25, ((FixPointAnchor) seq3Connection.getEnd()).getLocation().getY());
		
		//check outgoing sequence flow bottom
		assertEquals(1, seq7Connection.getBendpoints().size());
		assertThat(seq7Connection.getBendpoints().get(0)).isEqualTo(point(473, 330));
		
		//start anchor must be centered at the bottom side
		assertEquals(25, ((FixPointAnchor) seq7Connection.getStart()).getLocation().getX());
		assertEquals(51, ((FixPointAnchor) seq7Connection.getStart()).getLocation().getY());
				
		// end anchor must be centered on the left side
		assertEquals(0, ((FixPointAnchor) seq7Connection.getEnd()).getLocation().getX());
		assertEquals(25, ((FixPointAnchor) seq7Connection.getEnd()).getLocation().getY());
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

	@Test
	@DiagramResource
	public void testMoveTaskVerticalLayout() {
	   Shape taskShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_1");
	   ContainerShape participantShape = (ContainerShape) ShapeUtil.findShapeByBusinessObjectId(diagram, "Participant_1");
	   
	   FreeFormConnection seq1Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
	   FreeFormConnection seq2Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
	    
	   assertThat(seq1Connection.getBendpoints()).isEmpty();
	   assertThat(seq2Connection.getBendpoints()).isEmpty();
	   
	   move(taskShape, diagramTypeProvider)
	    .by(0 , 180)
	    .toContainer(participantShape)
	    .execute();

	   
	   IPeLayoutService peLayout = Graphiti.getPeLayoutService();
	   
	   ILocation seq1StartLoc = peLayout.getLocationRelativeToDiagram(seq1Connection.getStart());
	   assertThat(seq1StartLoc.getX()).isEqualTo(216);
	   assertThat(seq1StartLoc.getY()).isEqualTo(165);
	   
	   ILocation seq1EndLoc = peLayout.getLocationRelativeToDiagram(seq1Connection.getEnd());
     assertThat(seq1EndLoc.getX()).isEqualTo(320);
     assertThat(seq1EndLoc.getY()).isEqualTo(345);
	   
     assertThat(seq1Connection.getBendpoints().size()).isEqualTo(2);
     
     Point seq1FirstPoint = seq1Connection.getBendpoints().get(0);
     assertThat(seq1FirstPoint.getX()).isEqualTo(372);
     assertThat(seq1FirstPoint.getY()).isEqualTo(165);
     
     Point seq1SecondPoint = seq1Connection.getBendpoints().get(1);
     assertThat(seq1SecondPoint.getX()).isEqualTo(372);
     assertThat(seq1SecondPoint.getY()).isEqualTo(345);     
     
     ILocation seq2StartLoc = peLayout.getLocationRelativeToDiagram(seq1Connection.getStart());
     assertThat(seq2StartLoc.getX()).isEqualTo(430);
     assertThat(seq2StartLoc.getY()).isEqualTo(345);
     
     ILocation seq2EndLoc = peLayout.getLocationRelativeToDiagram(seq1Connection.getEnd());
     assertThat(seq2EndLoc.getX()).isEqualTo(534);
     assertThat(seq2EndLoc.getY()).isEqualTo(165);
     
     assertThat(seq2Connection.getBendpoints().size()).isEqualTo(2);
     
     Point seq2FirstPoint = seq2Connection.getBendpoints().get(0);
     assertThat(seq2FirstPoint.getX()).isEqualTo(482);
     assertThat(seq2FirstPoint.getY()).isEqualTo(345);
     
     Point seq2SecondPoint = seq2Connection.getBendpoints().get(1);
     assertThat(seq2SecondPoint.getX()).isEqualTo(482);
     assertThat(seq2SecondPoint.getY()).isEqualTo(165);     
     
	}

	@Test
	@DiagramResource
	public void testMoveStartEventVerticalLayoutSequenceFlow() {
		Shape gatewayShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		move(gatewayShape, diagramTypeProvider)
			.by(0 , 100)
			.execute();
		
		// The MoveFlowNodeFeature will call AnchorUtil.reConnect, which will in turn recalculate the
		// boundary anchors to update them, we need to hook in there
		
		// see also DefaultMoveBendPointFeature to see how a bend point is created 
		
		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		assertThat(connection).hasNoDiagonalEdges();
		assertThat(connection).hasBendpointCount(2);
	}

	// helpers /////////////////////////////////////
	
	private Point point(int x, int y) {
		return Graphiti.getGaService().createPoint(x, y);
	}
}
