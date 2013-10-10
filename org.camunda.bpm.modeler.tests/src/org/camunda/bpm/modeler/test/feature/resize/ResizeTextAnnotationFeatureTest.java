package org.camunda.bpm.modeler.test.feature.resize;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.junit.Test;

public class ResizeTextAnnotationFeatureTest extends AbstractResizeFeatureTest {

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeTextAnnotationFeatureTest.testBase.bpmn")
	public void testResizeBottom() {

		// y = -20 is allowed
		assertResize("TextAnnotation_1", point(0, -20), Sector.BOTTOM);
		
		// y = -10 makes text annotation too small
		assertNoResize("TextAnnotation_1", point(0, -10), Sector.BOTTOM);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/feature/resize/ResizeTextAnnotationFeatureTest.testBase.bpmn")
	public void testResizeRight() {

		// x = -50 is allowed
		assertResize("TextAnnotation_1", point(-50, 0), Sector.RIGHT);
		
		// x = -10 makes text annotation too small
		assertNoResize("TextAnnotation_1", point(-10, 0), Sector.RIGHT);
	}
	
}
