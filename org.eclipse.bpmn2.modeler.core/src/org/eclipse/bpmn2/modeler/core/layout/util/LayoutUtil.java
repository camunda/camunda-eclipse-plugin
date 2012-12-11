package org.eclipse.bpmn2.modeler.core.layout.util;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.location;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.vector;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.layout.ConnectionReconnectionContext;
import org.eclipse.bpmn2.modeler.core.layout.LayoutingException;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.internal.datatypes.impl.LocationImpl;
import org.eclipse.graphiti.internal.datatypes.impl.RectangleImpl;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.internal.services.IGefService;

public class LayoutUtil {
	
	/**
	 * evolutionary developed value to switch between the AboveBeneath and LeftRight Strategy for gateways 
	 */
	public static final double MAGIC_VALUE = 0.90;
	
	/**
	 * evolutionary developed value to decide if we should layout a connection, assumption: we suck at layouting long connections
	 */
	public static final double MAGIC_LENGTH = 700;
	
	
	public enum Sector {
		
		TOP_LEFT(true, false, true, false),
		LEFT(true, false, false, false),
		BOTTOM_LEFT(true, false, false, true),
		BOTTOM(false, false, false, true),
		BOTTOM_RIGHT(false, true, false, true),
		RIGHT(false, true, false, false),
		TOP_RIGHT(false, true, true, false),
		TOP(false, false, true, false),
		UNDEFINED(false, false, false, false);
		
		private boolean left;
		private boolean right;
		private boolean above;
		private boolean beneath;

		private Sector(boolean left, boolean right, boolean above, boolean beneath) {
			if (left && right) {
				throw new IllegalArgumentException("Cannot be left and right at the same time");
			}
			
			if (above && beneath) {
				throw new IllegalArgumentException("Cannot be above and beneath at the same time");
			}
			
			this.left = left;
			this.right = right;
			this.above = above;
			this.beneath = beneath;
		}
		
		public static Sector fromBooleans(boolean left, boolean right, boolean above, boolean beneath) {
			if (left && above) {
				return Sector.TOP_LEFT;
			} else if (left && beneath) {
				return Sector.BOTTOM_LEFT;
			} else if (left) {
				return Sector.LEFT;
			} else if (right && above) {
				return Sector.TOP_RIGHT;
			} else if (right && beneath) {
				return Sector.BOTTOM_RIGHT;
			} else if (right) {
				return Sector.RIGHT;
			} else if(above) {
				return Sector.TOP;
			} else if(beneath) {
				return Sector.BOTTOM;
			} else {
				return Sector.UNDEFINED;
			}
		}
		
		public boolean isAbove() {
			return above;
		}
		
		public boolean isBeneath() {
			return beneath;
		}
		
		public boolean isLeft() {
			return left;
		}
		
		public boolean isRight() {
			return right;
		}
	}
	
	public static Sector getEndShapeSector(FreeFormConnection connection) {
		Shape startShape = (Shape) connection.getStart().getParent();
		Shape endShape = (Shape) connection.getEnd().getParent();
		
		boolean left = LayoutUtil.isLeftToStartShape(startShape, endShape); 
		boolean right = LayoutUtil.isRightToStartShape(startShape, endShape);
		boolean above = LayoutUtil.isAboveStartShape(startShape, endShape);
		boolean beneath = LayoutUtil.isBeneathStartShape(startShape, endShape);
		
		return Sector.fromBooleans(left, right, above, beneath);
	}
	
	public static Sector getBoundaryEventRelativeSector(Shape boundaryEventShape) {
		EObject element = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(boundaryEventShape);
		if (!(element instanceof BoundaryEvent)) {
			throw new IllegalArgumentException("Can only extract relative sector from " + BoundaryEvent.class.getName());
		}
		for (Property prop : boundaryEventShape.getProperties()) {
			if (prop.getKey().equals("boundary.event.relative.pos")) {
				String value = prop.getValue();
				
				if (value.equals("positiononline:XY:TOP_LEFT")) {
					return Sector.TOP_LEFT;
				} else if (value.equals("positiononline:Y:TOP")) {
					return Sector.TOP;
				} else if (value.equals("positiononline:XY:TOP_RIGHT")) {
					return Sector.TOP_RIGHT;
				} else if (value.equals("positiononline:X:RIGHT")) {
					return Sector.RIGHT;
				} else if (value.equals("positiononline:XY:BOTTOM_RIGHT")) {
					return Sector.BOTTOM_RIGHT;
				} else if (value.equals("positiononline:Y:BOTTOM")) {
					return Sector.BOTTOM;
				} else if (value.equals("positiononline:XY:BOTTOM_LEFT")) {
					return Sector.BOTTOM_LEFT;
				} else if (value.equals("positiononline:X:LEFT")) {
					return Sector.LEFT;
				}
			}
		}
		return Sector.UNDEFINED;
	}
	
