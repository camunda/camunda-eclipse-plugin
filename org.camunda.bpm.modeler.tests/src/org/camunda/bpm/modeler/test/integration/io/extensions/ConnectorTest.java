package org.camunda.bpm.modeler.test.integration.io.extensions;

import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest;
import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest.Behavior;
import org.junit.Test;

/**
 * @author Roman Smirnov
 */
public class ConnectorTest extends AbstractIntegrationTest {
	
	@Test
	public void shouldKeepConnectorId() throws Exception {
		
		open("ConnectorTest.connectorId.bpmn");
		
		// when
		// edit
		execute(new Behavior() {
			
			public void run() throws Exception {
				
				// given
				addStartEvent(getDiagramTypeProvider())
					.atLocation(20, 20)
					.toContainer(getDiagram())
					.execute();
			};
		});
		
		String exportXml = save();
		
		// then
		assertThat(exportXml)
		
			/* connector */
			.contains("<camunda:connector>")
		
			/* connector id */
			.contains("<camunda:connectorId>abc</camunda:connectorId>")
			
			/* connector */
			.contains("</camunda:connector>")
			
			.doesNotContain("<![CDATA[");
	}
	
	@Test
	public void shouldKeepInputOutput() throws Exception {
		
		open("ConnectorTest.inputOutput.bpmn");
		
		// when
		// edit
		execute(new Behavior() {
			
			public void run() throws Exception {
				
				// given
				addStartEvent(getDiagramTypeProvider())
					.atLocation(20, 20)
					.toContainer(getDiagram())
					.execute();
			};
		});
		
		String exportXml = save();
		
		// then
		assertThat(exportXml)
		
			/* connector */
			.contains("<camunda:connector>")
		
			/* input output */
			.contains("<camunda:inputOutput>")

			/* input output */
			.contains("</camunda:inputOutput>")
			
			/* connector */
			.contains("</camunda:connector>")
			
			.doesNotContain("<![CDATA[");
	}

}
