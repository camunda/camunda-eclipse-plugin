package org.camunda.bpm.modeler.test.integration.io.extensions;

import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest;
import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest.Behavior;
import org.junit.Test;

/**
 * @author Roman Smirnov
 */
public class EntryTest extends AbstractIntegrationTest {

	@Test
	public void shouldKeepTextContent() throws Exception {
		
		open("EntryTest.textContentInsideEntry.bpmn");
		
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
			
			/* entry */
			.contains("<camunda:entry key=\"key\">abc</camunda:entry>")
			
			.doesNotContain("<![CDATA[");
	}
	
	@Test
	public void shouldEscapeLineBreak() throws Exception {
		
		open("EntryTest.textContentWithLineBreakInsideEntry.bpmn");
		
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
			
			/* entry (1) */
			.contains("<camunda:entry key=\"key1\"><![CDATA[")
			
			/* value */
			.contains("abc")

			/* entry (1) */
			.contains("]]></camunda:entry>")
			
			/* entry (2) */
			.contains("<camunda:entry key=\"key2\"><![CDATA[")
			
			/* value */
			.contains("${expression}")

			/* entry (2) */
			.contains("]]></camunda:entry>");
	
	}
	
	@Test
	public void shouldKeepScriptElement() throws Exception {
		
		open("EntryTest.scriptElementInsideEntry.bpmn");
		
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
		
			/* entry */
			.contains("<camunda:entry key=\"key\">")
			.doesNotContain("<camunda:entry key=\"key\"><![CDATA[")
			
			/* script */
			.contains("<camunda:script scriptFormat=\"javascript\" resource=\"entry.js\"/>")
			.doesNotContain("]]><camunda:script scriptFormat=\"javascript\" resource=\"entry.js\"/><![CDATA[")

			/* entry */
			.contains("</camunda:entry>")
			.doesNotContain("]]></camunda:entry>")
			
			.doesNotContain("<![CDATA[");
	}

	@Test
	public void shouldKeepMapElement() throws Exception {
		
		open("EntryTest.mapElementInsideEntry.bpmn");
		
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
		
			/* entry */
			.contains("<camunda:entry key=\"map\">")
			.doesNotContain("<camunda:entry key=\"map\"><![CDATA[")
			
			/* nested map */
			.contains("<camunda:map>")
			.doesNotContain("]]><camunda:map>")

			/* nested entry */
			.contains("<camunda:entry key=\"key\">abc</camunda:entry>")
			
			/* nested map */
			.contains("</camunda:map>")
			.doesNotContain("</camunda:map><![CDATA[")
			
			/* entry */
			.contains("</camunda:entry>")
			.doesNotContain("]]></camunda:entry>")
			
			.doesNotContain("<![CDATA[");
	}
	
	@Test
	public void shouldKeepListElement() throws Exception {
		
		open("EntryTest.listElementInsideEntry.bpmn");
		
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
			
			/* entry */
			.contains("<camunda:entry key=\"list\">")
			.doesNotContain("<camunda:entry key=\"list\"><![CDATA[")
			
			/* list */
			.contains("<camunda:list>")
			.doesNotContain("]]><camunda:list>")
	
			/* value */
			.contains("<camunda:value>abc</camunda:value>")
			
			/* list */
			.contains("</camunda:list>")
			.doesNotContain("</camunda:list><![CDATA[")
			
			/* entry */
			.contains("</camunda:entry>")
			.doesNotContain("]]></camunda:entry>")
			
			.doesNotContain("<![CDATA[");
	}

}
