package org.camunda.bpm.modeler.test.feature.add;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.AddTextAnnotationOperation.addTextAnnotation;

import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.di.BPMNShape;
import org.junit.Test;

public class AddTextAnnotationFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFeatureTestBase.testAddToDiagram.bpmn")
	public void testAddToDiagram() throws Exception {

		// when
		// element is added to it
		addTextAnnotation(diagramTypeProvider)
			.atLocation(10, 10)
			.toContainer(diagram)
			.execute();
		
		// get the added text annotation
		BPMNShape addedTextAnnotation = (BPMNShape) Util.findBpmnShapeByBusinessObjectId(diagram, "TextAnnotation_1");
		
		// then
		assertThat(addedTextAnnotation)
			.bounds()
			.height()
				.isEqualTo(50);

		assertThat(addedTextAnnotation)
			.bounds()
			.width()
				.isEqualTo(50);
	}
	
}
