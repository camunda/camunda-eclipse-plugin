package org.camunda.bpm.modeler.core.features;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.features.context.IContext;

public interface IBpmn2CreateFeature<T extends EObject, C extends IContext> {

	public T createBusinessObject(C context);
	public T getBusinessObject(C context);
	public void putBusinessObject(C context, T businessObject);
	public EClass getBusinessObjectClass();
	public void postExecute(IExecutionInfo executionInfo);
}
