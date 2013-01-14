package org.eclipse.bpmn2.modeler.core.test.feature.move;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveElementOperation.move;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

public abstract class AbstractMoveDataItemTest extends AbstractFeatureTest {

	protected String elementId;
	
	public AbstractMoveDataItemTest(String elementId) {
		this.elementId = elementId;
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/feature/move/AbstractMoveDataItemTest.testBase.bpmn")
	public void testGetLabel() {
		Shape shape = Util.findShapeByBusinessObjectId(diagram, elementId);
		
		assertThat(shape).isNotNull();
		
		
	}
	
	public void testMoveLabelOnElementNotAllowed() {
		
	}
}
