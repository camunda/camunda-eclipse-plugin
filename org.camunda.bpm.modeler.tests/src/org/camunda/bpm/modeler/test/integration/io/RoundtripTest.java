package org.camunda.bpm.modeler.test.integration.io;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.operations.AddCallActivityOperation.addCallActivity;
import static org.camunda.bpm.modeler.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.camunda.bpm.modeler.test.util.operations.DeleteElementOperation.deleteElement;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.runtime.engine.model.CallActivity;
import org.camunda.bpm.modeler.test.integration.AbstractIntegrationTest;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
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

	@Test
	public void shouldCreateCallActivityWithAttributes() throws Exception {

		final EditorAndModel editorAndModel = open("RoundtripTest.emptyProcess.bpmn");
		Diagram diagram = editorAndModel.getDiagram();

		final Shape processShape = Util.findShapeByBusinessObjectId(diagram, "Process_1");

		// add call activity to process
		execute(new Behavior() {

			public void run() throws Exception {
				Point addPosition = point(10, 10);

				addCallActivity(getDiagramTypeProvider())
					.atLocation(addPosition)
					.toContainer((ContainerShape) processShape)
					.execute();
			};
		});

		final CallActivity callActivity = Util.findBusinessObjectById(diagram, "CallActivity_1", CallActivity.class);
		assertThat(callActivity).isNotNull();

		// add some attributes to call activity
		execute(new Behavior() {

			public void run() throws Exception {
				callActivity.setCalledElement("test");
				callActivity.setCalledElementBinding("version");
				callActivity.setCalledElementVersion(1);
			}
		});

		String exportXml = save();

		// check processes xml
		assertThat(exportXml)
				  .contains("Process_1")
				  .contains("CallActivity_1")
				  .contains("calledElement")
				  .contains("camunda:calledElementBinding")
				  .contains("camunda:calledElementVersion");
	}
}
