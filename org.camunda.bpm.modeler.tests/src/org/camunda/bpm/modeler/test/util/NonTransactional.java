package org.camunda.bpm.modeler.test.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes that a test should run non-transactional
 * 
 * @author nico.rehwaldt
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface NonTransactional {
	
}
