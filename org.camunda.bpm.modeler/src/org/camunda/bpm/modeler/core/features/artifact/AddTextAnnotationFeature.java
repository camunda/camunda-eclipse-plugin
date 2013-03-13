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
package org.camunda.bpm.modeler.core.features.artifact;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import org.camunda.bpm.modeler.core.features.AbstractAddBpmnShapeFeature;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddTextAnnotationFeature extends AbstractAddBpmnShapeFeature<TextAnnotation> {

	public AddTextAnnotationFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean isAnnotation = getBusinessObject(context) instanceof TextAnnotation;
		boolean intoDiagram = context.getTargetContainer() instanceof Diagram;
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoSubProcess = FeatureSupport.isTargetSubProcess(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		
		return isAnnotation && (intoDiagram || intoLane || intoSubProcess || intoParticipant);
	}
	
	@Override
	public int getDefaultHeight() {
		return 100;
	}

	@Override
	public int getDefaultWidth() {
		return 50;
	}

	@Override
	protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {

		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		
		int x = bounds.getX();
		int y = bounds.getY();
		
		int width = bounds.getWidth();
		int height = bounds.getHeight();
		
		int commentEdge = 15;

		TextAnnotation textAnnotation = getBusinessObject(context);
		
		ContainerShape newShape = peService.createContainerShape(context.getTargetContainer(), true);
		
		Rectangle rect = gaService.createInvisibleRectangle(newShape);
		gaService.setLocationAndSize(rect, x, y, width, height);

		Shape lineShape = peService.createShape(newShape, false);
		Polyline line = gaService.createPolyline(lineShape, new int[] { commentEdge, 0, 0, 0, 0, height, commentEdge,
				height });
		line.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		line.setLineWidth(2);
		gaService.setLocationAndSize(line, 0, 0, commentEdge, height);

		Shape textShape = peService.createShape(newShape, false);
		MultiText text = gaService.createDefaultMultiText(getDiagram(), textShape, textAnnotation.getText());
		StyleUtil.applyStyle(text, textAnnotation);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		gaService.setLocationAndSize(text, 5, 5, width - 5, height - 5);
		
		link(textShape, textAnnotation);
		
		return newShape;
	}

	@Override
	protected boolean isCreateExternalLabel() {
		return false;
	}
}