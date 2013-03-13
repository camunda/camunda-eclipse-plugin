package org.camunda.bpm.modeler.core.features;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.features.context.IAddContext;

public interface IBpmn2AddFeature<T extends EObject> {

	public T getBusinessObject(IAddContext context);
	public void putBusinessObject(IAddContext context, T businessObject);
	public void postExecute(IExecutionInfo executionInfo);
}
