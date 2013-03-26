package org.camunda.bpm.modeler.test.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.*;
import static org.camunda.bpm.modeler.core.layout.util.RectangleUtil.*;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.layout.util.RectangleUtil.ResizeDiff;
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

	@Test
	public void testSubstract() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// check correct translation
		
		// translate by rect
		assertThat(substract(rect, rect)).isEqualTo(rectangle(0, 0, 0, 0));
		assertThat(substract(rect, rectangle(10, 10, 10, 10))).isEqualTo(rectangle(0, 0, 30, 30));
	}

	@Test
	public void testResizeDiff() {
		
		// given
		IRectangle rect = rectangle(10, 10, 40, 40);
		
		// check correct resize diff computation

		// top right
		ResizeDiff diff1 = resizeDiff(rect, rectangle(10, 0, 50, 50));
		
		assertThat(diff1.getResizeDelta()).isEqualTo(point(10, -10));
		assertThat(diff1.getResizeDirection()).isEqualTo(Sector.TOP_RIGHT);
		
		// top left
		ResizeDiff diff2 = resizeDiff(rect, rectangle(0, 0, 50, 50));
		
		assertThat(diff2.getResizeDelta()).isEqualTo(point(-10, -10));
		assertThat(diff2.getResizeDirection()).isEqualTo(Sector.TOP_LEFT);
		
		// bottom right
		ResizeDiff diff3 = resizeDiff(rect, rectangle(10, 10, 50, 50));
		
		assertThat(diff3.getResizeDelta()).isEqualTo(point(10, 10));
		assertThat(diff3.getResizeDirection()).isEqualTo(Sector.BOTTOM_RIGHT);
		
		// bottom left
		ResizeDiff diff0 = resizeDiff(rect, rectangle(0, 10, 50, 50));
		
		assertThat(diff0.getResizeDelta()).isEqualTo(point(-10, 10));
		assertThat(diff0.getResizeDirection()).isEqualTo(Sector.BOTTOM_LEFT);
	}
}