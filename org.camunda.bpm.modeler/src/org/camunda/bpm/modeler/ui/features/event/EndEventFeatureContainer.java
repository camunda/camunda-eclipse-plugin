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

import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.features.event.AbstractCreateEventFeature;
import org.camunda.bpm.modeler.core.features.event.AddEventFeature;
import org.camunda.bpm.modeler.core.features.event.EventDecorateFeature;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.camunda.bpm.modeler.ui.features.AbstractAppendNodeNodeFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.Ellipse;

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
		return new AddEventFeature<Event>(fp) {
//			@Override
//			protected void decorate(Ellipse decorateContainer) {
//				decorateContainer.setLineWidth(3);
//			}
		};
	}
	
	@Override
	public IDecorateFeature getDecorateFeature(IFeatureProvider fp) {
		return new EventDecorateFeature(fp) {
			
			@Override
			protected void decorate(Ellipse decorateContainer) {
				decorateContainer.setLineWidth(3);
			}
		};
	}
	
	public static class CreateEndEventFeature extends AbstractCreateEventFeature<EndEvent> {

		public CreateEndEventFeature(IFeatureProvider fp) {
			super(fp, "End Event", "Indicates the end of a process or choreography");
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_END_EVENT;
		}

		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getEndEvent();
		}
	}


	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider fp) {
		ICustomFeature[] superFeatures = super.getCustomFeatures(fp);
		List<ICustomFeature> thisFeatures = new ArrayList<ICustomFeature>();
		for (ICustomFeature f : superFeatures) {
			if (!(f instanceof AbstractAppendNodeNodeFeature)) {
				thisFeatures.add(f);
			}
		}
		thisFeatures.add(new MorphEndEventFeature(fp));
		
		return thisFeatures.toArray( new ICustomFeature[thisFeatures.size()] );
	}
}