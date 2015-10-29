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

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.ILocation;
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
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
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
		return new DefaultBpmn2LayoutShapeFeature(fp);
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new MoveGroupFeature(fp);
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
			Group businessObject = getBusinessObject(context);

			int width = bounds.getWidth();
			int height = bounds.getHeight();
			int x = bounds.getX();
			int y = bounds.getY();

			if (!(context.getTargetContainer() instanceof Diagram)) {
				ILocation loc = Graphiti.getPeService().getLocationRelativeToDiagram(context.getTargetContainer());
				x += loc.getX();
				y += loc.getY();
			}
			
			// Ensure group will always be added to the diagram itself
			// not to another element like subprocess, pool / lane, ...
			ContainerShape newShape = peService.createContainerShape(getDiagram(), true);

			// Create Polyline so that elements within the group remain selectable
			int xy[] = new int[] {0, 0, width, 0, width, height, 0, height, 0, 0};
			Polyline rect = gaService.createPolyline(newShape, xy);
			rect.setLineWidth(2);
			rect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			rect.setLineStyle(LineStyle.DASHDOT);
			gaService.setLocationAndSize(rect, x, y, width, height);

			link(newShape, businessObject);

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

	public static class MoveGroupFeature extends DefaultBpmn2MoveShapeFeature {
		public MoveGroupFeature(IFeatureProvider fp) {
			super(fp);
		}
		List<ContainerShape> containedShapes = new ArrayList<ContainerShape>();
		@Override
		public boolean canMoveShape(IMoveShapeContext context) {
			return true;
		}
		@Override
		protected void preMoveShape(IMoveShapeContext context) {
			super.preMoveShape(context);
			ContainerShape container = context.getTargetContainer();
			if (!(container instanceof Diagram)) {
				ILocation loc = Graphiti.getPeService().getLocationRelativeToDiagram(container);
				int x = context.getX() + loc.getX();
				int y = context.getY() + loc.getY();
				((MoveShapeContext)context).setX(x);
				((MoveShapeContext)context).setY(y);
				((MoveShapeContext)context).setDeltaX(x - preMoveBounds.getX());
				((MoveShapeContext)context).setDeltaY(y - preMoveBounds.getY());
				((MoveShapeContext)context).setTargetContainer(getDiagram());
			}
		}
	}	
	
	public class ResizeGroupFeature extends DefaultBpmn2ResizeShapeFeature {

		public ResizeGroupFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected void internalResize(IResizeShapeContext context) {
			int x = context.getX();
			int y = context.getY();
			int width = context.getWidth();
			int height = context.getHeight();

			Shape shape = context.getShape();
			Polyline polyline = (Polyline) shape.getGraphicsAlgorithm();

			EList<Point> points = polyline.getPoints();

			Point p = points.get(1);
			p.setX(width);

			p = points.get(2);
			p.setX(width);
			p.setY(height);

			p = points.get(3);
			p.setY(height);

			Graphiti.getGaService().setLocationAndSize(polyline, x, y, width, height);
		}
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp) {
		return new AbstractDefaultDeleteFeature(fp);
	}
}