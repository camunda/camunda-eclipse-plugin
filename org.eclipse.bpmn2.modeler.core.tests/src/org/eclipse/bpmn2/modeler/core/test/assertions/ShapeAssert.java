package org.eclipse.bpmn2.modeler.core.test.assertions;

import static java.lang.String.format;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.core.Condition;

public class ShapeAssert extends AbstractShapeAssert<ShapeAssert, Shape>{

	public ShapeAssert(Shape actual) {
		super(actual, ShapeAssert.class);
	}

	@Override
	public ShapeAssert hasChildren() {
		return failNoContainerShape();
	}

	@Override
	public ShapeAssert hasChildLinkedTo(Condition<EObject> condition) {
		return failNoContainerShape();
	}

	@Override
	public AbstractShapeAssert<ShapeAssert, Shape> hasNoChildren() {
		return myself;
	}

	@Override
	public AbstractShapeAssert<ShapeAssert, Shape> isContainerShape() {
		return failNoContainerShape();
	}
	
	protected ShapeAssert failNoContainerShape() {

		String message = format("Expected instance of ContainerShape, got <%s>", actual);
		
		Assertions.fail(message);
		return myself;
	}
}
