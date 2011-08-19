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
package org.eclipse.bpmn2.modeler.ui.features.activity.subprocess;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.modeler.core.features.activity.ActivityLayoutFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class SubProcessLayoutFeature extends ActivityLayoutFeature {

	private final static int MARGIN = 2;
	private final static int NUMBER_OF_PARENT_ELEMENTS = 3;
	
	public SubProcessLayoutFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected boolean layoutHook(Shape shape, GraphicsAlgorithm ga, Object bo, int newWidth, int newHeight) {
		if (bo != null && bo instanceof Activity && ga instanceof Text) {
			Graphiti.getGaService().setLocationAndSize(ga, 5, 5, newWidth - 10, 15);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm parentGa = containerShape.getGraphicsAlgorithm();
		int newWidth = parentGa.getWidth();
		int newHeight = parentGa.getHeight();

		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minWidth = 0;
		int minHeight = 0;
		int size = containerShape.getChildren().size();
		for (int i=NUMBER_OF_PARENT_ELEMENTS; i<size; ++i) {
			PictogramElement pe = containerShape.getChildren().get(i);
			GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();
			if (ga!=null) {
				int x = ga.getX();
				int y = ga.getY();
				if (x < minX)
					minX = x;
				if (y < minY)
					minY = y;
			}
		}
		for (int i=NUMBER_OF_PARENT_ELEMENTS; i<size; ++i) {
			PictogramElement pe = containerShape.getChildren().get(i);
			GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();
			if (ga!=null) {
				int w = ga.getX() - minX + ga.getWidth();
				int h = ga.getY() - minY + ga.getHeight();
				if (w > minWidth)
					minWidth = w;
				if (h > minHeight)
					minHeight = h;
			}
		}
		
		if (minX < MARGIN)
			minX = MARGIN;
		if (minY < MARGIN)
			minY = MARGIN;
		minWidth += 2 * MARGIN;
		minHeight += 2 * MARGIN;
		
		if (newWidth < minWidth) {
			parentGa.setWidth(minWidth);
		}
		if (newWidth < minX + minWidth) {
			int shift = minX + minWidth - newWidth;
			if (shift>minX-MARGIN) {
				shift = minX-MARGIN;
			}
			if (shift>0) {
				for (int i=NUMBER_OF_PARENT_ELEMENTS; i<size; ++i) {
					PictogramElement pe = containerShape.getChildren().get(i);
					GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();
					if (ga!=null) {
						int x = ga.getX() - shift;
						ga.setX(x);
					}
				}
			}
		}
		if (newHeight < minHeight) {
			parentGa.setHeight(minHeight);
		}
		if (newHeight < minY + minHeight) {
			int shift = minY + minHeight - newHeight;
			if (shift>minY-MARGIN) {
				shift = minY-MARGIN;
			}
			if (shift>0) {
				for (int i=NUMBER_OF_PARENT_ELEMENTS; i<size; ++i) {
					PictogramElement pe = containerShape.getChildren().get(i);
					GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();
					if (ga!=null) {
						int y = ga.getY() - shift;
						ga.setY(y);
					}
				}
			}
		}

		return super.layout(context);
	}
}
