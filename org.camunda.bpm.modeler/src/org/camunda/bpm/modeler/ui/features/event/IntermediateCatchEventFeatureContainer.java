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

import org.camunda.bpm.modeler.core.features.api.IDecorateFeature;
import org.camunda.bpm.modeler.core.features.event.AbstractCreateEventFeature;
import org.camunda.bpm.modeler.core.features.event.EventDecorateFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.Ellipse;

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
	public IDecorateFeature getDecorateFeature(IFeatureProvider fp) {
		return new EventDecorateFeature(fp) {
			
			@Override
			protected void decorate(Ellipse ellipse) {
				Ellipse circle = GraphicsUtil.createIntermediateEventCircle(ellipse);
				circle.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
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
		thisFeatures[i++] = new MorphIntermediateCatchEventFeature(fp);
		return thisFeatures;
	}

	public static class CreateIntermediateCatchEventFeature extends AbstractCreateEventFeature<IntermediateCatchEvent> {

		public CreateIntermediateCatchEventFeature(IFeatureProvider fp) {
			super(fp, "Catch Event", "Token remains at the event until event trigger will occur");
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_INTERMEDIATE_CATCH_EVENT;
		}

		@Override
		public EClass getBusinessObjectClass() {
			return Bpmn2Package.eINSTANCE.getIntermediateCatchEvent();
		}
	}
}