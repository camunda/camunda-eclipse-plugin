package org.camunda.bpm.modeler.test.util.operations.test;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.MoveShapeOperation.move;

import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class MoveFlowNodeOperationTest extends AbstractFeatureTest {

	@Test
	@DiagramResource
	public void testMoveNoContainerSpecified() {
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		move(gatewayShape, diagramTypeProvider)
			.by(0 , 100)
			.execute();
	}
	
	@Test
	@DiagramResource
	public void testMoveChangeCoordinates() {
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		ILocation oldLocation = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(gatewayShape);
		
		move(gatewayShape, diagramTypeProvider)
			.by(5, 100)
			.execute();
		
		ILocation newLocation = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(gatewayShape);

		assertThat(oldLocation.getX() + 5).isEqualTo(newLocation.getX());
		assertThat(oldLocation.getY() + 100).isEqualTo(newLocation.getY());
	}
	
	
	@Test
	@DiagramResource
	public void testMoveRetainsFormalExpressionId() {
		Shape gatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		FreeFormConnection flow2Connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_2");
		SequenceFlow flow2 =(SequenceFlow) BusinessObjectUtil.getBusinessObjectForPictogramElement(flow2Connection);
		
		assertThat(flow2.getConditionExpression().getId()).isNull();
		
		move(gatewayShape, diagramTypeProvider)
			.by(5, 100)
			.execute();
		
		assertThat(flow2.getConditionExpression().getId()).isNull();
		
		ModelUtil.setID(flow2.getConditionExpression(), "expression1");
		
		String oldId = flow2.getConditionExpression().getId();
		
		assertThat(oldId).isNotNull();
		
		move(gatewayShape, diagramTypeProvider)
		.by(5, 100)
		.execute();
	
		assertThat(flow2.getConditionExpression().getId()).isEqualTo(oldId);
	}
	
}
