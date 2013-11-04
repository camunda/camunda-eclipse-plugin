package org.camunda.bpm.modeler.core.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.location;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.camunda.bpm.modeler.core.layout.Docking;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
import org.camunda.bpm.modeler.ui.features.event.BoundaryAttachment;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
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

/**
 * Utility dealing with all sorts of layout specific concerns
 * 
 * @author nico.rehwaldt
 */
public class LayoutUtil {
	
	/**
	 * evolutionary developed value to switch between the AboveBeneath and LeftRight Strategy for gateways 
	 */
	public static final double MAGIC_VALUE = 0.83;
	
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
		private boolean top;
		private boolean bottom;

		private Sector(boolean left, boolean right, boolean above, boolean bottom) {
			if (left && right) {
				throw new IllegalArgumentException("Cannot be left and right at the same time");
			}
			
			if (above && bottom) {
				throw new IllegalArgumentException("Cannot be top and bottom at the same time");
			}
			
			this.left = left;
			this.right = right;
			this.top = above;
			this.bottom = bottom;
		}
		
		public static Sector fromBooleans(boolean left, boolean right, boolean top, boolean bottom) {
			if (left && top) {
				return Sector.TOP_LEFT;
			} else if (left && bottom) {
				return Sector.BOTTOM_LEFT;
			} else if (left) {
				return Sector.LEFT;
			} else if (right && top) {
				return Sector.TOP_RIGHT;
			} else if (right && bottom) {
				return Sector.BOTTOM_RIGHT;
			} else if (right) {
				return Sector.RIGHT;
			} else if(top) {
				return Sector.TOP;
			} else if(bottom) {
				return Sector.BOTTOM;
			} else {
				return Sector.UNDEFINED;
			}
		}
		
		public boolean isTop() {
			return top;
		}
		
		public boolean isBottom() {
			return bottom;
		}
		
		public boolean isLeft() {
			return left;
		}
		
		public boolean isRight() {
			return right;
		}

		/**
		 * Returns the inverted sector for myself.
		 * 
		 * @return
		 */
		public Sector inverted() {
			return fromBooleans(!left, !right, !top, !bottom);
		}
		
		/**
		 * Creates a sector from a direction const in 
		 * {@link IResizeShapeContext}. 
		 * 
		 * @param resizeDirection
		 * @return
		 */
		public static Sector fromResizeDirection(int resizeDirection) {
			
			switch (resizeDirection) {
			case IResizeShapeContext.DIRECTION_EAST:
				return RIGHT;
			case IResizeShapeContext.DIRECTION_WEST:
				return LEFT;
			case IResizeShapeContext.DIRECTION_NORTH_WEST:
				return TOP_LEFT;
			case IResizeShapeContext.DIRECTION_NORTH:
				return TOP;
			case IResizeShapeContext.DIRECTION_NORTH_EAST:
				return TOP_RIGHT;
			case IResizeShapeContext.DIRECTION_SOUTH_WEST:
				return BOTTOM_LEFT;
			case IResizeShapeContext.DIRECTION_SOUTH:
				return BOTTOM;
			case IResizeShapeContext.DIRECTION_SOUTH_EAST:
				return BOTTOM_RIGHT;
			default:
				return UNDEFINED;
			}
		}

