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
package org.camunda.bpm.modeler.ui.features.event;

import org.camunda.bpm.modeler.core.features.MultiUpdateFeature;
import org.camunda.bpm.modeler.core.features.event.AbstractCreateEventFeature;
import org.camunda.bpm.modeler.core.features.event.AbstractUpdateEventFeature;
import org.camunda.bpm.modeler.core.features.event.AddEventFeature;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

public class IntermediateThrowEventFeatureContainer extends AbstractEventFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof IntermediateThrowEvent;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateIntermediateThrowEventFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(super.getUpdateFeature(fp));
		multiUpdate.addUpdateFeature(new UpdateIntermediateThrowEventFeature(fp));
		return multiUpdate;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddEventFeature<IntermediateThrowEvent>(fp) {
			@Override
			protected void decorate(Ellipse e) {
				Ellipse circle = GraphicsUtil.createIntermediateEventCircle(e);
				circle.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			}

			@Override
			protected void setProperties(IAddContext context, ContainerShape newShape) {
				super.setProperties(context, newShape);
				
				IntermediateThrowEvent throwEvent = getBusinessObject(context);
				IPeService peService = Graphiti.getPeService();
				peService.setPropertyValue(newShape,
						UpdateIntermediateThrowEventFeature.INTERMEDIATE_THROW_EVENT_MARKER,
						AbstractUpdateEventFeature.getEventDefinitionsValue(throwEvent));
			}
		};
	}
	
	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider fp) {
		ICustomFeature[] superFeatures = super.getCustomFeatures(fp);
		ICustomFeature[] thisFeatures = new ICustomFeature[1 + superFeatures.length];
		int i;
		for (i=0; i<superFeatures.length; ++i)
			thisFeatures[i] = superFeatures[i];
		thisFeatures[i++] = new MorphIntermediateThrowEventFeature(fp);
		return thisFeatures;
	}

	public static class CreateIntermediateThrowEventFeature extends AbstractCreateEventFeature<IntermediateThrowEvent> {

		public CreateIntermediateThrowEventFeature(IFeatureProvider fp) {
			super(fp, "Throw Event", "Throws the event trigger and the event immediately occurs");
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_INTERMEDIATE_THROW_EVENT;
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getIntermediateThrowEvent();
		}
	}
	
	public static class UpdateIntermediateThrowEventFeature extends AbstractUpdateEventFeature {

		public static String INTERMEDIATE_THROW_EVENT_MARKER = "marker.intermediate.throw.event";

		/**
		 * @param fp
		 */
		public UpdateIntermediateThrowEventFeature(IFeatureProvider fp) {
			super(fp);
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.activity.AbstractUpdateMarkerFeature#getPropertyKey()
		 */
		@Override
		protected String getPropertyKey() {
			return INTERMEDIATE_THROW_EVENT_MARKER;
		}
	}
}