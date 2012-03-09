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

package org.eclipse.bpmn2.modeler.ui.features.participant;

import java.io.IOException;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

/**
 * @author Bob Brodt
 *
 */
public class RotatePoolFeature extends AbstractCustomFeature {

	/**
	 * @param fp
	 */
	public RotatePoolFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public String getName() {
	    return "Change Pool Orientation";
	}
	
	@Override
	public String getDescription() {
	    return "Switch the orientation of this Pool between horizontal and vertical";
	}

	@Override
	public String getImageId() {
		return ImageProvider.IMG_16_ROTATE;
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
			if (pe instanceof ContainerShape && bo instanceof Participant) {
				Object parent = getBusinessObjectForPictogramElement(((ContainerShape)pe).getContainer());
				if (parent instanceof BPMNDiagram) {
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.features.custom.ICustomFeature#execute(org.eclipse.graphiti.features.context.ICustomContext)
	 */
	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1 && pes[0] instanceof ContainerShape) {
			IGaService gaService = Graphiti.getGaService();
			ContainerShape container = (ContainerShape)pes[0];
			boolean horz = FeatureSupport.isHorizontal(container);
			changeOrientation(container, !horz);
			FeatureSupport.redraw(container);
		}
	}

	private void changeOrientation(ContainerShape container, boolean horz) {
		IGaService gaService = Graphiti.getGaService();
			GraphicsAlgorithm ga = container.getGraphicsAlgorithm();
			int x = ga.getX();
			int y = ga.getY();
			int width = ga.getWidth();
			int height = ga.getHeight();
			gaService.setLocationAndSize(ga, x, y, height, width);
			
		FeatureSupport.setHorizontal(container, horz);
			DIUtils.updateDIShape(container);

		for (PictogramElement pe : container.getChildren()) {
			if (pe instanceof ContainerShape) {
				if (BusinessObjectUtil.getFirstElementOfType(pe, Lane.class) != null) {
					changeOrientation((ContainerShape)pe,horz);
				}
		}
	}
	}

}
