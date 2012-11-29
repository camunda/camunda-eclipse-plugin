package org.eclipse.bpmn2.modeler.core.test.layout.util;

import static org.eclipse.bpmn2.modeler.core.test.util.assertions.Bpmn2ModelAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.internal.datatypes.impl.LocationImpl;
import org.eclipse.graphiti.internal.datatypes.impl.RectangleImpl;
import org.junit.Test;

public class LayoutUtilStaticTest {

	
	@Test
	public void testGetSector() {
		assertThat(LayoutUtil.getSector(1, 1, 0, 0)).isEqualTo(Sector.BOTTOM_RIGHT);
		assertThat(LayoutUtil.getSector(1, 0, 0, 0)).isEqualTo(Sector.RIGHT);
		assertThat(LayoutUtil.getSector(1, -1, 0, 0)).isEqualTo(Sector.TOP_RIGHT);
		assertThat(LayoutUtil.getSector(0, 1, 0, 0)).isEqualTo(Sector.BOTTOM);
		assertThat(LayoutUtil.getSector(0, 0, 0, 0)).isEqualTo(Sector.UNDEFINED);
		assertThat(LayoutUtil.getSector(0, -1, 0, 0)).isEqualTo(Sector.TOP);
		assertThat(LayoutUtil.getSector(-1, 1, 0, 0)).isEqualTo(Sector.BOTTOM_LEFT);
		assertThat(LayoutUtil.getSector(-1, 0, 0, 0)).isEqualTo(Sector.LEFT);
		assertThat(LayoutUtil.getSector(-1, -1, 0, 0)).isEqualTo(Sector.TOP_LEFT);
	}
	
	@Test
	public void testGetChopboxIntersectionSector() {
		IRectangle box = rectangle(-4, -2, 8, 4);

		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(4, 4))).isEqualTo(Sector.BOTTOM);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(2, 4))).isEqualTo(Sector.BOTTOM);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(6, 3))).isEqualTo(Sector.BOTTOM_RIGHT);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(8, 2))).isEqualTo(Sector.RIGHT);
		
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(4, -4))).isEqualTo(Sector.TOP);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(2, -4))).isEqualTo(Sector.TOP);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(6, -3))).isEqualTo(Sector.TOP_RIGHT);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(8, -2))).isEqualTo(Sector.RIGHT);

		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(-4, -4))).isEqualTo(Sector.TOP);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(-2, -4))).isEqualTo(Sector.TOP);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(-6, -3))).isEqualTo(Sector.TOP_LEFT);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(-8, -2))).isEqualTo(Sector.LEFT);
		
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(-4, 4))).isEqualTo(Sector.BOTTOM);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(-2, 4))).isEqualTo(Sector.BOTTOM);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(-6, 3))).isEqualTo(Sector.BOTTOM_LEFT);
		assertThat(LayoutUtil.getChopboxIntersectionSector(box, location(-8, 2))).isEqualTo(Sector.LEFT);
	}

	@Test
	public void testGetChopboxIntersectionPoint() {
		IRectangle box = rectangle(-4, -2, 8, 4);

		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(4, 4))).isEqualTo(location(2, 2));
	
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(2, 4))).isEqualTo(location(1, 2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(6, 3))).isEqualTo(location(4, 2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(8, 2))).isEqualTo(location(4, 1));

		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(4, -4))).isEqualTo(location(2, -2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(2, -4))).isEqualTo(location(1, -2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(6, -3))).isEqualTo(location(4, -2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(8, -2))).isEqualTo(location(4, -1));

		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(-4, 4))).isEqualTo(location(-2, 2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(-2, 4))).isEqualTo(location(-1, 2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(-6, 3))).isEqualTo(location(-4, 2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(-8, 2))).isEqualTo(location(-4, 1));

		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(-4, -4))).isEqualTo(location(-2, -2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(-2, -4))).isEqualTo(location(-1, -2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(-6, -3))).isEqualTo(location(-4, -2));
		assertThat(LayoutUtil.getChopboxIntersectionPoint(box, location(-8, -2))).isEqualTo(location(-4, -1));
	}
	
	// static helpers //////////////////////////////

 	public static ILocation location(int x, int y) {
 		return new LocationImpl(x + 100, y + 100);
 	}
 	
 	@SuppressWarnings("restriction")
	public static IRectangle rectangle(int x, int y, int width, int height) {
 		return new RectangleImpl(x + 100, y + 100, width, height);
 	}
}
