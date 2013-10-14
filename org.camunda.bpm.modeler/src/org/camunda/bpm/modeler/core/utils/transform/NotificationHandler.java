package org.camunda.bpm.modeler.core.utils.transform;

public interface NotificationHandler<T> {
	public void handle(T error);
}