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

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.ResizeShapeContext;

/**
 * Resize lane feature
 *  
 * @author nico.rehwaldt
 */
public class ResizeLaneFeature extends ResizeLaneSetFeature {

	public ResizeLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected IRectangle getPostResizeBounds(IResizeShapeContext context) {

		Sector direction = Sector.fromResizeDirection(context.getDirection());
		
		IRectangle preResizeBounds = LayoutUtil.getRelativeBounds(context.getShape());
		IRectangle postResizeBounds = super.getPostResizeBounds(context);
		
		IRectangle fixedBounds = resetHorizontalResize(direction, preResizeBounds, postResizeBounds);
		
		// update resize context if possible
		if (context instanceof ResizeShapeContext) {
			ResizeShapeContext resizeContext = (ResizeShapeContext) context;
			resizeContext.setWidth(fixedBounds.getWidth());
			resizeContext.setX(fixedBounds.getX());
		}
		
		return fixedBounds;
	}
	
	/**
	 * 
	 * @param direction
	 * @param preResizeBounds
	 * @param postResizeBounds
	 * @return
	 */
	private IRectangle resetHorizontalResize(Sector direction, IRectangle preResizeBounds, IRectangle postResizeBounds) {

		// reset x / width resize properties to their original
		
		int width = postResizeBounds.getWidth();
		int height = postResizeBounds.getHeight();
		
		int x = postResizeBounds.getX();
		int y = postResizeBounds.getY();
		
		switch (direction) {
		case BOTTOM_LEFT:
		case TOP_LEFT:
			x = preResizeBounds.getX();
			width = preResizeBounds.getWidth();
			break;
		
		case TOP_RIGHT:
		case BOTTOM_RIGHT:
			width = preResizeBounds.getWidth();
			break;
		}

		return rectangle(x, y, width, height);
	}

	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		
		Sector sector = Sector.fromResizeDirection(context.getDirection());
		
		switch (sector) {
		case LEFT:
		case RIGHT:
			return false;
		}
	
		return super.canResizeShape(context);
	}
}
