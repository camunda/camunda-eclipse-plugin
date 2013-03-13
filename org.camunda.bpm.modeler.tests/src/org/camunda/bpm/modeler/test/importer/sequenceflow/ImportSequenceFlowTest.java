package org.camunda.bpm.modeler.test.importer.sequenceflow;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.SequenceFlow;
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
		ModelImport importer = createModelImport();
		importer.execute();
		
		Connection connection = Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		assertThat(connection).isNotNull();
		
		SequenceFlow sequenceFlow = BusinessObjectUtil.getFirstElementOfType(connection, SequenceFlow.class);
		assertThat(sequenceFlow.getConditionExpression()).isNotNull();
	}

	@Test
	@DiagramResource
	public void testImportUnconditionalFlow() {
		ModelImport importer = createModelImport();
		importer.execute();

		Connection connection = Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		assertThat(connection).isNotNull();
	}
	
	@Test
	@DiagramResource
	public void testImportDefaultFlow() {
		ModelImport importer = createModelImport();
		importer.execute();

		Shape exclusiveGatewayShape = Util.findShapeByBusinessObjectId(diagram, "ExclusiveGateway_1");
		Connection defaultFlowShape = Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		
		assertThat(defaultFlowShape).isNotNull();
		assertThat(exclusiveGatewayShape).isNotNull();

		SequenceFlow sequenceFlow = BusinessObjectUtil.getFirstElementOfType(defaultFlowShape, SequenceFlow.class);
		ExclusiveGateway gateway = BusinessObjectUtil.getFirstElementOfType(exclusiveGatewayShape, ExclusiveGateway.class);
		
		assertThat(gateway.getDefault()).isEqualTo(sequenceFlow);
	}
	
	@Test
	@DiagramResource
	public void testChopboxAnchorModel() throws Exception {

		ModelImport importer = createModelImport();
		importer.execute();
		
		FreeFormConnection connection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_3");
		Shape startShape = Util.findShapeByBusinessObjectId(diagram, "Task_3");
		Shape endShape = Util.findShapeByBusinessObjectId(diagram, "Task_4");
		
		EList<Anchor> startShapeAnchors = startShape.getAnchors();
		
		assertThat(connection).hasBendpointCount(1);
		assertThat(startShapeAnchors).hasSize(5);
		
		// start anchor should be a chopbox anchor
		assertThat(connection.getStart()).isInstanceOf(ChopboxAnchor.class);
		
		assertThat(startShapeAnchors.get(0)).isSameAs(connection.getStart());
		
		// end should be a chopbox anchor, too (use it in favour of fix point anchor)
		assertThat(connection.getEnd()).isInstanceOf(ChopboxAnchor.class);
		
		Anchor endShapeCenterAnchor = LayoutUtil.getCenterAnchor(endShape);
		assertThat(endShapeCenterAnchor).isSameAs(connection.getEnd());
	}
}
