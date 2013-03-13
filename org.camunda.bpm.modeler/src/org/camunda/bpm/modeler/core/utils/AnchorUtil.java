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

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IAddBendpointFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IRemoveBendpointFeature;
import org.eclipse.graphiti.features.context.IAddBendpointContext;
import org.eclipse.graphiti.features.context.IRemoveBendpointContext;
import org.eclipse.graphiti.features.context.impl.AddBendpointContext;
import org.eclipse.graphiti.features.context.impl.RemoveBendpointContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.ICreateService;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.ILayoutService;
import org.eclipse.graphiti.services.IPeService;

public class AnchorUtil {

	public static final String BOUNDARY_FIXPOINT_ANCHOR = "boundary.fixpoint.anchor";

	// values for connection points
	public static final String CONNECTION_POINT = "connection.point"; //$NON-NLS-1$
	public static final String CONNECTION_POINT_KEY = "connection.point.key"; //$NON-NLS-1$
	public static final int CONNECTION_POINT_SIZE = 4;

	private static final IPeService peService = Graphiti.getPeService();
	private static final IGaService gaService = Graphiti.getGaService();
	private static final ICreateService createService = Graphiti.getCreateService();
	private static final ILayoutService layoutService = Graphiti.getLayoutService();
	
	public enum AnchorLocation {
		TOP("anchor.top"), BOTTOM("anchor.bottom"), LEFT("anchor.left"), RIGHT("anchor.right");

		private final String key;

		private AnchorLocation(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		public static AnchorLocation getLocation(String key) {
			for (AnchorLocation l : values()) {
				if (l.getKey().equals(key)) {
					return l;
				}
			}
			return null;
		}
	}

	public static class AnchorTuple {
		public FixPointAnchor sourceAnchor;
		public FixPointAnchor targetAnchor;
	}

	public static class BoundaryAnchor {
		public FixPointAnchor anchor;
		public AnchorLocation locationType;
		public ILocation location;
	}

	public static FixPointAnchor createAnchor(AnchorContainer ac, AnchorLocation loc, int x, int y) {
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		FixPointAnchor anchor = peService.createFixPointAnchor(ac);
		peService.setPropertyValue(anchor, BOUNDARY_FIXPOINT_ANCHOR, loc.getKey());
		anchor.setLocation(gaService.createPoint(x, y));
		gaService.createInvisibleRectangle(anchor);

		return anchor;
	}

	public static Map<AnchorLocation, BoundaryAnchor> getConnectionBoundaryAnchors(Shape connectionPointShape) {
		Map<AnchorLocation, BoundaryAnchor> map = new HashMap<AnchorLocation, BoundaryAnchor>(4);
		BoundaryAnchor a = new BoundaryAnchor();
		a.anchor = getConnectionPointAnchor(connectionPointShape);
		for (AnchorLocation al : AnchorLocation.values() ) {
			a.locationType = al;
			a.location = getConnectionPointLocation(connectionPointShape);
			map.put(a.locationType, a);
		}
		return map;
	}
	
	public static Map<AnchorLocation, BoundaryAnchor> getBoundaryAnchors(AnchorContainer ac) {
		Map<AnchorLocation, BoundaryAnchor> map = new HashMap<AnchorLocation, BoundaryAnchor>(4);
		
		if (ac instanceof FreeFormConnection) {
			// the anchor container is a Connection which does not have any predefined BoundaryAnchors
			// so we have to synthesize these by looking for connection point shapes owned by the connection
			for (Shape connectionPointShape : getConnectionPoints((FreeFormConnection)ac)) {
				// TODO: if there are multiple connection points, figure out which one to use
				return getConnectionBoundaryAnchors(connectionPointShape);
			}
		}
		else if (AnchorUtil.isConnectionPoint(ac)) {
			return getConnectionBoundaryAnchors((Shape)ac);
		}
		else {
			// anchor container is a ContainerShape - these already have predefined BoundaryAnchors
			Iterator<Anchor> iterator = ac.getAnchors().iterator();
			while (iterator.hasNext()) {
				Anchor anchor = iterator.next();
				String property = Graphiti.getPeService().getPropertyValue(anchor, BOUNDARY_FIXPOINT_ANCHOR);
				if (property != null && anchor instanceof FixPointAnchor) {
					BoundaryAnchor a = new BoundaryAnchor();
					a.anchor = (FixPointAnchor) anchor;
					a.locationType = AnchorLocation.getLocation(property);
					a.location = peService.getLocationRelativeToDiagram(anchor);
					map.put(a.locationType, a);
				}
			}
		}
		return map;
	}

	public static Anchor getAnchor(Shape shape, AnchorLocation anchorLocation) {

		EList<Anchor> anchors = shape.getAnchors();
		for (Anchor a: anchors) {
			for (Property p: a.getProperties()) {
				if (BOUNDARY_FIXPOINT_ANCHOR.equals(p.getKey())) {
					if (AnchorLocation.getLocation(p.getValue()) == anchorLocation) {
						return a;
					}
				}
			}
		}
		
		return null;
	}
	
