package org.camunda.bpm.modeler.test.integration.io.extensions;

import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest;
import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest.Behavior;
import org.junit.Test;

/**
 * @author Roman Smirnov
 */
public class TaskListenerTest extends AbstractIntegrationTest {

	@Test
	public void shouldKeepScriptElement() throws Exception {
		
		open("TaskListenerTest.scriptElementInsideTaskListener.bpmn");
		
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
					
			/* script */
			.contains("<camunda:script scriptFormat=\"javascript\" resource=\"listener.js\"/>")
						
			.doesNotContain("<![CDATA[");
	}
	
}
