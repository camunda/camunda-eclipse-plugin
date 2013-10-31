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
package org.camunda.bpm.modeler.ui.features.artifact;

import org.camunda.bpm.modeler.core.features.artifact.AbstractCreateArtifactFeature;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class CreateTextAnnotationFeature extends AbstractCreateArtifactFeature<TextAnnotation> {

	public CreateTextAnnotationFeature(IFeatureProvider fp) {
		super(fp, "Annotation", "Provide additional information");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoSubprocess = FeatureSupport.isTargetSubProcess(context);
		
		return intoDiagram || intoLane || intoParticipant || intoSubprocess;
	}

	@Override
	protected String getStencilImageId() {
		return ImageProvider.IMG_16_TEXT_ANNOTATION;
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_TEXT_ANNOTATION;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}

	/* (non-Javadoc)
	 * @see org.camunda.bpm.modeler.features.AbstractBpmn2CreateFeature#getBusinessObjectClass()
	 */
	@Override
	public EClass getBusinessObjectClass() {
		return Bpmn2Package.eINSTANCE.getTextAnnotation();
	}
}
