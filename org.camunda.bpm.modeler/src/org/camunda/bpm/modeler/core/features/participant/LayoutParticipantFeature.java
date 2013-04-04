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
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core.features.participant;

import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * Layout feature for participants
 * 
 * @author nico.rehwaldt
 */
public class LayoutParticipantFeature extends LayoutLaneSetFeature {

	public LayoutParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void updateMarkers(ContainerShape containerShape) {
		GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();

		Shape shape = FeatureSupport.getShape(containerShape, UpdateParticipantMultiplicityFeature.MULTIPLICITY_MARKER, Boolean.toString(true));
		
		if (shape != null) {
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			int x = (containerGa.getWidth() / 2) - 10;
			int y = containerGa.getHeight() - 20;
			
			Graphiti.getGaService().setLocation(ga, x, y);
		}
	}
}