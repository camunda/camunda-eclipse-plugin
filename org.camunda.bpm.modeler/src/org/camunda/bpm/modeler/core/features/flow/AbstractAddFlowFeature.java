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
package org.camunda.bpm.modeler.core.features.flow;

import static org.camunda.bpm.modeler.core.utils.ContextUtil.set;

import java.util.List;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.features.AbstractAddBpmnElementFeature;
import org.camunda.bpm.modeler.core.features.PropertyNames;
import org.camunda.bpm.modeler.core.layout.ConnectionService;
import org.camunda.bpm.modeler.core.layout.util.Layouter;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.ConnectionLabelUtil;
import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public abstract class AbstractAddFlowFeature<T extends BaseElement> extends AbstractAddBpmnElementFeature<T, FreeFormConnection> {
	
	public AbstractAddFlowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context instanceof IAddConnectionContext
				&& getBusinessObjectClass().isAssignableFrom(getBusinessObject(context).getClass());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.IAdd#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext ctx) {
		
		IPeService peService = Graphiti.getPeService();
		IGaService gaService = Graphiti.getGaService();

		T flow = getBusinessObject(ctx);
		IAddConnectionContext addContext = (IAddConnectionContext) ctx;

		Diagram diagram = getDiagram();
		
		BPMNEdge bpmnEdge = (BPMNEdge) ModelHandler.findDIElement(diagram, flow);
		FreeFormConnection connection = peService.createFreeFormConnection(diagram);
		
		// set anchors
		connection.setStart(addContext.getSourceAnchor());
		connection.setEnd(addContext.getTargetAnchor());

		// add initial bendpoints
		List<Point> initialBendpoints = ContextUtil.get(ctx, PropertyNames.CONNECTION_BENDPOINTS, List.class);
		if (initialBendpoints != null) {
			for (Point point : initialBendpoints) {
				connection.getBendpoints().add(point);
			}
		}
		
		if (bpmnEdge == null) {
			bpmnEdge = DIUtils.createDIEdge(connection, flow, diagram);
		}
		
		// link connection to edge and bpmn element
		link(connection, new Object[] { flow, bpmnEdge });
		
		createConnectionLine(connection);
		
		postAddHook(ctx, connection);
		
		if (!isImport(addContext)) {
			layoutAfterCreate(connection, addContext);
		}
		
		// create label if needed
		createLabel(addContext, connection);
		
		return connection;
	}

	protected void layoutAfterCreate(FreeFormConnection connection, IAddConnectionContext addContext) {
		Layouter.layoutAfterCreate(connection, getFeatureProvider());
	}

	protected abstract Class<? extends BaseElement> getBusinessObjectClass();

	protected Polyline createConnectionLine(Connection connection) {
		BaseElement be = BusinessObjectUtil.getFirstBaseElement(connection);
		Polyline connectionLine = Graphiti.getGaService().createPolyline(connection);
		StyleUtil.applyStyle(connectionLine, be);

		return connectionLine;
	}

	/**
	 * Creates a label for the newly created shape if any.
	 * 
	 * May be overridden by subclasses to perform actual actions.
	 * 
	 * @param context
	 * @param newShape
	 */
	protected void createLabel(IAddContext context, Connection connection) {
		
		// create label if the add shape feature wishes to do so
		if (isCreateExternalLabel()) {
			IAddContext addLabelContext = getAddLabelContext(context, connection);
			if (addLabelContext != null) {
				IAddFeature addFeature = getFeatureProvider().getAddFeature(addLabelContext);
				if (addFeature.canAdd(addLabelContext)) {
					addFeature.add(addLabelContext);
				}
			}
		}
	}
	
	/**
	 * Return a label add context for the given context and shape bounds.
	 * 
	 * @param context
	 * @param newShapeBounds
	 * @return
	 */
	protected IAddContext getAddLabelContext(IAddContext context, Connection connection) {
		AddContext labelAddContext = new AddContext();
		
		if (isImport(context)) {
			set(labelAddContext, DIUtils.IMPORT);
		}
		
		Object newObject = context.getNewObject();
		
		GraphicsUtil.prepareConnectionLabelAddContext(labelAddContext, connection, newObject);
		
		return labelAddContext;
	}
	
	protected boolean isCreateExternalLabel() {
		return false;
	}
}