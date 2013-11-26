package org.camunda.bpm.modeler.test.integration.io;

import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.camunda.bpm.modeler.test.util.operations.DeleteElementOperation.deleteElement;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * Test case testing a simple editor create diagram -> save roundtrip
 * 
 * @author nico.rehwaldt
 */
public class RoundtripTest extends AbstractIntegrationTest {
	
	@Test
	public void shouldCreateElementOnEmptyModel() throws Exception {

		// this test ensures that no cryptical
		//
		// 	java.lang.IllegalStateException: Cannot modify resource set without a write transaction
		// 
		// is thrown when wrong model resources are created during BPMN 2.0 import

		open("RoundtripTest.emptyDiagram.bpmn");
		
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
			.contains("startEvent")
			.contains("process");
	}
	
	@Test
	public void shouldDeleteElementInModel() throws Exception{
		// this test ensures that elements are deleted correctly
		
		open("RoundtripTest.deleteElement.bpmn");
		
		// when 
		// edit
		execute(new Behavior() {
			public void run() throws Exception {
				// given
				Shape serviceTaskShape = Util.findShapeByBusinessObjectId(getDiagram(), "ServiceTask_1");
				deleteElement(serviceTaskShape, getDiagramTypeProvider()).execute();				
			};
		});		
		String exportXml = save();
		
		// than
		assertThat(exportXml)
			.doesNotContain("serviceTask")
			.contains("startEvent")
			.contains("endEvent");
	}
}
