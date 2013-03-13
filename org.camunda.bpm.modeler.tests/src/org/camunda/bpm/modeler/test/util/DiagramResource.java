package org.camunda.bpm.modeler.test.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes a diagram resource to be loaded in tests.
 * 
 * @author nico.rehwaldt
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DiagramResource {

	public String value() default "";
}
