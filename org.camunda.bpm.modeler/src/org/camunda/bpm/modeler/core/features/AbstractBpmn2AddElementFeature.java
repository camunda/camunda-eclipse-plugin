package org.camunda.bpm.modeler.core.features;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.features.api.IBpmn2AddFeature;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public abstract class AbstractBpmn2AddElementFeature<T extends BaseElement, V extends PictogramElement> extends AbstractAddShapeFeature implements IBpmn2AddFeature<T> {

	public AbstractBpmn2AddElementFeature(IFeatureProvider fp) {
		super(fp);
	}


	/**
	 * Sets properties on the newly created pictogram element. 
	 * 
	 * May be overridden in subclasses to perform actual behavior.
	 * 
	 * @param context
	 * @param newPictogramElement
	 */
	protected void setProperties(IAddContext context, V newPictogramElement) {
		
	}

	/**
	 * Perform a post create operation on the given pictogram element.
	 * 
	 * May be overridden in subclasses to perform actual behavior.
	 * 
	 * @param context
	 * @param newPictogramElement
	 */
	protected void postCreateHook(IAddContext context, IRectangle bounds, V newPictogramElement) {
		
	}
	
	/**
	 * Perform a post add operation on the given pictogram element.
	 * Both, DI data creation and linking are already done once this method is called.
	 * 
	 * May be overridden in subclasses to perform actual behavior.
	 * 
	 * @param context
	 * @param newPictogramElement
	 */
	protected void postAddHook(IAddContext context, V newPictogramElement) {
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T getBusinessObject(IAddContext context) {
		Object businessObject = context.getProperty(PropertyNames.BUSINESS_OBJECT);
		if (businessObject instanceof BaseElement)
			return (T)businessObject;
		return (T)context.getNewObject();
	}

	@Override
	public void putBusinessObject(IAddContext context, T businessObject) {
		context.putProperty(PropertyNames.BUSINESS_OBJECT, businessObject);
	}

	@Override
	public void postExecute(IExecutionInfo executionInfo) {
		
	}

	/**
	 * Returns true if the context represents a import operation
	 * 
	 * @param context
	 * @return
	 */
	protected boolean isImport(IContext context) {
		return ContextUtil.is(context, DIUtils.IMPORT);
	}
}
