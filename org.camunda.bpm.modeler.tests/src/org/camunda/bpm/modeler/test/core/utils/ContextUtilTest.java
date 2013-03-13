package org.camunda.bpm.modeler.test.core.utils;

import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.fest.assertions.api.Fail;
import org.junit.Test;

public class ContextUtilTest {

	@Test
	public void testIs() {

		// given 
		IAddContext ctx = new AddContext();
		
		// when
		ctx.putProperty("PROP", "foobar");

		// then
		assertThat(ContextUtil.is(ctx, "PROP"));
	}
	
	@Test
	public void testIsFalse() {
		
		// given 
		IAddContext ctx = new AddContext();

		// when
		ctx.putProperty("PROP", false);
		
		// then
		assertThat(ContextUtil.is(ctx, "PROP"));
	}

	@Test
	public void testIsNot() {
		
		// given 
		IAddContext ctx = new AddContext();

		// when
		
		// then
		assertThat(ContextUtil.isNot(ctx, "NON_EXISTENT_PROPERTY"));
	}
	
	@Test
	public void testIsNotRemovedProperty() {
		
		// given 
		IAddContext ctx = new AddContext();

		// when
		ctx.putProperty("REMOVED_PROPERTY", false);
		ctx.putProperty("REMOVED_PROPERTY", null);
		
		// then
		assertThat(ContextUtil.isNot(ctx, "REMOVED_PROPERTY"));
	}
	
	@Test
	public void testGetCls() {

		// given 
		IAddContext ctx = new AddContext();

		// when
		ctx.putProperty("PROP", "foobar");
		
		String prop = ContextUtil.get(ctx, "PROP", String.class);
		
		// then
		assertThat(prop).isEqualTo("foobar");
	}
	
	@Test
	public void testGetClsThrowsExceptionOnWrongClass() {

		// given 
		IAddContext ctx = new AddContext();

		// when
		ctx.putProperty("PROP", "foobar");
		
		try {
			ContextUtil.get(ctx, "PROP", Integer.class);
			Fail.fail("Expected <IllegalArgumentException> to be thrown");
		} catch (IllegalArgumentException e) {
			; // expected
		}
	}
}
