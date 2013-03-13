package org.eclipse.bpmn2.modeler.core.test.layout.util;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.eclipse.bpmn2.modeler.core.test.util.operations.MoveShapeOperation.move;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.ScrollUtil;
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
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testContainerBoundsInProcess.bpmn")
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
		
		assertThat(scrollShape).position().isEqualTo(846, 706);
	}
	
}
