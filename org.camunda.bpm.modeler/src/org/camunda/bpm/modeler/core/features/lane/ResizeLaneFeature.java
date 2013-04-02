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
package org.camunda.bpm.modeler.core.features.lane;


import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;

/**
 * Resize lane feature
 *  
 * @author nico.rehwaldt
 */
public class ResizeLaneFeature extends ResizeLaneSetFeature {

	public static final String LANE_RESIZE_PROPERTY = "lane.resize";

	public ResizeLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		
		Sector sector = Sector.fromResizeDirection(context.getDirection());
		if (sector.isLeft() || sector.isRight()) {
			return false;
		}
	
		return super.canResizeShape(context);
	}
}
