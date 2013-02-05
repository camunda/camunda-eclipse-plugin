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

import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.layout.ConnectionService;
import org.eclipse.bpmn2.modeler.core.layout.util.Layouter;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DefaultResizeBPMNShapeFeature extends DefaultResizeShapeFeature {

	public DefaultResizeBPMNShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void resizeShape(IResizeShapeContext context) {
		super.resizeShape(context);
		
		Shape shape = context.getShape();
		
		relocateAnchors(shape, context);
		
		updateDi(shape);
		
		layout(shape, context);
	}

	private void layout(Shape shape, IResizeShapeContext context) {
		DiagramElement diagramElement = BusinessObjectUtil.getFirstElementOfType(shape, DiagramElement.class);

		if (diagramElement instanceof BPMNShape || diagramElement instanceof BPMNEdge) {
			Layouter.layoutShapeAfterResize(shape, getFeatureProvider());
		}
	}

	/**
	 * Relocate anchors after the shape has been resized
	 * 
	 * @param shape
	 * @param context
	 */
	protected void relocateAnchors(Shape shape, IResizeShapeContext context) {
		AnchorUtil.relocateFixPointAnchors(shape, context.getWidth(), context.getHeight());
	}

	/**
	 * Update di after feature has been executed
	 * 
	 * @param shape
	 */
	protected void updateDi(Shape shape) {
		DIUtils.updateDIShape(shape);
	}
}