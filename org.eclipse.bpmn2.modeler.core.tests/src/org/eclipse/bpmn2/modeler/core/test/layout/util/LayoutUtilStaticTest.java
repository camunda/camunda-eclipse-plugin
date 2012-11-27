package org.eclipse.bpmn2.modeler.core.test.layout.util;

import static org.fest.assertions.api.Assertions.assertThat;

import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil.Sector;
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
}
