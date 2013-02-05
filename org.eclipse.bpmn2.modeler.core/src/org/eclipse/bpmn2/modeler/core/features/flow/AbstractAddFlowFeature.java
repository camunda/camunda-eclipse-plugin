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
package org.eclipse.bpmn2.modeler.core.features.flow;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.ContextConstants;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.layout.util.SegmentInfo;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.bpmn2.modeler.ui.features.label.AddLabelFeature;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public abstract class AbstractAddFlowFeature<T extends BaseElement>
	extends AbstractAddBPMNShapeFeature<T> {
	
	public AbstractAddFlowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context instanceof IAddConnectionContext
				&& getBoClass().isAssignableFrom(getBusinessObject(context).getClass());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.IAdd#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context) {
		IPeService peService = Graphiti.getPeService();
		IGaService gaService = Graphiti.getGaService();

		T flow = getBusinessObject(context);
		IAddConnectionContext addConContext = (IAddConnectionContext) context;

		BPMNEdge bpmnEdge = (BPMNEdge) ModelHandler.findDIElement(flow);
		Connection connection = peService.createFreeFormConnection(getDiagram());

		Object importProp = context.getProperty(DIUtils.IMPORT_PROPERTY);
		if (importProp != null && (Boolean) importProp) {
			connection.setStart(addConContext.getSourceAnchor());
			connection.setEnd(addConContext.getTargetAnchor());
		} else {
			AnchorContainer sourceContainer = (AnchorContainer) addConContext.getSourceAnchor().eContainer();
			AnchorContainer targetContainer = (AnchorContainer) addConContext.getTargetAnchor().eContainer();
			Tuple<FixPointAnchor, FixPointAnchor> anchors = AnchorUtil.getSourceAndTargetBoundaryAnchors(
					sourceContainer, targetContainer, connection);

			connection.setStart(anchors.getFirst());
			connection.setEnd(anchors.getSecond());
		}

		if (bpmnEdge == null) {
			bpmnEdge = DIUtils.createDIEdge(connection, flow, getDiagram());
		}
		
		// link connection to edge and bpmn element
		link(connection, new Object[] { flow, bpmnEdge });
		PictogramElement labelShape = null;
		
		createConnectionLine(connection);
		hook(addConContext, connection, flow);
		
		if (ModelUtil.hasName(flow)) {

			/**
			 * in case of import we might have a label with missing bounds,
			 * getSegmentInfo will use the BPMNEdge waypoints to calculate the mid point
			 */
			ILocation midPoint = LayoutUtil.getConnectionMidPoint(connection);
			
			LayoutUtil.getConnectionLengthAtPoint(connection, ConversionUtil.point(midPoint.getX() + 10, midPoint.getY() + 10));
			
			int width = 50;
			int height = 30;
			
			AddLabelFeature addLabelFeature = new AddLabelFeature(getFeatureProvider());
			AddContext addLabelContext = new AddContext();
			addLabelContext.setTargetContainer(getDiagram());
			
			addLabelContext.putProperty(ContextConstants.WIDTH, width);
			addLabelContext.putProperty(ContextConstants.HEIGHT, height);
			addLabelContext.putProperty(ContextConstants.BUSINESS_OBJECT, flow);
			addLabelContext.putProperty(ContextConstants.CUSTOM_POSITION, true);
			
			addLabelContext.setX(midPoint.getX());
			addLabelContext.setY(midPoint.getY());
			
			if (addLabelFeature.canAdd(addLabelContext)) {
				labelShape = addLabelFeature.add(addLabelContext);
				GraphicsUtil.makeLabel(labelShape);
				link(labelShape, new Object[] { flow, bpmnEdge });
				
				peService.setPropertyValue(labelShape, UpdateBaseElementNameFeature.TEXT_ELEMENT, Boolean.toString(true));
				peService.setPropertyValue(labelShape, ContextConstants.LABEL_REF_LENGTH, Double.toString(labelReferenceLength));
				
				updatePictogramElement(labelShape);
			}
		}
		
		return connection;
	}
	
	@Override
	public int getHeight() {
		return -1;
	}

	@Override
	public int getWidth() {
		return -1;
	}

	protected abstract Class<? extends BaseElement> getBoClass();

	protected void hook(IAddContext context, Connection connection, BaseElement element) {
	}

	protected Polyline createConnectionLine(Connection connection) {
		BaseElement be = BusinessObjectUtil.getFirstBaseElement(connection);
		Polyline connectionLine = Graphiti.getGaService().createPolyline(connection);
		StyleUtil.applyStyle(connectionLine, be);

		return connectionLine;
	}
}