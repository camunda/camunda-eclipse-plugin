package org.camunda.bpm.modeler.test.importer.subprocess;

import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * Check the correct import behavior for a collapsed and expanded subprocess
 * with child elements
 * 
 * @author kristin.polenz
 *
 */
public class ImportSubProcessTest extends AbstractImportBpmnModelTest {
	
	@Test
	@DiagramResource
	public void testImportSubProcessToCheckExistingChildren() {
		ModelImport importer = createModelImport();
		importer.execute();

		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_3");
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(containerShape, BPMNShape.class);
		SubProcess subProcess = BusinessObjectUtil.getFirstElementOfType(containerShape, SubProcess.class);
		
		assertThat(bpmnShape).isNotNull();
		assertThat(subProcess).isNotNull();
		
		assertThat(containerShape)
			.bpmnChildren()
				.isNotEmpty()
				.hasSize(4);
	}
	
	@Test
	@DiagramResource
	public void testImportExpandedSubProcessWithVisibleChildren() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		BPMNShape bpmnShape = Util.findBpmnShapeByBusinessObjectId(diagram, "SubProcess_3");
		
		assertThat(bpmnShape).isNotNull();
		assertThat(bpmnShape.isIsExpanded()).isTrue();
		
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_3");
		List<Shape> subProcessChildren = FeatureSupport.getBpmnChildShapes(containerShape);
		
		assertThat(subProcessChildren).isNotEmpty();
		
		for (Shape shape : subProcessChildren) {
			isShapeVisible(shape, true);
		}
	}
	
	@Test
	@DiagramResource
	public void testImportCollapsedSubProcessWithNonVisibleChildren() {
		ModelImport importer = createModelImport();
		importer.execute();
		
		BPMNShape bpmnShape = Util.findBpmnShapeByBusinessObjectId(diagram, "SubProcess_3");
		
		assertThat(bpmnShape).isNotNull();
		assertThat(bpmnShape.isIsExpanded()).isFalse();
		
		ContainerShape containerShape = (ContainerShape) Util.findShapeByBusinessObjectId(diagram, "SubProcess_3");
		List<Shape> subProcessChildren = FeatureSupport.getBpmnChildShapes(containerShape);
		
		assertThat(subProcessChildren).isNotEmpty();
		
		for (Shape shape : subProcessChildren) {
			isShapeVisible(shape, false);
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

