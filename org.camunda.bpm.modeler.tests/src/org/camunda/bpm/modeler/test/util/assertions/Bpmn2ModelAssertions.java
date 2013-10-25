package org.camunda.bpm.modeler.test.util.assertions;

import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;

public class Bpmn2ModelAssertions extends Assertions {

	public static <S extends AbstractShapeAssert<S, A>, A extends Shape> AbstractShapeAssert<S, A> assertThat(Shape shape) {
		if (shape instanceof ContainerShape) {
			return (AbstractShapeAssert<S, A>) new ContainerShapeAssert((ContainerShape) shape);
		} else {
			return (AbstractShapeAssert<S, A>) new ShapeAssert(shape);
		}
	}

	public static LinkedObjectCondition elementOfType(Class<?> cls) {
		return new LinkedObjectCondition().ofType(cls);
	}

	public static LinkedObjectCondition element(EObject element) {
		return new LinkedObjectCondition().withIdentity(element);
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

	public static IRectangleAssert assertThat(IRectangle rectangle) {
		return new IRectangleAssert(rectangle);
	}
	
	public static VectorAssert assertThat(Vector vector) {
		return new VectorAssert(vector);
	}
	
	public static BpmnShapeAssert assertThat(BPMNShape bpmnShape) {
		return new BpmnShapeAssert(bpmnShape);
	}
}