		/**
		 * Translate the sector to a resize direction.
		 * 
		 * @return
		 */
		public int toResizeDirection() {
			switch (this) {
			case RIGHT:
				return IResizeShapeContext.DIRECTION_EAST;
			case LEFT:
				return IResizeShapeContext.DIRECTION_WEST;
			case TOP_LEFT: 
				return IResizeShapeContext.DIRECTION_NORTH_WEST;
			case TOP:
				return IResizeShapeContext.DIRECTION_NORTH;
			case TOP_RIGHT: 
				return IResizeShapeContext.DIRECTION_NORTH_EAST;
			case BOTTOM_LEFT: 
				return IResizeShapeContext.DIRECTION_SOUTH_WEST;
			case BOTTOM:
				return IResizeShapeContext.DIRECTION_SOUTH;
			case BOTTOM_RIGHT:
				return IResizeShapeContext.DIRECTION_SOUTH_EAST;
			default:
				return IResizeShapeContext.DIRECTION_UNSPECIFIED;
			}
		}
	}

	/**
	 * Bounding box computation utility
	 * 
	 * @author nico.rehwaldt
	 */
	public static class BBox {

		private Integer x1 = null;
		private Integer y1 = null;
		
		private Integer x2 = null;
		private Integer y2 = null;
		
		private int paddingX = 0;
		private int paddingY = 0;
		
		/**
		 * Create a BBox without initial bounds.
		 * 
		 * @param paddingX
		 * @param paddingY
		 */
		public BBox(int paddingX, int paddingY) {
			this.paddingX = paddingX;
			this.paddingY = paddingY;
		}

		/**
		 * Create a new BBox with the specified initial bounds and a x and y padding.
		 * 
		 * The padding is not applied to the initial bounds.
		 * 
		 * @param initialBounds
		 * 
		 * @param paddingX
		 * @param paddingY
		 */
		public BBox(IRectangle initialBounds, int paddingX, int paddingY) {
			
			if (initialBounds != null) {
				this.x1 = getX1(initialBounds);
				this.y1 = getY1(initialBounds);
				
				this.x2 = getX2(initialBounds);
				this.y2 = getY2(initialBounds);
			}
			
			this.paddingX = paddingX;
			this.paddingY = paddingY;
		}
		
		protected int getX1(IRectangle rect) {
			return rect.getX() - paddingX;
		}
		
		protected int getX2(IRectangle rect) {
			return rect.getX() + rect.getWidth() + paddingX;
		}
		
		protected int getY1(IRectangle rect) {
			return rect.getY() - paddingY;
		}
		
		protected int getY2(IRectangle rect) {
			return rect.getY() + rect.getHeight() + paddingY;
		}
		
		public void addBounds(IRectangle bounds) {

			int ax1 = getX1(bounds);
			int ax2 = getX2(bounds);
			
			int ay1 = getY1(bounds);
			int ay2 = getY2(bounds);
			
			if (!isInitialized()) {
				this.x1 = ax1;
				this.x2 = ax2;
				this.y1 = ay1;
				this.y2 = ay2;
			} else {
				this.x1 = Math.min(ax1, this.x1);
				this.x2 = Math.max(ax2, this.x2);
				this.y1 = Math.min(ay1, this.y1);
				this.y2 = Math.max(ay2, this.y2);
			}
		}

		/**
		 * Return true if this bbox is initialized with bounds.
		 * 
		 * @return
		 */
		public boolean isInitialized() {
			return this.x1 != null;
		}

		/**
		 * Returns the final bounding box or null if the bounding box 
		 * has not yet been initialized.
		 * 
		 * @return
		 */
		public IRectangle getBounds() {
			if (!isInitialized()) {
				return null;
			}
			
			return rectangle(x1, y1, x2 - x1, y2 - y1);
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
	
	public static Sector getBoundaryRelativeSector(Shape boundaryShape) {
		BoundaryAttachment attachment = BoundaryEventUtil.getStoredBoundaryAttachment(boundaryShape);
		if (attachment == null) {
			throw new IllegalArgumentException(String.format("Shape %s has no attachment", boundaryShape));
		}
		
		return attachment.getSector();
	}
	
	public static BaseElement getSourceBaseElement(FreeFormConnection connection) {
		EObject linkedObject = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(connection.getStart().getParent());
		
		if (linkedObject instanceof FreeFormConnection) {
			return (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement((FreeFormConnection) linkedObject);
		}
		
		return (BaseElement) linkedObject;	
	}
	
	public static BaseElement getTargetBaseElement(FreeFormConnection connection) {
		EObject linkedObject = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(connection.getEnd().getParent());
		
		if (linkedObject instanceof FreeFormConnection) {
			return (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement((FreeFormConnection) linkedObject);
		}
		
		return (BaseElement) linkedObject;	
	}
	
	public static Shape getStartShape(Connection connection) {
		return (Shape) connection.getStart().getParent();
	}
	
	public static Shape getEndShape(Connection connection) {
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
	 * @see #getVerticalLayoutTreshold(ILocation, ILocation)
	 */
	public static double getLayoutTreshold(FreeFormConnection connection) {
		ILocation startShapeCenter = getAbsoluteShapeCenter((Shape) connection.getStart().getParent());
		ILocation endShapeCenter = getAbsoluteShapeCenter((Shape) connection.getEnd().getParent());
		return getVerticalLayoutTreshold(startShapeCenter, endShapeCenter);
	}
	
	public static double getLayoutTreshold(Shape startShape, Shape endShape) {
		ILocation startShapeCenter = getAbsoluteShapeCenter(startShape);
		ILocation endShapeCenter = getAbsoluteShapeCenter(endShape);
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
	
	public static void addVerticalCenteredBendpoints(FreeFormConnection connection, Point start, Point end) {
		int midX = ((end.getX() - start.getX()) / 2) + start.getX();
		
		Point first = point(midX, start.getY());
		Point second = point(midX, end.getY());

		connection.getBendpoints().add(first);
		connection.getBendpoints().add(second);
	}

	public static void addHorizontalCenteredBendpoints(FreeFormConnection connection, Point start, Point end) {
		int midY = ((end.getY() - start.getY()) / 2) + start.getY();
		
		Point first = point(start.getX(), midY);
		Point second = point(end.getX(), midY);

		connection.getBendpoints().add(first);
		connection.getBendpoints().add(second);
	}
	
	public static void addRectangularBendpoint(FreeFormConnection connection, Point start, Point end) {
		connection.getBendpoints().add(point(start.getX(), end.getY()));
	}
	
	public static void addTurningBendpointsVertical(FreeFormConnection connection, Docking startDocking, Docking endDocking, boolean up) {
		int sign = 1;
		Integer startDistance = 30;
		
		if (up) {
			sign = -1;
		}
		
		startDistance *= sign;
		
		int dockingDistance = endDocking.getPosition().getY() - startDocking.getPosition().getY();
		
		startDistance += (dockingDistance * sign) > startDistance ? dockingDistance :  0;
		
		Point firstBendPoint = point(startDocking.getPosition().getX(), startDistance + startDocking.getPosition().getY());
		
		connection.getBendpoints().add(firstBendPoint);
		connection.getBendpoints().add(point(endDocking.getPosition().getX(), firstBendPoint.getY()));
	}
	
	public static void addTurningBendpointsHorizontal(FreeFormConnection connection, Point startDockingPos, Point endDockingPos, Point firstBendPoint) {
		connection.getBendpoints().add(point(startDockingPos.getX(), firstBendPoint.getY()));
		
		Point secondPoint = point(firstBendPoint.getX(), endDockingPos.getY());
		connection.getBendpoints().add(secondPoint);
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
		ILocation shapeLocation = getAbsoluteShapeCenter(shape);
		
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
	
	/**
	 * Get sector of a point with respect to the passed reference.
	 * 
	 * @param p
	 * @param reference
	 * @return
	 */
	public static Sector getSector(ILocation p, IRectangle reference) {

		int px = p.getX();
		int py = p.getY();
		
		int rx = reference.getX();
		int ry = reference.getY();
		
		int rwidth = reference.getWidth();
		int rheight = reference.getHeight();
		
		boolean above = py <= ry;
		boolean beneath = py >= ry + rheight;
		
		boolean left = px <= rx;
		boolean right = px >= rx + rwidth;
		
		return Sector.fromBooleans(left, right, above, beneath);
	}

	public static IRectangle getRelativeBounds(Shape shape) {
		GraphicsAlgorithm algorithm = shape.getGraphicsAlgorithm();
		ILocation position = location(algorithm.getX(), algorithm.getY());
		
		return rectangle(position.getX(), position.getY(), algorithm.getWidth(), algorithm.getHeight());
	}
	
	public static IRectangle getAbsoluteBounds(Shape shape) {
		ILocation position = getLocation(shape);
		GraphicsAlgorithm algorithm = shape.getGraphicsAlgorithm();
		
		return rectangle(position.getX(), position.getY(), algorithm.getWidth(), algorithm.getHeight());
	}
	
	public static IRectangle getResizedAbsoluteBounds(Shape shape, Point resizeDelta, Sector resizeDirection) {
		
		IRectangle preResizeBounds = LayoutUtil.getAbsoluteBounds(shape);
		return RectangleUtil.resize(preResizeBounds, resizeDelta, resizeDirection);
	}
	
	public static ILocation getAbsoluteShapeCenter(Shape shape) {
		Assert.isNotNull(shape);
		
		IRectangle bounds = getAbsoluteBounds(shape);
		
		return location(
			bounds.getX() + bounds.getWidth() / 2, 
			bounds.getY() + bounds.getHeight() / 2);
	}
	
	//// bounding box computation logic ////////////////////////////////////////////
	
	/**
	 * Get the bounding box of the containers children.
	 * 
	 * @param container
	 * @return
	 */
	public static IRectangle getChildrenBBox(ContainerShape container) {
		return getChildrenBBox(container, null, 0, 0);
	}

	/**
	 * Get the bounding box of the containers children with the given minimal dimension and as well as y padding.
	 * 
	 * @param container
	 * @param minDimensions
	 * @param paddingX
	 * @param paddingY
	 * 
	 * @return the bounding box, relative to the shapes coordinate system.
	 */
	public static IRectangle getChildrenBBox(ContainerShape container, IRectangle minBBox, int paddingX, int paddingY) {
		return getBBox(container.getChildren(), minBBox, paddingX, paddingY);
	}

	/**
	 * Get the bounding box of the given shapes, as relative coordinates.
	 * 
	 * @param shapes
	 * @return
	 */
	public static IRectangle getBBox(List<Shape> shapes, IRectangle minBBox, int paddingX, int paddingY) {
		
		BBox bbox = new BBox(minBBox, paddingX, paddingY);
		
		for (Shape s: shapes) {

			// only include shapes with a graphical representation
			if (s.getGraphicsAlgorithm() == null) {
				continue;
			}
			
			// never include scroll shape
			if (ScrollUtil.isScrollShape(s)) {
				continue;
			}
			
			// do not include non bpmn containers
			// (marker containers, etc...)
			if (s.getLink() == null) {
				continue;
			}
			
			if (s instanceof ContainerShape) {
				IRectangle shapeBounds = getRelativeBounds(s);
				bbox.addBounds(shapeBounds);
			}
		}
		
		return bbox.getBounds();
	}
	
	/**
	 * Returns the first non-anchor reference point for a given connection
	 * 
	 * @param anchor
	 * @param connection
	 * @return
	 */
	public static ILocation getFirstConnectionWaypoint(ChopboxAnchor anchor, Connection connection) {
		
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
		
		ILocation referencePoint = LayoutUtil.getFirstConnectionWaypoint((ChopboxAnchor) anchor, connection);
		IRectangle rectangle = rectangle(parentLocation.getX(), parentLocation.getY(), graphicsAlgorithm.getWidth(), graphicsAlgorithm.getHeight());
		
		return LayoutUtil.getChopboxIntersectionPoint(rectangle, referencePoint);
	}

	/**
	 * Return all connections this anchor container references. 
	 * 
	 * @param anchorContainer
	 * 
	 * @return
	 */
	public static Set<Connection> getConnections(AnchorContainer anchorContainer) {
		return new HashSet<Connection>(Graphiti.getPeService().getAllConnections(anchorContainer));
	}
	
	/**
	 * Returns the visible location of an anchor.
	 * 
	 * @param anchor
	 * @return
	 */
	public static ILocation getVisibleAnchorLocation(Anchor anchor, Connection connection) {
		if (anchor instanceof ChopboxAnchor) {
			ILocation location = getChopboxAnchorLocation((ChopboxAnchor) anchor, connection);
			
			// location may become null, when elements overlap
			if (location == null) {
				location = getAnchorLocation(anchor);
			}
			
			return location;
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

	////// connection label specific stuff ////////////////////////////////
	
	public static List<Point> getConnectionWaypoints(Connection connection) {
		Assert.isLegal(connection instanceof FreeFormConnection);
		
		FreeFormConnection freeFormConnection = (FreeFormConnection) connection;

		Anchor startAnchor = freeFormConnection.getStart();
		Anchor endAnchor = freeFormConnection.getEnd();
		
		ArrayList<Point> waypoints = new ArrayList<Point>();
		
		waypoints.add(point(getVisibleAnchorLocation(startAnchor, freeFormConnection)));
		waypoints.addAll(freeFormConnection.getBendpoints());
		waypoints.add(point(getVisibleAnchorLocation(endAnchor, freeFormConnection)));
		
		return waypoints;
	}
	
	////// private static helpers; do not expose //////////////////////////

	private static ILocation getLocation(Shape shape) {
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

	public static List<Point> getConnectionBendpointsTo(FreeFormConnection connection, Point pointOnLine) {
		List<Point> waypoints = getConnectionWaypoints(connection);
		
		List<Point> strippedWaypoints = ConnectionUtil.getPointsTo(waypoints, pointOnLine);
		
		return strippedWaypoints;
	}
	
	/**
	 * Return the reference point on a connection for a given point
	 * 
	 * @param connection
	 * @param point
	 * @return
	 */
	public static Point getConnectionReferencePoint(Connection connection, Point point) {
		List<Point> waypoints = getConnectionWaypoints(connection);
		
		return ConnectionUtil.getClosestPointOnConnection(waypoints, point);
	}

	/**
	 * Returns the list of shared connections between a shape and a number of other shapes
	 * 
	 * @param shape
	 * @param others
	 * @return
	 */
	public static <T extends PictogramElement> Set<Connection> getSharedConnections(Shape shape, List<T> others) {
		
		HashSet<PictogramElement> otherShapes = new HashSet<PictogramElement>(others);
		
		Set<Connection> sharedConnections = new HashSet<Connection>();

		Set<Connection> shapeConnections = getConnections(shape);
		
		for (Connection connection : shapeConnections) {
			
			AnchorContainer startContainer = connection.getStart().getParent();
			AnchorContainer endContainer = connection.getEnd().getParent();
			
			if (startContainer == shape && 
				endContainer == shape) {
				sharedConnections.add(connection);
			} else 
			if ((startContainer == shape && otherShapes.contains(endContainer)) || 
				(endContainer == shape && otherShapes.contains(startContainer))) {
				
				sharedConnections.add(connection);
			}
		}
		
		return sharedConnections;
	}

	public static boolean isContained(IRectangle rectangle, IRectangle container) {
		return isContained(rectangle, container, 0);
	}
	
	/**
	 * Checks the containment of rectangle in container with respect to a given sector.
	 * 
	 * @param rectangle
	 * @param container
	 * @param checkSector
	 * @return
	 */
	public static boolean isContained(IRectangle rectangle, IRectangle container, Sector checkSector) {
		return isContained(rectangle, container, 0, checkSector);
	}
	
	/**
	 * Checks the containment of rectangle in container with respect to a given sector and padding.
	 * 
	 * @param rectangle
	 * @param container
	 * @param padding
	 * @param checkSector
	 * @return
	 */
	public static boolean isContained(IRectangle rectangle, IRectangle container, int padding, Sector checkSector) {
		
		int rx1 = rectangle.getX() - padding;
		int ry1 = rectangle.getY() - padding;

		int rx2 = rectangle.getX() + rectangle.getWidth() + padding;
		int ry2 = rectangle.getY() + rectangle.getHeight() + padding;

		int cx1 = container.getX();
		int cy1 = container.getY();

		int cx2 = container.getX() + container.getWidth();
		int cy2 = container.getY() + container.getHeight();
		
		return 
			(!checkSector.isLeft() || rx1 > cx1) && 
			(!checkSector.isRight() || rx2 < cx2) && 
			(!checkSector.isTop() || ry1 > cy1) && 
			(!checkSector.isBottom() || ry2 < cy2);
	}
	
	public static boolean isContained(IRectangle rectangle, IRectangle container, int padding) {
		
		int rx1 = rectangle.getX() - padding;
		int ry1 = rectangle.getY() - padding;

		int rx2 = rectangle.getX() + rectangle.getWidth() + padding;
		int ry2 = rectangle.getY() + rectangle.getHeight() + padding;

		int cx1 = container.getX();
		int cy1 = container.getY();

		int cx2 = container.getX() + container.getWidth();
		int cy2 = container.getY() + container.getHeight();
		
		return rx1 > cx1 && rx2 < cx2 && ry1 > cy1 && ry2 < cy2;
	}
	
	/**
	 * The strategy for constraining an object into the parent box
	 * 
	 * @author Nico Rehwaldt
	 */
	public static enum BoxingStrategy {
		POSITION,
		POSITION_AND_SIZE,
		NONE;
	}
	
	/**
	 * Constraints a box with parent relative coordinates in a given parent box
	 * 
	 * @param relativeBox
	 * @param boundingBox
	 * @param padding
	 * @return
	 */
	public static IRectangle box(IRectangle relativeBox, IDimension boundingBox, int padding, BoxingStrategy boxingStrategy) {
		
		int x = relativeBox.getX();
		int y = relativeBox.getY();
		
		int width = relativeBox.getWidth();
		int height = relativeBox.getHeight();
		
		int maxWidth = boundingBox.getWidth() - (2 * padding);
		int maxHeight = boundingBox.getHeight() - (2 * padding);

		switch (boxingStrategy) {
		case NONE:
			break;
		case POSITION_AND_SIZE:
			if (width > maxWidth) {
				width = maxWidth;
			}
			
			if (height > maxHeight) {
				height = maxHeight;
			}
			// intentionally skipped break (POSITION part applies to POSITION_AND_SIZE, too)
		case POSITION:
			
			if (x + width > maxWidth + padding) {
				x = maxWidth + padding - width;
			}
			
			if (y + height > maxHeight + padding) {
				y = maxHeight + padding - height;
			}
			
			if (x < padding) {
				x = padding;
			}
			
			if (y < padding) {
				y = padding;
			}
		}
		
		return rectangle(x, y, width, height);
	}
	
	/**
	 * Return all the anchors contained in the shape 
	 * and its children.
	 * 
	 * @param shape
	 * @return
	 */
	public static List<Anchor> getContainerAnchors(Shape shape) {
		return getContainerAnchors(shape, false);
	}
	
	/**
	 * Return all the anchors contained in the shape 
	 * and its children.
	 * 
	 * @param shape
	 * @param nestedOnly true if only anchors by children should be returned
	 * @return
	 */
	public static List<Anchor> getContainerAnchors(Shape shape, boolean nestedOnly) {
		List<Anchor> ret = new ArrayList<Anchor>();
		
		if (!nestedOnly) {
			ret.addAll(shape.getAnchors());
		}
		
		if (shape instanceof ContainerShape) {
			ContainerShape containerShape = (ContainerShape) shape;
			List<Shape> children = containerShape.getChildren();
			for (Shape child : children) {
				if (child instanceof ContainerShape) {
					ret.addAll(getContainerAnchors((ContainerShape) child, false));
				} else {
					ret.addAll(child.getAnchors());
				}
			}
		}
		return ret;
	}
	
	/**
	 * Returns the diagram for a given shape.
	 * 
	 * @param shape
	 * @return
	 */
	public static Diagram getDiagram(Shape shape) {
		return Graphiti.getPeService().getDiagramForShape(shape);
	}
	
	/**
	 * Returns all connections contained in this shape, as they connect the shape or
	 * child shapes to each other. 
	 * 
	 * @param shape
	 * @param nestedOnly true if only connections between child shapes should be returned
	 * 
	 * @return
	 */
	public static Set<Connection> getContainerConnections(ContainerShape shape, boolean nestedOnly) {

		return getContainerConnections(shape, nestedOnly, true);
	}
	
	/**
	 * Returns all connections contained in this shape, as they connect the shape or
	 * child shapes to each other. 
	 * 
	 * @param shape
	 * @param nestedOnly true if only connections between child shapes should be returned
	 * @param containedOnly true if only connections starting AND ending in the container should be returned
	 * 
	 * @return
	 */
	public static Set<Connection> getContainerConnections(ContainerShape shape, boolean nestedOnly, boolean containedOnly) {

		List<Anchor> anchors = getContainerAnchors(shape, nestedOnly);

		Set<Connection> incomingConnections = getIncomingConnections(anchors);
		Set<Connection> outgoingConnections = getOutgoingConnections(anchors);
		
		if (containedOnly) {
			incomingConnections.retainAll(outgoingConnections);
		} else {
			incomingConnections.addAll(outgoingConnections);
		}
		
		return incomingConnections;
	}
	
	/**
	 * Stores bendpoint containments
	 * 
	 * @author nico.rehwaldt
	 */
	public static class BendpointContainment implements Iterable<Map.Entry<Point, FreeFormConnection>> {
		
		private List<Point> containedPoints;
		
		private Map<Point, FreeFormConnection> pointToConnectionMap;
		
		public BendpointContainment() {
			containedPoints = new ArrayList<Point>();
			pointToConnectionMap = new HashMap<Point, FreeFormConnection>();
		}
		
		public void add(Point point, FreeFormConnection connection) {
			containedPoints.add(point);
			pointToConnectionMap.put(point, connection);
		}
		
		public List<Point> getContainedPoints() {
			return containedPoints;
		}
		
		public Connection getConnectionForPoint(Point point) {
			return pointToConnectionMap.get(point);
		}

		@Override
		public Iterator<Entry<Point, FreeFormConnection>> iterator() {
			return pointToConnectionMap.entrySet().iterator();
		}
	}
	
	public static BendpointContainment getContainerBendpoints(ContainerShape shape) {
		
		IRectangle bounds = getAbsoluteBounds(shape);
		
		Set<Connection> connections = getContainerConnections(shape, true, false);
		
		BendpointContainment containment = new BendpointContainment();
		
		for (Connection connection: connections) {
			if (connection instanceof FreeFormConnection) {
				FreeFormConnection ffc = (FreeFormConnection) connection;
				
				List<Point> bendpoints = ffc.getBendpoints();
				
				for (Point bendpoint: bendpoints) {
					
					if (LayoutUtil.isContained(bounds, location(bendpoint))) {
						containment.add(bendpoint, ffc);
					}
				}
			}
		}
		
		return containment;
	}
	
	/**
	 * Returns all connections contained in this shape, as they connect the shape or
	 * child shapes to each other. 
	 * 
	 * @param shape
	 * @return
	 */
	public static Set<Connection> getContainerConnections(ContainerShape shape) {
		return getContainerConnections(shape, false);
	}
	
	/**
	 * Incoming connections for a list of anchors
	 * 
	 * @param anchors
	 * @return
	 */
	private static Set<Connection> getIncomingConnections(List<Anchor> anchors) {
		HashSet<Connection> connections = new HashSet<Connection>();
		for (Anchor anchor: anchors) {
			connections.addAll(anchor.getIncomingConnections());
		}
		
		return connections;
	}

	/**
	 * Incoming connections for a list of anchors
	 * 
	 * @param anchors
	 * @return
	 */
	private static Set<Connection> getOutgoingConnections(List<Anchor> anchors) {
		HashSet<Connection> connections = new HashSet<Connection>();
		for (Anchor anchor: anchors) {
			connections.addAll(anchor.getOutgoingConnections());
		}
		
		return connections;
	}
	
	/**
	 * 
	 * @param boundaryBounds
	 * @param rectangle
	 * @param tolerance
	 * 
	 * @return
	 */
	public static ILocation snapToBounds(ILocation boundaryBounds, IRectangle rectangle, int tolerance) {

		int x = boundaryBounds.getX();
		int y = boundaryBounds.getY();

		int x1 = rectangle.getX();
		int x2 = x1 + rectangle.getWidth();
		
		int y1 = rectangle.getY();
		int y2 = y1 + rectangle.getHeight();

		int dx1 = Math.abs(x - x1);
		int dx2 = Math.abs(x - x2);
		
		int dy1 = Math.abs(y - y1);
		int dy2 = Math.abs(y - y2);
		
		if (dx1 <= tolerance) {
			x = x1;
		}
		
		if (dx2 <= tolerance) {
			x = x2;
		}
		
		if (dy1 <= tolerance) {
			y = y1;
		}
		
		if (dy2 <= tolerance) {
			y = y2;
		}
		
		return location(x, y);
	}
}
