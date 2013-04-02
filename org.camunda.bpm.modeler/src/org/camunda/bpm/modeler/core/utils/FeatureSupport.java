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
package org.camunda.bpm.modeler.core.utils;

import static org.camunda.bpm.modeler.core.features.activity.AbstractAddActivityFeature.ACTIVITY_DECORATOR;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.features.DefaultMoveBPMNShapeFeature;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.BBox;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.core.layout.util.RectangleUtil.ResizeDiff;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class FeatureSupport {
	
	public static final String IS_HORIZONTAL_PROPERTY = "isHorizontal";

	public static boolean isTargetSubProcess(ITargetContext context) {
		return BusinessObjectUtil.containsElementOfType(context.getTargetContainer(), SubProcess.class);
	}

	public static boolean isTargetLane(ITargetContext context) {
		return isLane(context.getTargetContainer());
	}

	public static boolean isDiagram(PictogramElement shape) {
		Definitions definitions = BusinessObjectUtil.getFirstElementOfType(shape, Definitions.class);
		
		if (definitions!=null) {
			return true;
		}
		
		return BusinessObjectUtil.containsChildElementOfType(shape, Definitions.class);
	}
	
	public static boolean isLane(PictogramElement element) {
		return BusinessObjectUtil.containsElementOfType(element, Lane.class);
	}

	public static Lane getTargetLane(ITargetContext context) {
		PictogramElement element = context.getTargetContainer();
		return BusinessObjectUtil.getFirstElementOfType(element, Lane.class);
	}
	
	public static boolean isTargetParticipant(ITargetContext context) {
		return isParticipant(context.getTargetContainer());
	}

	public static boolean isParticipant(PictogramElement element) {
		return BusinessObjectUtil.containsElementOfType(element, Participant.class);
	}

	public static Participant getTargetParticipant(ITargetContext context) {
		PictogramElement element = context.getTargetContainer();
		return BusinessObjectUtil.getFirstElementOfType(element, Participant.class);
	}
	
	public static boolean isLaneOnTop(Lane lane) {
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}

	public static boolean isTargetLaneOnTop(ITargetContext context) {
		Lane lane = BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(), Lane.class);
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}

	public static boolean isHorizontal(ContainerShape container) {
		EObject parent = container.eContainer();
		if (parent instanceof PictogramElement) {
			// participant bands are always "vertical" so that
			// the label is drawn horizontally by the LayoutFeature
			if (BusinessObjectUtil.getFirstElementOfType((PictogramElement)parent, ChoreographyTask.class) != null)
				return false;
		}
		String v = Graphiti.getPeService().getPropertyValue(container, IS_HORIZONTAL_PROPERTY);
		if (v==null) {
			return Bpmn2Preferences.getInstance().isHorizontalDefault();
		}
		return Boolean.parseBoolean(v);
	}
	
	public static void setHorizontal(ContainerShape container, boolean isHorizontal) {
		Graphiti.getPeService().setPropertyValue(container, IS_HORIZONTAL_PROPERTY, Boolean.toString(isHorizontal));
		BPMNShape bs = BusinessObjectUtil.getFirstElementOfType(container, BPMNShape.class);
		if (bs!=null)
			bs.setIsHorizontal(isHorizontal);
	}
	
	public static boolean isHorizontal(IContext context) {
		Object v = context.getProperty(IS_HORIZONTAL_PROPERTY);
		if (v==null) {
			// TODO: get default orientation from preferences
			return true;
		}
		return (Boolean)v;
	}
	
	public static void setHorizontal(IContext context, boolean isHorizontal) {
		context.putProperty(IS_HORIZONTAL_PROPERTY, isHorizontal);
	}
	
	public static List<PictogramElement> getContainerChildren(ContainerShape container) {
		List<PictogramElement> list = new ArrayList<PictogramElement>();
		for (PictogramElement pe : container.getChildren()) {
			String value = Graphiti.getPeService().getPropertyValue(pe, ACTIVITY_DECORATOR);
			if (value!=null && "true".equals(value))
				continue;
			list.add(pe);
		}
		return list;
	}

	public static List<PictogramElement> getContainerDecorators(ContainerShape container) {
		List<PictogramElement> list = new ArrayList<PictogramElement>();
		for (PictogramElement pe : container.getChildren()) {
			String value = Graphiti.getPeService().getPropertyValue(pe, ACTIVITY_DECORATOR);
			if (value!=null && "true".equals(value))
				list.add(pe);
		}
		return list;
	}
	
	public static void setContainerChildrenVisible(ContainerShape container, boolean visible) {
		for (PictogramElement pe : getContainerChildren(container)) {
			pe.setVisible(visible);
			if (pe instanceof AnchorContainer) {
				AnchorContainer ac = (AnchorContainer)pe;
				for (Anchor a : ac.getAnchors()) {
					for (Connection c : a.getOutgoingConnections()) {
						c.setVisible(visible);
						for (ConnectionDecorator decorator : c.getConnectionDecorators()) {
							decorator.setVisible(visible);
						}
					}
				}
			}
		}
	}

	public static void recursiveResizeChildren(ContainerShape container, ResizeDiff diff, IFeatureProvider featureProvider) {

		Sector direction = diff.getResizeDirection();
		Point delta = diff.getResizeDelta();
		
		if (direction.isTop() || direction.isBottom()) { 
			recursiveResizeChildrenVertically(container, direction.isTop(), delta.getY(), featureProvider);
		}

		if (direction.isLeft() || direction.isRight()) {
			recursiveResizeChildrenHorizontally(container, direction.isLeft(), delta.getX(), featureProvider);
		}
	}
	
	public static void restoreFlowElementPositions(ContainerShape container, Map<Shape, IRectangle> preMoveBounds, IFeatureProvider featureProvider) {
		
		for (Map.Entry<Shape, IRectangle> entry : preMoveBounds.entrySet()) {
			
			Shape shape = entry.getKey();
			
			Point preMovePosition = point(entry.getValue());
			Point postMovePosition = point(LayoutUtil.getAbsoluteBounds(shape));
			
			int dx = preMovePosition.getX() - postMovePosition.getX();
			int dy = preMovePosition.getY() - postMovePosition.getY();
			
			moveShapeNoLayout(shape, point(dx, dy), featureProvider);
		}
	}

	private static void recursiveResizeChildrenHorizontally(ContainerShape container, boolean left, int dx, IFeatureProvider featureProvider) {
		
		List<Shape> childLanes = getChildLanes(container);
		if (childLanes.isEmpty()) {
			if (left) {
				moveChildren(container, point(-dx, 0), featureProvider);
			}
			
			return;
		}
		
		for (Shape childLane: childLanes) {
			
			// recurse into children
			recursiveResizeChildrenHorizontally((ContainerShape) childLane, left, dx, featureProvider);
			
			GraphicsAlgorithm childLaneGA = childLane.getGraphicsAlgorithm();
			
			int x = GraphicsUtil.PARTICIPANT_LABEL_OFFSET;
			int y = childLaneGA.getY();
			
			int newWidth = childLaneGA.getWidth() + (left ? -1 * dx : dx);

			Graphiti.getGaService().setWidth(childLaneGA, newWidth);
			Graphiti.getGaService().setLocation(childLaneGA, x, y);
		}
	}

	private static void recursiveResizeChildrenVertically(ContainerShape container, boolean top, int dy, IFeatureProvider featureProvider) {
		
		List<Shape> childLanes = getChildLanes(container);
		if (childLanes.isEmpty()) {
			if (top) {
				moveChildren(container, point(0, -dy), featureProvider);
			}
			
			return;
		}
			
		Shape resizeCandidate = null;
		if (top) {
			resizeCandidate = childLanes.get(0);
		} else {
			resizeCandidate = childLanes.get(childLanes.size() - 1);
		}
		
		recursiveResizeChildrenVertically((ContainerShape) resizeCandidate, top, dy, featureProvider);
		
		GraphicsAlgorithm resizeGA = resizeCandidate.getGraphicsAlgorithm();
		int newHeight = resizeGA.getHeight() + (top ? -dy : dy);
		
		Graphiti.getGaService().setHeight(resizeGA, newHeight);
		
		int m = 0;
		int y = 0;
		
		for (Shape childLane: childLanes) {
			GraphicsAlgorithm childLaneGA = childLane.getGraphicsAlgorithm();
			
			Graphiti.getGaService().setLocation(childLaneGA, childLaneGA.getX(), y - m);
			
			// to avoid a double border lanes have -1px margin bottom
			y += childLaneGA.getHeight() - m;
			m = 1;
		}
	}

	public static Map<Shape, IRectangle> buildFlowElementPositionMap(ContainerShape container) {
		
		HashMap<Shape, IRectangle> positionMap = new HashMap<Shape, IRectangle>();
		
		TreeIterator<EObject> contents = container.eAllContents();
		
		while (contents.hasNext()) {
			EObject element = contents.next();
			if (!isBpmn20Shape(element)) {
				continue;
			}
			
			ContainerShape shape = (ContainerShape) element;
			
			if (!isParticipant(shape) && !isLane(shape)) {
				positionMap.put(shape, LayoutUtil.getAbsoluteBounds(shape));
			}
		}
		
		return positionMap;
	}

	public static void resizeLaneSet(ContainerShape container) {
		
		ContainerShape parentContainer = container.getContainer();
		
		if (!isParticipant(parentContainer) && !isLane(parentContainer)) {
			// we have reached the top of a participant / lane nesting
			return;
		}
		
		int topResize = 0;
		
		List<Shape> childLanes = getChildLanes(parentContainer);
		
		GraphicsAlgorithm containerGa = container.getGraphicsAlgorithm();
		
		Shape sibling;
		GraphicsAlgorithm siblingGA;
		
		int idx = childLanes.indexOf(container);
		
		if (idx == 0) {
			topResize = containerGa.getY();
		}
		
		if (idx > 0) {
			
			// check for top resize
			
			sibling = childLanes.get(idx - 1);
			siblingGA = sibling.getGraphicsAlgorithm();
			
			// compensate 1px margin
			if (siblingGA.getY() + siblingGA.getHeight() != containerGa.getY() + 1) {
				topResize = containerGa.getY() - (siblingGA.getY() + siblingGA.getHeight());
			}
		}
		
		int m = 0; // mystical m
		int y = 0;
		
		for (Shape shape : childLanes) {
			GraphicsAlgorithm shapeGA = shape.getGraphicsAlgorithm();
			
			Graphiti.getGaService().setLocation(shapeGA, GraphicsUtil.PARTICIPANT_LABEL_OFFSET, y - m);

			// to avoid a double border lanes have -1px margin bottom
			y += shapeGA.getHeight() - m;
			m = 1;
		}
		
		GraphicsAlgorithm parentGa = parentContainer.getGraphicsAlgorithm();
		
		if (topResize != 0) {
			Graphiti.getGaService().setLocation(parentGa, parentGa.getX(), parentGa.getY() + topResize);
		}
		
		Graphiti.getGaService().setHeight(parentGa, y);
		
		// recurse into parent container
		resizeLaneSet(parentContainer);
	}
	
	public static void redrawLaneSet(ContainerShape container) {
		ContainerShape root = getRootContainer(container);
		resizeRecursively(root, container);
		postResizeFixLenghts(root);
		updateDI(root);
	}

	private static void updateDI(ContainerShape root) {
		DIUtils.updateDIShape(root);
	}

	public static ContainerShape getRootContainer(ContainerShape container) {
		ContainerShape parent = container.getContainer();
		EObject bo = BusinessObjectUtil.getFirstElementOfType(parent, BaseElement.class);
		if (bo != null && (bo instanceof Lane || bo instanceof Participant)) {
			return getRootContainer(parent);
		}
		return container;
	}

	private static IRectangle resize(ContainerShape container) {
		
		EObject elem = BusinessObjectUtil.getFirstElementOfType(container, BaseElement.class);
		IGaService service = Graphiti.getGaService();
		int height = 0;
		int width = container.getGraphicsAlgorithm().getWidth() - 30;
		boolean horz = isHorizontal(container);
		if (horz) {
			height = 0;
			width = container.getGraphicsAlgorithm().getWidth() - 30;
		}
		else {
			width = 0;
			height = container.getGraphicsAlgorithm().getHeight() - 30;
		}

		EList<Shape> children = container.getChildren();
		ECollections.sort(children, new SiblingLaneComparator());
		for (Shape s : children) {
			Object bo = BusinessObjectUtil.getFirstElementOfType(s, BaseElement.class);
			if (bo != null && (bo instanceof Lane || bo instanceof Participant) && !bo.equals(elem)) {
				GraphicsAlgorithm ga = s.getGraphicsAlgorithm();
				if (horz) {
					service.setLocation(ga, 30, height);
					height += ga.getHeight() - 1;
					if (ga.getWidth() >= width) {
						width = ga.getWidth();
					} else {
						service.setSize(ga, width, ga.getHeight());
					}
				}
				else {
					service.setLocation(ga, width, 30);
					width += ga.getWidth() - 1;
					if (ga.getHeight() >= height) {
						height = ga.getHeight();
					} else {
						service.setSize(ga, ga.getWidth(), height);
					}
				}
			}
		}

		GraphicsAlgorithm ga = container.getGraphicsAlgorithm();

		if (horz) {
			if (height == 0) {
				return rectangle(ga.getX(), ga.getY(), ga.getWidth(), ga.getHeight());
			} else {
				int newWidth = width + 30;
				int newHeight = height + 1;
				service.setSize(ga, newWidth, newHeight);
	
				for (Shape s : children) {
					GraphicsAlgorithm childGa = s.getGraphicsAlgorithm();
					if (childGa instanceof Text) {
						Text text = (Text)childGa;
						text.setAngle(-90);
						service.setLocationAndSize(text, 5, 0, 15, newHeight);
					} else if (childGa instanceof Polyline) {
						Polyline line = (Polyline) childGa;
						Point p0 = line.getPoints().get(0);
						Point p1 = line.getPoints().get(1);
						p0.setX(30); p0.setY(0);
						p1.setX(30); p1.setY(newHeight);
					}
				}
	
				return rectangle(ga.getX(), ga.getY(), newWidth, newHeight);
			}
		}
		else {
			if (width == 0) {
				return rectangle(ga.getX(), ga.getY(), ga.getWidth(), ga.getHeight());
			} else {
				int newWidth = width + 1;
				int newHeight = height + 30;
				service.setSize(ga, newWidth, newHeight);
	
				for (Shape s : children) {
					GraphicsAlgorithm childGa = s.getGraphicsAlgorithm();
					if (childGa instanceof Text) {
						Text text = (Text)childGa;
						text.setAngle(0);
						service.setLocationAndSize(text, 0, 5, newWidth, 15);
					} else if (childGa instanceof Polyline) {
						Polyline line = (Polyline) childGa;
						Point p0 = line.getPoints().get(0);
						Point p1 = line.getPoints().get(1);
						p0.setX(0); p0.setY(30);
						p1.setX(newWidth); p1.setY(30);
					}
				}
	
				return rectangle(ga.getX(), ga.getY(), newWidth, newHeight);
			}
		}
	}
	
	private static IRectangle resizeRecursively(ContainerShape root, ContainerShape resizedShape) {
		
		BaseElement elem = BusinessObjectUtil.getFirstElementOfType(root, BaseElement.class);
		IGaService service = Graphiti.getGaService();
		int foundContainers = 0;
		boolean horz = isHorizontal(root);

		BBox allBounds = new BBox(null, 0, 0);
		
		for (Shape s : root.getChildren()) {
			Object bo = BusinessObjectUtil.getFirstElementOfType(s, BaseElement.class);
			if (checkForResize(elem, s, bo)) {
				foundContainers += 1;
				IRectangle bounds = resizeRecursively((ContainerShape) s, resizedShape);
				if (bounds != null) {
					allBounds.addBounds(bounds);
				}
			}
		}

		if (foundContainers == 0) {
			GraphicsAlgorithm ga = root.getGraphicsAlgorithm();
			for (Shape s : root.getChildren()) {
				GraphicsAlgorithm childGa = s.getGraphicsAlgorithm();
				if (childGa instanceof Text) {
					if (horz) {
						Text text = (Text)childGa;
						text.setAngle(-90);
						service.setLocationAndSize(text, 5, 0, 15, ga.getHeight());
					}
					else {
						Text text = (Text)childGa;
						text.setAngle(0);
						service.setLocationAndSize(text, 0, 5, ga.getWidth(), 15);
					}
				} else if (childGa instanceof Polyline) {
					Polyline line = (Polyline) childGa;
					Point p0 = line.getPoints().get(0);
					Point p1 = line.getPoints().get(1);
					if (horz) {
						p0.setX(30); p0.setY(0);
						p1.setX(30); p1.setY(ga.getHeight());
					}
					else {
						p0.setX(0); p0.setY(30);
						p1.setX(ga.getWidth()); p1.setY(30);
					}
				}
			}
			return rectangle(ga.getX(), ga.getY(), ga.getWidth(), ga.getHeight());
		} else {
			return resize(root);
		}
	}

	/**
	 * One can only resize lanes and participants
	 */
	private static boolean checkForResize(BaseElement currentBo, Shape s, Object bo) {
		if (!(s instanceof ContainerShape)) {
			return false;
		}
		if (bo == null) {
			return false;
		}
		if (!(bo instanceof Lane || bo instanceof Participant)) {
			return false;
		}
		return !bo.equals(currentBo);
	}

	private static IRectangle getBounds(BBox boundingBox) {
		return boundingBox.getBounds();
	}

	private static void postResizeFixLenghts(ContainerShape root) {
		IGaService service = Graphiti.getGaService();
		BaseElement elem = BusinessObjectUtil.getFirstElementOfType(root, BaseElement.class);
		GraphicsAlgorithm ga = root.getGraphicsAlgorithm();
		int width = ga.getWidth() - 30;
		int height = ga.getHeight() - 30;
		boolean horz = isHorizontal(root);

		for (Shape s : root.getChildren()) {
			Object o = BusinessObjectUtil.getFirstElementOfType(s, BaseElement.class);
			if (checkForResize(elem, s, o)) {
				GraphicsAlgorithm childGa = s.getGraphicsAlgorithm();
				if (horz)
					service.setSize(childGa, width, childGa.getHeight());
				else
					service.setSize(childGa, childGa.getWidth(), height);
				postResizeFixLenghts((ContainerShape) s);
			}
		}
	}

	public static String getShapeValue(IPictogramElementContext context) {
		String value = null;

		PictogramElement pe = context.getPictogramElement();
		if (pe instanceof ContainerShape) {
			ContainerShape cs = (ContainerShape) pe;
			for (Shape shape : cs.getChildren()) {
				if (shape.getGraphicsAlgorithm() instanceof AbstractText) {
					AbstractText text = (AbstractText) shape.getGraphicsAlgorithm();
					value = text.getValue();
				}
			}
		}
		return value;
	}

	public static String getBusinessValue(IPictogramElementContext context) {
		Object o = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
		if (o instanceof FlowElement) {
			FlowElement e = (FlowElement) o;
			return e.getName();
		} else if (o instanceof TextAnnotation) {
			TextAnnotation a = (TextAnnotation) o;
			return a.getText();
		} else if (o instanceof Participant) {
			Participant p = (Participant) o;
			return p.getName();
		} else if (o instanceof Lane) {
			Lane l = (Lane) o;
			return l.getName();
		}
		return null;
	}

	public static Participant getTargetParticipant(ITargetContext context, ModelHandler handler) {
		if (context.getTargetContainer() instanceof Diagram) {
			return handler.getInternalParticipant();
		}

		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(), BaseElement.class);

		if (bo instanceof Participant) {
			return (Participant) bo;
		}

		return handler.getParticipant(bo);
	}

	public static Shape getShape(ContainerShape container, String property, String expectedValue) {
		IPeService peService = Graphiti.getPeService();
		Iterator<Shape> iterator = peService.getAllContainedShapes(container).iterator();
		while (iterator.hasNext()) {
			Shape shape = iterator.next();
			String value = peService.getPropertyValue(shape, property);
			if (value != null && value.equals(expectedValue)) {
				return shape;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends EObject> T getChildElementOfType(PictogramElement container, String property, String expectedValue, Class<T> clazz) {
		IPeService peService = Graphiti.getPeService();
		Iterator<PictogramElement> iterator = peService.getAllContainedPictogramElements(container).iterator();
		while (iterator.hasNext()) {
			PictogramElement pe = iterator.next();
			String value = peService.getPropertyValue(pe, property);
			if (value != null && value.equals(expectedValue) && clazz.isInstance(pe)) {
				return (T) pe;
			}
		}
		return null;
	}
	
	/**
	 * Returns a list of {@link Shape}s which contains an element to the
	 * assigned businessObjectClazz, i.e. the list contains {@link Shape}s
	 * which meet the following constraint:<br>
	 * <code>
	 * 	foreach child of root:<br>
	 *  BusinessObjectUtil.containsChildElementOfType(child, businessObjectClazz) == true
	 * </code>
	 * @param root
	 * @param businessObjectClazz
	 * @return
	 */
	public static List<Shape> getChildrenLinkedToType(ContainerShape root, Class<?> businessObjectClazz) {
		List<Shape> result = new ArrayList<Shape>();
		for (Shape currentShape : root.getChildren()) {
			if (BusinessObjectUtil.containsChildElementOfType(currentShape, businessObjectClazz)) {
				result.add(currentShape);
			}
		}
		return result;
	}
	
	public static Shape getNestedLastLaneInContainer(ContainerShape root) {
		
		Shape lane = getLastLaneInContainer(root);
		Shape nested = lane;
		
		while (nested != null) {
			nested = getLastLaneInContainer((ContainerShape) lane);
			if (nested != null) {
				lane = nested;
			}
		}
		
		return lane;
	}

	public static Shape getNestedFirstLaneInContainer(ContainerShape root) {
		
		Shape lane = getFirstLaneInContainer(root);
		Shape nested = lane;
		
		while (nested != null) {
			nested = getFirstLaneInContainer((ContainerShape) lane);
			if (nested != null) {
				lane = nested;
			}
		}
		
		return lane;
	}
	
	public static Shape getFirstLaneInContainer(ContainerShape root) {
		List<Shape> laneShapes = getChildrenLinkedToType(root, Lane.class);
		if (!laneShapes.isEmpty()) {
			Iterator<Shape> iterator = laneShapes.iterator();
			Shape result = iterator.next();
			GraphicsAlgorithm ga = result.getGraphicsAlgorithm();
			if (isHorizontal(root)) {
				while (iterator.hasNext()) {
					Shape currentShape = iterator.next();
					if (currentShape.getGraphicsAlgorithm().getY() < ga.getY()) {
						result = currentShape;
					}
				}
			} else {
				while (iterator.hasNext()) {
					Shape currentShape = iterator.next();
					if (currentShape.getGraphicsAlgorithm().getX() < ga.getX()) {
						result = currentShape;
					}
				}				
			}
			return (Shape) result;
		}
		return root;
	}
	
	public static Shape getLastLaneInContainer(ContainerShape root) {
		List<Shape> laneShapes = getChildrenLinkedToType(root, Lane.class);
		if (!laneShapes.isEmpty()) {
			Iterator<Shape> iterator = laneShapes.iterator();
			Shape result = iterator.next();
			GraphicsAlgorithm ga = result.getGraphicsAlgorithm();
			if (isHorizontal(root)) {
				while (iterator.hasNext()) {
					Shape currentShape = iterator.next();
					if (currentShape.getGraphicsAlgorithm().getY() > ga.getY()) {
						result = currentShape;
					}
				}
			} else {
				while (iterator.hasNext()) {
					Shape currentShape = iterator.next();
					if (currentShape.getGraphicsAlgorithm().getX() > ga.getX()) {
						result = currentShape;
					}
				}				
			}
			return (Shape) result;
		}
		return root;
	}
	
	public static ContainerShape getLaneBefore(ContainerShape container) {
		if (!BusinessObjectUtil.containsElementOfType(container, Lane.class)) {
			return null;
		}
		
		ContainerShape parentContainerShape = container.getContainer();
		if (parentContainerShape == null) {
			return null;
		}
		
		GraphicsAlgorithm ga = container.getGraphicsAlgorithm();
		int x = ga.getX();
		int y = ga.getY();
		boolean isHorizontal = isHorizontal(container);
		
		ContainerShape result = null;
		for (PictogramElement picElem : getChildrenLinkedToType(parentContainerShape, Lane.class)) {
			if (picElem instanceof ContainerShape && !picElem.equals(container)) {
				ContainerShape currentContainerShape = (ContainerShape) picElem;
				GraphicsAlgorithm currentGA = currentContainerShape.getGraphicsAlgorithm();
				if (isHorizontal) {
					if (currentGA.getY() < y) {
						if (result != null) {
							GraphicsAlgorithm resultGA = result.getGraphicsAlgorithm();
							if (resultGA.getY() < currentGA.getY()) {
								result = currentContainerShape;
							}
						} else {
							result = currentContainerShape;
						}
					}
				} else {
					if (currentGA.getX() < x) {
						if (result != null) {
							GraphicsAlgorithm resultGA = result.getGraphicsAlgorithm();
							if (resultGA.getX() < currentGA.getX()) {
								result = currentContainerShape;
							}
						} else {
							result = currentContainerShape;
						}
					}
				}
			}
		}
		return result;
	}
	
	public static ContainerShape getLaneAfter(ContainerShape container) {
		if (!BusinessObjectUtil.containsElementOfType(container, Lane.class)) {
			return null;
		}
		
		ContainerShape parentContainerShape = container.getContainer();
		if (parentContainerShape == null) {
			return null;
		}
		
		GraphicsAlgorithm ga = container.getGraphicsAlgorithm();
		int x = ga.getX();
		int y = ga.getY();
		boolean isHorizontal = isHorizontal(container);
		
		ContainerShape result = null;
		for (PictogramElement picElem : getChildrenLinkedToType(parentContainerShape, Lane.class)) {
			if (picElem instanceof ContainerShape && !picElem.equals(container)) {
				ContainerShape currentContainerShape = (ContainerShape) picElem;
				GraphicsAlgorithm currentGA = currentContainerShape.getGraphicsAlgorithm();
				if (isHorizontal) {
					if (currentGA.getY() > y) {
						if (result != null) {
							GraphicsAlgorithm resultGA = result.getGraphicsAlgorithm();
							if (resultGA.getY() > currentGA.getY()) {
								result = currentContainerShape;
							}
						} else {
							result = currentContainerShape;
						}
					}
				} else {
					if (currentGA.getX() > x) {
						if (result != null) {
							GraphicsAlgorithm resultGA = result.getGraphicsAlgorithm();
							if (resultGA.getX() > currentGA.getX()) {
								result = currentContainerShape;
							}
						} else {
							result = currentContainerShape;
						}
					}
				}
			}
		}
		return result;
	}
	
	public static boolean isSourceParticipant(IMoveShapeContext context) {
		return isParticipant(context.getSourceContainer());
	}

	public static boolean isSourceLane(IMoveShapeContext context) {
		return isLane(context.getSourceContainer());
	}

	public static boolean isTargetDiagram(ITargetContext context) {
		return isDiagram(context.getTargetContainer());
	}
	
	public static boolean isSourceDiagram(IMoveShapeContext context) {
		return isDiagram(context.getSourceContainer());
	}

	/**
	 * Move all children in the given container by delta to compensate the container movement
	 * 
	 * @param container
	 * @param delta
	 * @param featureProvider
	 */
	public static void moveChildren(ContainerShape container, Point delta, IFeatureProvider featureProvider) {
		moveShapes(new ArrayList<Shape>(container.getChildren()), delta, featureProvider);
	}

	public static void moveShapes(ArrayList<Shape> shapes, Point delta, IFeatureProvider featureProvider) {
		for (Shape shape : shapes) {
		
			if (!isBpmn20Shape(shape)) {
				continue;
			}
			
			moveShapeNoLayout(shape, delta, featureProvider);
		}
	}
	
	private static void moveShapeNoLayout(Shape shape, Point delta, IFeatureProvider featureProvider) {

		ILocation location = LayoutUtil.getRelativeBounds(shape);
		
		MoveShapeContext moveContext = new MoveShapeContext(shape);
		moveContext.setLocation(location.getX() + delta.getX(), location.getY() + delta.getY());
		moveContext.setSourceContainer(shape.getContainer());
		moveContext.setTargetContainer(shape.getContainer());
		
		ContextUtil.set(moveContext, DefaultMoveBPMNShapeFeature.SKIP_REPAIR_CONNECTIONS_AFTER_MOVE);
		ContextUtil.set(moveContext, DefaultMoveBPMNShapeFeature.SKIP_MOVE_BENDPOINTS);
		
		IMoveShapeFeature moveShapeFeature = featureProvider.getMoveShapeFeature(moveContext);
		
		executeFeature(moveShapeFeature, moveContext);
	}

	public static boolean isBpmn20Shape(EObject element) {
		if (element instanceof ContainerShape) {
			BaseElement be = BusinessObjectUtil.getFirstBaseElement((Shape) element);
			return be != null;
		} else {
			return false;
		}
	}

	/**
	 * Execute the given feature (null save) with the given context.
	 * 
	 * @param feature
	 * @param context
	 * 
	 * @throws IllegalArgumentException if the feature is null or it does not execute in the given context.
	 */
	public static void executeFeature(IFeature feature, IContext context) {

		if (feature == null) {
			throw new IllegalArgumentException(String.format("No executable feature for context %s", context));
		}
		
		if (feature.canExecute(context)) {
			feature.execute(context);
		} else {
			throw new IllegalArgumentException(String.format("Failed to execute move feature %s", feature));
		}
	}
	
	/**
	 * Returns the list of top level lanes in the correct order.
	 *  
	 * @param rootShape
	 * @return
	 */
	public static List<Shape> getTopLevelLanes(ContainerShape rootShape) {
		
		// get direct child lanes
		List<Shape> directChildLanes = getChildLanes(rootShape);
		
		List<Shape> result = new ArrayList<Shape>(directChildLanes);
		
		// recurse into nested child lanes
		for (Shape laneShape: directChildLanes) {

			if (laneShape instanceof ContainerShape) {
				List<Shape> nestedLanes = getTopLevelLanes((ContainerShape) laneShape);
				if (!nestedLanes.isEmpty()) {
					int laneIndex = result.indexOf(laneShape);
					result.remove(laneIndex);
					result.addAll(laneIndex, nestedLanes);
				}
			}
		}
		
		return result;
	}

	/**
	 * Get a list of ordered child lanes in the given root shape.
	 * 
	 * @param rootShape
	 * @return
	 */
	public static List<Shape> getChildLanes(ContainerShape rootShape) {
		
		List<Shape> childLanes = FeatureSupport.getChildrenLinkedToType(rootShape, Lane.class);
		if (childLanes.isEmpty()) {
			return Collections.emptyList();
		}
		
		// sort from top to bottom
		Collections.sort(childLanes, new Comparator<Shape>() {
			@Override
			public int compare(Shape a, Shape b) {
				int ya = a.getGraphicsAlgorithm().getY();
				int yb = b.getGraphicsAlgorithm().getY();
				
				return ((Integer) ya).compareTo(yb);
			}
		});
		
		return childLanes;
	}

	public static void eachLaneExecute(ContainerShape container, LaneSetOperation operation) {
		
		ContainerShape root = getRootContainer(container);
		
		recursiveEachLaneExecute(root, operation);
	}
	
	private static void recursiveEachLaneExecute(ContainerShape container, LaneSetOperation operation) {
		
		List<Shape> lanes = getChildLanes(container);
		
		operation.execute(container);
		
		for (Shape lane : lanes) {
			recursiveEachLaneExecute((ContainerShape) lane, operation);
		}
	}

	public interface LaneSetOperation {
		
		/**
		 * Executes an operation for all lanes in a lane set
		 * 
		 * @param lane
		 */
		public void execute(Shape lane);
	}
}
