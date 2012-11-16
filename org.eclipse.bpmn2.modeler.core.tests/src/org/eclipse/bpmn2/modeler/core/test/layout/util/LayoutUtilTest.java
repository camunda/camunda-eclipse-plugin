package org.eclipse.bpmn2.modeler.core.test.layout.util;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.ShapeUtil;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Nico Rehwaldt
 */
public class LayoutUtilTest extends AbstractFeatureTest {
	

	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testHorizontalTreshold() {
		Shape task1 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_1");
		Shape start2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		Shape task2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_2");

		assertThat(LayoutUtil.getHorizontalLayoutTreshold(LayoutUtil.getCenter(task1), LayoutUtil.getCenter(start2))).isGreaterThan(0);
		assertThat(LayoutUtil.getHorizontalLayoutTreshold(LayoutUtil.getCenter(start2), LayoutUtil.getCenter(task1))).isLessThan(0);
		assertThat(LayoutUtil.getHorizontalLayoutTreshold(LayoutUtil.getCenter(start2), LayoutUtil.getCenter(task2))).isEqualTo(0);
	}

	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testAssertNoDiagonalEdgesPass() {
		Shape start1 = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape task1 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Shape start2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		Shape task2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_2");

		assertThat(LayoutUtil.getLayoutTreshold(start1, task1)).isEqualTo(-0.683);
		assertThat(LayoutUtil.getLayoutTreshold(task1, start1)).isEqualTo(0.682); // 45 degree
		
		double treshold1 = LayoutUtil.getLayoutTreshold(start2, task2);
		assertThat(treshold1).isEqualTo(1.0);

		double treshold2 = LayoutUtil.getLayoutTreshold(task1, task2);
		assertThat(treshold2).isEqualTo(0.0);
		
		double treshold3 = LayoutUtil.getLayoutTreshold(task2, task1);
		assertThat(treshold3).isEqualTo(0.0);
		
		double treshold4 = LayoutUtil.getLayoutTreshold(start2, task1);
		assertThat(treshold4).isEqualTo(0.668);
		
		double treshold5 = LayoutUtil.getLayoutTreshold(task2, start1); // target is top right
		assertThat(treshold5).isEqualTo(0.69);
	}
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testLeftRightDetection () {
		Shape start1 = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape task1 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Shape start2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		Shape task2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_2");
		
		assertTrue(LayoutUtil.isRightToStartShape(start2, task1));
		assertTrue(LayoutUtil.isLeftToStartShape(start1, task2));
		
		assertFalse(LayoutUtil.isRightToStartShape(task1, task2));
		assertFalse(LayoutUtil.isLeftToStartShape(task1, task2));
	}
	
	
	@Test
	@DiagramResource("org/eclipse/bpmn2/modeler/core/test/layout/util/LayoutUtilTest.testBase.bpmn")
	public void testAboveBeneathDetection () {
		Shape start1 = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_1");
		Shape task1 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_1");
		
		Shape start2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "StartEvent_2");
		Shape task2 = ShapeUtil.findShapeByBusinessObjectId(diagram, "Task_2");
		
		assertTrue(LayoutUtil.isAboveStartShape(start2, task1));
		assertTrue(LayoutUtil.isBeneathStartShape(task1, start1));
		
		assertFalse(LayoutUtil.isAboveStartShape(start2, task2));
		assertFalse(LayoutUtil.isBeneathStartShape(start2, task2));
	}
	
}
