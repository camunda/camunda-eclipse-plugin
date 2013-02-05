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
package org.eclipse.bpmn2.modeler.core.features.lane;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.AbstractAddBpmnShapeFeature;
import org.eclipse.bpmn2.modeler.core.features.DefaultMoveBPMNShapeFeature;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ContextUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddLaneFeature extends AbstractAddBpmnShapeFeature<Lane> {
	
	public static final int DEFAULT_LANE_WIDTH = 600;
	public static final int DEFAULT_LANE_HEIGHT = 100;

	public AddLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean isLane = getBusinessObject(context) instanceof Lane;
		boolean intoLane = FeatureSupport.isTargetLane(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		return isLane && (intoLane || intoParticipant);
	}

	@Override
	protected ContainerShape createPictogramElement(IAddContext context, IRectangle bounds) {

		// adding a lane is a two step process  
		// we add the basic shape here and will
		// add the text + flow elements later in #postAddHook
		Lane lane = getBusinessObject(context);
		
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		
		int x = bounds.getX();
		int y = bounds.getY();
		int width = bounds.getWidth();
		int height = bounds.getHeight();
		
		ContainerShape newShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle rect = gaService.createRectangle(newShape);
		
		StyleUtil.applyStyle(rect, lane);
		
		gaService.setLocationAndSize(rect, x, y, width, height); 
		
		return newShape;
	}
	
	@Override
	protected void postAddHook(IAddContext context, ContainerShape newLaneShape) {
		super.postAddHook(context, newLaneShape);
		
		Lane lane = getBusinessObject(context);

		IPeService peService = Graphiti.getPeService();
		
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(newLaneShape, BPMNShape.class);
		
		boolean isImport = isImport(context);

		BaseElement targetBaseElement = null;
		
		// move children of lane or participant target container
		List<FlowNode> newLaneFlowNodeRefs = lane.getFlowNodeRefs();
		
		if (FeatureSupport.isTargetLane(context)) {
			Lane targetLane = FeatureSupport.getTargetLane(context);
			targetBaseElement = targetLane;
			
			newLaneFlowNodeRefs.addAll(targetLane.getFlowNodeRefs());
			targetLane.getFlowNodeRefs().clear();
		} else
		if (FeatureSupport.isTargetParticipant(context)) {
			Participant targetParticipant = FeatureSupport.getTargetParticipant(context);
			Process targetProcess = targetParticipant.getProcessRef();
			targetBaseElement = targetParticipant;
			
			if (getNumberOfLanes(context) == 1) { // this is the first lane of the participant, move flow nodes
				moveFlowNodes(targetProcess, lane);
			}
		} else {
			throw new IllegalArgumentException("May only add lanes on pools or other lanes");
		}

		// update horizontal flag from parent shape
		if (!isImport) {
			BPMNShape targetBpmnShape = findDIShape(targetBaseElement);
			if (targetBpmnShape != null) {
				bpmnShape.setIsHorizontal(targetBpmnShape.isIsHorizontal());
			}
		}
		
		boolean horizontal = bpmnShape.isIsHorizontal();
		FeatureSupport.setHorizontal(newLaneShape, horizontal);
		
		// move shapes contained in target container
		for (Shape containedFlowNodeShape : getFlowNodeShapes(context, lane)) {
			Graphiti.getPeService().sendToFront(containedFlowNodeShape);
			containedFlowNodeShape.setContainer(newLaneShape);
		}
		
		// add text
		createLaneLabel(newLaneShape, lane, horizontal);

		ContainerShape newShapeContainer = newLaneShape.getContainer();

		if (!isImport) {
			FeatureSupport.redraw(newShapeContainer);
		}
		
		peService.sendToBack(newLaneShape);
		peService.sendToBack(newShapeContainer);
		
		compensateShapeMovements(newLaneShape);
	}

	@Override
	protected void updateAndLayout(ContainerShape newShape) {
		// no updating / layouting for lanes
	}
	
	private void createLaneLabel(ContainerShape newShape, Lane lane, boolean horizontal) {
		
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		
		IRectangle newShapeBounds = LayoutUtil.getRelativeBounds(newShape);

		int width = newShapeBounds.getWidth();
		int height = newShapeBounds.getHeight();
		
		Shape textShape = peService.createShape(newShape, false);
		Text text = gaService.createText(textShape, lane.getName());
		StyleUtil.applyStyle(text, lane);
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		if (horizontal) {
			text.setAngle(-90);
			gaService.setLocationAndSize(text, 0, 0, 15, height);
		} else {
			gaService.setLocationAndSize(text, 0, 0, width, 15);
		}
		
		link(textShape, lane);
	}

	private void compensateShapeMovements(ContainerShape shape) {
		
		ContainerShape parentShape = shape.getContainer();

		IRectangle shapeBounds = LayoutUtil.getAbsoluteBounds(shape);
		IRectangle parentShapeBounds = LayoutUtil.getAbsoluteBounds(parentShape);
		
		Point boundsDiff = point(parentShapeBounds.getX() - shapeBounds.getX(), parentShapeBounds.getY() - shapeBounds.getY());
		
		List<Shape> containedShapes = new ArrayList<Shape>(shape.getChildren());
		
		for (PictogramElement e: containedShapes) {
			if (e instanceof ContainerShape) {
				ContainerShape c = (ContainerShape) e;
				
				IRectangle cBounds = LayoutUtil.getRelativeBounds(c);
				
				MoveShapeContext moveContext = new MoveShapeContext(c);
				
				// container
				moveContext.setTargetContainer(c.getContainer());
				moveContext.setSourceContainer(c.getContainer());
				
				// delta
				moveContext.setDeltaX(boundsDiff.getX());
				moveContext.setDeltaY(boundsDiff.getY());
				
				// relative coordinates
				moveContext.setX(cBounds.getX() + boundsDiff.getX());
				moveContext.setY(cBounds.getY() + boundsDiff.getY());

				moveContext.putProperty(DefaultMoveBPMNShapeFeature.SKIP_RECONNECT_AFTER_MOVE, false);
				moveContext.putProperty(DefaultMoveBPMNShapeFeature.SKIP_MOVE_BENDPOINTS, true);
				moveContext.putProperty(DefaultMoveBPMNShapeFeature.SKIP_MOVE_LABEL, true);
				
				IMoveShapeFeature moveShapeFeature = getFeatureProvider().getMoveShapeFeature(moveContext);
				
				// need to execute can move shape first, 
				// to initialize the move feature with the correct
				if (moveShapeFeature.canMoveShape(moveContext)) {
					
					// execute move
					moveShapeFeature.execute(moveContext);
				}
			}
		}
	}

	private void moveFlowNodes(Process targetProcess, Lane lane) {
		for (FlowElement element : targetProcess.getFlowElements()) {
			if (element instanceof FlowNode) {
				lane.getFlowNodeRefs().add((FlowNode) element);
			}
		}
	}

	private List<Shape> getFlowNodeShapes(IAddContext context, Lane lane) {
		List<FlowNode> nodes = lane.getFlowNodeRefs();
		List<Shape> shapes = new ArrayList<Shape>();
		for (Shape s : context.getTargetContainer().getChildren()) {
			Object bo = getBusinessObjectForPictogramElement(s);
			if (bo != null && nodes.contains(bo)) {
				shapes.add(s);
			}
		}
		return shapes;
	}

	private int getNumberOfLanes(ITargetContext context) {
		ContainerShape targetContainer = context.getTargetContainer();
		Object bo = getBusinessObjectForPictogramElement(targetContainer);
		if (bo instanceof Lane) {
			Lane lane = (Lane) bo;
			return lane.getChildLaneSet().getLanes().size();
		} else if (bo instanceof Participant) {
			List<LaneSet> laneSets = ((Participant) bo).getProcessRef().getLaneSets();
			if (laneSets.size() > 0) {
				return laneSets.get(0).getLanes().size();
			}
			return laneSets.size();
		} else if (bo instanceof SubProcess) {
			List<LaneSet> laneSets = ((SubProcess) bo).getLaneSets();
			if (laneSets.size() > 0) {
				return laneSets.get(0).getLanes().size();
			}
			return laneSets.size();
		}
		return 0;
	}
	
	private Bounds getPreviouslyAddedLaneBounds(IAddContext context) {
		EObject bo = (EObject) getBusinessObjectForPictogramElement(context.getTargetContainer());
		if (bo instanceof Participant) {
			List<LaneSet> laneSets = ((Participant) bo).getProcessRef().getLaneSets();
			List<Lane> lanes = null;
			
			if (laneSets.size() > 0 && laneSets.get(0).getLanes().size() > 1) {
				lanes = laneSets.get(0).getLanes();
				Lane lane = lanes.get(lanes.size() - 2); // get the lane created before, current lane is already included
				BPMNShape laneShape = findDIShape(lane);
				Bounds bounds = laneShape.getBounds();
				return bounds;
			}
		}
		return null;
	}
	
	
	@Override
	protected int getHeight(IAddContext context) {
		if (isImport(context)) {
			return context.getHeight();
		}
		
		if (context.getTargetContainer() instanceof Diagram) {
			return getDefaultHeight();
		}
		int height = context.getTargetContainer().getGraphicsAlgorithm().getHeight();
		
		Bounds previousBounds = getPreviouslyAddedLaneBounds(context);
		if (previousBounds != null) {
			height = (int) previousBounds.getHeight();
		}
		
		return height;
	}
	
	@Override
	public int getWidth(IAddContext context) {
		if (isImport(context)) {
			return context.getWidth();
		}
		
		if (context.getTargetContainer() instanceof Diagram) {
			return getDefaultWidth();
		}
		int width = context.getTargetContainer().getGraphicsAlgorithm().getWidth() - 30;
		
		Bounds bounds = getPreviouslyAddedLaneBounds(context);
		if (bounds != null) {
			width = (int) bounds.getWidth();
		}
		
		return width;
	}

	@Override
	public int getDefaultHeight() {
		return DEFAULT_LANE_HEIGHT;
	}

	@Override
	public int getDefaultWidth() {
		return DEFAULT_LANE_WIDTH;
	}
	
	@Override
	protected boolean isCreateExternalLabel() {
		return false;
	}
}