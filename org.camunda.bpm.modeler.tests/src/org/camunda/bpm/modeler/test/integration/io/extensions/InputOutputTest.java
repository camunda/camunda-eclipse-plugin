package org.camunda.bpm.modeler.test.integration.io.extensions;

import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest;
import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest.Behavior;
import org.junit.Test;

/**
 * @author Roman Smirnov
 */
public class InputOutputTest extends AbstractIntegrationTest {
	
	@Test
	public void shouldKeepInputParameter() throws Exception {
		
		open("InputOutputTest.inputParameter.bpmn");
		
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
			
			/* input output */
			.contains("<camunda:inputOutput>")

			/* input parameter */
			.contains("<camunda:inputParameter name=\"input\">abc</camunda:inputParameter>")
			
			/* input output */
			.contains("</camunda:inputOutput>")
			
			.doesNotContain("<![CDATA[");
	}
	
	@Test
	public void shouldKeepOutputParameter() throws Exception {
		
		open("InputOutputTest.outputParameter.bpmn");
		
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
			
			/* input output */
			.contains("<camunda:inputOutput>")

			/* output parameter */
			.contains("<camunda:outputParameter name=\"input\">abc</camunda:outputParameter>")
			
			/* input output */
			.contains("</camunda:inputOutput>")
			
			.doesNotContain("<![CDATA[");
	}

}
