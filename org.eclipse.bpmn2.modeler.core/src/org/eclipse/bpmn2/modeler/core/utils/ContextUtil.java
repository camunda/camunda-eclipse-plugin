package org.eclipse.bpmn2.modeler.core.utils;

import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;

/**
 * Utility to query a {@link IContext} for flags an properties. 
 * 
 * @author nico.rehwaldt
 */
public class ContextUtil {

	/**
	 * Queries for the existence of the given flag in the ctx.
	 * 
	 * @param ctx
	 * @param flag
	 * 
	 * @return true if the flag is set
	 */
	public static boolean is(IContext ctx, Object flag) {
		return get(ctx, flag) != null;
	}
	
	/**
	 * Queries for the absence of the given flag in the ctx.
	 * 
	 * @param ctx
	 * @param flag
	 * 
	 * @return true if the flag is set
	 */
	public static boolean isNot(IContext ctx, Object flag) {
		return !is(ctx, flag);
	}
	
	/**
	 * Activates the flag on the ctx.
	 * 
	 * @param ctx
	 * @param flag
	 */
	public static void set(IContext ctx, Object flag) {
		ctx.putProperty(flag, true);
	}

	/**
	 * Sets the given value of a property on the ctx.
	 * 
	 * @param ctx
	 * @param property
	 * @param value
	 */
	public static void set(IContext ctx, Object property, Object value) {
		ctx.putProperty(property, value);
	}
	
	/**
	 * Returns the value of the given property on the 
	 * @param ctx
	 * @param property
	 * @return
	 */
	public static Object get(IContext ctx, Object property) {
		return ctx.getProperty(property);
	}
	
	/**
	 * Returns the value of a given property in the ctx.
	 * 
	 * @param ctx
	 * @param flag
	 * @param cls
	 * 
	 * @return the value or null if no value was set
	 * 
	 * @throws IllegalArgumentException if the value has the wrong type
	 */
	public static <T> T get(IContext ctx, Object property, Class<T> cls) {
		
		Object o = get(ctx, property);
		if (o != null) {
			if (cls.isInstance(o)) {
				return cls.cast(o);
			} else {
				throw new IllegalArgumentException(
						String.format("Required value of property <%s> to be an instance of <%s> but was <%s>", property, cls.getSimpleName(), o));
			}
		}
		
		return null;
	}

	/**
	 * Copy properties from one context to the other
	 * 
	 * @param source
	 * @param target
	 * 
	 * @param propertyNames
	 */
	public static void copyProperties(IContext source, IContext target, String ... propertyNames) {
		for (String s: propertyNames) {
			target.putProperty(s, source.getProperty(s));
		}
	}
}