	public static BaseElement getSourceBaseElement(FreeFormConnection connection) {
		return (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(connection.getStart().getParent());	
	}
	
	public static BaseElement getTargetBaseElement(FreeFormConnection connection) {
		return (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(connection.getEnd().getParent());	
	}
	
	public static Shape getStartShape (Connection connection) {
		return (Shape) connection.getStart().getParent();
	}
	
	public static Shape getEndShape (Connection connection) {
		return (Shape) connection.getStart().getParent();
	}
	
	public static boolean anchorEqual(Anchor some, Anchor that) {
		
		// this case happens, when you reconnect the connection manually with a shape
		// so in that case it is not equal to the assigned object 'that'
		if (some instanceof ChopboxAnchor) {
			return some.getIncomingConnections().containsAll(that.getIncomingConnections()) &&
				   that.getIncomingConnections().containsAll(some.getIncomingConnections()) &&
				   some.getIncomingConnections().size() == that.getIncomingConnections().size() &&
				   some.getOutgoingConnections().containsAll(that.getOutgoingConnections()) &&
				   that.getOutgoingConnections().containsAll(some.getOutgoingConnections()) &&
				   some.getOutgoingConnections().size() == that.getOutgoingConnections().size();
		}

		if (!(some instanceof FixPointAnchor)) {
			throw new IllegalArgumentException("Can only compare " + FixPointAnchor.class.getName());
		}
		
		FixPointAnchor someFixAnchor = (FixPointAnchor) some;
		FixPointAnchor thatFixAnchor = (FixPointAnchor) that;
		
		return someFixAnchor.getLocation().getX() == thatFixAnchor.getLocation().getX() && someFixAnchor.getLocation().getY() == thatFixAnchor.getLocation().getY();
	}
	
	public static List<Anchor> getDefaultAnchors(AnchorContainer container) {
		ArrayList<Anchor> defaultAnchors = new ArrayList<Anchor>();
		EList<Anchor> anchors = container.getAnchors();
		
		for (int i = 1; i < 5; i++) {
			if (anchors.size() <= i) {
				break;
			}
			
			defaultAnchors.add(anchors.get(i));
		}
		
		return defaultAnchors;
	}

	public static Anchor getCenterAnchor(AnchorContainer container) {
		return container.getAnchors().get(0);
	}
	
	public static Anchor getLeftAnchor(Shape shape) {
		return shape.getAnchors().get(4);
	}
	
	public static Anchor getRightAnchor(Shape shape) {
		return shape.getAnchors().get(2);
	}
	
	public static Anchor getTopAnchor(Shape shape) {
		return shape.getAnchors().get(1);
	}
	
	public static Anchor getBottomAnchor(Shape shape) {
		return shape.getAnchors().get(3);
	}
	
	/**
	 * Return true if the given anchor is one of the elements 
	 * default anchors
	 * 
	 * @param anchor
	 * @return
	 */
	public static boolean isDefaultAnchor(Anchor anchor) {
		
		AnchorContainer parent = anchor.getParent();
		int anchorIndex = parent.getAnchors().indexOf(anchor);

		// NRE: WARNING: default anchor identified by index in anchor 
		// list (0..4 =  default)
		// Not my fault :o)
		if (anchorIndex != -1 && anchorIndex <= 4) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isAboveStartShape(Shape startShape, Shape endShape) {
		ILocation startShapeLocation = diagramRelativeLocation(startShape);
		ILocation endShapeLocation = diagramRelativeLocation(endShape);
		
		ILocation endShapeBoundary = location(endShapeLocation.getX(), endShapeLocation.getY() + endShape.getGraphicsAlgorithm().getHeight());
		ILocation startShapeLocationPoint = location(startShapeLocation.getX() ,startShapeLocation.getY());
		
		return getHorizontalLayoutTreshold(startShapeLocationPoint, endShapeBoundary) > 0 ? false : true;
	}	
	
	public static boolean isBeneathStartShape(Shape startShape, Shape endShape) {
		ILocation startShapeLocation = diagramRelativeLocation(startShape);
		ILocation endShapeLocation = diagramRelativeLocation(endShape);
		
		ILocation startShapeBoundary = location(startShapeLocation.getX(), startShapeLocation.getY() + startShape.getGraphicsAlgorithm().getHeight());
		ILocation endShapeLocationPoint = location(endShapeLocation.getX() ,endShapeLocation.getY());
		
		return getHorizontalLayoutTreshold(startShapeBoundary, endShapeLocationPoint) >= 0 ? true : false;
	}
	
	public static boolean isLeftToStartShape (Shape startShape, Shape endShape) {
		ILocation startShapeLocation = diagramRelativeLocation(startShape);
		ILocation endShapeLocation = diagramRelativeLocation(endShape);
		
		ILocation endShapeBoundary = location(endShapeLocation.getX() + endShape.getGraphicsAlgorithm().getWidth(), endShapeLocation.getY());
		ILocation startShapeLocationPoint = location(startShapeLocation.getX() ,startShapeLocation.getY());
		
		return getVerticalLayoutTreshold(startShapeLocationPoint, endShapeBoundary) > 0 ? false : true;
	}
	
	
	public static boolean isRightToStartShape (Shape startShape, Shape endShape) {
		ILocation startShapeLocation = diagramRelativeLocation(startShape);
		ILocation endShapeLocation = diagramRelativeLocation(endShape);
		
		ILocation startShapeBoundary = location(startShapeLocation.getX() + startShape.getGraphicsAlgorithm().getWidth(), startShapeLocation.getY());
		ILocation endShapeLocationPoint = location(endShapeLocation.getX() ,endShapeLocation.getY());
		
		return getVerticalLayoutTreshold(startShapeBoundary, endShapeLocationPoint) >= 0 ? true : false;
	}
	
	public static double getAbsLayoutTreshold(FreeFormConnection connection) {
		return Math.abs(getLayoutTreshold(connection));
	}

	/**
	 * 
	 * @param connection
	 * @return negative value if end shape is left to start shape and
	 * positive if end shape is right to start shape.
	 */
	public static double getLayoutTreshold(FreeFormConnection connection) {
		ILocation startShapeCenter = getShapeCenter((Shape) connection.getStart().getParent());
		ILocation endShapeCenter = getShapeCenter((Shape) connection.getEnd().getParent());
		return getVerticalLayoutTreshold(startShapeCenter, endShapeCenter);
	}
	
	public static double getLayoutTreshold(Shape startShape, Shape endShape) {
		ILocation startShapeCenter = getShapeCenter(startShape);
		ILocation endShapeCenter = getShapeCenter(endShape);
		return getVerticalLayoutTreshold(startShapeCenter, endShapeCenter);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @return 1.0 if the points are on the same vertical line, 0.0 if the points are on the same horizontal line,
	 * sign is negative if end point is above the start point 
	 */
	public static double getHorizontalLayoutTreshold(ILocation start, ILocation end) {
		Vector shapeVector = new Vector(end.getX() - start.getX(), end.getY() - start.getY());
		Vector unitYVector = new Vector (0, 1);
		
		double product = Math.floor(shapeVector.getDivided(shapeVector.getLength()).getDotProduct(unitYVector) * 1000) / 1000;
		return product;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return 1.0 if the points are on the same horizonal line, 0.0 if the points are on the same vertical line,
	 * sign is negative if end point is left to start point 
	 */
	public static double getVerticalLayoutTreshold(ILocation start, ILocation end) {
		Vector shapeVector = new Vector(end.getX() - start.getX(), end.getY() - start.getY());
		Vector unitXVector = new Vector (1,0);
		
		double product = Math.floor(shapeVector.getDivided(shapeVector.getLength()).getDotProduct(unitXVector) * 1000) / 1000;
		return product;
	}
	
	public static ILocation getShapeCenter(Shape shape) {
		
		if (shape == null) {
			throw new NullPointerException("Argument is null");
		}
		
		return getShapeLocationMidpoint(shape);
	}

	public static ILocation getRectangleCenter(IRectangle rectangle) {
		
		ILocation center = location(
			rectangle.getX() + rectangle.getWidth() / 2, 
			rectangle.getY() + rectangle.getHeight() / 2 );
		
		return center;
	}
	
	/**
	 * Returns true if the given rectangle contains the point.
	 * 
	 * Points on the rectangles border are not contained.
	 * 
	 * @param rectangle
	 * @param point
	 * 
	 * @return true if the point is contained
	 */
	public static boolean isContained(IRectangle rectangle, ILocation point) {
		return isContained(rectangle, point, 0);
	}
	
	/**
	 * Returns true if the given rectangle contains the point
	 * 
	 * @param rectangle
	 * @param point
	 * 
	 * @param tolerance the tolerance in pixels to check if an element is contained
	 * 		 			setting that to 1 will make points on the border to be contained, too
	 * 
	 * @return true if the point is contained when taking the tolerance into account
	 */
	public static boolean isContained(IRectangle rectangle, ILocation point, int tolerance) {
		
		// translation of scenario to positive coordinates
		int dx = Math.min(0, Math.min(rectangle.getX(), point.getX())) * -1;
		int dy = Math.min(0, Math.min(rectangle.getY(), point.getY())) * -1;
		
		int px = point.getX() + dx;
		int py = point.getY() + dy;
		
		int rx1 = rectangle.getX() + dx;
		int rx2 = rx1 + rectangle.getWidth();
		
		int ry1 = rectangle.getY() + dy;
		int ry2 = ry1 + rectangle.getHeight();
		
		return 
			px + tolerance > rx1 && 
			px - tolerance < rx2 &&
			py + tolerance > ry1 &&
			py - tolerance < ry2;
	}
	
	public static void addVerticalCenteredBendpoints(FreeFormConnection connection) {
		ILocation startAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
		ILocation endAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getEnd());
		
		int midX = ((endAnchorLocation.getX() - startAnchorLocation.getX()) / 2) + startAnchorLocation.getX();
		
		ILocation first = location(midX, startAnchorLocation.getY());
		ILocation second = location(midX, endAnchorLocation.getY());
		
		connection.getBendpoints().add(point(first));
		connection.getBendpoints().add(point(second));
	}

	public static void addHorizontalCenteredBendpoints(FreeFormConnection connection) {
		ILocation startAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
		ILocation endAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getEnd());
		
		int midY = ((endAnchorLocation.getY() - startAnchorLocation.getY()) / 2) + startAnchorLocation.getY();
		
		ILocation first = location(startAnchorLocation.getX(), midY);
		ILocation second = location(endAnchorLocation.getX(), midY);

		connection.getBendpoints().add(point(first));
		connection.getBendpoints().add(point(second));
	}
	
	public static void addRectangularBendpoint(FreeFormConnection connection) {
		ILocation startAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
		ILocation endAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getEnd());

		ILocation location = location(startAnchorLocation.getX(), endAnchorLocation.getY());
		
		connection.getBendpoints().add(point(location));
	}
	
	public static void layoutConnection(Connection connection) {
		
		// check if new anchor point is neccessary
		AnchorContainer startAnchorContainer = connection.getStart().getParent();
		AnchorContainer endAnchorContainer = connection.getEnd().getParent();
		
		if (startAnchorContainer instanceof Shape && endAnchorContainer instanceof Shape) {
			new ConnectionReconnectionContext(connection).reconnect();
		} else {
			throw new LayoutingException("Cannot handle connection: " + connection);
		}
		
		DIUtils.updateDIEdge(connection);
	}
	
	/**
	 * Get the sector in which a specific anchor resides
	 * relative to the anchors container
	 * 
	 * @param anchor
	 * @return
	 */
	public static Sector getAnchorSector(Anchor anchor) {
		
		AnchorContainer container = anchor.getParent();
		if (container == null || !(container instanceof Shape)) {
			throw new IllegalArgumentException("Need anchor connected to shape");
		}
		
		Shape shape = (Shape) container;
		
		ILocation anchorLocation = getAnchorLocation(anchor);
		ILocation shapeLocation = getShapeLocationMidpoint(shape);
		
		return getSector(
			anchorLocation.getX(), 
			anchorLocation.getY(), 
			shapeLocation.getX(), 
			shapeLocation.getY());
	}
	
	/**
	 * Returns the sector in which a given point (x) is located in 
	 * comparision to another point (comp)
	 * 
	 * @param px the X coordinate of the point
	 * @param py the Y coordinate of the point
	 * @param rx the X coordinate of the reference point
	 * @param ry the Y coordinate of the reference point
	 * 
	 * @return
	 */
	public static Sector getSector(int px, int py, int rx, int ry) {
		
		boolean above = py < ry;
		boolean beneath = py > ry;
		
		boolean left = px < rx;
		boolean right = px > rx;
		
		return Sector.fromBooleans(left, right, above, beneath);
	}

	public static Sector getSector(Point p, Point reference) {
		return getSector(p.getX(), p.getY(), reference.getX(), reference.getY());
	}
	
	public static Sector getSector(ILocation p, ILocation reference) {
		return getSector(p.getX(), p.getY(), reference.getX(), reference.getY());
	}
	
	public static IRectangle getAbsoluteRectangle(Shape shape) {
		ILocation position = getShapeLocation(shape);
		GraphicsAlgorithm algorithm = shape.getGraphicsAlgorithm();
		
		return rectangle(position.getX(), position.getY(), algorithm.getWidth(), algorithm.getHeight());
	}
	
	@SuppressWarnings("restriction")
	public static ILocation getShapeLocationMidpoint(Shape shape) {

		ILocation location = getShapeLocation(shape);

		GraphicsAlgorithm graphicsAlgorithm = shape.getGraphicsAlgorithm();
		
		return new LocationImpl(
			location.getX() + graphicsAlgorithm.getWidth() / 2, 
			location.getY() + graphicsAlgorithm.getHeight() / 2);
	}
	
	public static double getLength(FreeFormConnection connection) {
		double resultLength = 0.0;
		
		ILocation startLoc = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
		ILocation endLoc = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getEnd());
		
		if (connection.getBendpoints().size() == 0) {
			return new Vector(endLoc.getX() - startLoc.getX(), endLoc.getY() - startLoc.getY()).getLength();
		}
		
		for (int index = 0; index < connection.getBendpoints().size(); index++) {
			Point point = connection.getBendpoints().get(index);
			
			if (index !=  connection.getBendpoints().size() -1) {
				Point next = connection.getBendpoints().get(index+1);
				resultLength += new Vector(next.getX() - point.getX(), next.getY() - point.getY()).getLength();
			}
		}
		
		Point first = connection.getBendpoints().get(0);
		Point last = connection.getBendpoints().get(connection.getBendpoints().size()-1);
		
		resultLength += new Vector(first.getX() - startLoc.getX(), first.getY() - startLoc.getY()).getLength();
		resultLength += new Vector(endLoc.getX() - last.getX(), endLoc.getY() - last.getY()).getLength();
		
		return resultLength;
	}


	/**
	 * Returns the first non-anchor reference point for a given connection
	 * 
	 * @param anchor
	 * @param connection
	 * @return
	 */
	public static ILocation getConnectionReferencePoint(ChopboxAnchor anchor, Connection connection) {
		
		if (!(connection instanceof FreeFormConnection)) {
			throw new IllegalArgumentException("Can handle instances of " + FreeFormConnection.class.getName() + " only");
		}
		
		FreeFormConnection ffconnection = (FreeFormConnection) connection;

		EList<Point> bendpoints = ffconnection.getBendpoints();
		
		if (ffconnection.getStart().equals(anchor)) {
			if (bendpoints.isEmpty()) {
				return getAnchorLocation(ffconnection.getEnd());
			} else {
				Point point = bendpoints.get(0);
				return location(point);
			}
		}

		if (ffconnection.getEnd().equals(anchor)) {
			if (bendpoints.isEmpty()) {
				return getAnchorLocation(ffconnection.getStart());
			} else {
				Point point = bendpoints.get(bendpoints.size() - 1);
				return location(point);
			}
		}
		
		throw new IllegalArgumentException("Anchor not connected to connection: " + anchor + " / " + connection);
	}
	
	/**
	 * DO NOT TOUCH; DO NOT ALTER
	 * 
	 * @param rectangle
	 * @param referencePt
	 * @return
	 */
	public static Sector getChopboxIntersectionSector(IRectangle rectangle, ILocation referencePt) {
		
		ILocation centerPt = getRectangleCenter(rectangle);
		
		Sector directionSector = getSector(referencePt, centerPt);
		
		Vector center = vector(centerPt);
		Vector reference = vector(referencePt);
		
		Vector lineVector = center.getSubtracted(reference);
		
		double crossProduct;
		Vector cornerVector;
		
		switch (directionSector) {
		case UNDEFINED:
			return Sector.UNDEFINED;
		case BOTTOM: 
			return Sector.BOTTOM;
		case TOP:
			return Sector.TOP;
		case LEFT:
			return Sector.LEFT;
		case RIGHT:
			return Sector.RIGHT;
		
		case BOTTOM_LEFT:
			cornerVector = bottomLeft(rectangle);

			crossProduct = lineVector.getCrossProduct(center.getSubtracted(cornerVector));

			if (crossProduct > 0) {
				return Sector.BOTTOM;
			} else
			if (crossProduct < 0) {
				return Sector.LEFT;
			} else {
				return Sector.BOTTOM_LEFT;
			}
		case TOP_LEFT:
			cornerVector = topLeft(rectangle);

			crossProduct = lineVector.getCrossProduct(center.getSubtracted(cornerVector));
			
			if (crossProduct < 0) {
				return Sector.TOP;
			} else
			if (crossProduct > 0) {
				return Sector.LEFT;
			} else {
				return Sector.TOP_LEFT;
			}
		case BOTTOM_RIGHT:
			cornerVector = bottomRight(rectangle);

			crossProduct = lineVector.getCrossProduct(center.getSubtracted(cornerVector));
			
			if (crossProduct < 0) {
				return Sector.BOTTOM;
			} else
			if (crossProduct > 0) {
				return Sector.RIGHT;
			} else {
				return Sector.BOTTOM_RIGHT;
			}
		case TOP_RIGHT:
			cornerVector = topRight(rectangle);

			crossProduct = lineVector.getCrossProduct(center.getSubtracted(cornerVector));
			
			if (crossProduct > 0) {
				return Sector.TOP;
			} else
			if (crossProduct < 0) {
				return Sector.RIGHT;
			} else {
				return Sector.TOP_RIGHT;
			}
		}
		
		// e.g. because it is exactly the center point of the rectangle
		return Sector.UNDEFINED;
	}

	public static ILocation getChopboxIntersectionPoint(IRectangle rectangle, int px, int py) {
		return getChopboxIntersectionPoint(rectangle, location(px, py));
	}
	
	/**
	 * Returns the absolute location of that anchor
	 * 
	 * @param anchor
	 * @return
	 */
	public static ILocation getAnchorLocation(Anchor anchor) {
		return Graphiti.getPeLayoutService().getLocationRelativeToDiagram(anchor);
	}

	/**
	 * Returns the absolute location of a chopbox anchor from the users perspective
	 * 
	 * @param anchor
	 * @param connection
	 * @return
	 */
	public static ILocation getChopboxAnchorLocation(ChopboxAnchor anchor, Connection connection) {
		GraphicsAlgorithm graphicsAlgorithm = anchor.getParent().getGraphicsAlgorithm();
		ILocation parentLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram((Shape) anchor.getParent());
		
		ILocation referencePoint = LayoutUtil.getConnectionReferencePoint((ChopboxAnchor) anchor, connection);
		IRectangle rectangle = new RectangleImpl(parentLocation.getX(), parentLocation.getY(), graphicsAlgorithm.getWidth(), graphicsAlgorithm.getHeight());
		
		return LayoutUtil.getChopboxIntersectionPoint(rectangle, referencePoint);
	}
	
	/**
	 * Returns the visible location of an anchor.
	 * 
	 * @param anchor
	 * @return
	 */
	public static ILocation getVisibleAnchorLocation(Anchor anchor, Connection connection) {
		if (anchor instanceof ChopboxAnchor) {
			return getChopboxAnchorLocation((ChopboxAnchor) anchor, connection);
		} else {
			return getAnchorLocation(anchor);
		}
	}
	
	/**
	 * DO NOT TOUCH; DO NOT ALTER
	 * 
	 * @param rectangle
	 * @param referencePt
	 * @return
	 * 
	 * @see {@link IGefService#getChopboxLocationOnBox(org.eclipse.draw2d.geometry.Point, Rectangle)}
	 */
	public static ILocation getChopboxIntersectionPoint(IRectangle rectangle, ILocation referencePt) {
		
		Sector intersectionSection = getChopboxIntersectionSector(rectangle, referencePt);
		
		ILocation centerPt = getRectangleCenter(rectangle);
		Vector center = vector(centerPt);
		
		Vector intersectionLine = center.getSubtracted(vector(referencePt));
		
		Vector adjacentLeg;
		Vector hypotenuse;
		
		double crossProduct;
		
		// angle to perform length magic
		double alpha;
		
		switch (intersectionSection) {
		case TOP_LEFT:
			return location(topLeft(rectangle));
		case TOP_RIGHT:
			return location(topRight(rectangle));
		case BOTTOM_LEFT: 
			return location(bottomLeft(rectangle));
		case BOTTOM_RIGHT:
			return location(bottomRight(rectangle));
			
		case TOP:
		case BOTTOM: 
			adjacentLeg = new Vector(0, rectangle.getHeight() / 2);
			crossProduct = adjacentLeg.getCrossProduct(intersectionLine);
			
			// orthogonal to adjacent leg?
			if (crossProduct == 0) {
				if (intersectionSection == Sector.TOP) {
					return location(center.getSubtracted(adjacentLeg));
				} else {
					return location(center.getAdded(adjacentLeg));
				}
			}
			// no? get distance by angle
			else {
				if (intersectionSection == Sector.TOP) {
					adjacentLeg = adjacentLeg.getMultiplied(-1.0);
				}
				
				alpha = adjacentLeg.getAngle(intersectionLine);
				
				double length = adjacentLeg.getLength() / Math.cos(Math.toRadians(alpha));
				
				hypotenuse = intersectionLine.getDivided(intersectionLine.getLength()).getMultiplied(length);
				return location(center.getAdded(hypotenuse));
			}
			
		case LEFT:
		case RIGHT:
			adjacentLeg = new Vector(rectangle.getWidth() / 2, 0);
			crossProduct = adjacentLeg.getCrossProduct(intersectionLine);

			// orthogonal to adjacent leg?
			if (crossProduct == 0) {
				if (intersectionSection == Sector.LEFT) {
					return location(center.getSubtracted(adjacentLeg));
				} else {
					return location(center.getAdded(adjacentLeg));
				}
			}
			// no? get distance by angle
			else {
				if (intersectionSection == Sector.LEFT) {
					adjacentLeg = adjacentLeg.getMultiplied(-1.0);
				}
				
				alpha = adjacentLeg.getAngle(intersectionLine);
				
				double length = adjacentLeg.getLength() / Math.cos(Math.toRadians(alpha));
				
				hypotenuse = intersectionLine.getDivided(intersectionLine.getLength()).getMultiplied(length);
				return location(center.getAdded(hypotenuse));
			}
		
		case UNDEFINED: 
			// must be the center of the rectangle
			
		}
		
		return null;
	}

	// private static helpers; do not expose //////////////////////////////

	private static ILocation getShapeLocation(Shape shape) {
		return Graphiti.getPeLayoutService().getLocationRelativeToDiagram(shape);
	}
	
	private static Vector bottomRight(IRectangle rectangle) {
		return new Vector(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight());
	}

	private static Vector topLeft(IRectangle rectangle) {
		return new Vector(rectangle.getX(), rectangle.getY());
	}

	private static Vector topRight(IRectangle rectangle) {
		return new Vector(rectangle.getX() + rectangle.getWidth(), rectangle.getY());
	}

	private static Vector bottomLeft(IRectangle rectangle) {
		return new Vector(rectangle.getX(), rectangle.getY() + rectangle.getHeight());
	}

	private static ILocation diagramRelativeLocation(Shape startShape) {
		return Graphiti.getLayoutService().getLocationRelativeToDiagram(startShape);
	}
}