	public static void addFixedPointAnchors(Shape shape) {
		
		IRectangle bounds = LayoutUtil.getAbsoluteBounds(shape);
		int w = bounds.getWidth();
		int h = bounds.getHeight();
		
		createAnchor(shape, AnchorLocation.TOP, w / 2, 0);
		createAnchor(shape, AnchorLocation.RIGHT, w, h / 2);
		createAnchor(shape, AnchorLocation.BOTTOM, w / 2, h);
		createAnchor(shape, AnchorLocation.LEFT, 0, h / 2);
	}
	
	public static void relocateFixPointAnchors(Shape shape) {
		relocateFixPointAnchors(shape, shape.getGraphicsAlgorithm().getWidth(), shape.getGraphicsAlgorithm().getHeight());
	}

	public static void relocateFixPointAnchors(Shape shape, int w, int h) {
		Map<AnchorLocation, BoundaryAnchor> anchors = getBoundaryAnchors(shape);

		FixPointAnchor anchor = anchors.get(AnchorLocation.TOP).anchor;
		anchor.setLocation(gaService.createPoint(w / 2, 0));

		anchor = anchors.get(AnchorLocation.RIGHT).anchor;
		anchor.setLocation(gaService.createPoint(w, h / 2));

		anchor = anchors.get(AnchorLocation.BOTTOM).anchor;
		anchor.setLocation(gaService.createPoint(w / 2, h));

		anchor = anchors.get(AnchorLocation.LEFT).anchor;
		anchor.setLocation(gaService.createPoint(0, h / 2));
	}

	// Connection points allow creation of anchors on FreeFormConnections
	
	public static Shape createConnectionPoint(IFeatureProvider fp,
			FreeFormConnection connection, ILocation location) {

		Shape connectionPointShape = null;

		Point bendPoint = null;
		Diagram diagram = fp.getDiagramTypeProvider().getDiagram();

		// TODO: fix this
		for (Point p : connection.getBendpoints()) {
			int px = p.getX();
			int py = p.getY();
			
			if (GraphicsUtil.isPointNear(p, point(location), 20)) {
				bendPoint = p;
				location.setX(px);
				location.setY(py);
			}

			for (Shape s : diagram.getChildren()) {
				if (isConnectionPointNear(s, point(location), 0)) {
					// this is the connection point on the target connection line
					// reuse this connection point if it's "close enough" to
					// target location otherwise create a new connection point
					if (isConnectionPointNear(s, point(location), 20)) {
						bendPoint = p;
						connectionPointShape = s;
						location.setX(px);
						location.setY(py);
					}
					break;
				}
			}
		}

		if (connectionPointShape == null) {
			connectionPointShape = createConnectionPoint(location, diagram);
			fp.link(connectionPointShape, connection);
			connection.getLink().getBusinessObjects().add(connectionPointShape);

			if (bendPoint == null) {
				bendPoint = createService.createPoint(location.getX(),
						location.getY());

				IAddBendpointContext addBpContext = new AddBendpointContext(connection, bendPoint.getX(), bendPoint.getY(), 0);
				IAddBendpointFeature addBpFeature = fp.getAddBendpointFeature(addBpContext);
				addBpFeature.addBendpoint(addBpContext);
			}
		}
		return connectionPointShape;
	}

	public static Shape createConnectionPoint(ILocation location, ContainerShape cs) {
		
		// create a circle for the connection point shape
		Shape connectionPointShape = createService.createShape(cs, true);
		peService.setPropertyValue(connectionPointShape, CONNECTION_POINT_KEY, CONNECTION_POINT);
		Ellipse ellipse = createService.createEllipse(connectionPointShape);
		int x = 0, y = 0;
		if (location != null) {
			x = location.getX();
			y = location.getY();
		}
		ellipse.setFilled(true);
		Diagram diagram = peService.getDiagramForPictogramElement(connectionPointShape);
		ellipse.setForeground(Graphiti.getGaService().manageColor(diagram, StyleUtil.CLASS_FOREGROUND));
		
		// create the anchor
		getConnectionPointAnchor(connectionPointShape);
		
		// set the location
		setConnectionPointLocation(connectionPointShape, x, y);
	
		return connectionPointShape;
	}

