package org.eclipse.bpmn2.modeler.core.test.layout.util;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.bpmn2.modeler.core.utils.LayoutUtil;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class LayoutUtilTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testAssertNoDiagonalEdgesPass() {
		Shape start1 = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape task1 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Shape start2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		Shape task2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_2");

		assertThat(LayoutUtil.getLayoutTreshold(start1, task1)).isEqualTo(-100.0);
		assertThat(LayoutUtil.getLayoutTreshold(task1, start1)).isEqualTo(100.0);
		
		double treshold1 = LayoutUtil.getLayoutTreshold(start2, task2);
		assertThat(treshold1).isEqualTo(0.0);

		double treshold2 = LayoutUtil.getLayoutTreshold(task1, task2);
		assertThat(treshold2).isEqualTo(0.0);
		
		double treshold3 = LayoutUtil.getLayoutTreshold(task2, task1);
		assertThat(treshold3).isEqualTo(0.0);

	}
}
