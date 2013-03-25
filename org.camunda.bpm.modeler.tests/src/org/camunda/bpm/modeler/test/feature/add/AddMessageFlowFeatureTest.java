package org.camunda.bpm.modeler.test.feature.add;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.location;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.ui.features.flow.MessageFlowFeatureContainer;
import org.camunda.bpm.modeler.ui.features.flow.MessageFlowFeatureContainer.CreateMessageFlowFeature;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.junit.Test;

public class AddMessageFlowFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddBoundaryEventFeatureTest.testBase.bpmn")
	public void testCannotCreateOnDiagram() throws Exception {

		// given
		// create context with diagram as source
		
		CreateConnectionContext context = new CreateConnectionContext();
		context.setSourcePictogramElement(getDiagram());
		context.setSourceLocation(location(150, 150));
		
		// and create feature for message flow
		CreateMessageFlowFeature createMessageFlowFeature = new MessageFlowFeatureContainer.CreateMessageFlowFeature(getDiagramTypeProvider().getFeatureProvider());
		
		// when
		boolean canStart = createMessageFlowFeature.canStartConnection(context);
		
		// then
		assertThat(canStart).isFalse();
	}
}
