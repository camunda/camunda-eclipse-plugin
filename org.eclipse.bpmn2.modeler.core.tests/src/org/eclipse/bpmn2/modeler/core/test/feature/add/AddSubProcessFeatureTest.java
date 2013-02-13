package org.eclipse.bpmn2.modeler.core.test.feature.add;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddTaskOperation.addTask;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author nico.rehwaldt
 */
public class AddSubProcessFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddFlowElementFeatureTestBase.testSplitConnection.bpmn")
	public void testAddToDiagramSplitConnection() throws Exception {

		// given diagram
		// with event, task and a label
		FreeFormConnection targetConnection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		List<Point> preSplitBendpoints = new ArrayList<Point>(targetConnection.getBendpoints());

		List<Point> expectedBeforeSplitBendpoints = preSplitBendpoints.subList(0, 1);
		List<Point> expectedAfterSplitBendpoints = preSplitBendpoints.subList(2, preSplitBendpoints.size());
		
		Shape preSplitStart = (Shape) targetConnection.getStart().getParent();
		Shape preSplitConnectionEnd = (Shape) targetConnection.getEnd().getParent();
		
		// when
		// element is added to it
		addTask(diagramTypeProvider)
			.toConnection(targetConnection)
			.atLocation(210, 139)
			.toContainer(diagram)
			.execute();
		
		// then
		// connection should be split and 
		Shape postSplitEndShape = (Shape) targetConnection.getEnd().getParent();
		Shape postSplitStartShape = (Shape) targetConnection.getStart().getParent();

		assertThat(postSplitEndShape)
			.isLinkedTo(elementOfType(Task.class));
		
		assertThat(targetConnection)
			.hasExactBendpoints(expectedBeforeSplitBendpoints);
		
		assertThat(postSplitStartShape)
			.isSameAs(preSplitStart);
		
		assertThat(postSplitEndShape)
			.outgoingConnectionTo(preSplitConnectionEnd)
				.hasExactBendpoints(expectedAfterSplitBendpoints);
	}
}
