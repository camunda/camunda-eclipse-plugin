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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Choreography;
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.Style;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.internal.util.ui.PopupMenu;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

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
				return DIUtils.findBPMNDiagram(getDiagramEditor(), (BaseElement)bo) != null;
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
		
		// find out which BPMNPlane this sub process lives in - this will be the new home
		// for the DI elements in the existing BPMNDiagram.
		BPMNDiagram newBpmnDiagram = (BPMNDiagram)ModelHandler.findDIElement(container).eContainer().eContainer();
		BPMNPlane newPlane = newBpmnDiagram.getPlane();
		Diagram newDiagram = DIUtils.findDiagram(getDiagramEditor(), newBpmnDiagram);
		
		BPMNDiagram oldBpmnDiagram = DIUtils.findBPMNDiagram(getDiagramEditor(), container);
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
					List <EObject> moved = new ArrayList<EObject>();
					for (Shape shape : oldDiagram.getChildren()) {
						TreeIterator<EObject> iter = shape.eAllContents();
						while (iter.hasNext()) {
							EObject o = iter.next();
							if (o instanceof PictogramLink || o instanceof Color || o instanceof Font || o instanceof Style)
								moved.add(o);
						}
					}
					for (Connection connection : oldDiagram.getConnections()) {
						TreeIterator<EObject> iter = connection.eAllContents();
						while (iter.hasNext()) {
							EObject o = iter.next();
							if (o instanceof PictogramLink || o instanceof Color || o instanceof Font || o instanceof Style)
								moved.add(o);
						}
					}
					((ContainerShape)p).getChildren().addAll( oldDiagram.getChildren() );
					newDiagram.getConnections().addAll( oldDiagram.getConnections() );
					
					oldDiagram.getPictogramLinks().removeAll(moved);
					oldDiagram.getColors().removeAll(moved);
					oldDiagram.getFonts().removeAll(moved);
					oldDiagram.getStyles().removeAll(moved);
					
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
