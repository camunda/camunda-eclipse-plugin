package org.eclipse.bpmn2.modeler.core.features;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.features.context.IContext;

public interface IBpmn2AddFeature<T extends EObject, C extends IContext> {

	public T getBusinessObject(C context);
	public void postExecute(IExecutionInfo executionInfo);
}
