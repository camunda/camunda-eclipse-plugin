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

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.Style;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * @author Bob Brodt
 *
 */
public class PushdownFeature extends AbstractCustomFeature {

	/**
	 * @param fp
	 */
	public PushdownFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public String getName() {
	    return "Push down";
	}
	
	@Override
	public String getDescription() {
	    return "Push the contents of this Activity Container into a new Diagram";
	}

	@Override
	public String getImageId() {
		return Images.IMG_16_PUSHDOWN;
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
				return DIUtils.findBPMNDiagram(getDiagramEditor(), (BaseElement)bo, false) == null;
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
		Definitions definitions = ModelUtil.getDefinitions(container);

		BPMNDiagram oldBpmnDiagram = (BPMNDiagram)ModelHandler.findDIElement(getDiagram(), container).eContainer().eContainer();
		Diagram oldDiagram = DIUtils.findDiagram(getDiagramEditor(), oldBpmnDiagram);
		
		// the contents of this expandable element is in the flowElements list 
        BPMNDiagram newBpmnDiagram = DIUtils.createBPMNDiagram(definitions, container);
		BPMNPlane newPlane = newBpmnDiagram.getPlane();

		Diagram newDiagram = DIUtils.getOrCreateDiagram(getDiagramEditor(), newBpmnDiagram);
		
		for (FlowElement fe : container.getFlowElements()) {
			DiagramElement de = ModelHandler.findDIElement(getDiagram(), fe);
			newPlane.getPlaneElement().add(de);
			
			List <PictogramElement> pes = Graphiti.getLinkService().getPictogramElements(oldDiagram, fe);
			List <EObject> moved = new ArrayList<EObject>();
			for (PictogramElement p : pes) {
				PictogramElement pictogramElement = null;
				if (p instanceof Shape) {
					if (BusinessObjectUtil.getFirstElementOfType(p, BPMNShape.class)!=null) {
						newDiagram.getChildren().add((Shape)p);
						pictogramElement = p;
					}
					else if (LabelUtil.isLabel(p)) {
						newDiagram.getChildren().add((Shape)p);
						pictogramElement = p;
					}
				}
				else if (p instanceof Connection) {
					if (BusinessObjectUtil.getFirstElementOfType(p, BPMNEdge.class)!=null) {
						newDiagram.getConnections().add((Connection)p);
						pictogramElement = p;
					}
				}
				if (pictogramElement!=null) {
					TreeIterator<EObject> iter = pictogramElement.eAllContents();
					while (iter.hasNext()) {
						EObject o = iter.next();
						if (o instanceof PictogramLink) {
							newDiagram.getPictogramLinks().add((PictogramLink)o);
							moved.add(o);
						}
						else if (o instanceof Color) {
							newDiagram.getColors().add((Color)o);
							moved.add(o);
						}
						else if (o instanceof Font) {
							newDiagram.getFonts().add((Font)o);
							moved.add(o);
						}
						else if (o instanceof Style) {
							newDiagram.getStyles().add((Style)o);
							moved.add(o);
						}
					}
				}
			}
			oldDiagram.getPictogramLinks().removeAll(moved);
			oldDiagram.getColors().removeAll(moved);
			oldDiagram.getFonts().removeAll(moved);
			oldDiagram.getStyles().removeAll(moved);
		}

		// now collapse the sub process
		CollapseFlowNodeFeature collapseFeature = new CollapseFlowNodeFeature(getFeatureProvider());
		collapseFeature.execute(context);
	}
}
