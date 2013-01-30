package org.eclipse.bpmn2.modeler.core.test.di;

import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddEventDefinitionOperation.addEventDefinition;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class UpdateDiTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testUpdateEventDiInSubprocess() {
		Shape eventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");

		// just double checking...
		assertThatDiHasEventBounds(eventShape);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/di/UpdateDiTest.testUpdateEventDiInSubprocess.bpmn")
	public void testUpdateDiAfterEventDefinitionInSubprocessAdd() {
		
		CatchEvent event = (CatchEvent) Util.findBusinessObjectById(diagram, "StartEvent_1");
		Shape eventShape = Util.findShapeByBusinessObjectId(diagram, "StartEvent_1");

		// message event definition to be added
		MessageEventDefinition messageEventDefinition = Bpmn2Factory.eINSTANCE.createMessageEventDefinition();
		event.getEventDefinitions().add(messageEventDefinition);
		
		// when
		// adding definition to shape
		
		addEventDefinition(diagramTypeProvider)
			.addDefinition(messageEventDefinition)
			.toEvent(eventShape)
			.execute();
		
		// then
		// di should still be O.K.

		assertThatDiHasEventBounds(eventShape);
	}

	private void assertThatDiHasEventBounds(Shape eventShape) {

		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(eventShape, BPMNShape.class);

		assertThat(bpmnShape).isNotNull();
		
		Bounds bpmnShapeBounds = bpmnShape.getBounds();

		assertThat(bpmnShapeBounds).isNotNull();
		
		assertThat(bpmnShapeBounds.getWidth()).isEqualTo(36.0f);
		assertThat(bpmnShapeBounds.getHeight()).isEqualTo(36.0f);
	}
}
