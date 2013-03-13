package org.camunda.bpm.modeler.core.utils;

import org.eclipse.graphiti.PropertyBag;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

/**
 * Utility to query a {@link PropertyBag} for flags an properties. 
 * 
 * @author nico.rehwaldt
 */
public class PictogramElementPropertyUtil {

	/**
	 * Queries for the existence of the given flag in the pictogram element.
	 * 
	 * @param pictogramElement
	 * @param flag
	 * 
	 * @return true if the flag is set
	 */
	public static boolean is(PictogramElement pictogramElement, Object flag) {
		return get(pictogramElement, flag) != null;
	}
	
	/**
	 * Queries for the absence of the given flag in the bag.
	 * 
	 * @param ctx
	 * @param flag
	 * 
	 * @return true if the flag is set
	 */
	public static boolean isNot(PictogramElement pictogramElement, Object flag) {
		return !is(pictogramElement, flag);
	}
	
	/**
	 * Activates the flag on the ctx.
	 * 
	 * @param pictogramElement
	 * @param flag
	 */
	public static void set(PictogramElement pictogramElement, Object flag) {
		set(pictogramElement, flag, "true");
	}

	/**
	 * Sets the given value of a property on the ctx.
	 * 
	 * @param pictogramElement
	 * @param property
	 * @param value
	 */
	public static void set(PictogramElement pictogramElement, Object property, Object value) {
		Graphiti.getPeService().setPropertyValue(pictogramElement, property.toString(), value.toString());
	}
	
	/**
	 * Returns the value of the given property on the 
	 * @param pictogramElement
	 * @param property
	 * @return
	 */
	public static String get(PictogramElement pictogramElement, Object property) {
		return Graphiti.getPeService().getPropertyValue(pictogramElement, property.toString());
	}
	
	/**
	 * Returns the value of a given property in the ctx.
	 * 
	 * @param pictogramElement
	 * @param flag
	 * @param cls
	 * 
	 * @return the value or null if no value was set
	 * 
	 * @throws IllegalArgumentException if the value has the wrong type
	 */
	public static <T> T get(PictogramElement pictogramElement, Object property, Class<T> cls) {
		
		String o = get(pictogramElement, property);
		if (o != null) {
			if (cls.isAssignableFrom(Integer.class)) {
				return cls.cast(new Integer(o));
			} else
			if (cls.isAssignableFrom(Double.class) || cls.isAssignableFrom(Float.class)) {
				return cls.cast(new Double(o));
			} else
			if (cls.isAssignableFrom(Boolean.class)) {
				return cls.cast(new Boolean(o));
			} else
			if (cls.isInstance(o)) {
				return cls.cast(o);
			} else {
				throw new IllegalArgumentException(
						String.format("Required value of property <%s> to be an instance of <%s> but was <%s>", property, cls.getSimpleName(), o));
			}
		}
		
		return null;
	}
}
