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
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

public class IntermediateCatchEventFeatureContainer extends AbstractEventFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof IntermediateCatchEvent;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateIntermediateCatchEventFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(super.getUpdateFeature(fp));
		multiUpdate.addUpdateFeature(new UpdateIntermediateCatchEventFeature(fp));
		return multiUpdate;
	}
	
	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddEventFeature<IntermediateCatchEvent>(fp) {
			@Override
			protected void decorate(Ellipse e) {
				Ellipse circle = GraphicsUtil.createIntermediateEventCircle(e);
				circle.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			}

			@Override
			protected void setProperties(IAddContext context, ContainerShape newShape) {
				super.setProperties(context, newShape);
				
				IntermediateCatchEvent event = getBusinessObject(context);
				
				IPeService peService = Graphiti.getPeService();
				peService.setPropertyValue(newShape,
						UpdateIntermediateCatchEventFeature.INTERMEDIATE_CATCH_EVENT_MARKER,
						AbstractUpdateEventFeature.getEventDefinitionsValue(event));
			}
		};
	}

	public static class CreateIntermediateCatchEventFeature extends AbstractCreateEventFeature<IntermediateCatchEvent> {

		public CreateIntermediateCatchEventFeature(IFeatureProvider fp) {
			super(fp, "Catch Event", "Token remains at the event until event trigger will occur");
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_INTERMEDIATE_CATCH_EVENT;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getIntermediateCatchEvent();
		}
	}
	
	public static class UpdateIntermediateCatchEventFeature extends AbstractUpdateEventFeature {

		public static String INTERMEDIATE_CATCH_EVENT_MARKER = "marker.intermediate.catch.event";

		/**
		 * @param fp
		 */
		public UpdateIntermediateCatchEventFeature(IFeatureProvider fp) {
			super(fp);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.activity.AbstractUpdateMarkerFeature#getPropertyKey()
		 */
		@Override
		protected String getPropertyKey() {
			return INTERMEDIATE_CATCH_EVENT_MARKER;
		}
	}
}