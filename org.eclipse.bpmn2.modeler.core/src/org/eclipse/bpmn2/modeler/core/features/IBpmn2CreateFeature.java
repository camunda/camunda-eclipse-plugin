package org.eclipse.bpmn2.modeler.core.features;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.features.context.IContext;

public interface IBpmn2CreateFeature<T extends EObject, C extends IContext> {

	public static String BUSINESS_OBJECT_KEY = "business.object";
	
	public T createBusinessObject(C context);
	public T getBusinessObject(C context);
	public void putBusinessObject(C context, T businessObject);
	public EClass getBusinessObjectClass();
	public void postExecute(IExecutionInfo executionInfo);
}