	public static boolean deleteConnectionPointIfPossible(IFeatureProvider fp,Shape connectionPointShape) {
		if (isConnectionPoint(connectionPointShape)) {
			Anchor anchor = getConnectionPointAnchor(connectionPointShape);
			List<Connection> allConnections = Graphiti.getPeService().getAllConnections(anchor);
			if (allConnections.size()==0) {
				// remove the bendpoint from target connection if there are no other connections going to it
				FreeFormConnection oldTargetConnection = (FreeFormConnection) connectionPointShape.getLink().getBusinessObjects().get(0);
				
				Point bp = null;
				for (Point p : oldTargetConnection.getBendpoints()) {
					if (AnchorUtil.isConnectionPointNear(connectionPointShape, p, 0)) {
						bp = p;
						break;
					}
				}
				
				if (bp != null) {
					IRemoveBendpointContext removeBpContext = new RemoveBendpointContext(oldTargetConnection, bp);
					IRemoveBendpointFeature removeBpFeature = fp.getRemoveBendpointFeature(removeBpContext);
					removeBpFeature.removeBendpoint(removeBpContext);
				}
				
				RemoveContext ctx = new RemoveContext(connectionPointShape);
				fp.getRemoveFeature(ctx).remove(ctx);
			}
		}
		return false;
	}
	
	public static FixPointAnchor getConnectionPointAnchor(Shape connectionPointShape) {
		if (connectionPointShape.getAnchors().size()==0) {
			FixPointAnchor anchor = createService.createFixPointAnchor(connectionPointShape);
			peService.setPropertyValue(anchor, CONNECTION_POINT_KEY, CONNECTION_POINT);
			
			// if the anchor doesn't have a GraphicsAlgorithm, GEF will throw a fit
			// so create an invisible rectangle for it
			createService.createInvisibleRectangle(anchor);
		}
		return (FixPointAnchor)connectionPointShape.getAnchors().get(0);
	}

	public static ILocation getConnectionPointLocation(Shape connectionPointShape) {
		ILocation location = GraphicsUtil.peService.getLocationRelativeToDiagram(connectionPointShape);
		int x = location.getX() + CONNECTION_POINT_SIZE / 2;
		int y = location.getY() + CONNECTION_POINT_SIZE / 2;
		location.setX(x);
		location.setY(y);
		return location;
	}
	
	public static void setConnectionPointLocation(Shape connectionPointShape, int x, int y) {
		
		if (connectionPointShape.getAnchors().size()==0) {
			// anchor has not been created yet - need to set both location AND size
			layoutService.setLocationAndSize(
					connectionPointShape.getGraphicsAlgorithm(),
					x - CONNECTION_POINT_SIZE / 2, y - CONNECTION_POINT_SIZE / 2,
					CONNECTION_POINT_SIZE, CONNECTION_POINT_SIZE);
		}
		else {
			// already created - just set the location
			layoutService.setLocation(
					connectionPointShape.getGraphicsAlgorithm(),
					x - CONNECTION_POINT_SIZE / 2, y - CONNECTION_POINT_SIZE / 2);
		}
		
		FixPointAnchor anchor = getConnectionPointAnchor(connectionPointShape);
		anchor.setLocation( Graphiti.getCreateService().createPoint(CONNECTION_POINT_SIZE / 2, CONNECTION_POINT_SIZE / 2) );
		layoutService.setLocation(
				anchor.getGraphicsAlgorithm(), 
				CONNECTION_POINT_SIZE / 2, CONNECTION_POINT_SIZE / 2);
	}
	
	public static List<Shape> getConnectionPoints(FreeFormConnection connection) {
		ArrayList<Shape> list = new ArrayList<Shape>();
		
		for (Object o : connection.getLink().getBusinessObjects()) {
			if (o instanceof AnchorContainer) {
				AnchorContainer c = (AnchorContainer)o;
				if (AnchorUtil.isConnectionPoint(c)) {
					list.add((Shape)c);
				}
			}
		}
		
		return list;
	}
	
	public static Shape getConnectionPointAt(FreeFormConnection connection, Point point) {
		for (Shape connectionPointShape : getConnectionPoints(connection)) {
			if (AnchorUtil.isConnectionPointNear(connectionPointShape, point, 0)) {
				return connectionPointShape;
			}
		}
		return null;
	}


	public static boolean isConnectionPoint(PictogramElement pe) {
		String value = Graphiti.getPeService().getPropertyValue(pe, CONNECTION_POINT_KEY);
		return CONNECTION_POINT.equals(value);
	}

	public static boolean isConnectionPointNear(PictogramElement pe, Point p, int dist) {
		if (isConnectionPoint(pe)) {
			
			Point p1 = point(
				pe.getGraphicsAlgorithm().getX() + CONNECTION_POINT_SIZE / 2, 
				pe.getGraphicsAlgorithm().getY() + CONNECTION_POINT_SIZE / 2);
			
			return GraphicsUtil.isPointNear(p1, p, dist);
		}
		return false;
	}
	
	public static FreeFormConnection getConnectionPointOwner(Shape connectionPointShape) {
		if (isConnectionPoint(connectionPointShape)) {
			return (FreeFormConnection)connectionPointShape.getLink().getBusinessObjects().get(0); 
		}
		return null;
	}

	/**
	 * Adds a chop box anchor to the given shape.
	 * 
	 * @param shape
	 */
	public static ChopboxAnchor addChopboxAnchor(ContainerShape shape) {
		return Graphiti.getPeService().createChopboxAnchor(shape);
	}
}