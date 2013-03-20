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
package org.camunda.bpm.modeler.ui.features.activity.subprocess;

import org.camunda.bpm.modeler.core.features.DefaultResizeBPMNShapeFeature;
import org.camunda.bpm.modeler.core.features.MultiUpdateFeature;
import org.camunda.bpm.modeler.core.features.activity.AbstractCreateExpandableFlowNodeFeature;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.camunda.bpm.modeler.ui.features.activity.ResizeActivityFeature;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CallableElement;
import org.eclipse.bpmn2.GlobalBusinessRuleTask;
import org.eclipse.bpmn2.GlobalManualTask;
import org.eclipse.bpmn2.GlobalScriptTask;
import org.eclipse.bpmn2.GlobalTask;
import org.eclipse.bpmn2.GlobalUserTask;
import org.eclipse.bpmn2.Process;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class CallActivityFeatureContainer extends AbstractExpandableActivityFeatureContainer {

	private static final int MARKER_OFFSET = 4;
	private static final String CALL_ACTIVITY_REF_PROPERTY = "call.activity.ref";
	private static final String GLOBAL_TASK_SHAPE_PROPERTY = "global.task.shape";

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof CallActivity;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateCallActivityFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddExpandableActivityFeature<CallActivity>(fp) {
			
			@Override
			protected void setProperties(IAddContext context, ContainerShape newShape) {
				super.setProperties(context, newShape);
				
				CallActivity activity = getBusinessObject(context);
				
				Graphiti.getPeService().setPropertyValue(newShape, CALL_ACTIVITY_REF_PROPERTY,
						getCallableElementStringValue(activity.getCalledElementRef()));
			}
			
			@Override
			protected void decorate(RoundedRectangle rect) {
				rect.setLineWidth(4);
			}

			@Override
			protected int getMarkerContainerOffset() {
				return MARKER_OFFSET;
			}

			@Override
			public int getDefaultWidth() {
				return GraphicsUtil.getActivitySize(getDiagram()).getWidth();
			}

			@Override
			public int getDefaultHeight() {
				return GraphicsUtil.getActivitySize(getDiagram()).getHeight();
			}
		};
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutCallActivityFeature(fp);
	}

	@Override
	public MultiUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = super.getUpdateFeature(fp);
		multiUpdate.addUpdateFeature(new UpdateCallActivityFeature(fp));
		return multiUpdate;
	}
	
	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new ResizeActivityFeature(fp);
	}
	
	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider fp) {
		ICustomFeature[] superFeatures = super.getCustomFeatures(fp);
		ICustomFeature[] thisFeatures = new ICustomFeature[superFeatures.length];
		int index = 1;
		for (int i = 0; i < superFeatures.length; ++i) {
			if(thisFeatures.length < (index+i)) {
				thisFeatures[index + i] = superFeatures[i];
			}
		}
		return thisFeatures;
	}

	public static class LayoutCallActivityFeature extends LayoutExpandableActivityFeature {
		private LayoutCallActivityFeature(IFeatureProvider fp) {
			super(fp);
		}

		protected int getMarkerContainerOffset() {
			return MARKER_OFFSET;
		}
	}

	public static class CreateCallActivityFeature extends AbstractCreateExpandableFlowNodeFeature<CallActivity> {

		// NOTE: Even though the Call Activity is an expandable figure, the contents for its "innards"
		// are (usually) defined somewhere else, so it doesn't make much sense to be able to expand it in the
		// same sense that a SubProcess would be expanded and rendered. When the "expand" button is clicked
		// we should probably locate the process where this thing is defined (if possible) and open an
		// editor to display its contents.
		
		public CreateCallActivityFeature(IFeatureProvider fp) {
			super(fp, "Call Activity",
					"Identifies a point in the Process where a global Process or a Global Task is called");
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_CALL_ACTIVITY;
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getCallActivity();
		}
	}

	private class UpdateCallActivityFeature extends AbstractUpdateFeature {

		public UpdateCallActivityFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canUpdate(IUpdateContext context) {
			CallActivity callActivity = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
					CallActivity.class);
			return callActivity != null && context.getPictogramElement() instanceof ContainerShape;
		}

		@Override
		public IReason updateNeeded(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			PictogramElement element = context.getPictogramElement();
			String property = peService.getPropertyValue(element, CALL_ACTIVITY_REF_PROPERTY);
			if (property == null) {
				return Reason.createFalseReason();
			}
			CallActivity callActivity = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
					CallActivity.class);
			String value = getCallableElementStringValue(callActivity.getCalledElementRef());
			boolean changed = !value.equals(property);
			return changed ? Reason.createTrueReason() : Reason.createFalseReason();
		}

		@Override
		public boolean update(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			IGaService gaService = Graphiti.getGaService();

			ContainerShape container = (ContainerShape) context.getPictogramElement();
			CallActivity callActivity = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
					CallActivity.class);

			Shape globalTaskShape = GraphicsUtil.getContainedShape(container, GLOBAL_TASK_SHAPE_PROPERTY);

			if (callActivity.getCalledElementRef() == null) {
				GraphicsUtil.hideActivityMarker(container, GraphicsUtil.ACTIVITY_MARKER_EXPAND);
				if (globalTaskShape != null) {
					peService.deletePictogramElement(globalTaskShape);
				}
			}

			else if (callActivity.getCalledElementRef() instanceof GlobalTask) {
				GraphicsUtil.hideActivityMarker(container, GraphicsUtil.ACTIVITY_MARKER_EXPAND);
				GlobalTask t = (GlobalTask) callActivity.getCalledElementRef();
				if (globalTaskShape == null) {
					globalTaskShape = peService.createShape(container, false);
					peService.setPropertyValue(globalTaskShape, GLOBAL_TASK_SHAPE_PROPERTY, Boolean.toString(true));
				}
				String imageId = getImageId(t);
				if (imageId != null) {
					Image image = gaService.createImage(globalTaskShape, imageId);
					gaService.setLocationAndSize(image, MARKER_OFFSET + 2, MARKER_OFFSET + 2, 16, 16);
				}
			}

			else if (callActivity.getCalledElementRef() instanceof Process) {
				if (globalTaskShape != null) {
					peService.deletePictogramElement(globalTaskShape);
				}
				GraphicsUtil.showActivityMarker(container, GraphicsUtil.ACTIVITY_MARKER_EXPAND);
			}

			peService.setPropertyValue(container, CALL_ACTIVITY_REF_PROPERTY,
					getCallableElementStringValue(callActivity.getCalledElementRef()));
			return true;
		}
	}

	private String getCallableElementStringValue(CallableElement element) {
		if (element == null) {
			return "null";
		}
		return element.getClass().getSimpleName();
	}

	private String getImageId(GlobalTask task) {
		if (task instanceof GlobalBusinessRuleTask) {
			return ImageProvider.IMG_16_BUSINESS_RULE_TASK;
		} else if (task instanceof GlobalManualTask) {
			return ImageProvider.IMG_16_MANUAL_TASK;
		} else if (task instanceof GlobalScriptTask) {
			return ImageProvider.IMG_16_SCRIPT_TASK;
		} else if (task instanceof GlobalUserTask) {
			return ImageProvider.IMG_16_USER_TASK;
		} else {
			return null;
		}
	}
}