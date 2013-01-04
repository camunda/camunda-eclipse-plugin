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

import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

/**
 * @author Bob Brodt
 *
 */
public class PullupFeature extends AbstractCustomFeature {

	/**
	 * @param fp
	 */
	public PullupFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public String getName() {
	    return "Pull up";
	}
	
	@Override
	public String getDescription() {
	    return "Pull the contents of the Diagram for this Activity Container back into the container";
	}

	@Override
	public String getImageId() {
		return ImageProvider.IMG_16_PULLUP;
	}

	@Override
	public boolean isAvailable(IContext context) {
		return true;
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe = pes[0];
			Object bo = getBusinessObjectForPictogramElement(pe);
			if (bo instanceof FlowElementsContainer) {
				return DIUtils.findBPMNDiagram(getDiagramEditor(), (BaseElement)bo, false) != null;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.features.custom.ICustomFeature#execute(org.eclipse.graphiti.features.context.ICustomContext)
	 */
	@Override
	public void execute(ICustomContext context) {
		// we already know there's one and only one PE element in canExecute() and that it's
		// a ContainerShape for an expandable activity
		PictogramElement pe = context.getPictogramElements()[0];
		FlowElementsContainer container = (FlowElementsContainer)getBusinessObjectForPictogramElement(pe);
		
		// find out which BPMNPlane this sub process lives in - this will be the new home
		// for the DI elements in the existing BPMNDiagram.
		BPMNDiagram newBpmnDiagram = (BPMNDiagram)ModelHandler.findDIElement(container).eContainer().eContainer();
		BPMNPlane newPlane = newBpmnDiagram.getPlane();
		Diagram newDiagram = DIUtils.findDiagram(getDiagramEditor(), newBpmnDiagram);
		
		BPMNDiagram oldBpmnDiagram = DIUtils.findBPMNDiagram(getDiagramEditor(), container, false);
		BPMNPlane oldPlane = oldBpmnDiagram.getPlane();
		Diagram oldDiagram = DIUtils.findDiagram(getDiagramEditor(), oldBpmnDiagram);
		
		// copy the elements into the same plane as the sub process
		while (oldPlane.getPlaneElement().size()>0) {
			DiagramElement de = oldPlane.getPlaneElement().get(0);
			newPlane.getPlaneElement().add(de);
		}
		
		// copy the Graphiti diagram elements: first find the ContainerShape for the sub process
		List <PictogramElement> pes = Graphiti.getLinkService().getPictogramElements(newDiagram, container);
		for (PictogramElement p : pes) {
			if (p instanceof ContainerShape) {
				if (BusinessObjectUtil.getFirstElementOfType(p, BPMNShape.class)!=null) {
					// this is it!
					((ContainerShape)p).getChildren().addAll( oldDiagram.getChildren() );
					newDiagram.getConnections().addAll( oldDiagram.getConnections() );
					
					newDiagram.getPictogramLinks().addAll(oldDiagram.getPictogramLinks());
					newDiagram.getColors().addAll(oldDiagram.getColors());
					newDiagram.getFonts().addAll(oldDiagram.getFonts());
					newDiagram.getStyles().addAll(oldDiagram.getStyles());
					
					break;
				}
			}
		}
		
		// get rid of the old BPMNDiagram
		DIUtils.deleteDiagram(getDiagramEditor(), oldBpmnDiagram);
		
		// now expand the sub process
		ExpandFlowNodeFeature expandFeature = new ExpandFlowNodeFeature(getFeatureProvider());
		expandFeature.execute(context);
	}
}
