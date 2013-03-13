package org.camunda.bpm.modeler.test.layout.util;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.*;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;

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
	
	@Test
	public void testIsContained() {
		IRectangle box = rectangle(-50, -25, 100, 50);
		
		assertThat(LayoutUtil.isContained(box, location(-50, -25))).isFalse();
		assertThat(LayoutUtil.isContained(box, location(50, -25))).isFalse();
		assertThat(LayoutUtil.isContained(box, location(-50, 25))).isFalse();
		assertThat(LayoutUtil.isContained(box, location(50, 25))).isFalse();

		assertThat(LayoutUtil.isContained(box, location(-49, -24))).isTrue();
		assertThat(LayoutUtil.isContained(box, location(49, -24))).isTrue();
		assertThat(LayoutUtil.isContained(box, location(-49, 24))).isTrue();
		assertThat(LayoutUtil.isContained(box, location(49, 24))).isTrue();

		// custom tolerance can be set, too
		assertThat(LayoutUtil.isContained(box, location(-50, -25), 1)).isTrue();
		assertThat(LayoutUtil.isContained(box, location(50, -25), 1)).isTrue();
		assertThat(LayoutUtil.isContained(box, location(-50, 25), 1)).isTrue();
		assertThat(LayoutUtil.isContained(box, location(50, 25), 1)).isTrue();
	}
	
	@Test
	public void testBox() {
		IRectangle dimensions = rectangle(-1, -1, 400, 100);

		assertThat(LayoutUtil.box(rectangle(10, 10, 50, 50), dimensions, 10)).isEqualTo(rectangle(10, 10, 50, 50));
		assertThat(LayoutUtil.box(rectangle(0, 0, 50, 50), dimensions, 10)).isEqualTo(rectangle(10, 10, 50, 50));

		assertThat(LayoutUtil.box(rectangle(340, 10, 50, 50), dimensions, 10)).isEqualTo(rectangle(340, 10, 50, 50));
		assertThat(LayoutUtil.box(rectangle(390, 0, 50, 50), dimensions, 10)).isEqualTo(rectangle(340, 10, 50, 50));

		assertThat(LayoutUtil.box(rectangle(10, 40, 50, 50), dimensions, 10)).isEqualTo(rectangle(10, 40, 50, 50));
		assertThat(LayoutUtil.box(rectangle(10, 60, 50, 50), dimensions, 10)).isEqualTo(rectangle(10, 40, 50, 50));

		assertThat(LayoutUtil.box(rectangle(340, 40, 50, 50), dimensions, 10)).isEqualTo(rectangle(340, 40, 50, 50));
		assertThat(LayoutUtil.box(rectangle(390, 60, 50, 50), dimensions, 10)).isEqualTo(rectangle(340, 40, 50, 50));
		
		assertThat(LayoutUtil.box(rectangle(10, 10, 400, 50), dimensions, 10)).isEqualTo(rectangle(10, 10, 380, 50));
		assertThat(LayoutUtil.box(rectangle(10, 10, 50, 150), dimensions, 10)).isEqualTo(rectangle(10, 10, 50, 80));
	}
}
