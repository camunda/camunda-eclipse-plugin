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
package org.eclipse.bpmn2.modeler.ui.features.artifact;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Group;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBpmnShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.DefaultMoveBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.DefaultResizeBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.artifact.AbstractCreateArtifactFeature;
import org.eclipse.bpmn2.modeler.core.features.container.BaseElementFeatureContainer;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.features.AbstractDefaultDeleteFeature;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class GroupFeatureContainer extends BaseElementFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof Group;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateGroupFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddGroupFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new DefaultMoveBPMNShapeFeature(fp);
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new DefaultResizeBPMNShapeFeature(fp);
	}

	private final class AddGroupFeature extends AbstractAddBpmnShapeFeature<Group> {
		private AddGroupFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canAdd(IAddContext context) {
			return true;
		}

		@Override
		protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {
			IGaService gaService = Graphiti.getGaService();
			IPeService peService = Graphiti.getPeService();
			
			int width = bounds.getWidth();
			int height = bounds.getHeight();
			int x = bounds.getX();
			int y = bounds.getY();
			
			ContainerShape newShape = peService.createContainerShape(context.getTargetContainer(), true);
			RoundedRectangle rect = gaService.createRoundedRectangle(newShape, 5, 5);
			rect.setFilled(false);
			rect.setLineWidth(2);
			rect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			rect.setLineStyle(LineStyle.DASHDOT);
			gaService.setLocationAndSize(rect, x, y, width, height);
			
			return newShape;
		}

		@Override
		public int getDefaultHeight() {
			return 400;
		}

		@Override
		public int getDefaultWidth() {
			return 400;
		}

		@Override
		protected boolean isCreateExternalLabel() {
			return false;
		}
	}

	public static class CreateGroupFeature extends AbstractCreateArtifactFeature<Group> {

		public CreateGroupFeature(IFeatureProvider fp) {
			super(fp, "Group", "Visually groups elements");
		}

		@Override
		public boolean canCreate(ICreateContext context) {
			return true;
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_GROUP;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature#getBusinessObjectClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getGroup();
		}
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp) {
		return new AbstractDefaultDeleteFeature(fp);
	}
}