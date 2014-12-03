package org.camunda.bpm.modeler.test.integration.io.extensions;

import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest;
import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest.Behavior;
import org.junit.Test;

/**
 * @author Roman Smirnov
 */
public class ParameterTest extends AbstractIntegrationTest {
	
	@Test
	public void shouldKeepTextContent() throws Exception {
		
		open("ParameterTest.textContentInsideParameter.bpmn");
		
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
			
			/* input parameter (1) */
			.contains("<camunda:inputParameter name=\"constantInputValue\">abc</camunda:inputParameter>")
			
			/* input parameter (2) */
			.contains("<camunda:inputParameter name=\"expressionInputValue\">${abc}</camunda:inputParameter>")
			
			/* output parameter (1) */
			.contains("<camunda:outputParameter name=\"constantOutputValue\">abc</camunda:outputParameter>")
			
			/* output parameter (2) */
			.contains("<camunda:outputParameter name=\"expressionOutputValue\">${abc}</camunda:outputParameter>")
						
			.doesNotContain("<![CDATA[");
	}
	
	@Test
	public void shouldEscapeLineBreak() throws Exception {
		
		open("ParameterTest.textContentWithLineBreakInsideParameter.bpmn");
		
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
			
			/* input parameter (1) */
			.contains("<camunda:inputParameter name=\"constantInputValue\"><![CDATA[")
			
			/* value */
			.contains("abc")

			/* input parameter (1) */
			.contains("]]></camunda:inputParameter>")

			/* input parameter (2) */
			.contains("<camunda:inputParameter name=\"expressionInputValue\"><![CDATA[")
			
			/* value */
			.contains("${expression1}")

			/* input parameter (2) */
			.contains("]]></camunda:inputParameter>")
			
			/* output parameter (1) */
			.contains("<camunda:outputParameter name=\"constantOutputValue\"><![CDATA[")
			
			/* value */
			.contains("def")

			/* output parameter (1) */
			.contains("]]></camunda:outputParameter>")

			/* output parameter (2) */
			.contains("<camunda:outputParameter name=\"expressionOutputValue\"><![CDATA[")
			
			/* value */
			.contains("${expression2}")

			/* output parameter (2) */
			.contains("]]></camunda:outputParameter>")
			
			/* input output*/
			.contains("</camunda:inputOutput>");
	}
	
	@Test
	public void shouldKeepScriptElement() throws Exception {
		
		open("ParameterTest.scriptElementInsideParameter.bpmn");
		
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
			
			/* input parameter */
			.contains("<camunda:inputParameter name=\"sciptInputValue\">")
			.doesNotContain("<camunda:inputParameter name=\"sciptInputValue\"><![CDATA[")
			
			/* script */
			.contains("<camunda:script scriptFormat=\"javascript\" resource=\"input.js\"/>")
			.doesNotContain("]]><camunda:script scriptFormat=\"javascript\" resource=\"input.js\"/><![CDATA[")
			
			/* input parameter */
			.contains("</camunda:inputParameter>")
			.doesNotContain("]]></camunda:inputParameter>")
			
			/* output parameter */
			.contains("<camunda:outputParameter name=\"sciptOutputValue\">")
			.doesNotContain("<camunda:outputParameter name=\"sciptOutputValue\"><![CDATA[")
			
			/* script */
			.contains("<camunda:script scriptFormat=\"javascript\" resource=\"output.js\"/>")
			.doesNotContain("]]><camunda:script scriptFormat=\"javascript\" resource=\"output.js\"/><![CDATA[")
			
			/* output parameter */
			.contains("</camunda:outputParameter>")
			.doesNotContain("]]></camunda:outputParameter>")
						
			.doesNotContain("<![CDATA[");
	}

	@Test
	public void shouldKeepMapElement() throws Exception {
		
		open("ParameterTest.mapElementInsideParameter.bpmn");
		
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
			
			/* input parameter */
			.contains("<camunda:inputParameter name=\"mapInputValue\">")
			.doesNotContain("<camunda:inputParameter name=\"mapInputValue\"><![CDATA[")
			
			/* map */
			.contains("<camunda:map>")
			.doesNotContain("]]><camunda:map><![CDATA[")
			
			/* key */
			.contains("<camunda:entry key=\"key1\">input</camunda:entry>")
			.doesNotContain("]]><camunda:entry key=\"key1\">input</camunda:entry><![CDATA[")
			
			/* map */
			.contains("</camunda:map>")
			.doesNotContain("]]></camunda:map><![CDATA[")
			
			/* input parameter*/
			.contains("</camunda:inputParameter>")
			.doesNotContain("]]></camunda:inputParameter>")
			
			/* output parameter */
			.contains("<camunda:outputParameter name=\"mapOutputValue\">")
			.doesNotContain("<camunda:outputParameter name=\"mapOutputValue\"><![CDATA[")
			
			/* map */
			.contains("<camunda:map>")
			.doesNotContain("]]><camunda:map><![CDATA[")
			
			/* key */
			.contains("<camunda:entry key=\"key2\">output</camunda:entry>")
			.doesNotContain("]]><camunda:entry key=\"key2\">output</camunda:entry><![CDATA[")
			
			/* map */
			.contains("</camunda:map>")
			.doesNotContain("]]></camunda:map><![CDATA[")
			
			/* output parameter*/
			.contains("</camunda:outputParameter>")
			.doesNotContain("]]></camunda:outputParameter>")
						
			.doesNotContain("<![CDATA[");
	}
	
	@Test
	public void shouldKeepListElement() throws Exception {
		
		open("ParameterTest.listElementInsideParameter.bpmn");
		
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
			
			/* input parameter */
			.contains("<camunda:inputParameter name=\"listInputValue\">")
			.doesNotContain("<camunda:inputParameter name=\"listInputValue\"><![CDATA[")
			
			/* list */
			.contains("<camunda:list>")
			.doesNotContain("]]><camunda:list>")
			
			/* value */
			.contains("<camunda:value>abc</camunda:value>")
			
			/* list */
			.contains("</camunda:list>")
			.doesNotContain("</camunda:list><![CDATA[")
			
			/* input parameter*/
			.contains("</camunda:inputParameter>")
			.doesNotContain("]]></camunda:inputParameter>")
			
			/* output parameter */
			.contains("<camunda:outputParameter name=\"listOutputValue\">")
			.doesNotContain("<camunda:outputParameter name=\"listOutputValue\"><![CDATA[")
			
			/* list */
			.contains("<camunda:list>")
			.doesNotContain("]]><camunda:list>")
			
			/* value */
			.contains("<camunda:value>def</camunda:value>")
			
			/* list */
			.contains("</camunda:list>")
			.doesNotContain("</camunda:list><![CDATA[")
			
			/* output parameter*/
			.contains("</camunda:outputParameter>")
			.doesNotContain("]]></camunda:outputParameter>")
						
			.doesNotContain("<![CDATA[");
	}

}
