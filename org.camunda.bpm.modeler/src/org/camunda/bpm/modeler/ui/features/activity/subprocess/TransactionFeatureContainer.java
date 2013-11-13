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

import org.camunda.bpm.modeler.core.features.MultiUpdateFeature;
import org.camunda.bpm.modeler.core.features.activity.AbstractCreateExpandableFlowNodeFeature;
import org.camunda.bpm.modeler.core.features.activity.ActivityDecorateFeature;
import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.camunda.bpm.modeler.ui.Images;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Transaction;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class TransactionFeatureContainer extends AbstractExpandableActivityFeatureContainer {

	private static final int offset = 3;

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof Transaction;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateTransactionFeature(fp);
	}

	@Override
	public IDecorateFeature getDecorateFeature(IFeatureProvider fp) {
		return new ActivityDecorateFeature(fp) {

			@Override
			protected void decorate(RoundedRectangle decorateContainer) {
				IGaService gaService = Graphiti.getGaService();
				RoundedRectangle innerRect = gaService.createRoundedRectangle(decorateContainer, 5, 5);
				
				innerRect.setFilled(false);
				innerRect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				
				gaService.setLocationAndSize(innerRect, offset, offset, decorateContainer.getWidth() - (2 * offset), decorateContainer.getHeight() - (2 * offset));
			}
		};
	}
	
	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddExpandableActivityFeature<Transaction>(fp);
	}

	@Override
	public MultiUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = super.getUpdateFeature(fp);
		UpdateExpandableActivityFeature updateFeature = new UpdateExpandableActivityFeature(fp);
		multiUpdate.addUpdateFeature(updateFeature);
		return multiUpdate;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutTransactionFeature(fp);
	}

	public static class LayoutTransactionFeature extends LayoutExpandableActivityFeature {
		
		public LayoutTransactionFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected void layoutActivityRectangle(ContainerShape container, Shape activityRectangle, IRectangle bounds) {
			super.layoutActivityRectangle(container, activityRectangle, bounds);

			// update inner rectangle, too
			GraphicsAlgorithm rectangleGa = activityRectangle.getGraphicsAlgorithm();
			
			RoundedRectangle innerRect = (RoundedRectangle) rectangleGa.getGraphicsAlgorithmChildren().get(0);
			Graphiti.getGaService().setSize(innerRect, rectangleGa.getWidth() - 6, rectangleGa.getHeight() - 6);
		}

		@Override
		protected int getMarkerContainerOffset() {
			return offset;
		}
	}

	public static class CreateTransactionFeature extends AbstractCreateExpandableFlowNodeFeature<Transaction> {

		public CreateTransactionFeature(IFeatureProvider fp) {
			super(fp, "Transaction",
					"Specialized sub-process that will have behavior controlled by transaction protocol");
		}

		@Override
		protected String getStencilImageId() {
			return Images.IMG_16_TRANSACTION;
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getTransaction();
		}
	}
}