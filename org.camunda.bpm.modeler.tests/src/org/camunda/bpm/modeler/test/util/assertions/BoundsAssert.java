package org.camunda.bpm.modeler.test.util.assertions;

import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.api.FloatAssert;

public class BoundsAssert extends AbstractAssert<BoundsAssert, Bounds> {

	protected BoundsAssert(Bounds actual) {
		super(actual, BoundsAssert.class);
	}
	
	public BoundsAssert isEqualTo(IRectangle expected) {
		if (actual == null) {
			Assertions.fail(String.format("Expected actual to equal <%s> but was <null>", expected));
		}
		
		if (expected.getHeight() != actual.getHeight()) {
			Assertions.fail(String.format("Expected actual height to equal <%s> but was <%s>", expected.getHeight(), actual.getHeight()));
		}

		if (expected.getWidth() != actual.getWidth()) {
			Assertions.fail(String.format("Expected actual width to equal <%s> but was <%s>", expected.getWidth(), actual.getWidth()));
		}
		
		if (expected.getX() != actual.getX()) {
			Assertions.fail(String.format("Expected actual x to equal <%s> but was <%s>", expected.getX(), actual.getX()));
		}
		
		if (expected.getY() != actual.getY()) {
			Assertions.fail(String.format("Expected actual y to equal <%s> but was <%s>", expected.getY(), actual.getY()));
		}
		
		return myself;
	}
	
	public FloatAssert x() {
		return Assertions.assertThat(actual.getX());
	}

	public FloatAssert y() {
		return Assertions.assertThat(actual.getY());
	}
	
	public FloatAssert width() {
		return Assertions.assertThat(actual.getWidth());
	}
	
	public FloatAssert height() {
		return Assertions.assertThat(actual.getHeight());
	}
	
}
