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

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.features.MultiUpdateFeature;
import org.camunda.bpm.modeler.core.features.event.AbstractCreateEventFeature;
import org.camunda.bpm.modeler.core.features.event.AbstractUpdateEventFeature;
import org.camunda.bpm.modeler.core.features.event.AddEventFeature;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.camunda.bpm.modeler.ui.features.AbstractAppendNodeNodeFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EndEvent;
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

public class EndEventFeatureContainer extends AbstractEventFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof EndEvent;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateEndEventFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddEventFeature<EndEvent>(fp) {
			
			@Override
			protected void decorate(Ellipse e) {
				e.setLineWidth(3);
			}
			
			@Override
			protected void setProperties(IAddContext context, ContainerShape newShape) {
				super.setProperties(context, newShape);
				
				EndEvent endEvent = getBusinessObject(context);

				IPeService peService = Graphiti.getPeService();
				peService.setPropertyValue(newShape,
						UpdateEndEventFeature.END_EVENT_MARKER,
						AbstractUpdateEventFeature.getEventDefinitionsValue(endEvent));
			}
		};
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature updateFeature = new MultiUpdateFeature(fp);
		updateFeature.addUpdateFeature(super.getUpdateFeature(fp));
		updateFeature.addUpdateFeature(new UpdateEndEventFeature(fp));
		return updateFeature;
	}
	
	public static class CreateEndEventFeature extends AbstractCreateEventFeature<EndEvent> {

		public CreateEndEventFeature(IFeatureProvider fp) {
			super(fp, "End Event", "Indicates the end of a process or choreography");
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_END_EVENT;
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getEndEvent();
		}
	}

	protected static class UpdateEndEventFeature extends AbstractUpdateEventFeature {

		public static String END_EVENT_MARKER = "marker.end.event";

		/**
		 * @param fp
		 */
		public UpdateEndEventFeature(IFeatureProvider fp) {
			super(fp);
		}

		/* (non-Javadoc)
		 * @see org.camunda.bpm.modeler.features.activity.AbstractUpdateMarkerFeature#getPropertyKey()
		 */
		@Override
		protected String getPropertyKey() {
			return END_EVENT_MARKER;
		}
	}

	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider fp) {
		ICustomFeature[] superFeatures = super.getCustomFeatures(fp);
		List<ICustomFeature> thisFeatures = new ArrayList<ICustomFeature>();
		int i;
		for (ICustomFeature f : superFeatures) {
			if (!(f instanceof AbstractAppendNodeNodeFeature))
			thisFeatures.add(f);
		}
		return thisFeatures.toArray( new ICustomFeature[thisFeatures.size()] );
	}
}