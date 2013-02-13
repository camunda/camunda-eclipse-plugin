package org.eclipse.bpmn2.modeler.core.test.util.assertions;

import org.eclipse.bpmn2.modeler.core.layout.util.VectorUtil;
import org.eclipse.draw2d.geometry.Vector;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;

public class VectorAssert extends AbstractAssert<VectorAssert, Vector> {

	protected VectorAssert(Vector actual) {
		super(actual, VectorAssert.class);
	}

	public VectorAssert isEqualTo(Vector expected) {
		return isEqualTo(expected, 0.001);
	}
	
	public VectorAssert isEqualTo(Vector expected, double tolerance) {
		
		if (actual == null) {
			Assertions.fail(String.format("Expected actual to equal <%s> but was <null>", expected));
		}
		
		if (!VectorUtil.equal(expected, actual, tolerance)) {
			Assertions.fail(String.format("Expected actual to equal <%s> but was <%s>", VectorUtil.asString(expected), VectorUtil.asString(actual)));
		}
		
		return myself;
	}
}
