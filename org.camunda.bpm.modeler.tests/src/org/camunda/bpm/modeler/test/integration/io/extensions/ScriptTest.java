package org.camunda.bpm.modeler.test.integration.io.extensions;

import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest;
import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest.Behavior;
import org.junit.Test;

/**
 * @author Roman Smirnov
 */
public class ScriptTest extends AbstractIntegrationTest {
	
	@Test
	public void shouldKeepResource() throws Exception {
		
		open("ScriptTest.scriptElement.bpmn");
		
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
		
			/* resource */
			.contains("resource=\"myResource.js\"");
	}

	@Test
	public void shouldKeepScriptFormat() throws Exception {
		
		open("ScriptTest.scriptElement.bpmn");
		
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
		
			/* script format */
			.contains("scriptFormat=\"javascript\"");
	}
	
	@Test
	public void shouldKeepTextContent() throws Exception {
		
		open("ScriptTest.scriptElementWithTextContent.bpmn");
		
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
			.contains("<camunda:script scriptFormat=\"javascript\">doSomething();</camunda:script>")
			
			.doesNotContain("<![CDATA[");
	}
	
	@Test
	public void shouldEscapeLineBreak() throws Exception {
		
		open("ScriptTest.textContentWithLineBreak.bpmn");
		
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
		
			/* map */
			.contains("<camunda:script scriptFormat=\"javascript\"><![CDATA[")
		
			/* text content */
			.contains("doSomething();")
			
			/* map */
			.contains("]]></camunda:script>");
	}
}
