package org.eclipse.bpmn2.modeler.core.test.util.assertions;

import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;

public class Bpmn2ModelAssertions extends Assertions {

	public static AbstractShapeAssert<?, ?> assertThat(Shape shape) {
		if (shape instanceof ContainerShape) {
			return new ContainerShapeAssert((ContainerShape) shape);
		} else {
			return new ShapeAssert(shape);
		}
	}

	public static LinkedObjectCondition elementOfType(Class<?> cls) {
		return new LinkedObjectCondition().ofType(cls);
	}
	
	public static LinkedObjectCondition elementIdentifiedBy(String id) {
		return new LinkedObjectCondition().identifiedBy(id);
	}
	
	public static FreeFormConnectionAssert assertThat(FreeFormConnection connection) {
		return new FreeFormConnectionAssert(connection);
	}
	
	public static PointAssert assertThat(Point point) {
		return new PointAssert(point);
	}
}
