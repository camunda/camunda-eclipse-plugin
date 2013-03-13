package org.camunda.bpm.modeler.test.util.assertions;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.location;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;

import java.util.List;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Fail;

public class IRectangleAssert extends AbstractAssert<IRectangleAssert, IRectangle> {


	private static final String EXPECT_CONTAINMENT = 
			"Expected point <(%s, %s)> to be contained in rect <(%s, %s, %s, %s)>";
	
	private static final String EXPECT_NO_CONTAINMENT = 
			"Expected point <(%s, %s)> NOT to be contained in rect <(%s, %s, %s, %s)>";
		
		protected IRectangleAssert(IRectangle actual) {
			super(actual, IRectangleAssert.class);
		}

		public IRectangleAssert contains(int x, int y) {
			return contains(point(x, y));
		}

		public IRectangleAssert contains(Point point) {
			if (!LayoutUtil.isContained(actual, location(point))) {
				Fail.fail(failMessage(EXPECT_CONTAINMENT, point, actual));
			}
			
			return myself;
		}

		public IRectangleAssert doesNotContain(int x, int y) {
			return doesNotContain(point(x, y));
		}

		public IRectangleAssert doesNotContain(Point point) {
			if (LayoutUtil.isContained(actual, location(point))) {
				Fail.fail(failMessage(EXPECT_NO_CONTAINMENT, point, actual));
			}
			
			return myself;
		}
		
		private String failMessage(String expectContainment, Point point, IRectangle rect) {
			
			return String.format(expectContainment, point.getX(), point.getY(), rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		}

		public IRectangleAssert doNotContainAnyOf(List<Point> points) {
			
			for (Point p: points) {
				doesNotContain(p);
			}
			
			return myself;
		}

		public PointAssert position() {
			return new PointAssert(point(actual.getX(), actual.getY()));
		}
	}
