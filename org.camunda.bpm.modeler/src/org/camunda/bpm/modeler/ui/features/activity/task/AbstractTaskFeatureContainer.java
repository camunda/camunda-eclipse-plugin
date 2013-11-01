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
package org.camunda.bpm.modeler.ui.features.activity.task;

import org.camunda.bpm.modeler.core.features.AbstractUpdateBaseElementFeature;
import org.camunda.bpm.modeler.core.features.MultiUpdateFeature;
import org.camunda.bpm.modeler.core.features.activity.LayoutActivityFeature;
import org.camunda.bpm.modeler.core.features.activity.task.AddTaskFeature;
import org.camunda.bpm.modeler.core.features.activity.task.DirectEditActivityFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.ui.features.activity.AbstractActivityFeatureContainer;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public abstract class AbstractTaskFeatureContainer extends AbstractActivityFeatureContainer {

	@Override
	public MultiUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = super.getUpdateFeature(fp);
		AbstractUpdateBaseElementFeature nameUpdateFeature = new AbstractUpdateBaseElementFeature(fp) {

			@Override
			public boolean canUpdate(IUpdateContext context) {
				Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
				return bo != null && bo instanceof BaseElement && canApplyTo((BaseElement) bo);
			}
		};
		multiUpdate.addUpdateFeature(nameUpdateFeature);
		return multiUpdate;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddTaskFeature<ServiceTask>(fp);
	}
	
	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return new DirectEditActivityFeature(fp);
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutTaskFeature(fp);
	}
	
	public static class LayoutTaskFeature extends LayoutActivityFeature {
		
		private LayoutTaskFeature(IFeatureProvider fp) {
			super(fp);
		}
		
		@Override
		protected void layoutLabel(ContainerShape container, Shape labelShape, IRectangle bounds) {
			Text text = (Text) labelShape.getGraphicsAlgorithm();
			
			int padding = GraphicsUtil.TASK_IMAGE_SIZE;
			Graphiti.getGaService().setLocationAndSize(text, 3, padding, bounds.getWidth() - 6, bounds.getHeight() - padding);
		}
	}
}