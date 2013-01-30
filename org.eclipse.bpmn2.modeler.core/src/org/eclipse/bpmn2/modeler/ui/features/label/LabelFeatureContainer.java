package org.eclipse.bpmn2.modeler.ui.features.label;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.DataStore;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.ContextConstants;
import org.eclipse.bpmn2.modeler.core.features.FeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
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
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class LabelFeatureContainer implements FeatureContainer {

	@Override
	public Object getApplyObject(IContext context) {
		if (context instanceof IAddContext) {
			if (ContextUtil.is(context, ContextConstants.LABEL_CONTEXT)) {
				IAddContext addContext = (IAddContext) context;
				return addContext.getNewObject();
			}
		} else
		if (context instanceof IPictogramElementContext) {
			IPictogramElementContext pictogramElementCtx = (IPictogramElementContext) context;
			PictogramElement e = pictogramElementCtx.getPictogramElement();
			
			if (GraphicsUtil.isLabel(e)) {
				return BusinessObjectUtil.getFirstElementOfType(e, BaseElement.class);
			}
		}
		
		return null;
	}

	@Override
	public boolean canApplyTo(Object o) {
		// these all have Label features
		return o instanceof Gateway ||
				o instanceof Event ||
				o instanceof Message ||
				o instanceof DataInput ||
				o instanceof DataOutput ||
				o instanceof DataObject ||
				o instanceof DataObjectReference ||
				o instanceof DataStore ||
				o instanceof DataStoreReference;
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
		return new UpdateBaseElementNameFeature(fp);
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
		return new MoveLabelFeature(fp);
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp) {
		return null;
	}
	
	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider fp) {
		return null;
	}
	
	public static class MoveLabelFeature extends DefaultMoveShapeFeature {
		
		public MoveLabelFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canMoveShape(IMoveShapeContext context) {
			return true;
		}

		@Override
		protected void internalMove(IMoveShapeContext context) {
			// perform move but do not change container
			
			if (!getUserDecision()) {
				return;
			}
			
			Shape shapeToMove = context.getShape();

			ContainerShape targetContainer = context.getTargetContainer();
			IRectangle bounds = LayoutUtil.getAbsoluteBounds(targetContainer);
			
			// context x and y are relative to new target container
			// we ignore that and have to add the containers position 
			// to get the new diagram local bounds of the label
			int x = bounds.getX() + context.getX();
			int y = bounds.getY() + context.getY();

			// always move within the original container
			if (shapeToMove.getGraphicsAlgorithm() != null) {
				Graphiti.getGaService().setLocation(shapeToMove.getGraphicsAlgorithm(), x, y,
						avoidNegativeCoordinates());
				
				Graphiti.getPeService().sendToFront(shapeToMove);
			}
		}
		
		@Override
		protected void postMoveShape(IMoveShapeContext context) {
			super.postMoveShape(context);
			
			Shape labelShape = context.getShape();
			
			BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(labelShape, BPMNShape.class);
			
			DIUtils.updateDILabel((ContainerShape) labelShape, bpmnShape);
		}
	}
}
