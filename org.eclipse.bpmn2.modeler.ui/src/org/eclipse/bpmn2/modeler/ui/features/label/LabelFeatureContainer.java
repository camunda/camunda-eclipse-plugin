package org.eclipse.bpmn2.modeler.ui.features.label;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.modeler.core.features.ContextConstants;
import org.eclipse.bpmn2.modeler.core.features.FeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class LabelFeatureContainer implements FeatureContainer {

	@Override
	public Object getApplyObject(IContext context) {
		if (context.getProperty(ContextConstants.LABEL_CONTEXT) != null
				&& (Boolean) context.getProperty(ContextConstants.LABEL_CONTEXT) == true) {
			if (context instanceof IAddContext) {
				IAddContext addContext = (IAddContext) context;
				return addContext.getNewObject();
			}
		} else if (context instanceof IPictogramElementContext) {
			IPictogramElementContext peContext = (IPictogramElementContext) context;
			BaseElement o = BusinessObjectUtil.getFirstElementOfType(peContext.getPictogramElement(), BaseElement.class);
			if (o != null && (o instanceof Gateway || o instanceof Event)) {
				if (peContext.getPictogramElement() instanceof ContainerShape) {
					ContainerShape container = (ContainerShape) peContext.getPictogramElement();
					if (container.getChildren().size() == 1) {
						Shape shape = container.getChildren().get(0);
						if (shape.getGraphicsAlgorithm() instanceof AbstractText) {
							return o;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean canApplyTo(Object o) {
		return o instanceof Gateway || o instanceof Event;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddLabelFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new UpdateBaseElementNameFeature(fp){
			
			@Override
			public boolean update(IUpdateContext context) {
				super.update(context);
				IGaService gaService = Graphiti.getGaService();
				
				ContainerShape container = (ContainerShape) context.getPictogramElement();
				Shape shape = container.getChildren().get(0); // Otherwise, this would never be reached!
				
				GraphicsAlgorithm textGA = container.getGraphicsAlgorithm();
				AbstractText text = (AbstractText) shape.getGraphicsAlgorithm();
				
				int oldWidth = textGA.getWidth() - GraphicsUtil.SHAPE_PADDING;
				int x = textGA.getX() + ((oldWidth + GraphicsUtil.SHAPE_PADDING) / 2);
				int y = textGA.getY();
				
				BaseElement o = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
				String name = ModelUtil.getName(o);
				
				if (name == null) {
					gaService.setLocationAndSize(textGA, x, y, 0, 0);
					gaService.setLocationAndSize(text, 0, 0, 0, 0);
					container.setVisible(false);
				} else {
					int newWidth = GraphicsUtil.getLabelWidth(text);
					int newHeight = GraphicsUtil.getLabelHeight(text);
					x = x - ((newWidth + GraphicsUtil.SHAPE_PADDING) / 2);
					gaService.setLocationAndSize(textGA, x, y, newWidth + GraphicsUtil.SHAPE_PADDING, newHeight + GraphicsUtil.SHAPE_PADDING);
					gaService.setLocationAndSize(text, 0, 0, newWidth + GraphicsUtil.TEXT_PADDING, newHeight + GraphicsUtil.TEXT_PADDING);
					container.setVisible(true);
				}
				return true;
			}
		};
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutLabelFeature(fp);
	}

	@Override
	public IRemoveFeature getRemoveFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new DefaultMoveShapeFeature(fp);
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp) {
		return null;
	}
}
