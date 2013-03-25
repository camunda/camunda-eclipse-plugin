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
package org.camunda.bpm.modeler.ui.features.activity.subprocess;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.ui.features.activity.ResizeActivityFeature;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.ResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

public class ResizeExpandableActivityFeature extends ResizeActivityFeature {
	
	public final static int MARGIN = 20;
	
	public ResizeExpandableActivityFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		ContainerShape containerShape = (ContainerShape) context.getShape();
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(containerShape, BPMNShape.class);
		
		// can only resize non-expanded containers
		if (!bpmnShape.isIsExpanded()) {
			return false;
		}
	
		boolean contained = true;
		
		// see if we are before the actual resize operation
		int resizeDirection = context.getDirection();
		if (resizeDirection != IResizeShapeContext.DIRECTION_UNSPECIFIED) {
			
			IRectangle preResizeBounds = LayoutUtil.getRelativeBounds(containerShape);
	
			// check if children still fit into element
			IRectangle childrenBBox = LayoutUtil.getChildrenBBox(containerShape, null, 0, 0);
			if (childrenBBox != null) {
				
				IRectangle postResizeBounds = rectangle(
					context.getX() - preResizeBounds.getX(), 
					context.getY() - preResizeBounds.getY(), 
					context.getWidth(), 
					context.getHeight());
				
				Sector direction = Sector.fromResizeDirection(resizeDirection);
				contained = LayoutUtil.isContained(childrenBBox, postResizeBounds, direction);
			}
		}
		
		return contained && super.canResizeShape(context);
	}
	
	@Override
	protected boolean isCompensateMovementOnChildren() {
		return true;
	}
	
	@Override
	public void resizeShape(IResizeShapeContext context) {
		ResizeShapeContext resizeShapeContext = (ResizeShapeContext) context;

		ContainerShape containerShape = (ContainerShape) context.getShape();
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(containerShape, BPMNShape.class);
		
		if (bpmnShape.isIsExpanded()) {

		} else {
			
			// SubProcess is collapsed
			
			for (PictogramElement pe : FeatureSupport.getContainerDecorators(containerShape)) {
				GraphicsAlgorithm childGa = pe.getGraphicsAlgorithm();
				if (childGa!=null) {
					childGa.setWidth(GraphicsUtil.getActivitySize(getDiagram()).getWidth());
					childGa.setHeight(GraphicsUtil.getActivitySize(getDiagram()).getHeight());
				}
			}
			
			resizeShapeContext.setWidth(GraphicsUtil.getActivitySize(getDiagram()).getWidth());
			resizeShapeContext.setHeight(GraphicsUtil.getActivitySize(getDiagram()).getHeight());
		}
		
		Graphiti.getPeService().sendToBack(containerShape);
		
		super.resizeShape(context);
	}
}
