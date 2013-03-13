package org.camunda.bpm.modeler.core.layout;

/**
 * Abstract strategy class
 * 
 * @author nico.rehwaldt
 *
 * @param <T> return type of the strategy
 */
public abstract class Strategy<T> {

	public abstract T execute();
}
