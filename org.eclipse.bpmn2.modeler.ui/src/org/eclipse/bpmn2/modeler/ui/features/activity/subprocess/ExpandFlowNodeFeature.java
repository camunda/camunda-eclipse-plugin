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
package org.eclipse.bpmn2.modeler.ui.features.activity.subprocess;

import static org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.IS_EXPANDED;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.features.choreography.ShowDiagramPageFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.ResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

// NOT USED YET
public class ExpandFlowNodeFeature extends ShowDiagramPageFeature {

	private final static String NAME = "Expand";
	private final static String DESCRIPTION = "Expand the Activity and show contents";
	
	private String name = NAME;
	private String description = DESCRIPTION;
	
	public ExpandFlowNodeFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public String getName() {
	    return name;
	}
	
	@Override
	public String getDescription() {
	    return description;
	}

	@Override
	public String getImageId() {
		return ImageProvider.IMG_16_EXPAND;
	}

	@Override
	public boolean isAvailable(IContext context) {
		return true;
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		if (super.canExecute(context)) {
			name = super.getName();
			description = super.getDescription();
			return true;
		}
		else {
			name = NAME;
			description = DESCRIPTION;
		}
		
		boolean ret = false;
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			Object bo = getBusinessObjectForPictogramElement(pes[0]);
			if (AbstractExpandableActivityFeatureContainer.isExpandableElement(bo)) {
				try {
					BPMNShape bpmnShape = (BPMNShape) ModelHandlerLocator.getModelHandler(getDiagram().eResource()).findDIElement((FlowNode)bo);
					if (!bpmnShape.isIsExpanded())
						ret = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	@Override
	public void execute(ICustomContext context) {
		if (super.canExecute(context)) {
			super.execute(context);
			return;
		}
		
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe0 = pes[0];
			Object bo = getBusinessObjectForPictogramElement(pe0);
			if (pe0 instanceof ContainerShape && bo instanceof FlowNode) {
				ContainerShape containerShape = (ContainerShape)pe0;
				FlowNode flowNode = (FlowNode)bo;
				try {
					BPMNShape bpmnShape = (BPMNShape) ModelHandlerLocator.getModelHandler(getDiagram().eResource()).findDIElement(flowNode);
					if (!bpmnShape.isIsExpanded()) {
						
						// SubProcess is collapsed - resize to minimum size such that all children are visible
						// NOTE: children tasks will be set visible in LayoutExpandableActivityFeature

						bpmnShape.setIsExpanded(true);

						GraphicsAlgorithm ga = containerShape.getGraphicsAlgorithm();
						ResizeShapeContext resizeContext = new ResizeShapeContext(containerShape);
						IResizeShapeFeature resizeFeature = getFeatureProvider().getResizeShapeFeature(resizeContext);
						int oldWidth = ga.getWidth();
						int oldHeight = ga.getHeight();
						ResizeExpandableActivityFeature.SizeCalculator newSize = new ResizeExpandableActivityFeature.SizeCalculator(containerShape);
						int newWidth = newSize.getWidth();
						int newHeight = newSize.getHeight();
						resizeContext.setX(ga.getX() + oldWidth/2 - newWidth/2);
						resizeContext.setY(ga.getY() + oldHeight/2 - newHeight/2);
						resizeContext.setWidth(newWidth);
						resizeContext.setHeight(newHeight);
						resizeFeature.resizeShape(resizeContext);
						
						UpdateContext updateContext = new UpdateContext(containerShape);
						IUpdateFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
						if (updateFeature.updateNeeded(updateContext).toBoolean())
							updateFeature.update(updateContext);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}