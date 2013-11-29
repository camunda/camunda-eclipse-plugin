package org.camunda.bpm.modeler.test.feature.update;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.CollapseOperation.collapse;
import static org.camunda.bpm.modeler.test.util.operations.ExpandOperation.expand;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * Test to check the correct behavior of a collapsed and expanded sub process
 * after some changes on the diagram
 *   
 * @author kristin.polenz
 *
 */
public class UpdateSubProcessTest extends AbstractFeatureTest {

	@Test
	@DiagramResource
	public void testChangeSubProcessToCollapsed() {
		// given
		BPMNShape oldBpmnShape = Util.findBpmnShapeByBusinessObjectId(diagram, "SubProcess_3");
		
		assertThat(oldBpmnShape).isNotNull();
		assertThat(oldBpmnShape.isIsExpanded()).isTrue();
		
		ContainerShape oldContainerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_3");
		
		// when
		collapse(oldContainerShape, getDiagramTypeProvider())
			.execute();
		
		// then
		BPMNShape bpmnShape = Util.findBpmnShapeByBusinessObjectId(diagram, "SubProcess_3");
		assertThat(bpmnShape).isNotNull();
		assertThat(bpmnShape.isIsExpanded()).isFalse();
		
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_3");
		List<Shape> subProcessChildren = FeatureSupport.getBpmnChildShapes(containerShape);
		
		assertThat(subProcessChildren)
		.isNotEmpty()
		.hasSize(4);
	
		for (Shape shape : subProcessChildren) {
			isShapeVisible(shape, false);
		}
		
	}
	
	@Test
	@DiagramResource
	public void testChangeSubProcessToExpanded() {
		// given
		BPMNShape oldBpmnShape = Util.findBpmnShapeByBusinessObjectId(diagram, "SubProcess_3");
		
		assertThat(oldBpmnShape).isNotNull();
		assertThat(oldBpmnShape.isIsExpanded()).isFalse();
		
		ContainerShape oldContainerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_3");
		
		// when
		expand(oldContainerShape, getDiagramTypeProvider())
			.execute();
		
		// then
		BPMNShape bpmnShape = Util.findBpmnShapeByBusinessObjectId(diagram, "SubProcess_3");
		assertThat(bpmnShape).isNotNull();
		assertThat(bpmnShape.isIsExpanded()).isTrue();
		
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_3");
		List<Shape> subProcessChildren = FeatureSupport.getBpmnChildShapes(containerShape);
		
		assertThat(subProcessChildren)
		.isNotEmpty()
		.hasSize(4);
	
		for (Shape shape : subProcessChildren) {
			isShapeVisible(shape, true);
		}
		
	}	
	
	private void isShapeVisible(Shape shape, boolean visible) {
		assertThat(shape.isVisible()).isEqualTo(visible);

		Shape labelShape = LabelUtil.getLabelShape(shape, diagram);
		if (labelShape != null) {
			assertThat(labelShape.isVisible()).isEqualTo(visible);
		}

		if (shape instanceof AnchorContainer) {
			for (Anchor a : shape.getAnchors()) {
				for (Connection c : a.getOutgoingConnections()) {
					assertThat(c.isVisible()).isEqualTo(visible);

					Shape connectionLabelShape = LabelUtil.getLabelShape(c, c.getParent());
					if (connectionLabelShape != null) {
						assertThat(connectionLabelShape.isVisible()).isEqualTo(visible);
					}

					for (ConnectionDecorator decorator : c.getConnectionDecorators()) {
						assertThat(decorator.isVisible()).isEqualTo(visible);
					}
				}
			}
		}
	}
}
