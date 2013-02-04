package org.eclipse.bpmn2.modeler.core.test.feature.add;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddStartEventOperation.addStartEvent;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.AddEndEventOperation.addEndEvent;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class AddEventFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddFlowElementFeatureTestBase.testSplitConnection.bpmn")
	public void testAddStartEventToDiagramSplitConnection() throws Exception {

		// given diagram
		// with event, task and a label
		FreeFormConnection targetConnection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		List<Point> preSplitBendpoints = new ArrayList<Point>(targetConnection.getBendpoints());

		List<Point> expectedAfterSplitBendpoints = preSplitBendpoints.subList(2, preSplitBendpoints.size());
		
		Shape preSplitStartShape = (Shape) targetConnection.getStart().getParent();
		Shape preSplitEndShape = (Shape) targetConnection.getEnd().getParent();
		
		// when
		// element is added to it
		addStartEvent(diagramTypeProvider)
			.toConnection(targetConnection)
			.atLocation(210, 139)
			.toContainer(diagram)
			.execute();
		
		// then
		// connection should be reconnected from newly created start shape to end of connection
		Shape postSplitEndShape = (Shape) targetConnection.getEnd().getParent();
		Shape postSplitStartShape = (Shape) targetConnection.getStart().getParent();

		assertThat(postSplitStartShape)
			.isLinkedTo(elementOfType(StartEvent.class))
			.isNotEqualTo(preSplitStartShape);
		
		assertThat(postSplitEndShape)
			.isEqualTo(preSplitEndShape);
		
		assertThat(targetConnection)
			.hasExactBendpoints(expectedAfterSplitBendpoints);
	}
	

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/add/AddFlowElementFeatureTestBase.testSplitConnection.bpmn")
	public void testAddEndEventToDiagramSplitConnection() throws Exception {

		// given diagram
		// with event, task and a label
		FreeFormConnection targetConnection = (FreeFormConnection) Util.findConnectionByBusinessObjectId(diagram, "SequenceFlow_1");
		List<Point> preSplitBendpoints = new ArrayList<Point>(targetConnection.getBendpoints());

		List<Point> expectedBeforeSplitBendpoints = preSplitBendpoints.subList(0, 1);
		
		Shape preSplitStartShape = (Shape) targetConnection.getStart().getParent();
		Shape preSplitEndShape = (Shape) targetConnection.getEnd().getParent();
		
		// when
		// element is added to it
		addEndEvent(diagramTypeProvider)
			.toConnection(targetConnection)
			.atLocation(210, 139)
			.toContainer(diagram)
			.execute();
		
		// then
		// connection should be reconnected from newly created start shape to end of connection
		Shape postSplitEndShape = (Shape) targetConnection.getEnd().getParent();
		Shape postSplitStartShape = (Shape) targetConnection.getStart().getParent();

		assertThat(postSplitEndShape)
			.isLinkedTo(elementOfType(EndEvent.class))
			.isNotEqualTo(preSplitEndShape);
		
		assertThat(postSplitStartShape)
			.isEqualTo(preSplitStartShape);
		
		assertThat(targetConnection)
			.hasExactBendpoints(expectedBeforeSplitBendpoints);
	}
}
