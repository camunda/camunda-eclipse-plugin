package org.eclipse.bpmn2.modeler.core.layout.util;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.layout.ConnectionReconnectionContext;
import org.eclipse.bpmn2.modeler.core.layout.LayoutingException;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
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
	
	
	public static boolean isAboveStartShape (Shape startShape, Shape endShape) {
		ILocation startShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(startShape);
		ILocation endShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(endShape);
		
		Point endShapeBoundary = Graphiti.getGaService().createPoint(endShapeLocation.getX(), endShapeLocation.getY() + endShape.getGraphicsAlgorithm().getHeight());
		Point startShapeLocationPoint = Graphiti.getGaService().createPoint(startShapeLocation.getX() ,startShapeLocation.getY());
		
		return getHorizontalLayoutTreshold(startShapeLocationPoint, endShapeBoundary) > 0 ? false : true;
	}
	
	
	public static boolean isBeneathStartShape (Shape startShape, Shape endShape) {
		ILocation startShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(startShape);
		ILocation endShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(endShape);
		
		Point startShapeBoundary = Graphiti.getGaService().createPoint(startShapeLocation.getX(), startShapeLocation.getY() + startShape.getGraphicsAlgorithm().getHeight());
		Point endShapeLocationPoint = Graphiti.getGaService().createPoint(endShapeLocation.getX() ,endShapeLocation.getY());
		
		return getHorizontalLayoutTreshold(startShapeBoundary, endShapeLocationPoint) >= 0 ? true : false;
	}
	
	public static boolean isLeftToStartShape (Shape startShape, Shape endShape) {
		ILocation startShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(startShape);
		ILocation endShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(endShape);
		
		Point endShapeBoundary = Graphiti.getGaService().createPoint(endShapeLocation.getX() + endShape.getGraphicsAlgorithm().getWidth(), endShapeLocation.getY());
		Point startShapeLocationPoint = Graphiti.getGaService().createPoint(startShapeLocation.getX() ,startShapeLocation.getY());
		
		return getVerticalLayoutTreshold(startShapeLocationPoint, endShapeBoundary) > 0 ? false : true;
	}
	
	
	public static boolean isRightToStartShape (Shape startShape, Shape endShape) {
		ILocation startShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(startShape);
		ILocation endShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(endShape);
		
		Point startShapeBoundary = Graphiti.getGaService().createPoint(startShapeLocation.getX() + startShape.getGraphicsAlgorithm().getWidth(), startShapeLocation.getY());
		Point endShapeLocationPoint = Graphiti.getGaService().createPoint(endShapeLocation.getX() ,endShapeLocation.getY());
		
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
		Point startShapeCenter = getCenter((Shape) connection.getStart().getParent());
		Point endShapeCenter = getCenter((Shape) connection.getEnd().getParent());
		return getVerticalLayoutTreshold(startShapeCenter, endShapeCenter);
	}
	
	public static double getLayoutTreshold(Shape startShape, Shape endShape) {
		Point startShapeCenter = getCenter(startShape);
		Point endShapeCenter = getCenter(endShape);
		return getVerticalLayoutTreshold(startShapeCenter, endShapeCenter);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @return 1.0 if the points are on the same vertical line, 0.0 if the points are on the same horizontal line,
	 * sign is negative if end point is above the start point 
	 */
	public static double getHorizontalLayoutTreshold(Point start, Point end) {
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
	public static double getVerticalLayoutTreshold(Point start, Point end) {
		Vector shapeVector = new Vector(end.getX() - start.getX(), end.getY() - start.getY());
		Vector unitXVector = new Vector (1,0);
		
		double product = Math.floor(shapeVector.getDivided(shapeVector.getLength()).getDotProduct(unitXVector) * 1000) / 1000;
		return product;
	}
	
	public static Point getCenter(Shape shape) {
		
		if (shape == null) {
			throw new NullPointerException("Argument is null");
		}
		
		ILocation shapeLocation = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(shape);
		GraphicsAlgorithm shapeGa = shape.getGraphicsAlgorithm();
		Point shapeCenter = Graphiti.getGaService().createPoint(shapeLocation.getX() + shapeGa.getWidth() / 2, shapeLocation.getY() + shapeGa.getHeight() / 2 );
		
		return shapeCenter;
	}
	
	public static void addVerticalCenteredBendpoints(FreeFormConnection connection) {
		ILocation startAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
		ILocation endAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getEnd());
		
		int midX = ((endAnchorLocation.getX() - startAnchorLocation.getX()) / 2) + startAnchorLocation.getX();
		
		Point firstPoint = Graphiti.getCreateService().createPoint(midX, startAnchorLocation.getY());
		Point secondPoint = Graphiti.getCreateService().createPoint(midX, endAnchorLocation.getY());
		
		connection.getBendpoints().add(firstPoint);
		connection.getBendpoints().add(secondPoint);
	}
	
	public static void addHorizontalCenteredBendpoints(FreeFormConnection connection) {
		ILocation startAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
		ILocation endAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getEnd());
		
		int midY = ((endAnchorLocation.getY() - startAnchorLocation.getY()) / 2) + startAnchorLocation.getY();
		
		Point firstPoint = Graphiti.getCreateService().createPoint(startAnchorLocation.getX(), midY);
		Point secondPoint = Graphiti.getCreateService().createPoint(endAnchorLocation.getX(), midY);
		
		connection.getBendpoints().add(firstPoint);
		connection.getBendpoints().add(secondPoint);
	}
	
	public static void addRectangularBendpoint(FreeFormConnection connection) {
		ILocation startAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
		ILocation endAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getEnd());

		Point point = Graphiti.getCreateService().createPoint(startAnchorLocation.getX(), endAnchorLocation.getY());
		
		connection.getBendpoints().add(point);
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
		
		ILocation anchorLocation = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(anchor);
		ILocation shapeLocation = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(shape);
		
		return getSector(
			anchorLocation.getX(), 
			anchorLocation.getY(), 
			shapeLocation.getX() + shape.getGraphicsAlgorithm().getWidth() / 2, 
			shapeLocation.getY() + shape.getGraphicsAlgorithm().getHeight() / 2);
	}
	
	/**
	 * Returns the sector in which a given point (x) is located in 
	 * comparision to another point (comp)
	 * 
	 * @param px
	 * @param py
	 * @param compx
	 * @param compy
	 * 
	 * @return
	 */
	public static Sector getSector(int px, int py, int compx, int compy) {
		
		boolean above = py < compy;
		boolean beneath = py > compy;
		
		boolean left = px < compx;
		boolean right = px > compx;
		
		return Sector.fromBooleans(left, right, above, beneath);
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
	
}
