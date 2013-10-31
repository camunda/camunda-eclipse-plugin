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
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core.features;


import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.features.rules.ModelOperations;
import org.camunda.bpm.modeler.core.features.rules.ModelOperations.ModelCreateOperation;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public abstract class AbstractCreateFlowElementFeature<T extends FlowElement> extends AbstractBpmn2CreateFeature<T> {

	public AbstractCreateFlowElementFeature(IFeatureProvider fp, String name, String description) {
		super(fp, name, description);
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		ModelCreateOperation operation = ModelOperations.getFlowNodeCreateOperation(context);
		return operation.canExecute(context);
	}

	@Override
	public Object[] create(ICreateContext context) {
		ModelHandler handler = ModelHandler.getInstance(getDiagram());
		T element = createBusinessObject(context);
		if (FeatureSupport.isTargetLane(context) && element instanceof FlowNode) {
			((FlowNode) element).getLanes().add(
					(Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
		}
		
		handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()), element);
		
		if (ContextUtil.isNot(context, SKIP_ADD_GRAPHICS)) {
			addGraphicalRepresentation(context, element);
		}
		
		return new Object[] { element };
	}
	
	protected abstract String getStencilImageId();
	
	@Override
	public String getCreateImageId() {
	    return getStencilImageId();
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}
}