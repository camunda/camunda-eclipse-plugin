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
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.ui.features.event;

import org.camunda.bpm.modeler.core.features.DefaultBpmn2LayoutShapeFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class LayoutBoundaryEventFeature extends DefaultBpmn2LayoutShapeFeature {

	public LayoutBoundaryEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		return true;
	}
	
	protected void layoutShape(ContainerShape shape) {
		super.layoutShape(shape);
		
		GraphicsUtil.sendToFront(shape);
	}
}