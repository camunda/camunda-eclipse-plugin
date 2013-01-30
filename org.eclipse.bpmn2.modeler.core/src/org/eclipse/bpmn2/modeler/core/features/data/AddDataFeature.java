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
package org.eclipse.bpmn2.modeler.core.features.data;

import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public abstract class AddDataFeature<T extends ItemAwareElement> extends AbstractAddBPMNShapeFeature<T> {

	public AddDataFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoSubProcess = FeatureSupport.isTargetSubProcess(context);
		return intoDiagram || intoLane || intoParticipant || intoSubProcess;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		@SuppressWarnings("unchecked")
		T t = getBusinessObject(context);

		int width = getWidth();
		int height = getHeight();
		int e = 10;
		
		ContainerShape container = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle invisibleRect = gaService.createInvisibleRectangle(container);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height);

		Shape rectShape = peService.createShape(container, false);
		Polygon rect = gaService.createPolygon(rectShape, new int[] { 0, 0, width - e, 0, width, e, width, height, 0,
				height });
		rect.setLineWidth(1);
		StyleUtil.applyStyle(rect,t);
		decorate(rect);

		int p = width - e - 1;
		Polyline edge = gaService.createPolyline(rect, new int[] { p, 0, p, e + 1, width, e + 1 });
		edge.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		edge.setLineWidth(1);

		if (isSupportCollectionMarkers()) {
			int whalf = width / 2;
			createCollectionShape(container, new int[] { whalf - 2, height - 8, whalf - 2, height });
			createCollectionShape(container, new int[] { whalf, height - 8, whalf, height });
			createCollectionShape(container, new int[] { whalf + 2, height - 8, whalf + 2, height });

			String value = "false";
			EStructuralFeature feature = ((EObject)t).eClass().getEStructuralFeature("isCollection");
			if (feature!=null && t.eGet(feature)!=null)
				value = ((Boolean)t.eGet(feature)).toString();

			Graphiti.getPeService().setPropertyValue(container, Properties.COLLECTION_PROPERTY, value);
		}
		
		peService.createChopboxAnchor(container);
		AnchorUtil.addFixedPointAnchors(container, invisibleRect);
		boolean isImport = context.getProperty(DIUtils.IMPORT) != null;
		createDIShape(container, t, !isImport);
		layoutPictogramElement(container);
		
		this.prepareAddContext(context, width, height);
		this.getFeatureProvider().getAddFeature(context).add(context);
		
		return container;
	}

	private Shape createCollectionShape(ContainerShape container, int[] xy) {
		IPeService peService = Graphiti.getPeService();
		IGaService gaService = Graphiti.getGaService();
		Shape collectionShape = peService.createShape(container, false);
		Polyline line = gaService.createPolyline(collectionShape, xy);
		line.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		line.setLineWidth(1);
		line.setLineVisible(false);
		peService.setPropertyValue(collectionShape, Properties.HIDEABLE_PROPERTY, Boolean.toString(true));
		return collectionShape;
	}

	@Override
	public int getHeight() {
		return GraphicsUtil.DATA_HEIGHT;
	}

	@Override
	public int getWidth() {
		return GraphicsUtil.DATA_WIDTH;
	}
	
	protected void decorate(Polygon p) {
	}

	protected boolean isSupportCollectionMarkers() {
		return true;
	}
	
	public abstract String getName(T t);
}