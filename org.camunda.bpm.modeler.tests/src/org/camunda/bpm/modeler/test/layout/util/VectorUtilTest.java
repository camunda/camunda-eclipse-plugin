package org.camunda.bpm.modeler.test.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.vector;
import static org.camunda.bpm.modeler.core.layout.util.VectorUtil.add;
import static org.camunda.bpm.modeler.core.layout.util.VectorUtil.divide;
import static org.camunda.bpm.modeler.core.layout.util.VectorUtil.multiply;
import static org.camunda.bpm.modeler.test.util.assertions.Bpmn2ModelAssertions.assertThat;

import org.camunda.bpm.modeler.core.layout.util.VectorUtil;
import org.eclipse.draw2d.geometry.Vector;
import org.junit.Test;

public class VectorUtilTest {

	@Test
	public void testAdd() {
		
		// given
		Vector a = vector(3, 4);
		Vector b = vector(10, -1);
		
		// when
		Vector sum = add(a, b);
		
		// then 
		assertThat(sum).isEqualTo(vector(13, 3));
	}

	@Test
	public void testMultiplyTruncate() {
		
		// given
		Vector a = vector(3, 4);
		
		// when
		Vector multiplied = multiply(a, 1.5);
		
		// then 
		assertThat(multiplied).isEqualTo(vector(4.5, 6));
	}
	
	@Test
	public void testMultiply() {
		
		// given
		Vector a = vector(3, 4);
		
		// when
		Vector multiplied = multiply(a, 2);
		
		// then 
		assertThat(multiplied).isEqualTo(vector(6, 8));
	}

	@Test
	public void testDivide() {
		
		// given
		Vector a = vector(4.5, 9);
		
		// when
		Vector sum = divide(a, 3);
		
		// then 
		assertThat(sum).isEqualTo(vector(1.5, 3));
	}

	@Test
	public void testEqual() {
		
		// given
		Vector a = vector(4.5, 9);
		Vector b = vector(4.5, 9);
		
		// then 
		assertThat(VectorUtil.equal(a, b)).isTrue();
	}
}