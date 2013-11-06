/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.camunda.bpm.modeler.core.features;

import java.util.List;

import org.camunda.bpm.modeler.core.features.api.IBpmn2CreateFeature;
import org.camunda.bpm.modeler.core.runtime.ModelEnablementDescriptor;
import org.camunda.bpm.modeler.core.runtime.TargetRuntime;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * @author Bob Brodt
 *
 */
public abstract class AbstractBpmn2CreateFeature<T extends BaseElement>
		extends AbstractCreateFeature
		implements IBpmn2CreateFeature<T, ICreateContext> {
	
	public static final String SKIP_ADD_GRAPHICS = "DONT_ADD";
	
	/**
	 * @param fp
	 * @param name
	 * @param description
	 */
	public AbstractBpmn2CreateFeature(IFeatureProvider fp, String name, String description) {
		super(fp, name, description);
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return false;
	}

	@Override
	public Object[] create(ICreateContext context) {
		return null;
	}

	@Override
	public boolean isAvailable(IContext context) {
		List<ModelEnablementDescriptor> enablements = TargetRuntime.getCurrentRuntime().getModelEnablements();
		for (ModelEnablementDescriptor e : enablements) {
			if (e.isEnabled(getBusinessObjectClass()))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.features.impl.AbstractCreateFeature#getCreateDescription()
	 */
	@Override
	public String getCreateDescription() {
		return "Create " + ModelUtil.toDisplayName(getBusinessObjectClass().getName());
	}
	
	@SuppressWarnings({ "unchecked" })
	public T createBusinessObject(ICreateContext context) {
		Shape shape = context.getTargetContainer();
		
		EObject container = BusinessObjectUtil.getBusinessObjectForPictogramElement(shape);
		Resource resource = container.eResource();
		
		EClass eCls = getBusinessObjectClass();
		
		EClass actualECls = ((Bpmn2FeatureProvider) getFeatureProvider()).getActualEClass(eCls);
		
		T newObject = (T) actualECls.getEPackage().getEFactoryInstance().create(actualECls);
		
		// assign id to new object
		ModelUtil.setID(newObject, resource);
		
		putBusinessObject(context, newObject);
		
		return newObject;
	}
	
	@SuppressWarnings("unchecked")
	public T getBusinessObject(ICreateContext context) {
		return (T) context.getProperty(PropertyNames.BUSINESS_OBJECT);
	}
	
	public void putBusinessObject(ICreateContext context, T businessObject) {
		context.putProperty(PropertyNames.BUSINESS_OBJECT, businessObject);
	}
	
	@Override
	public void postExecute(IExecutionInfo executionInfo) {
		
	}
}
