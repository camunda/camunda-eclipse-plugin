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
import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveConnectionDecoratorFeature;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.MoveConnectionDecoratorContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.Diagram;
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

		Diagram diagram = getDiagram();
		
		BPMNEdge bpmnEdge = (BPMNEdge) ModelHandler.findDIElement(diagram, flow);
		Connection connection = peService.createFreeFormConnection(diagram);
		
		if (ContextUtil.is(context, DIUtils.IMPORT)) {
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
			bpmnEdge = DIUtils.createDIEdge(connection, flow, diagram);
		}
		
		// link connection to edge and bpmn element
		link(connection, new Object[] { flow, bpmnEdge });
		
		if (ModelUtil.hasName(flow)) {
			
			ConnectionDecorator labelDecorator = Graphiti.getPeService().createConnectionDecorator(connection, true, 0.5, true);
			Text text = gaService.createText(labelDecorator, ModelUtil.getName(flow));
			
			GraphicsUtil.makeLabel(labelDecorator);
			
			link(labelDecorator, new Object[] { flow, bpmnEdge });
			
			peService.setPropertyValue(labelDecorator, UpdateBaseElementNameFeature.TEXT_ELEMENT, Boolean.toString(true));
			StyleUtil.applyStyle(text, flow);
			
			BPMNLabel bpmnLabel = bpmnEdge.getLabel();
			
			// move after link if bpmnLabel is given
			if (bpmnLabel != null && bpmnLabel.getBounds() != null) {
				IRectangle decoratorBounds = LayoutUtil.getAbsoluteBounds(labelDecorator);

				int x = (int) bpmnLabel.getBounds().getX() - decoratorBounds.getX();
				int y = (int) bpmnLabel.getBounds().getY() - decoratorBounds.getY();
				
				MoveConnectionDecoratorContext ctx = new MoveConnectionDecoratorContext(labelDecorator, x, y, true);
				IMoveConnectionDecoratorFeature moveDecoratorFeature = getFeatureProvider().getMoveConnectionDecoratorFeature(ctx);
				
				if (moveDecoratorFeature.canExecute(ctx)) {
					moveDecoratorFeature.execute(ctx);
				}
			}
		}
		
		createConnectionLine(connection);
		hook(addConContext, connection, flow);

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