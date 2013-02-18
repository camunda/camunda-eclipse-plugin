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
package org.eclipse.bpmn2.modeler.core.features;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DirectEditNamedElementFeature extends AbstractDirectEditingFeature {

	public DirectEditNamedElementFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_MULTILINETEXT;
	}
	
	protected String getNameValue(Object businessObject) {
		return ((FlowElement) businessObject).getName();
	}
	
	protected void setNameValue(Object businessObject, String value) {
		((FlowElement) businessObject).setName(value);
	}
	
	@Override
	public String getInitialValue(IDirectEditingContext context) {
		return getNameValue(getBusinessObject(context));
	}

	@Override
	public void setValue(String value, IDirectEditingContext context) {
		setNameValue(getBusinessObject(context), value);
		PictogramElement e = context.getPictogramElement();
		updatePictogramElement(((Shape) e).getContainer());
	}
	
	protected Object getBusinessObject(IDirectEditingContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement());
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		boolean val = bo != null && isNamedBusinessObject(bo) && (LabelUtil.getLabelShape(pe, getDiagram()) != null);
		return val;
	}
	
	protected boolean isNamedBusinessObject(Object bo) {
		 return (bo instanceof FlowElement);
	}

	@Override
	public boolean stretchFieldToFitText() {
		return true;
	}
}
