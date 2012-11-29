package org.eclipse.bpmn2.modeler.core.test.importer.sequenceflow;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmnModelTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class ImportSequenceFlowTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportConditionalFlow() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		Connection connection = ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		assertThat(connection).isNotNull();
		
		SequenceFlow sequenceFlow = BusinessObjectUtil.getFirstElementOfType(connection, SequenceFlow.class);
		assertThat(sequenceFlow.getConditionExpression()).isNotNull();
	}

	@Test
	@DiagramResource
	public void testImportUnconditionalFlow() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		Connection connection = ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		assertThat(connection).isNotNull();
	}
	
	@Test
	@DiagramResource
	public void testImportDefaultFlow() {
		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();

		Shape exclusiveGatewayShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		Connection defaultFlowShape = ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		assertThat(defaultFlowShape).isNotNull();
		assertThat(exclusiveGatewayShape).isNotNull();

		SequenceFlow sequenceFlow = BusinessObjectUtil.getFirstElementOfType(defaultFlowShape, SequenceFlow.class);
		ExclusiveGateway gateway = BusinessObjectUtil.getFirstElementOfType(exclusiveGatewayShape, ExclusiveGateway.class);
		
		assertThat(gateway.getDefault()).isEqualTo(sequenceFlow);
	}
	
	@Test
	@DiagramResource
	public void testChopboxAnchorModel() throws Exception {

		ModelImport importer = new ModelImport(diagramTypeProvider, resource);
		importer.execute();
		
		FreeFormConnection connection = (FreeFormConnection) ShapeUtil.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		Shape startShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_3");
		Shape endShape = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_4");
		
		EList<Anchor> startShapeAnchors = startShape.getAnchors();
		
		assertThat(connection).hasBendpointCount(1);
		assertThat(startShapeAnchors).hasSize(5);
		
		// start anchor should be a chopbox anchor
		assertThat(connection.getStart()).isInstanceOf(ChopboxAnchor.class);
		
		assertThat(startShapeAnchors.get(0)).isSameAs(connection.getStart());
		
		// end however should be a fix point anchor (points directly to the guy)
		assertThat(connection.getEnd()).isInstanceOf(FixPointAnchor.class);
		
		Anchor endShapeRightAnchor = LayoutUtil.getLeftAnchor(endShape);
		assertThat(endShapeRightAnchor).isSameAs(connection.getEnd());
	}
}
