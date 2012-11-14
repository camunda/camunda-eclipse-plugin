package org.eclipse.bpmn2.modeler.core.test.util.operations.test;

import static org.eclipse.bpmn2.modeler.core.test.util.operations.ShapeOperation.move;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.graphiti.datatypes.ILocation;
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
		Shape gatewayShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		move(gatewayShape, diagramTypeProvider)
			.by(0 , 100)
			.execute();
	}
	
	@Test
	@DiagramResource
	public void testMoveChangeCoordinates() {
		Shape gatewayShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		
		ILocation oldLocation = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(gatewayShape);
		
		move(gatewayShape, diagramTypeProvider)
			.by(5, 100)
			.execute();
		
		ILocation newLocation = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(gatewayShape);

		assertThat(oldLocation.getX() + 5).isEqualTo(newLocation.getX());
		assertThat(oldLocation.getY() + 100).isEqualTo(newLocation.getY());
	}
}
