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
package org.camunda.bpm.modeler.ui.features.artifact;

import org.camunda.bpm.modeler.core.features.AbstractBpmn2AddShapeFeature;
import org.camunda.bpm.modeler.core.features.DefaultBpmn2LayoutShapeFeature;
import org.camunda.bpm.modeler.core.features.DefaultBpmn2MoveShapeFeature;
import org.camunda.bpm.modeler.core.features.DefaultBpmn2ResizeShapeFeature;
import org.camunda.bpm.modeler.core.features.artifact.AbstractCreateArtifactFeature;
import org.camunda.bpm.modeler.core.features.container.BaseElementFeatureContainer;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.camunda.bpm.modeler.ui.Images;
import org.camunda.bpm.modeler.ui.features.AbstractDefaultDeleteFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Group;
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
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
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
		return new LayoutGroupFeature(fp);
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new DefaultBpmn2MoveShapeFeature(fp);
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new ResizeGroupFeature(fp);
	}

	private final class AddGroupFeature extends AbstractBpmn2AddShapeFeature<Group> {
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

			// Create Polyline instead of rectangle so that elements within the group remain selectable
			int xy[] = new int[] {0, 0, width, 0, width, height, 0, height, 0, 0};
			Polyline rect = gaService.createPolyline(newShape, xy);
			rect.setLineWidth(2);
			rect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			rect.setLineStyle(LineStyle.DASHDOT);
			gaService.setLocationAndSize(rect, x, y, width, height);

			return newShape;
		}

		@Override
		public int getDefaultHeight() {
			return 200;
		}

		@Override
		public int getDefaultWidth() {
			return 200;
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
			return Images.IMG_16_GROUP;
		}

		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getGroup();
		}
	}

	public class ResizeGroupFeature extends DefaultBpmn2ResizeShapeFeature {

		public ResizeGroupFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected void internalResize(IResizeShapeContext context) {
			Shape shape = context.getShape();

			int x = context.getX();
			int y = context.getY();
			int w = context.getWidth();
			int h = context.getHeight();
			Polyline rect = (Polyline) shape.getGraphicsAlgorithm();
			Point p;
			p = rect.getPoints().get(1);
			p.setX(w);
			p = rect.getPoints().get(2);
			p.setX(w);
			p.setY(h);
			p = rect.getPoints().get(3);
			p.setY(h);

			Graphiti.getGaService().setLocationAndSize(rect, x, y, w, h);
		}
	}

	public class LayoutGroupFeature extends DefaultBpmn2LayoutShapeFeature {

		public LayoutGroupFeature(IFeatureProvider fp) {
			super(fp);
		}
		
		@Override
		protected void postLayoutShapeAndChildren(ContainerShape shape, final ILayoutContext context) {
			// Polyline layout is destroyed after moving a parent container (e.g. pool, ...)
			// ==>> repair the Polyline rectangle if parent is not the diagram itself
			if (!(shape.getContainer() instanceof Diagram)) {
				Polyline rect = (Polyline) shape.getGraphicsAlgorithm();
				
				Point p;
				p = rect.getPoints().get(2);
				int w = p.getX();
				int h = p.getY();
				
				p = rect.getPoints().get(0);
				p.setX(0);
				p.setY(0);
				p = rect.getPoints().get(1);
				p.setX(w);
				p.setY(0);
				p = rect.getPoints().get(3);
				p.setX(0);
				p.setY(h);
				p = rect.getPoints().get(4);
				p.setX(0);
				p.setY(0);
	
				Graphiti.getGaService().setLocationAndSize(rect, rect.getX(), rect.getY(), w, h);
			}
		}		
	}
	
	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp) {
		return new AbstractDefaultDeleteFeature(fp);
	}
}