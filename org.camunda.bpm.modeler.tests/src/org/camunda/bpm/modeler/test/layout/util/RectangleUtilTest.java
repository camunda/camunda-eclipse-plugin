package org.camunda.bpm.modeler.test.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.*;
import static org.camunda.bpm.modeler.core.layout.util.RectangleUtil.*;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.layout.util.VectorUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.junit.Test;

public class RectangleUtilTest {

	@Test
	public void testResizeShrinkTopLeft() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// when
		IRectangle resizedRect = resize(rect, point(10, 10), Sector.TOP_LEFT);
		
		// then 
		assertThat(resizedRect).isEqualTo(rectangle(20, 20, 30, 30));
	}

	@Test
	public void testResizeEnlargeTopLeft() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// when
		IRectangle resizedRect = resize(rect, point(-10, -10), Sector.TOP_LEFT);
		
		// then 
		assertThat(resizedRect).isEqualTo(rectangle(0, 0, 50, 50));
	}
	
	@Test
	public void testResizeShrinkBottomRight() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// when
		IRectangle resizedRect = resize(rect, point(-10, -10), Sector.BOTTOM_RIGHT);
		
		// then 
		assertThat(resizedRect).isEqualTo(rectangle(10, 10, 30, 30));
	}

	@Test
	public void testResizeEnlargeBottomRight() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// when
		IRectangle resizedRect = resize(rect, point(10, 10), Sector.BOTTOM_RIGHT);
		
		// then 
		assertThat(resizedRect).isEqualTo(rectangle(10, 10, 50, 50));
	}

	@Test
	public void testResizeShrinkTopRight() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// when
		IRectangle resizedRect = resize(rect, point(-10, 10), Sector.TOP_RIGHT);
		
		// then
		assertThat(resizedRect).isEqualTo(rectangle(10, 20, 30, 30));
	}

	@Test
	public void testResizeEnlargeTopRight() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// when
		IRectangle resizedRect = resize(rect, point(10, -10), Sector.TOP_RIGHT);
		
		// then 
		assertThat(resizedRect).isEqualTo(rectangle(10, 0, 50, 50));
	}
	

	@Test
	public void testResizeShrinkBottomLeft() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// when
		IRectangle resizedRect = resize(rect, point(10, -10), Sector.BOTTOM_LEFT);
		
		// then
		assertThat(resizedRect).isEqualTo(rectangle(20, 10, 30, 30));
	}

	@Test
	public void testResizeEnlargeBottomLeft() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// when
		IRectangle resizedRect = resize(rect, point(-10, 10), Sector.BOTTOM_LEFT);
		
		// then 
		assertThat(resizedRect).isEqualTo(rectangle(0, 10, 50, 50));
	}

	@Test
	public void testTranslate() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// check correct translation
		
		// translate by rect
		assertThat(translate(rect, rectangle(10, 10, 10, 10))).isEqualTo(rectangle(20, 20, 50, 50));
		assertThat(translate(rect, rectangle(-10, -10, -10, -10))).isEqualTo(rectangle(0, 0, 30, 30));
		
		// translate by point
		assertThat(translate(rect, point(-10, -10))).isEqualTo(rectangle(0, 0, 40, 40));
		assertThat(translate(rect, point(10, 10))).isEqualTo(rectangle(20, 20, 40, 40));
	}
}