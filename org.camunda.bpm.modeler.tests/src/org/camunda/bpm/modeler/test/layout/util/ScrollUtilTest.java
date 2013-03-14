package org.camunda.bpm.modeler.test.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.camunda.bpm.modeler.test.util.operations.CreateParticipantOperation.createParticipant;
import static org.camunda.bpm.modeler.test.util.operations.MoveShapeOperation.move;

import org.camunda.bpm.modeler.core.layout.util.ConversionUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author adrobisch
 */
public class ScrollUtilTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/layout/util/LayoutUtilTest.testContainerBoundsInProcess.bpmn")
	public void testScrollShapeUpdate() {
		// given
		Diagram diagramUnderTest = diagram;
		
		// when
		Shape scrollShape = ScrollUtil.getScrollShape(diagramUnderTest);
		
		// then
		assertThat(scrollShape).position().isEqualTo(846, 506);
		
		Shape taskShape = Util.findShapeByBusinessObjectId(diagram, "UserTask_1");

		Point movement = point(0, 200);

		move(taskShape, diagramTypeProvider)
			.by(movement)
			.execute();
		
		IRectangle diagramBounds = LayoutUtil.getBounds(diagramUnderTest, 0, 0, 0, 0);
		
		assertThat(diagramBounds).isEqualTo(ConversionUtil.rect(288, 298, 408, 258));
		assertThat(scrollShape).position().isEqualTo(846, 706);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/layout/util/LayoutUtilTest.testContainerBoundsInProcess.bpmn")
	public void testScrollShapeAfterParticipantAdd() {
		// given
		Diagram diagramUnderTest = diagram;
		assertThat(ScrollUtil.getScrollShape(diagramUnderTest)).position().isEqualTo(846, 506);
		
		// when
		createParticipant(20, 20, 400, 200, getDiagram(), getDiagramTypeProvider()).execute();
		
		Shape scrollShape = ScrollUtil.getScrollShape(diagramUnderTest);
		
		// then
		assertThat(scrollShape).position().isEqualTo(957, 617);
	}
	
}
