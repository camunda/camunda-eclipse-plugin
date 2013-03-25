package org.camunda.bpm.modeler.test.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.location;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
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

	@Test
	public void testBBox() {
		LayoutUtil.BBox bbox = new LayoutUtil.BBox(10, 10);
		
		// bbox bounds is null unless initialized
		assertThat(bbox.getBounds()).isNull();
		
		// when
		// adding first bounds
		bbox.addBounds(rectangle(10, 10, 50, 50));
		
		// then
		// bounds should be first bounds + padding
		assertThat(bbox.getBounds()).isEqualTo(rectangle(0, 0, 70, 70));
		
		// when
		// adding second bounds
		bbox.addBounds(rectangle(40, 40, 30, 30));

		assertThat(bbox.getBounds()).isEqualTo(rectangle(0, 0, 80, 80));
	}

	@Test
	public void testIsContainedRectangle() {
		IRectangle box = rectangle(-50, -25, 100, 50);

		// contain
		assertThat(LayoutUtil.isContained(rectangle(-49, -24, 98, 48), box)).isTrue();
		// no left contain
		assertThat(LayoutUtil.isContained(rectangle(-51, -24, 98, 48), box)).isFalse();
		// no top contain
		assertThat(LayoutUtil.isContained(rectangle(-49, -26, 98, 48), box)).isFalse();
		// no right contain
		assertThat(LayoutUtil.isContained(rectangle(-49, -24, 101, 48), box)).isFalse();
		// no bottom contain
		assertThat(LayoutUtil.isContained(rectangle(-49, -24, 98, 51), box)).isFalse();
		
		// equal != contained
		assertThat(LayoutUtil.isContained(box, box)).isFalse();

		// equal == contained with tolerance 1
		assertThat(LayoutUtil.isContained(box, box, -1)).isTrue();
	}
	
	@Test
	public void testIsContainedSectorRectangle() {
		IRectangle box = rectangle(-50, -25, 100, 50);

		// no left contain
		assertThat(LayoutUtil.isContained(rectangle(-51, -24, 98, 48), box, Sector.LEFT)).isFalse();
		// but contain with different sector check
		assertThat(LayoutUtil.isContained(rectangle(-51, -24, 98, 48), box, Sector.RIGHT)).isTrue();
		
		// no top contain
		assertThat(LayoutUtil.isContained(rectangle(-49, -26, 98, 48), box, Sector.TOP)).isFalse();
		// but contain with different sector check
		assertThat(LayoutUtil.isContained(rectangle(-49, -26, 98, 48), box, Sector.BOTTOM)).isTrue();
		
		// no right contain
		assertThat(LayoutUtil.isContained(rectangle(-49, -24, 101, 48), box, Sector.RIGHT)).isFalse();
		// but contain with different sector check
		assertThat(LayoutUtil.isContained(rectangle(-49, -24, 101, 48), box, Sector.LEFT)).isTrue();
		
		// no bottom contain
		assertThat(LayoutUtil.isContained(rectangle(-49, -24, 98, 51), box, Sector.BOTTOM)).isFalse();
		// but contain with different sector check
		assertThat(LayoutUtil.isContained(rectangle(-49, -24, 98, 51), box, Sector.TOP)).isTrue();
	}
}
