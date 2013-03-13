package org.camunda.bpm.modeler.test.feature.add;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.elementOfType;
import static org.camunda.bpm.modeler.test.util.operations.AddBoundaryEventOperation.addBoundaryEvent;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public class AddBoundaryEventFeatureTest extends AbstractFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddBoundaryEventFeatureTest.testBase.bpmn")
	public void testCorrectPositioningOnAddToTask() throws Exception {

		int EVENT_WIDTH = GraphicsUtil.getEventSize(getDiagram()).getWidth();
		int EVENT_HEIGHT = GraphicsUtil.getEventSize(getDiagram()).getHeight();
		
		// given diagram
		// with task shape
		ContainerShape taskShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "Task_1");
		IRectangle shapeBounds = LayoutUtil.getAbsoluteBounds(taskShape);
		
		Point boundaryAttachPosition = point(60, 70); // relative positioning
		Point expectedBoundaryMidpoint = point(
				shapeBounds.getX() + boundaryAttachPosition.getX(), 
				shapeBounds.getY() + 80); // snapping
		
		// default label is 3 * 3 px wide
		
		Point expectedBoundaryLabelPosition = point(
				expectedBoundaryMidpoint.getX() - 3, 
				expectedBoundaryMidpoint.getY() + EVENT_HEIGHT / 2 + 5); 
		
		// when
		// element is added to it
		addBoundaryEvent(diagramTypeProvider)
			.atLocation(boundaryAttachPosition)
			.toContainer(taskShape)
			.execute();
		
		// then
		EList<Shape> diagramChildren = diagram.getChildren();
		
		Shape createdShape = diagramChildren.get(diagramChildren.size() - 2); // last element diagramChildren.size() - 1 is label

		// shape should have been created
		assertThat(createdShape)
			.isLinkedTo(elementOfType(BoundaryEvent.class))
			.midPoint()
				.isEqualTo(expectedBoundaryMidpoint);
		
		// and label is positioned correctly, too
		Shape boundaryLabelShape = LabelUtil.getLabelShape(createdShape, getDiagram());
		
		assertThat(boundaryLabelShape)
			.position()
				.isEqualTo(expectedBoundaryLabelPosition);
	}
	

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddBoundaryEventFeatureTest.testBase.bpmn")
	public void testCorrectPositioningOnAddToSubprocess() throws Exception {

		int EVENT_WIDTH = GraphicsUtil.getEventSize(getDiagram()).getWidth();
		int EVENT_HEIGHT = GraphicsUtil.getEventSize(getDiagram()).getHeight();
		
		// given diagram
		// with subprocess shape
		ContainerShape subprocessShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_1");
		IRectangle shapeBounds = LayoutUtil.getAbsoluteBounds(subprocessShape);
		
		Point boundaryAttachPosition = point(230, 290); // relative positioning
		Point expectedBoundaryMidpoint = point(
				shapeBounds.getX() + boundaryAttachPosition.getX(), 
				shapeBounds.getY() + 300); // snapping
		
		// default label is 3 * 3 px wide
		
		Point expectedBoundaryLabelPosition = point(
				expectedBoundaryMidpoint.getX() - 3, 
				expectedBoundaryMidpoint.getY() + EVENT_HEIGHT / 2 + 5);
		
		// when
		// element is added to it
		addBoundaryEvent(diagramTypeProvider)
			.atLocation(boundaryAttachPosition) 
			.toContainer(subprocessShape)
			.execute();
		
		// then
		EList<Shape> diagramChildren = diagram.getChildren();
		
		Shape createdShape = diagramChildren.get(diagramChildren.size() - 2); // last element diagramChildren.size() - 1 is label
		
		// shape should have been created
		assertThat(createdShape)
			.isLinkedTo(elementOfType(BoundaryEvent.class))
			.midPoint()
				.isEqualTo(expectedBoundaryMidpoint);
		
		// and label is positioned correctly, too
		Shape boundaryLabelShape = LabelUtil.getLabelShape(createdShape, getDiagram());
		
		assertThat(boundaryLabelShape)
			.position()
				.isEqualTo(expectedBoundaryLabelPosition);
	}
}
