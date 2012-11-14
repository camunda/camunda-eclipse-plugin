package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.ShapeOperation.move;
import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
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
	
	protected void assertPointEqual(Point expected, Point actual) {
		if (expected.getX() == actual.getX() && expected.getY() == actual.getY()) {
			
		}else {
			Assert.failNotEquals("Points not equal", expected, actual);
		}
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
		
		FreeFormConnection seq2Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		FreeFormConnection seq3Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		FreeFormConnection seq7Connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_7");
		
		// check incoming sequence flow
		assertEquals(2, seq2Connection.getBendpoints().size());
		
		// check bendboints coordiantes
		assertPointEqual(Graphiti.getGaService().createPoint(420, 229), seq2Connection.getBendpoints().get(0));
		assertPointEqual(Graphiti.getGaService().createPoint(420, 144), seq2Connection.getBendpoints().get(1));
		
		// start anchor must be centered on the right side
		assertEquals(110, ((FixPointAnchor) seq2Connection.getStart()).getLocation().getX());
		assertEquals(25, ((FixPointAnchor) seq2Connection.getStart()).getLocation().getY());

		// end anchor must be centered on the left side
		assertEquals(0, ((FixPointAnchor) seq2Connection.getEnd()).getLocation().getX());
		assertEquals(25, ((FixPointAnchor) seq2Connection.getEnd()).getLocation().getY());
		
		//check outgoing sequence flow left
		assertEquals(2, seq3Connection.getBendpoints().size());
		
		assertPointEqual(Graphiti.getGaService().createPoint(511, 144), seq3Connection.getBendpoints().get(0));
		assertPointEqual(Graphiti.getGaService().createPoint(511, 229), seq3Connection.getBendpoints().get(1));

		// start anchor must be centered on the right side
		assertEquals(51, ((FixPointAnchor) seq3Connection.getStart()).getLocation().getX());
		assertEquals(25, ((FixPointAnchor) seq3Connection.getStart()).getLocation().getY());
		
		// end anchor must be centered on the left side
		assertEquals(0, ((FixPointAnchor) seq3Connection.getEnd()).getLocation().getX());
		assertEquals(25, ((FixPointAnchor) seq3Connection.getEnd()).getLocation().getY());
		
		//check outgoing sequence flow bottom
		assertEquals(1, seq7Connection.getBendpoints().size());
		assertPointEqual(Graphiti.getGaService().createPoint(473, 330), seq7Connection.getBendpoints().get(0));
		
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

}
