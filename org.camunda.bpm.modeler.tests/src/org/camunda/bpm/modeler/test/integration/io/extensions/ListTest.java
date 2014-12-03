package org.camunda.bpm.modeler.test.integration.io.extensions;

import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest;
import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest.Behavior;
import org.junit.Test;

/**
 * @author Roman Smirnov
 */
public class ListTest extends AbstractIntegrationTest {
	
	@Test
	public void shouldKeepScriptElement() throws Exception {
		
		open("ListTest.scriptElementInsideList.bpmn");
		
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
			
			/* list */
			.contains("<camunda:list>")
			
			/* script */
			.contains("<camunda:script scriptFormat=\"javascript\" resource=\"list.js\"/>")
			
			/* list */
			.contains("</camunda:list>")
						
			.doesNotContain("<![CDATA[");
	}


	@Test
	public void shouldKeepMapElement() throws Exception {
		
		open("ListTest.mapElementInsideList.bpmn");
		
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
				
			/* list */
			.contains("<camunda:list>")
			
			/* map */
			.contains("<camunda:map>")
			
			/* entry */
			.contains("<camunda:entry key=\"key\">abc</camunda:entry>")

			/* map */
			.contains("</camunda:map>")
			
			/* list */
			.contains("</camunda:list>")
						
			.doesNotContain("<![CDATA[");
	}
	
	@Test
	public void shouldKeepListElement() throws Exception {
		
		open("ListTest.listElementInsideList.bpmn");
		
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
				
			/* list */
			.contains("<camunda:list>")
			
			/* list */
			.contains("<camunda:list>")
			
			/* value */
			.contains("<camunda:value>abc</camunda:value>")
		
			/* list */
			.contains("</camunda:list>")
			
			/* list */
			.contains("</camunda:list>")
						
			.doesNotContain("<![CDATA[");
	}
	
	@Test
	public void shouldKeepDifferentValues() throws Exception {
		
		open("ListTest.differentValues.bpmn");
		
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
				
			/* list */
			.contains("<camunda:list>")
			
			/* value (1) */
			.contains("<camunda:value>abc</camunda:value>")
			
			/* value (2) */
			.contains("<camunda:value>def</camunda:value>")

			/* value (3) */
			.contains("<camunda:value>xyz</camunda:value>")
			
			/* map */
			.contains("<camunda:map>")
			
			/* script */
			.contains("<camunda:script scriptFormat=\"javascript\" resource=\"list.js\"/>")
								
			.doesNotContain("<![CDATA[");
	}
}
