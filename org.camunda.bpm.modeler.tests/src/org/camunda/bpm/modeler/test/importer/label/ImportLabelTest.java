package org.camunda.bpm.modeler.test.importer.label;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * Import tests for labels
 * 
 * @author nico.rehwaldt
 */
public class ImportLabelTest extends AbstractImportBpmnModelTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/label/ImportLabelTest.testBase.bpmn")
	public void testImportLabelWithDiBounds() {

		// given
		Point expectedPosition = point(150, 190);
		String elementId = "StartEvent_1";

		// when
		importDiagram();
		
		// then
		assertHasLabelFlowElementLabelWithPosition(elementId, expectedPosition);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/label/ImportLabelTest.testBase.bpmn")
	public void testImportLongLabelWithoutDiBounds() {
		
		// given
		Point expectedPosition = point(350, 180);
		String elementId = "InclusiveGateway_1";

		// when
		importDiagram();
		
		// then
		assertHasLabelFlowElementLabelWithPosition(elementId, expectedPosition);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/label/ImportLabelTest.testBase.bpmn")
	public void testImportShortLabelWithoutDiBounds() {
		
		// given
		Point expectedPosition = point(500, 175);
		String elementId = "_DataStoreReference_3";

		// when
		importDiagram();
		
		// then
		assertHasLabelFlowElementLabelWithPosition(elementId, expectedPosition);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/label/ImportLabelTest.testBase.bpmn")
	public void testImportNestedElementLabelWithDiBounds() {
		
		// given
		Point expectedPosition = point(678, 260);
		String elementId = "DataObjectReference_1";

		// when
		importDiagram();
		
		// then
		assertHasLabelFlowElementLabelWithPosition(elementId, expectedPosition);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/label/ImportLabelTest.testBase.bpmn")
	public void testImportNestedElementLabelWithoutDiBounds() {
		
		// given
		Point expectedPosition = point(798, 225);
		String elementId = "DataOutput_1";

		// when
		importDiagram();
		
		// then
		assertHasLabelFlowElementLabelWithPosition(elementId, expectedPosition);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/label/ImportLabelTest.testBase.bpmn")
	public void testImportBoundaryLabelWithoutDiBounds() {
		
		// given
		Point expectedPosition = point(166, 370);
		String elementId = "BoundaryEvent_1";

		// when
		importDiagram();
		
		// then
		assertHasLabelFlowElementLabelWithPosition(elementId, expectedPosition);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/label/ImportLabelTest.testBase.bpmn")
	public void testImportBoundaryLabelWithDiBounds() {
		
		// given
		Point expectedPosition = point(338, 371);
		String elementId = "BoundaryEvent_2";

		// when
		importDiagram();
		
		// then
		assertHasLabelFlowElementLabelWithPosition(elementId, expectedPosition);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/label/ImportLabelTest.testBase.bpmn")
	public void testImportNestedBoundaryLabelWithoutBounds() {
		
		// given
		Point expectedPosition = point(818, 371);
		String elementId = "BoundaryEvent_3";

		// when
		importDiagram();
		
		// then
		assertHasLabelFlowElementLabelWithPosition(elementId, expectedPosition);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/label/ImportLabelTest.testConnectionBase.bpmn")
	public void testImportSequenceFlowLabelWithoutBounds() {
		
		// given
		Point expectedPosition = point(716, 288);
		String elementId = "SequenceFlow_2";

		// when
		importDiagram();
		
		// then
		assertSequenceFlowLabelWithPosition(elementId, expectedPosition);
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/importer/label/ImportLabelTest.testConnectionBase.bpmn")
	public void testImportSequenceFlowLabelWithBounds() {
		
		// given
		Point expectedPosition = point(423, 220);
		String elementId = "SequenceFlow_1";

		// when
		importDiagram();
		
		// then
		assertSequenceFlowLabelWithPosition(elementId, expectedPosition);
	}
	
	protected void assertHasLabelWithPosition(PictogramElement pictogramElement, Point expectedPosition, Point expectedLabelOffset) {
		
		assertThat(pictogramElement)
			.isNotNull();
		
		Shape labelShape = LabelUtil.getLabelShape(pictogramElement, getDiagram());
		
		assertThat(labelShape)
			.isNotNull();
		
		Point labelPosition = LabelUtil.getLabelPosition(labelShape);
		
		// shape position should equal expected
		assertThat(labelPosition)
			.isEqualTo(expectedPosition);
		
		// label offset should be set as expected
		
		Point storedLabelOffset = LabelUtil.getStoredLabelOffset(labelShape);
		assertThat(storedLabelOffset)
			.isEqualTo(expectedLabelOffset);
	}

	protected void assertSequenceFlowLabelWithPosition(String elementId, Point expectedPosition) {
		Connection connection = Util.findConnectionByBusinessObjectId(getDiagram(), elementId);
		
		Point connectionReferencePoint = LayoutUtil.getConnectionReferencePoint(connection, expectedPosition);
		Point expectedLabelOffset = point(expectedPosition.getX() - connectionReferencePoint.getX(), expectedPosition.getY() - connectionReferencePoint.getY());
		
		assertHasLabelWithPosition(connection, expectedPosition, expectedLabelOffset);	
	}
	
	protected void assertHasLabelFlowElementLabelWithPosition(String elementId, Point expectedPosition) {
		Shape shape = Util.findShapeByBusinessObjectId(getDiagram(), elementId);
		
		Point shapeCenter = point(LayoutUtil.getAbsoluteShapeCenter(shape));
		Point expectedLabelOffset = point(expectedPosition.getX() - shapeCenter.getX(), expectedPosition.getY() - shapeCenter.getY());
		
		assertHasLabelWithPosition(shape, expectedPosition, expectedLabelOffset);
	}
}
