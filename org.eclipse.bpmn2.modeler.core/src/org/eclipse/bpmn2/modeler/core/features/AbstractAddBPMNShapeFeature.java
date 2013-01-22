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
package org.eclipse.bpmn2.modeler.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.dc.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.IExecutionInfo;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.ILayoutService;

public abstract class AbstractAddBPMNShapeFeature<T extends BaseElement>
	extends AbstractAddShapeFeature
	implements IBpmn2AddFeature<T> {

	public AbstractAddBPMNShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	protected BPMNShape findDIShape(BaseElement elem) {
		try {
			return (BPMNShape) ModelHandlerLocator.getModelHandler(getDiagram().eResource()).findDIElement(elem);
		} catch (IOException e) {
			Activator.logError(e);
		}
		return null;
	}
	
	protected BPMNShape createDIShape(Shape gShape, BaseElement elem, boolean applyDefaults) {
		return createDIShape(gShape, elem, findDIShape(elem), applyDefaults);
	}

	protected BPMNShape createDIShape(Shape shape, BaseElement elem, BPMNShape bpmnShape, boolean applyDefaults) {
		if (bpmnShape == null) {
			IRectangle bounds = LayoutUtil.getAbsoluteBounds(shape);
			bpmnShape = DIUtils.createDIShape(elem, bounds, getDiagram());
		}
		
		link(shape, new Object[] { elem, bpmnShape });
		
		if (applyDefaults) {
			Bpmn2Preferences.getInstance(bpmnShape.eResource()).applyBPMNDIDefaults(bpmnShape, null);
		}
		
		return bpmnShape;
	}
	
	protected void prepareAddContext(IAddContext context, int width, int height) {
		context.putProperty(ContextConstants.LABEL_CONTEXT, true);
		context.putProperty(ContextConstants.WIDTH, width);
		context.putProperty(ContextConstants.HEIGHT, height);
		context.putProperty(ContextConstants.BUSINESS_OBJECT, getBusinessObject(context));
	}
	
	protected void adjustLocation(IAddContext context, int width, int height) {
		if (context.getProperty(DIUtils.IMPORT_PROPERTY) != null) {
			return;
		}
		
		int x = context.getX();
		int y = context.getY();
		((AddContext)context).setWidth(width);
		((AddContext)context).setHeight(height);
		
		Connection connection = context.getTargetConnection();
		if (connection!=null) {
			// if the drop target is a connection line, adjust the context
			// x or y so that the point lies on the line instead of just near it.
			Anchor a0 = connection.getStart();
			Anchor a1 = connection.getEnd();
			
			double x0 = getRelativeLocationX(a0);
			double y0 = getRelativeLocationY(a0);
			double x1 = getRelativeLocationX(a1);
			double y1 = getRelativeLocationY(a1);
			
			if (x0 != x1) {
				double m = (y1 - y0) / (x1 - x0);
				double b = y0 - m * x0;
				int y2 = (int)(m * x + b);
				// because of roundoff errors when the slope is nearly vertical, the
				// adjusted y may be way off; in this case, adjust the x coordinate instead
				if (Math.abs(m) > 100) {
					x = (int)((y - b) / m);
				}
				else {
					y = y2;
				}
				
				// [x,y] is now the correct location on the connection line of the Activity's
				// center point: calculate new location of the Activity figure.
			}
			else {
				// vertical line: place drop x == line's x
				x = (int)x0;
			}
			
			// TODO: do we want to keep the connection bendpoints?
			if (connection instanceof FreeFormConnection) {
				FreeFormConnection ffc = (FreeFormConnection)connection;
				ffc.getBendpoints().clear();
				DIUtils.updateDIEdge(connection);
			}
		}
		y -= height/2;
		x -= width / 2;
		((AddContext)context).setY(y);
		((AddContext)context).setX(x);
	}
	
	private double getRelativeLocationX(Anchor anchor) {
		double result = 0.0;
		if (anchor instanceof FixPointAnchor) {
			FixPointAnchor fpa = (FixPointAnchor) anchor;
			IRectangle gaBoundsForAnchor = Graphiti.getPeService().getGaBoundsForAnchor(anchor);
			result = gaBoundsForAnchor.getX() + fpa.getLocation().getX();
			
			AnchorContainer anchorContainer = anchor.getParent();
			if (anchorContainer instanceof Shape) {
				Shape shape = (Shape) anchorContainer;
				result = result + shape.getGraphicsAlgorithm().getX();
			}
		}
		return result;
	}
	
	private double getRelativeLocationY(Anchor anchor) {
		double result = 0.0;
		if (anchor instanceof FixPointAnchor) {
			FixPointAnchor fpa = (FixPointAnchor) anchor;
			IRectangle gaBoundsForAnchor = Graphiti.getPeService().getGaBoundsForAnchor(anchor);
			result = gaBoundsForAnchor.getY() + fpa.getLocation().getY();
			
			AnchorContainer anchorContainer = anchor.getParent();
			if (anchorContainer instanceof Shape) {
				Shape shape = (Shape) anchorContainer;
				result = result + shape.getGraphicsAlgorithm().getY();
			}
		}
		return result;
	}

	protected void splitConnection(IAddContext context, ContainerShape containerShape) {
		if (context.getProperty(DIUtils.IMPORT_PROPERTY) != null) {
			return;
		}
		
		Object newObject = getBusinessObject(context);
		Connection connection = context.getTargetConnection();
		if (connection!=null) {
			// determine how to split the line depending on where the new object was dropped:
			// the longer segment will remain the original connection, and a new connection
			// will be created for the shorter segment
			ILayoutService layoutService = Graphiti.getLayoutService();
			Anchor a0 = connection.getStart();
			Anchor a1 = connection.getEnd();
			double x0 = layoutService.getLocationRelativeToDiagram(a0).getX();
			double y0 = layoutService.getLocationRelativeToDiagram(a0).getY();
			double x1 = layoutService.getLocationRelativeToDiagram(a1).getX();
			double y1 = layoutService.getLocationRelativeToDiagram(a1).getY();
			double dx = x0 - context.getX();
			double dy = y0 - context.getY();
			double len0 = Math.sqrt(dx*dx + dy*dy);
			dx = context.getX() - x1;
			dy = context.getY() - y1;
			double len1 = Math.sqrt(dx*dx + dy*dy);

			AnchorContainer oldSourceContainer = connection.getStart().getParent();
			AnchorContainer oldTargetContainer = connection.getEnd().getParent();
			BaseElement baseElement = BusinessObjectUtil.getFirstElementOfType(connection, BaseElement.class);
			ILocation targetLocation = layoutService.getLocationRelativeToDiagram(containerShape);
			
			ReconnectionContext rc;
			Tuple<FixPointAnchor, FixPointAnchor> anchors;
			
			if (newObject instanceof StartEvent || len0 < len1) {
				anchors = AnchorUtil.getSourceAndTargetBoundaryAnchors(containerShape, oldTargetContainer, connection);
				rc = new ReconnectionContext(connection, connection.getStart(), anchors.getFirst(), targetLocation);
				rc.setReconnectType(ReconnectionContext.RECONNECT_SOURCE);
				rc.setTargetPictogramElement(containerShape);
			}
			else {
				anchors = AnchorUtil.getSourceAndTargetBoundaryAnchors(oldSourceContainer, containerShape, connection);
				rc = new ReconnectionContext(connection, connection.getEnd(), anchors.getSecond(), targetLocation);
				rc.setReconnectType(ReconnectionContext.RECONNECT_TARGET);
				rc.setTargetPictogramElement(containerShape);
			}
			IReconnectionFeature rf = getFeatureProvider().getReconnectionFeature(rc);
			rf.reconnect(rc);
			
			if (!(newObject instanceof EndEvent) && !(newObject instanceof StartEvent)) {
				// connection = get create feature, create connection
				CreateConnectionContext ccc = new CreateConnectionContext();
				if (len0 < len1) {
					ccc.setSourcePictogramElement(oldSourceContainer);
					ccc.setTargetPictogramElement(containerShape);
					anchors = AnchorUtil.getSourceAndTargetBoundaryAnchors(oldSourceContainer, containerShape, connection);
					ccc.setSourceAnchor(anchors.getFirst());
					ccc.setTargetAnchor(anchors.getSecond());
				}
				else {
					ccc.setSourcePictogramElement(containerShape);
					ccc.setTargetPictogramElement(oldTargetContainer);
					anchors = AnchorUtil.getSourceAndTargetBoundaryAnchors(containerShape, oldTargetContainer, connection);
					ccc.setSourceAnchor(anchors.getFirst());
					ccc.setTargetAnchor(anchors.getSecond());
				}
				
				Connection newConnection = null;
				for (ICreateConnectionFeature cf : getFeatureProvider().getCreateConnectionFeatures()) {
					if (cf instanceof AbstractCreateFlowFeature) {
						AbstractCreateFlowFeature acf = (AbstractCreateFlowFeature) cf;
						if (acf.getBusinessObjectClass().isInstance(baseElement)) {
							newConnection = acf.create(ccc);
							DIUtils.updateDIEdge(newConnection);
							break;
						}
					}
				}
			}
			DIUtils.updateDIEdge(connection);
		}
	}
	
	protected int getHeight(IAddContext context) {
		return context.getHeight() > 0 ? context.getHeight() :
			(isHorizontal(context) ? getHeight() : getWidth());
	}
	
	protected int getWidth(IAddContext context) {
		return context.getWidth() > 0 ? context.getWidth() :
			(isHorizontal(context) ? getWidth() : getHeight());
	}

	protected boolean isHorizontal(ITargetContext context) {
		if (context.getProperty(DIUtils.IMPORT_PROPERTY) == null) {
			// not importing - set isHorizontal to be the same as parent Pool
			if (FeatureSupport.isTargetParticipant(context)) {
				Participant targetParticipant = FeatureSupport.getTargetParticipant(context);
				BPMNShape participantShape = findDIShape(targetParticipant);
				if (participantShape!=null)
					return participantShape.isIsHorizontal();
			}
			else if (FeatureSupport.isTargetLane(context)) {
				Lane targetLane = FeatureSupport.getTargetLane(context);
				BPMNShape laneShape = findDIShape(targetLane);
				if (laneShape!=null)
					return laneShape.isIsHorizontal();
			}
		}
		return Bpmn2Preferences.getInstance().isHorizontalDefault();
	}
	
	public abstract int getHeight();
	public abstract int getWidth();

	@Override
	public T getBusinessObject(IAddContext context) {
		Object businessObject = context.getProperty(ContextConstants.BUSINESS_OBJECT);
		if (businessObject instanceof BaseElement)
			return (T)businessObject;
		return (T)context.getNewObject();
	}

	@Override
	public void putBusinessObject(IAddContext context, T businessObject) {
		context.putProperty(ContextConstants.BUSINESS_OBJECT, businessObject);
	}

	@Override
	public void postExecute(IExecutionInfo executionInfo) {
	}
}