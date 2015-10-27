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
package org.camunda.bpm.modeler.ui.features.activity.subprocess;

import org.camunda.bpm.modeler.core.features.activity.LayoutActivityFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class LayoutExpandableActivityFeature extends LayoutActivityFeature {

	public LayoutExpandableActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void layoutLabel(ContainerShape container, Shape labelShape, IRectangle bounds) {

		AbstractText text = (AbstractText) labelShape.getGraphicsAlgorithm();

		int padding = 5;

		int width = bounds.getWidth() - padding;
		int height = Math.min(GraphicsUtil.getLabelHeight(text), bounds.getHeight() - padding);

		Graphiti.getGaService().setLocationAndSize(text, padding, padding, width, height);
	}
}
